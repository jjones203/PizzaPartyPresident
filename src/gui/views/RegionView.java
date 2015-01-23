package gui.views;

import model.Region;

import java.awt.geom.Area;

/**
 * Created by winston on 1/23/15.
 * Phase_01
 * CS 351 spring 2015
 */
public class RegionView
{
  private Region region;
  private MapConverter converter; //set at class def.
  private Area area;


  public RegionView(Region region)
  {
    this.region = region;
  }

  public Area getArea()
  {
    if (area == null)
    {
      area = converter.regionToArea(region);
    }
    return area;
  }
}
