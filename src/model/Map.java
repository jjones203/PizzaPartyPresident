package model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;

/**
 * Created by winston on 1/23/15.
 * Phase_01
 * CS 351 spring 2015
 */
public class Map
{
  private Collection<Region> world;

  public Map()
  {
    this(new ArrayList<Region>());
  }

  public Map(Collection<Region> world)
  {
    this.world = world;
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
