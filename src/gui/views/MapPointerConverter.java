package gui.views;

import model.MapPoint;
import model.Region;

import java.awt.*;
import java.awt.geom.Area;

/**
 * Created by winston on 1/23/15.
 * Phase_01
 * CS 351 spring 2015
 */
public class MapPointerConverter implements MapProjections
{
  /*
   * defining a draft for an API of sorts...
   */

  public Area regionToArea(Region r)
  {
    Polygon polygon = new Polygon();

    for (MapPoint mp : r.getPerimeter())
    {
      Point p = new Point(mapPointToPoint(mp));
      polygon.addPoint(p.x, p.y);
    }
    return new Area(polygon);
  }

  private Point mapPointToPoint(MapPoint mp)
  {
    return null;
  }
}
