package gui;

import gui.displayconverters.MapConverter;
import gui.regionlooks.RegionView;
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
  private boolean isActive;
  private RegionView look;
  private Polygon poly;


  public GUIRegion(Region region, MapConverter converter, RegionView look)
  {
    this.region = region;
    this.converter = converter;
    this.look = look;
  }

  public boolean isActive()
  {
    return isActive;
  }

  public void setActive(boolean isActive)
  {
    this.isActive = isActive;
  }

  public RegionView getLook()
  {
    return look;
  }

  public void setLook(RegionView look)
  {
    this.look = look;
  }

  public String getName()
  {
    return region.getName();
  }

  public Polygon getPoly()
  {
    if (poly == null)
    {
      poly = converter.regionToPolygon(region);
    }
    return poly;
  }

  public double getSurfaceArea()
  {
    return getPoly().getBounds().getWidth() * getPoly().getBounds().getHeight();
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

  public Region getRegion()
  {
    return region;
  }
}
