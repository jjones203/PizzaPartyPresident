package worldfoodgame.model;

import worldfoodgame.common.EnumCropType;

import java.util.*;

/**
 @author david
 created: 2015-04-05

 description: */
public class TradingOptimizer
{
  private static World world = World.getWorld();
  private final int year;
  private final Collection<Country> countries;

  public TradingOptimizer(Collection<Country> countries, int year)
  {
    this.year = year;
    this.countries = countries;
    for(EnumCropType crop : EnumCropType.values())
    {
      new SingleCropTrader(crop).start();
    }
  }
  
  
  private class SingleCropTrader extends Thread
  {
    private static final int TRIALS = 5_000;
    private final EnumCropType crop;
    private final List<TradePair> pairs = new ArrayList<>();
    private final TradePairList master;


    /**
     Construct a new SingleCropTrader with a specified crop, Collection of
     countries and the year to trade for.
     @param crop      crop to trade
     */
    private SingleCropTrader(EnumCropType crop)
    {
      this.crop = crop;
      List<Country> importers = new ArrayList<>();
      List<Country> exporters = new ArrayList<>();
      for(Country c : countries)
      {
        double surplus = c.getSurplus(year, crop);
        if(surplus < 0) importers.add(c);
        else if(surplus > 0) exporters.add(c);
      }
      for(Country i : importers)
      {
        for(Country e : exporters)
        {
          pairs.add(new TradePair(e, i));
        }
      }
      master = new TradePairList(pairs, countries, crop, year);
    }

    @Override
    public void run()
    {
      TradePairList tmp;
      TradePairList best = new TradePairList(master);
      double tmpResult = 0;
      double bestResult = best.getTradeResult();
      for (int i = 0; i < TRIALS; i++)
      {
        tmp = new TradePairList(master);
        tmp.shuffleOnExporters();
        tmpResult = tmp.getTradeResult();
        if(tmpResult > bestResult)
        {
          best = tmp;
          bestResult = tmpResult;
        }
      }
      best.implementTrades();
    }
  }

  private class TradePairList extends ArrayList<TradePair>
  {
    private final EnumCropType crop;
    private Map<Country, Double> exporterMap = new HashMap<>();
    private Map<Country, Double> importerMap = new HashMap<>();
    
    private TradePairList(Collection<TradePair> pairs, Collection<Country> all, EnumCropType crop, int year)
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

    private void createMaps(Collection<Country> countries)
    {
      for (Country c : countries)
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
    
    private double getTradeResult()
    {
      for(TradePair pair : this)
      {
        double need;
        double supply;
        if(importerMap.containsKey(pair.importer) && exporterMap.containsKey(pair.exporter))
        {
          need = importerMap.get(pair.importer);
          supply = exporterMap.get(pair.exporter);
        }
        else continue;

        double toGive = Math.min(need/pair.efficiency, supply);
        double toReceive = toGive * pair.efficiency;
        
        double newNeed = need - toReceive;
        double newSupply = supply - toGive;
        
        if(newNeed > 0) importerMap.put(pair.importer, newNeed);
        else importerMap.remove(pair.importer);
        
        if(newSupply > 0) exporterMap.put(pair.exporter, newSupply);
        else exporterMap.remove(pair.exporter);
      }
      
      double result = 0;
      
      for(Country ex : exporterMap.keySet())
      {
        result -= exporterMap.get(ex);
      }
      for(Country im : importerMap.keySet())
      {
        result += importerMap.get(im);
      }
      return result;
    }
    
    private void implementTrades()
    {
      for(TradePair pair: this)
      {
        pair.implementTrade(crop, year);
      }
    }
    
    private void shuffleOnImporters()
    {
      final MapPoint pt = randomMapPoint();
      Collections.sort(this, new Comparator<TradePair>()
      {
        @Override
        public int compare(TradePair o1, TradePair o2)
        {
          if(o1.importer == o2.importer)
          {
            double ef1 = o1.efficiency;
            double ef2 = o2.efficiency;
            return ef1 > ef2? 1 : ef1 < ef2 ? -1 : 0;
          }
          else
          {
            double d1 = pt.distanceSq(o1.importer.getCapitolLocation());
            double d2 = pt.distanceSq(o2.importer.getCapitolLocation());
            return d1 > d2 ? 1 : d1 < d2 ? -1 : 0; 
          }
        }
      });
    }
    
    
    private void shuffleOnExporters()
    {
      final MapPoint pt = randomMapPoint();
      Collections.sort(this, new Comparator<TradePair>()
      {
        @Override
        public int compare(TradePair o1, TradePair o2)
        {
          if(o1.exporter == o2.exporter)
          {
            double ef1 = o1.efficiency;
            double ef2 = o2.efficiency;
            return ef1 > ef2? 1 : ef1 < ef2 ? -1 : 0;
          }
          else
          {
            double d1 = pt.distanceSq(o1.exporter.getCapitolLocation());
            double d2 = pt.distanceSq(o2.exporter.getCapitolLocation());
            return d1 > d2 ? 1 : d1 < d2 ? -1 : 0;
          }
        }
      });
    }

    private MapPoint randomMapPoint()
    {
      double lat = (Math.random() - 0.5) * 360;
      double lon = (Math.random() - 0.5) * 180;
      return new MapPoint(lon, lat);
    }

  }
  
  private static class TradePair
  {
    private final Country importer;
    private final Country exporter;
    private final double efficiency;

    private TradePair(Country exporter, Country importer)
    {
      this.exporter = exporter;
      this.importer = importer;
      this.efficiency = calcEfficiency(exporter, importer);
    }
    
    private TradePair(TradePair t)
    {
      importer = t.importer;
      exporter = t.exporter;
      efficiency = t.efficiency;
    }
    
    private static double calcEfficiency(Country c1, Country c2)
    {
      return 1 - c1.getShippingDistance(c2.getCapitolLocation())/20_000d;
    }

    private void implementTrade(EnumCropType crop, int year)
    {
      double need = -importer.getSurplus(year, crop);
      double supply = exporter.getSurplus(year, crop);
      if(need > 0 && supply > 0)
      {
        double toGive = Math.min(need/efficiency, supply);
        double toReceive = toGive*efficiency;
        exporter.setCropExport(year, crop, toGive);
        importer.setCropImport(year, crop, toReceive);
      }
    }
  }


}
