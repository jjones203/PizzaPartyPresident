package worldfoodgame.model;

import worldfoodgame.common.AbstractScenario;
import worldfoodgame.catastrophes.Blight;
import worldfoodgame.catastrophes.Catastrophe;
import worldfoodgame.catastrophes.Flood;
import worldfoodgame.catastrophes.Drought;
import worldfoodgame.common.EnumCropType;
import worldfoodgame.common.EnumCropZone;

import java.util.*;

/**
 * Created by winston on 1/23/15.
 * Phase_01
 * CS 351 spring 2015
 * <p/>
 * The world is everything that is the case.
 * The world is the totality of facts, not of things.
 * The facts in logical space are the world.
 * <p/>
 * - L. W.
 */
public class World extends AbstractScenario
{
  private static World theOneWorld;
  private Collection<Region> world;
  private ArrayList<Continent> continents;
  private Collection<Country> politicalWorld;
  private TileManager tileManager;
  private Calendar currentDate;
  private List<TradingOptimizer.TradePair>[] lastTrades;
  private boolean DEBUG = true;

  private World(Collection<Region> world, Collection<Country> countries, Calendar cal)
  {
    this.world = world;
    this.politicalWorld = countries;
    this.currentDate = cal;
    this.continents = new ArrayList<Continent>();
  }

  /**
   * This method is used to create the world object. The world object is a
   * singleton class, there is one and only one world.
   *
   * @param world     the list of regions that make up the world.
   * @param countries the countries in the world
   * @param cal       the starting date of the world.
   */
  public static void makeWorld(Collection<Region> world,
      Collection<Country> countries,
      TileManager allTheLand,
      Calendar cal)
  {
    if (theOneWorld != null)
    {
      new RuntimeException("Make World can only be called once!");
    }

    // calculate OTHER_CROPS temp & rain requirements for each country
    for (Country country:countries)
    {
      country.setOtherCropsData();
    }
    
    theOneWorld = new World(world, countries, cal);
    theOneWorld.populateContinents();
    theOneWorld.tileManager = allTheLand;
  }

  /**
   * used to return the world => singleton design pattern.
   * @return  the world
   */
  public static World getWorld()
  {
    if (theOneWorld == null)
    {
      throw new RuntimeException("WORLD HAS NOT BEEN MADE YET!");
    }
    return theOneWorld;
  }

  /**
   * Get the current time of this particular world.
   *
   * @return a calendar object, with the date and time in side.
   */
  public Calendar getCurrentDate()
  {
    return currentDate;
  }

  /**
   * Set the world time to the given calendar date.
   *
   * @param currentDate date the world will be after calling this method.
   */
  public void setCurrentDate(Calendar currentDate)
  {
    this.currentDate = currentDate;
  }

  /**
   * returns the year as an int.
   *
   * @return
   */
  public int getCurrentYear()
  {
    return getCurrentDate().get(Calendar.YEAR);
  }

  public Collection<Region> getWorldRegions()
  {
    return world;
  }

  public Collection<Country> getCountries()
  {
    return politicalWorld;
  }

  public ArrayList<Continent> getRelevantContinents(List<Country> activeCountries)
  {
    ArrayList<Continent> temp = new ArrayList<>();
    for (Country c : activeCountries)
    {
      if (!temp.contains(c.getContinent()))
      {
        temp.add(c.getContinent());
      }
    }
    return temp;
  }

  /**
   * Returns the number of year remaining in the model as an int.
   *
   * @return
   */
  public int yearRemaining()
  {
    return AbstractScenario.END_YEAR - getCurrentYear();
  }

  /**
   * @return world population at current world time, in millions as a double.
   */
  public double getWorldPopulationMil()
  {
    double totalPop = 0;
    int year = getCurrentYear();
    for (Country country : politicalWorld)
    {
      totalPop += country.getPopulation(year);
    }
    totalPop = totalPop / 1000000;
    return totalPop;
  }


  /**
   * @return percent of world's population that is happy at current world time
   */
  public double getWorldHappinessPercent()
  {
    double unhappyPeople = 0;
    int year = getCurrentYear();
    for (Country country : politicalWorld)
    {
      unhappyPeople += country.getUnhappyPeople(year);
    }
    double percentUnhappy = unhappyPeople/(getWorldPopulationMil() * 1000000);
    double percentHappy = 1 - percentUnhappy;
    return percentHappy;
  }

  /**
   * Returns projected sea level increase during given year
   *
   * @param year
   * @return rise in cm
   */
  @Deprecated
  public double getBaseSeaLevelRise(int year)
  {
    double rise;
    if (year >= 2015 && year < 2020)
    {
      rise = 0.32;
    }
    else if (year >= 2020 && year < 2040)
    {
      rise = 0.3;
    }
    else if (year >= 2040 && year <= 2050)
    {
      rise = 0.4;
    }
    else
    {
      rise = 0;
    }
    return rise;
  }


  /**
   * performs operations needed when stepping from 1 year to next
   */
  public void stepWorld()
  {
    if (DEBUG) System.out.println("\n\nStarting world stepping in " + getCurrentYear());

    long start = System.currentTimeMillis();

    if (DEBUG) System.out.println("Mutating climate data...");
    updateEcoSystems();
    if (DEBUG) System.out.printf("climate data mutated in %dms%n", System.currentTimeMillis() - start);

    currentDate.add(Calendar.YEAR, 1);
    start = System.currentTimeMillis();

    if (DEBUG) System.out.println("Planting tiles...");
    
    rollCatastropheDie(continents); // planting and harvesting occurs in here.
    
    if (DEBUG) System.out.printf("tiles planted in %dms%n", System.currentTimeMillis() - start);
    if (DEBUG) System.out.println("Date is now " + getCurrentYear());

    adjustPopulation(); // need this before shipping

    start = System.currentTimeMillis();
    if (DEBUG) System.out.println("Shipping and recieving...");
    shipAndReceive();
    if (DEBUG) System.out.printf("Done shipping and receiving in: %dms%n", System.currentTimeMillis() - start);

    adjustUndernourished();  // implemented    
    
    start = System.currentTimeMillis();

    if (DEBUG) System.out.println("Mutating country demographics...");
    if (DEBUG) System.out.printf("country demographics mutated in %dms%n", System.currentTimeMillis() - start);
    if (DEBUG) System.out.println("year stepping done");
  }

  /*
    Chooses random number from 1 to 100, giving a 30% chance that
    a catastrophe will occur */
  private void rollCatastropheDie(Collection<Continent> continents)
  {
    if (DEBUG) System.out.println("Rolling catastrophe die...");
    
    Random ran = new Random();
    int die = ran.nextInt(100)+1;

    if (DEBUG) System.out.println("Die says "+die);    
       
    if (die>0 && die<11) // 10% chance of drought catastrophe
    {
      Catastrophe Drought = new Drought(continents);
      plantAndHarvestCrops(); 
      Drought.recuperateAfterCatastrophe();
    }
    else if(die>10 && die<21) // 10% chance of flood catastrophe
    {
      Catastrophe Flood = new Flood(continents);
      plantAndHarvestCrops(); 
      Flood.recuperateAfterCatastrophe();
    }
    else if(die>20 && die<31) // 10% chance of crop disease catastrophe
    {
      Catastrophe Blight = new Blight(continents);
      plantAndHarvestCrops(); 
    }
    else
    {
      plantAndHarvestCrops(); 
    }
  }

  private void adjustPopulation()
  {
    int year = getCurrentYear();
    for (Country country:politicalWorld)
    {
      country.updateMortalityRate(year);
      country.updatePopulation(year);
    }
  }

  private void adjustUndernourished()
  {
    int year = getCurrentYear();
    for (Country country : politicalWorld)
    {
      country.updateUndernourished(year);
    }
  }

  /*
    implements the benevolent trading between countries with surpluses and
    deficits by crop through the TradingOptimizer. */
  private void shipAndReceive()
  {
    TradingOptimizer optimizer = new TradingOptimizer(politicalWorld, getCurrentYear());
    optimizer.optimizeAndImplementTrades();
    while(!optimizer.doneTrading());
    lastTrades = optimizer.getAllTrades();
  }


  private void plantAndHarvestCrops()
  {
    int year = getCurrentYear();
    for (Continent continent:continents)
    {
      ContinentCropAllocator allocator = new ContinentCropAllocator(year,continent);
      allocator.allocateCrops();
    }
  }

  /*
    Mutate the LandTile data through the TileManger.  This steps climate data,
    interpolating based on 2050 predictions with random noise added.
   */
  private void updateEcoSystems()
  {
    tileManager.stepTileData();
  }

  /**
   * @return a Collection holding all the LandTiles in the world, including those
   * not assigned to countries and those without data
   */
  public Collection<LandTile> getAllTiles()
  {
    return tileManager.allTiles();
  }


  /**
   * Returns a Collection of the tiles held by this TileManager that actually
   * contain data.  This, in effect, excludes tiles that would be over ocean and
   * those at the extremes of latitude.  For all tiles, use allTiles();
   * @return  a Collection holding only those tiles for which there exists raster data.
   */
  public List<LandTile> dataTiles()
  {
    return tileManager.dataTiles();
  }


  /**
   * @return a Collection of all the tiles registered with Countries.
   */
  public Collection<LandTile> getAllCountrifiedTiles() { return tileManager.countryTiles(); }


  /**
   * Set the TileManager for the World
   * @param mgr TileManager to set to this World
   */
  public void setTileManager(TileManager mgr)
  {
    this.tileManager = mgr;
  }

  /**
   * @return  the randomization percentage for the World (inherited from AbstractScenario)
   */
  public double getRandomizationPercentage()
  {
    return randomizationPercentage;
  }

  /**
   * Return the LandTile containing given longitude and latitude coordinates.
   * See TileManager.getTile()
   *@param lon longitude of coord
   *@param lat latitude of coord
   *@return    LandTile containing the coordinates
   */
  public LandTile getTile(double lon, double lat)
  {
    return tileManager.getTile(lon, lat);
  }

  @Deprecated
  @Override
  public EnumCropZone classifyZone(EnumCropType crop, double minTemp, double maxTemp, double dayTemp, double nightTemp, double rain)
  {
    throw new UnsupportedOperationException("Call down to LandTile.rateTileForCrop");
    /* Impossible to implement without a Country parameter because the temp and rain values for EnumCropType.OTHER_CROPS vary
     * by country. See rateTileForCrop and rateTileForOtherCrops methods in LandTile class.
     */
  }

  @Deprecated
  @Override
  public double calculateSeaLevelRise(int year)
  {
    return getBaseSeaLevelRise(year);
  }


  public List<TradingOptimizer.TradePair>[] getTrades()
  {
    return lastTrades;
  }

  public Country getCountry(String name)
  {
    for (Country c: politicalWorld)
    {
      if (c.getName().equals(name))
      {
        return c;
      }
    }
    return null;
  }
  
  public ArrayList<Continent> getContinents()
  {
    return continents;
  }
  
  private void populateContinents()
  {
    for (EnumContinentNames continentName:EnumContinentNames.values())
    {
      Continent continent = new Continent(continentName);
      continents.add(continent);
      for (Country country:politicalWorld)
      {
        if (country.getContinentName() == continentName)
        {
          continent.addCountry(country);
          country.setContinent(continent);
        }
      }
      continent.initializeData();
      ContinentCropAllocator allocator = new ContinentCropAllocator(START_YEAR, continent);
      allocator.allocateCrops();
    }
  } 
  
}
