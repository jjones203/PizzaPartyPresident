package model;

import testing.generators.AttributeGenerator;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;

/**
 * Created by winston on 1/23/15.
 * Phase_01
 * CS 351 spring 2015
 */
public class World
{
  private Random random = new Random(44);
  private Collection<Region> world;
  private Calendar currentDate;


  public World()
  {
    this(new ArrayList<Region>(), Calendar.getInstance());
  }

  public World(Collection<Region> world)
  {
    this(world, Calendar.getInstance());
  }

  public World(Collection<Region> world, Calendar cal)
  {
    this.world = world;
    this.currentDate = cal;
  }

  public static void main(String[] args)
  {
    World world = new World();
    System.out.println(world.currentDate.getTime());
    world.stepByMonth();
    System.out.println(world.currentDate.getTime());
  }

  public Calendar getCurrentDate()
  {
    return currentDate;
  }

  public void setCurrentDate(Calendar currentDate)
  {
    this.currentDate = currentDate;
  }

  /**
   * advances the game world forward one month.
   */
  public void stepByMonth()
  {
    currentDate.add(Calendar.MONTH, 1);
    AttributeGenerator.stepAttributes(random, world);
  }

  public Collection<Region> getWorld()
  {
    return world;
  }

  public void setWorld(Collection<Region> world)
  {
    this.world = world;
  }
}
