package gui.views;

import gui.MapConverter;
import model.Region;

import java.awt.*;
import java.awt.geom.Area;

/**
 * Created by winston on 1/23/15.
 * Phase_01
 * CS 351 spring 2015
 */
public class GUIRegion
{
  private Region region;
  private MapConverter converter; //set at class def.
  private Area area;
  private RegionView look = new PassiveRegion(); // as default value
  private Polygon poly;

  public GUIRegion(Region region)
  {
    this.region = region;
  }

  public Polygon getPoly()
  {
    if (poly == null)
    {
      poly = converter.regionToPolygon(region);
    }
    return poly;
  }


  public Area getArea()
  {
    if (area == null)
    {
      area = new Area(getPoly());
    }
    return area;
  }

  public void draw(Graphics g)
  {
    look.draw(g, this);
  }
}
