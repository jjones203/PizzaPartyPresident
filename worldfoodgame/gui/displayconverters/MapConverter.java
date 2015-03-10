package gui.displayconverters;

import model.MapPoint;
import model.Region;

import java.awt.*;
import java.awt.geom.Line2D;

/**
 * Created by winston on 1/23/15.
 * Phase_01
 *
 * class is used to convert between the model and the GUI
 * for converting between lat lon =>  x, y and back.
 */
public abstract class MapConverter
{

  /**
   * Transforms the given Region object into a polygon suitable for drawing.
   * @param r region object to be transformed.
   * @return polygon representing the region in the appropriate map projection.
   */
  public Polygon regionToPolygon(Region r)
  {
    Polygon polygon = new Polygon();
    for (MapPoint mp : r.getPerimeter())
    {
      Point p = new Point(mapPointToPoint(mp));
      polygon.addPoint(p.x, p.y);
    }
    return polygon;
  }

  /**
   * Factor that the map points are scaled by.
   * @return double representing the conversion scaled between
   * gui space and map.modle space.
   */
  public abstract double getScale();


  /**
   * Converts a latitude point to a y value.
   * @param lat latitude measurement to be converted.
   * @return double y, in gui/Cartesian space.
   */
  public abstract double latToY(double lat);

  /**
   * Converts a longitude point to a y value.
   * @param lon longitude measurement to be converted.
   * @return double x, in gui/Cartesian space.
   */
  public abstract double lonToX(double lon);

  /**
   * Converts a point on a map given in latitude and longitude, and transforms
   * it into an x,y point on a Cartesian system, suitable for drawing.
   * @param mp point defined in map space.
   * @return converted point.
   */
  public abstract Point mapPointToPoint(MapPoint mp);

  /**
   * in the inverse function of mapPointToPoint. Given a point in Cartesian
   * space, returns a point in the map space.
   * @param p point defined in gui space
   * @return converted point.
   */
  public abstract MapPoint pointToMapPoint(Point p);

  /**
   * Returns a conventional grid in latitude and longitude as defined by the
   * converter.
   *
   * @return list of line2d objects representing a grid.
   */
  public abstract java.util.List<Line2D> getLatLonGrid();

}
