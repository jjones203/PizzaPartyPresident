package worldfoodgame.model;

import worldfoodgame.IO.AttributeGenerator;
import worldfoodgame.common.AbstractScenario;
import worldfoodgame.common.EnumCropType;
import worldfoodgame.common.EnumCropZone;

import java.util.Calendar;
import java.util.Collection;
import java.util.List;
import java.util.Random;

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
  private Random random = new Random(44);
  private Collection<Region> world;
  private Collection<Country> politicalWorld;
  private TileManager tileManager;
  private Calendar currentDate;

  private World(Collection<Region> world, Collection<Country> countries, Calendar cal)
  {
    this.world = world;
    this.politicalWorld = countries;
    this.currentDate = cal;
  }

  /**
   * This method is used to create the world object. The world object is a
   * singleton class, there is one and only one world.
   *
   * @param world     the list of regions that make up the world.
   * @param countries the countryies in the world
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
      CropOptimizer optimizer = new CropOptimizer(AbstractScenario.START_YEAR, country);
      optimizer.optimizeCrops();
    }

    theOneWorld = new World(world, countries, cal);
    theOneWorld.tileManager = allTheLand;
  }

  /**
   * used to return the world => singleton design pattern.
   *
   * @return
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

  /**
   * Advances the world forward by the given number of days. Every new month
   * the worlds attributes are randomly modulated.
   *
   * @param numOfDays number of days to travel in the future too.
   * @return true - the world was change, false otherwise.
   */
  @Deprecated
  public boolean setByDays(int numOfDays)
  {
    int previousMonth = currentDate.get(Calendar.MONTH);
    currentDate.add(Calendar.DATE, numOfDays);

    boolean isNewMonth = previousMonth != currentDate.get(Calendar.MONTH);
    if (isNewMonth) AttributeGenerator.stepAttributes(random, world);

    return isNewMonth;
  }

  public Collection<Region> getWorldRegions()
  {
    return world;
  }

  public Collection<Country> getCountries()
  {
    return politicalWorld;
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


  // stetck of top down look at world step:
  public void stepWorld()
  {


    System.out.println("\n\nStarting world stepping in " + getCurrentYear());

    long start = System.currentTimeMillis();

    System.out.println("Mutating climate data...");
    updateEcoSystems();
    System.out.printf("climate data mutated in %dms%n", System.currentTimeMillis() - start);
    
    currentDate.add(Calendar.YEAR, 1);
    start = System.currentTimeMillis();
    System.out.println("Planting tiles...");
    plantAndHarvestCrops();       // implemented
    System.out.printf("tiles planted in %dms%n", System.currentTimeMillis() - start);
    System.out.println("Date is now " + getCurrentYear());
    adjustPopulation(); // need this before shipping
    System.out.printf("population adjusted in %dms%n", System.currentTimeMillis() - start);

    start = System.currentTimeMillis();
    System.out.println("Shipping and recieving...");
    shipAndReceive();
    System.out.printf("Done shipping and receiving in: %dms%n", System.currentTimeMillis() - start);

    start = System.currentTimeMillis();
    System.out.println("Mutating country demographics...");
    adjustUndernourished();  // implemented
    System.out.printf("country demographics mutated in %dms%n", System.currentTimeMillis() - start);
    System.out.println("year stepping done");
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

  private void shipAndReceive()
  {
    new TradingOptimizer(politicalWorld, currentYear).optimizeAndImplementTrades();
  }

  private void plantAndHarvestCrops()
  {
    int year = getCurrentYear();
    for (Country country:politicalWorld)
    {
      CropOptimizer optimizer = new CropOptimizer(year,country);
      optimizer.optimizeCrops();
      /*if (country.getName().equals("Brazil")) 
      {
          System.out.println("in world corn prod is "+country.getCropProduction(year, EnumCropType.CORN));
          System.out.println("in world corn prod in yr-1 is "+country.getCropProduction(year+1, EnumCropType.CORN));
      }*/
    }
  }

  private void updateEcoSystems()
  {
    tileManager.stepTileData();
  }

  public Collection<LandTile> getAllTiles()
  {
    return tileManager.allTiles();
  }

  /**
   Returns a Collection of the tiles held by this TileManager that actually
   contain data.  This, in effect, excludes tiles that would be over ocean and
   those at the extremes of latitude.  For all tiles, use allTiles();

   @return a Collection holding only those tiles for which there exists raster
   data.
   */
  public List<LandTile> dataTiles()
  {
    return tileManager.dataTiles();
  }

  public Collection<LandTile> getAllCountrifiedTiles() { return tileManager.countryTiles(); }

  public void setTileManager(TileManager mgr)
  {
    this.tileManager = mgr;
  }

  public double getRandomizationPercentage()
  {
    return randomizationPercentage;
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

  public LandTile getTile(double lon, double lat)
  {
    return tileManager.getTile(lon, lat);
  }


}
