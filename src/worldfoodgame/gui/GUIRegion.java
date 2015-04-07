package worldfoodgame.gui;

import worldfoodgame.gui.displayconverters.MapConverter;
import worldfoodgame.gui.regionlooks.RegionView;
import worldfoodgame.model.Country;
import worldfoodgame.model.MapPoint;
import worldfoodgame.model.Region;

import java.awt.*;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;

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

  private Boolean isPrimaryRegion;

  public GUIRegion(Region region, MapConverter converter, RegionView look)
  {
    this.region = region;
    this.converter = converter;
    this.look = look;

    isPrimaryRegion = null;
  }

  public boolean isPrimaryRegion()
  {
    if (isPrimaryRegion == null)
    {
      MapPoint mapPoint = getRegion().getCountry().getCapitolLocation();
      Point point = converter.mapPointToPoint(mapPoint);
      isPrimaryRegion = getPoly().getBounds().contains(point);
    }
    return isPrimaryRegion;
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

  public boolean intersects(Rectangle2D r)
  {
    return getPoly().intersects(r);
  }

  /**
   * Returns the country associated with a given guiRegion, deligation.
   * @return
   */
  public Country getCountry()
  {
    return region.getCountry();
  }

  public boolean contains(double x, double y)
  {
    return getPoly().contains(x, y);

  }
}
