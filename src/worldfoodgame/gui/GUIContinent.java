package worldfoodgame.gui;

import worldfoodgame.model.Continent;

import java.util.ArrayList;

/**
 * Created by Tim on 5/4/15. A class to hold a continents
 * GUI regions and check if it should be active.
 */
public class GUIContinent
{
  private ArrayList<GUIRegion> guiRegions;
  private boolean isActive = false;
  private Continent continent;

  public GUIContinent(ArrayList<GUIRegion> regions, Continent continent)
  {
    guiRegions = regions;
    this.continent = continent;
    for (GUIRegion gR : regions)
    {
      gR.setGUIContinent(this);
    }
  }

  public void setActive(boolean input)
  {
    isActive = input;
  }

  public boolean isActive()
  {
    return isActive;
  }

  public ArrayList<GUIRegion> getGUIRegions()
  {
    return guiRegions;
  }

  public Continent getContinent()
  {
    return continent;
  }
}