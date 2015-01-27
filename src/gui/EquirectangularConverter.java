package gui;

import model.MapPoint;

import java.awt.*;
import java.awt.geom.Rectangle2D;

/**
 * @author david
 *         created: 2015-01-26
 *         <p/>
 *         description:  Converter implementation for Equirectangular map
 *         projections
 */
public class EquirectangularConverter extends MapConverter
{

  /**
   * Convert latitude to cartesian Y
   *
   * @param lat
   * @param latRef
   * @return
   */
  public double latToY(double lat, double latRef)
  {
    return lat; /* silly, but keeps API consistent */
  }

  /**
   * Convert longitude to cartesian X
   *
   * @param lon
   * @param refPoint
   * @return
   */
  public double lonToX(double lon, MapPoint refPoint)
  {
    return lon * Math.cos(Math.toRadians(refPoint.getLat()));
  }

  /*
   * convert a floating point shape from spherical to cartesian
   */
  public Rectangle2D convertRect(Rectangle2D rect, MapPoint refPoint)
  {
    double newX = lonToX(rect.getX(), refPoint);
    double newW = newX - lonToX(rect.getMaxX(), refPoint);
    return new Rectangle2D.Double(newX, rect.getY(), newW, rect.getHeight());
  }

  /**
   * Convert a MapPoint (lat, lon) to a cartesian point, assuming the parallel
   * of no distortion is the equator.  This is a Plate-Caree projection.
   *
   * @param mp  MapPoint to convert
   * @return
   */
  @Override
  public Point mapPointToPoint(MapPoint mp)
  {
    return new Point((int)mp.getLon(), (int)mp.getLat());
  }

  /**
   * Convert a Point to a MapPoint assuming the parallel of no distortioin is
   * the equator.  This converts from a Plate-Caree projection back to lat and
   * lon
   *
   * @param p
   * @return
   */
  @Override
  public MapPoint pointToMapPoint(Point p)
  {
    return new MapPoint(p.x, p.y);
  }

  public double yToLon(double y, MapPoint refPoint)
  {
    return y/Math.cos(Math.toRadians(refPoint.getLat()));
  }

  public double xToLat(double x, MapPoint refPoint)
  {
    return x;
  }
}
