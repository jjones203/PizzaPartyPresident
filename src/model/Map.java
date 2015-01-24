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
  private Collection<Region> selectedRegions;

  public Map()
  {
    this(new ArrayList<Region>(), new LinkedList<Region>());
  }

  public Map(Collection<Region> world, Collection<Region> selectedRegions)
  {
    this.world = world;
    this.selectedRegions = selectedRegions;
  }

  public Collection<Region> getSelectedRegions()
  {
    return selectedRegions;
  }

  public void addRegionToSelected(Region r)
  {
    selectedRegions.add(r);
  }

  public boolean deselect(Region r)
  {
    return selectedRegions.remove(r);
  }

  public void setSelectedRegions(Collection<Region> selectedRegions)
  {
    this.selectedRegions = selectedRegions;
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
