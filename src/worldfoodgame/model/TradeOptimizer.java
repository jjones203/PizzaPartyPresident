package worldfoodgame.model;

import worldfoodgame.common.EnumCropType;

import java.util.*;

import static worldfoodgame.planningpoints.PlanningPointCategory.TradeEfficiency;

/**
 @author david
 created: 2015-04-05
 Modified by Ken Kressin for Milestone 3.
 Description:
 TradeOptimizer casts the import-export problem as something akin to the
 Generalized Assignment Problem, wherein either the importers or exporters
 may be conceptualized as the bins or knapsacks.
 The algorithm follows the ideas outlined at:
 http://www.cs.technion.ac.il/~lirank/pubs/2006-IPL-Generalized-Assignment-Problem.pdf

 The strategy is to to look at each bin as an individual knapsack problem 
 (in this case, the fractional knapsack) with a set of shared resources to place
 in that knapsack, and run them in an arbitrary order until the resources to
 place have been exhausted.

 To mitigate the odds of a sub-optimal solution, the algorithm is
 threaded for each crop to be traded and run multiple times, the best result of
 which is the actual implemented set of trades.
 */
@SuppressWarnings("unchecked")
public class TradeOptimizer
{
  private final int year;
  private              Map<String, Continent> continentMap = new LinkedHashMap<>();
  private              Collection<Continent> cont = new ArrayList<>();
  private static final boolean                DEBUG        = true;
  private final        List<TradePair>[]      allTrades    = new ArrayList[]
                                                               {
                                                                 new ArrayList<>(), new ArrayList<>(),
                                                                 new ArrayList<>(), new ArrayList<>(),
                                                                 new ArrayList<>()
                                                               };

  private List<SingleCropTrader> traders;

  /**
   Construct a new TradeOptimizer with the set of continents to trade between.

   @param continents   Collection of Continents to trade between
   @param year         year to calculate trades for
   */
  public TradeOptimizer(Collection<Continent> continents, int year)
  {
    this.year = year;
    /*
     * We need to remove the player continent from the computer-traded continents.
     * To do this, we use an iterator to copy the passed in continents collection
     * to a HashMap, then put the values of the HashMap into a new ArrayList
     * and use this list in the optimizer.  This preserves the original
     * collection, which the rest of the game depends on.  Ken
     */
    Iterator<Continent> iter = continents.iterator();
    while (iter.hasNext())
    {
      Continent c = iter.next();
      if (!c.isPlayer())
      {
        continentMap.put(c.getContName(), c);
      }
    }
    cont.addAll(continentMap.values());

    if (DEBUG)
    {
      System.out.println("Trade Optimizer initialized.");
    }
  }

  /**
   Starts the optimization algorithm.  This creates a new threaded
   SingleCropTrader
   for each crop to be analyzed independently
   */
  public void optimizeAndImplementTrades()
  {
    if (DEBUG)
    {
      System.out.println("Entering method optimizeAndImplementTrades()");
    }
    traders = new ArrayList<>();
    for(EnumCropType crop : EnumCropType.values())
    {
      traders.add(new SingleCropTrader(crop));
    }
    if(DEBUG)
    {
      System.out.println("Starting for loop to kick trader.start()...");
    }
    for(SingleCropTrader trader : traders) trader.start();
  }

  /**
   * Checks to see if all trading threads are done.
   * @return a boolean indicating whether the SingleCropTrader() threads are all
   * complete.  If any of the threads are still working, this will return false.
   */
  public boolean doneTrading()
  {
    for(SingleCropTrader t : traders) if (!t.isDone()) return false;
    return true;
  }

  public List<TradePair>[] getAllTrades(){ return allTrades; }

  /**
   TradePair class represents a potential pairing between an importer and
   exporter
   Very little is saved in each object, save the relatively expensive efficiency
   calculation and the Continent references.  They may be instantiated from an
   existing TradePair to mitigate computational complexity.
   */
  public static class TradePair implements Comparable<TradePair>
  {
    public final Continent importer;
    public final Continent exporter;
    public final double efficiency;
    private double amount = 0;


    private TradePair(Continent exporter, Continent importer)
    {
      this.exporter = exporter;
      this.importer = importer;
      this.efficiency = 1 - calcEfficiency(exporter, importer);
    }

    private TradePair(TradePair t)
    {
      importer = t.importer;
      exporter = t.exporter;
      efficiency = t.efficiency;
    }

    /* Calculates the efficiency between two continents based on the trade efficiency
     between them, as implemented in the Continent class, using the lesser of the two
      Trading Efficiency numbers.*/
    private static double calcEfficiency(Continent c1, Continent c2)
    {
      if(DEBUG)
      {
        System.out.println("Trade efficiency of " + c1.getContName() + ": " + c1.getPlanningPointsFactor(TradeEfficiency));
        System.out.println("Trade efficiency of " + c2.getContName() + ": " + c2.getPlanningPointsFactor(TradeEfficiency));
      }
      return Math.min(c1.getPlanningPointsFactor(TradeEfficiency), c2.getPlanningPointsFactor(TradeEfficiency));
    }

    /* implement a trade between this pair, given a crop and year to trade in.
      The trade will only be implemented if both the importer still has a need
      for the crop and if the exporter can supply the crop.  The amount traded
      is bounded by both the need and the surplus of importer and exporter,
      respectively.  Returns true if a trade is made, false otherwise.
     */
    private boolean implementTrade(EnumCropType crop, int year)
    {
      double need = -(importer.getNetCropAvailable(year, crop) - importer.getTotalCropNeed(year, crop));
      double supply = exporter.getNetCropAvailable(year, crop) - exporter.getTotalCropNeed(year, crop);

      if (need > 0 && supply > 0)
      {
        double curExport = exporter.getCropExport(year, crop);
        double curImport = importer.getCropImport(year, crop);

        double toGive = Math.min(need / efficiency, supply);
        double toReceive = toGive * efficiency;

        amount = toReceive;
        exporter.setCropExport(year, crop, toGive + curExport);
        importer.setCropImport(year, crop, toReceive + curImport);

        return true;
      }
      return false;
    }

    @Override
    public int compareTo(TradePair o)
    {
      return o.efficiency > efficiency? 1 : efficiency > o.efficiency ? -1 : 0;
    }

    public double getAmountTraded()
    {
      return amount;
    }
  }

  /**
   SingleCropTrader manages the trading optimization algorithm for a single
   crop.
   */
  private class SingleCropTrader extends Thread
  {
    private boolean isDone = false;
    private static final int TRIALS = 6; //number of times the optimization will run
    private final EnumCropType crop;
    private final List<TradePair> pairs = new ArrayList<>();
    private final TradePairList master;


    /**
     Construct a new SingleCropTrader with a specified crop.
     @param crop      crop to trade
     */
    private SingleCropTrader(EnumCropType crop)
    {
      this.crop = crop;
      List<Continent> importers = new ArrayList<>();
      List<Continent> exporters = new ArrayList<>();
      double net = 0;

      /* Divide the continents in the parent class into importers and exporters
      based on the crop surplus for each continent */
      for(Continent c : cont)
      {
        double surplus = c.getSurplus(year, crop);
        net += surplus;
        if(surplus < 0) importers.add(c);
        else if(surplus > 0) exporters.add(c);
      }

      /* create the TradePairs between importer and exporter */
      for(Continent i : importers)
      {
        for(Continent e : exporters)
        {
          pairs.add(new TradePair(e, i));
        }
      }

      /* create the master list used to instantiate all the temporary lists
      during the multiple runs of the algorithm */
      master = new TradePairList(pairs, cont, crop, year);

      if (DEBUG) System.out.printf("Trader for %s setup:%n" +
                                   "net availability: %.3f tons, importers.size(): %d, exporters.size(): %d%n",
                                   crop.toString(), net, importers.size(), exporters.size());
    }

    /**
     Defines the running behavior of the SingleCropTrader.
     */
    @Override
    public void run()
    {
      tradeDeterministic();
      isDone = true;
    }

    public boolean isDone() { return isDone; }

    private void tradeDeterministic()
    {
      Collections.sort(master);
      allTrades[crop.ordinal()].addAll(master.implementTrades());
    }


    /* Multiple runs of the GAP algorithm are run, the best result of which is
    saved, along with the TradePair ordering that produced it.
    This ordering is then implemented in the actual continent objects. */
    private void tradeProbablistic()
    {
      long start = System.currentTimeMillis();
      TradePairList tmp;
      TradePairList best = new TradePairList(master);

      double tmpResult;

      double bestResult = best.getTradeResult();

      double sum = 0;
      for (int i = 0; i < TRIALS; i++)
      {
        tmp = new TradePairList(master);

        /* switch the ordering scheme every loop */
        if (i % 2 == 0)
        {
          tmp.shuffleOnExporters();
        }
        else
        {
          tmp.shuffleOnImporters();
        }

        tmpResult = tmp.getTradeResult();
        sum += tmpResult;

        if(tmpResult > bestResult)
        {
          best = tmp;
          bestResult = tmpResult;
        }
      }

      allTrades[crop.ordinal()].addAll(best.implementTrades());

      long end = System.currentTimeMillis();
      if (DEBUG) System.out.printf("Trader for %s done in %dms%n" +
                                   "best result: %.3f, avg: %.3f%n", crop.toString(), end - start, bestResult, sum / TRIALS);
    }
  }

  private class TradePairList extends ArrayList<TradePair>
  {
    private final EnumCropType crop;

    /* maintain the state of the need/surplus for each importer/exporter here.
      Values are adjusted with each theoretical trade in getTradeResult() */
    private Map<Continent, Double> exporterMap = new HashMap<>();
    private Map<Continent, Double> importerMap = new HashMap<>();

    private TradePairList(Collection<TradePair> pairs, Collection<Continent> all, EnumCropType crop, int year)
    {
      super(pairs);
      this.crop = crop;
      createMaps(all);
    }

    private TradePairList(TradePairList list)
    {
      super(list.size());
      crop = list.crop;

      for(TradePair pair: list)
      {
        add(new TradePair(pair));
      }
      importerMap = new HashMap<>(list.importerMap);
      exporterMap = new HashMap<>(list.exporterMap);
    }

    private void createMaps(Collection<Continent> continents)
    {
      for (Continent c : continents)
      {
        double surplus = c.getSurplus(year, crop);
        if (surplus > 0)
        {
          exporterMap.put(c, surplus);
        }
        else if (surplus < 0)
        {
          importerMap.put(c, -surplus);
        }
      }
    }

    /* given the ordering of this TradePairList, get the theoretical result
      of all the trading that would be done with a greedy solution to the
      fractional knapsack algorithm */
    private double getTradeResult()
    {
      for(TradePair pair : this)
      {
        double need;
        double supply;

        /* if either importer or exporter are not present, they have been removed
          as a result of exhausting their need/supply */
        if(importerMap.containsKey(pair.importer) && exporterMap.containsKey(pair.exporter))
        {
          need = importerMap.get(pair.importer);
          supply = exporterMap.get(pair.exporter);
        }
        else continue;

        /* calculate the amounts to export and import */
        double toGive = Math.min(need/pair.efficiency, supply);
        double toReceive = toGive * pair.efficiency;

        /* calculate the new values for the importer and exporter maps */
        double newNeed = need - toReceive;
        double newSupply = supply - toGive;

        /* if either value is 0 or less, remove the appropriate country from
          the map.  It can no longer be used for trading.
          Otherwise, update the values in the maps to the results of the trade.
         */
        if(newNeed > 0) importerMap.put(pair.importer, newNeed);
        else importerMap.remove(pair.importer);

        if(newSupply > 0) exporterMap.put(pair.exporter, newSupply);
        else exporterMap.remove(pair.exporter);
      }

      double result = 0;

      /* Sum all the surpluses and deficits of the exporters to obtain the
         result of this trading sequence.  Any continents not in the maps have
         achieved a "balance" of the given crop, and do not contribute to the
         sum.
       */
      for(Continent ex : exporterMap.keySet())
      {
        /* values in export map are surpluses */
        result += exporterMap.get(ex);
      }
      for(Continent im : importerMap.keySet())
      {
        /* values in import map are deficits (as postive numbers) */
        result -= importerMap.get(im);
      }
      return result;
    }

    /* implement the trades in this TradePairList at the country level.  This
      mutates the underlying data used to calculate undernourishment, happiness,
      etc.  */
    private List<TradePair> implementTrades()
    {
      List<TradePair> list = new ArrayList<>();
      for(TradePair pair: this)
      {
        if(pair.implementTrade(crop, year)) list.add(pair);
      }
      return list;
    }
    /*
     * Both shuffleOnImporters() and shuffleOnExporters() methosds are designed to
     * randomly re-order the import and export lists to allow the optimization
     * algorithm to try different solutions.
     */
    private void shuffleOnImporters()
    {
      Collections.shuffle(Arrays.asList(importerMap));
      System.out.println("Shuffled importerMap: " + importerMap.toString());
    }

    private void shuffleOnExporters()
    {
      Collections.shuffle(Arrays.asList(exporterMap));
      System.out.println("Shuffled exporterMap: " + exporterMap.toString());

    }
  }
}
