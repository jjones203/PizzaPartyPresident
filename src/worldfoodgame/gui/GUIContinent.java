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

  /**
   * Constructor sets the list of GUIRegions for this continent.
   * @param regions   Appropriate regions.
   * @param continent Continent this GUIContinent represents
   */
  public GUIContinent(ArrayList<GUIRegion> regions, Continent continent)
  {
    guiRegions = regions;
    this.continent = continent;
    for (GUIRegion gR : regions)
    {
      gR.setGUIContinent(this);
    }
  }

  /**
   * Sets the GUIContinent as selected or not
   * @param input
   */
  public void setActive(boolean input)
  {
    isActive = input;
  }

  /**
   * @return  Is selected
   */
  public boolean isActive()
  {
    return isActive;
  }

  /**
   * @return  GUIRegions in this continent
   */
  public ArrayList<GUIRegion> getGUIRegions()
  {
    return guiRegions;
  }

  /**
   * @return Continent this GUIContinent represents
   */
  public Continent getContinent()
  {
    return continent;
  }
}