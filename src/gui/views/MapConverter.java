package gui.views;

import model.MapPoint;
import model.Region;

import java.awt.*;
import java.awt.geom.Area;
import java.util.Map;

/**
 * Created by winston on 1/23/15.
 * Phase_01
 * class is used to convert between the model and the GUI
 * for converting between lat lon =>  x, y and back
 */
public abstract class MapConverter
{

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


  public abstract Point mapPointToPoint(MapPoint mp);


  public abstract MapPoint pointToMapPoint(Point p);

}
