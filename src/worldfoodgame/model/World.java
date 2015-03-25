package worldfoodgame.model;

import worldfoodgame.IO.AttributeGenerator;
import worldfoodgame.IO.XMLparsers.CountryXMLparser;
import worldfoodgame.common.AbstractScenario;

import java.util.Calendar;
import java.util.Collection;
import java.util.Random;

/**
 * Created by winston on 1/23/15.
 * Phase_01
 * CS 351 spring 2015
 *
 *  The world is everything that is the case.
 *  The world is the totality of facts, not of things.
 *  The facts in logical space are the world.
 *
 *  - L. W.
 */
public class World
{
  private Random random = new Random(44);
  private Collection<Region> world;
  private Collection<Country> politicalWorld;
  private Calendar currentDate;

  /**
   * Class constructor. To build a world one must have a collection of regions.
   * @param world collection of regions that we represent the world
   */
  public World(Collection<Region> world)
  {
    this(world, Calendar.getInstance());
  }

  /**
   * One can also build a world starting at a particular date.
   * @param world
   */
  public World(Collection<Region> world, Calendar cal)
  {
    this(world, CountryXMLparser.RegionsToCountries(world), cal);
  }


  public World(Collection<Region> world, Collection<Country> countries, Calendar cal)
  {
    this.world = world;
    this.politicalWorld = countries;
    this.currentDate = cal;
  }

  /**
   * Get the current time of this particular world.
   * @return a calendar object, with the date and time in side.
   */
  public Calendar getCurrentDate()
  {
    return currentDate;
  }

  /**
   * Set the world time to the given calendar date.
   * @param currentDate date the world will be after calling this method.
   */
  public void setCurrentDate(Calendar currentDate)
  {
    this.currentDate = currentDate;
  }

  /**
   * returns the year as an int.
   * @return
   */
  public int getCurrentYear()
  {
    return getCurrentDate().get(Calendar.YEAR);
  }

  /**
   * Advances the world forward by the given number of days. Every new month
   * the worlds attributes are randomly modulated.
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
   * @return
   */
  public int yearRemaining()
  {
    return AbstractScenario.END_YEAR - getCurrentYear();
  }
  
  /**
   * @return    world population at current world time, in millions as a double.
   */
  public double getWorldPopulation()
  {
    double totalPop = 0;
    int year = getCurrentYear();
    for (Country country:politicalWorld)
    {
      totalPop += country.getPopulation(year);
    }
    totalPop = totalPop/1000000;
    return totalPop;
  }
  
  
  /**
   * @return  percent of world's population that is happy at current world time
   */
  public double getWorldHappinessPercent()
  {
    double unhappyPeople = 0;
    int year = getCurrentYear();
    for (Country country:politicalWorld)
    {
      unhappyPeople += country.getUnhappyPeople(year);
    }
    double percentHappy = 1 - unhappyPeople/getWorldPopulation();
    return percentHappy;
  }
  
  
}
