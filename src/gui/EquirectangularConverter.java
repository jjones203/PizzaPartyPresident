package gui;

import model.MapPoint;

import java.awt.*;
import java.awt.geom.Rectangle2D;

/**
 * @author david
 *         created: 2015-01-26
 *         <p/>
 *         description:  Converter implementation for Equirectangular map
 *         projections with a constant scaling factor
 */
public class EquirectangularConverter extends MapConverter
{

  /* Converter scales projections using this value */
  public static final int SCALING_FACTOR = 10000;

  /**
   * Convert latitude to cartesian Y
   *
   * @param lat
   * @param latRef
   * @return
   */
  public double latToY(double lat, double latRef)
  {
    return lat * SCALING_FACTOR; /* silly, but keeps API consistent */
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
    return lon * Math.cos(Math.toRadians(refPoint.getLat())) * SCALING_FACTOR;
  }


  /*
   * testing rectangle projections
   */
  public Rectangle2D convertRect(Rectangle2D rect, MapPoint refPoint)
  {
    double newX = lonToX(rect.getX(), refPoint) * SCALING_FACTOR;
    double newW = newX - lonToX(rect.getMaxX(), refPoint) * SCALING_FACTOR;
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
    int x = (int) mp.getLon() * SCALING_FACTOR;
    int y = (int) mp.getLat() * SCALING_FACTOR;
    return new Point(x, y);
  }

  /**
   * Convert a Point to a MapPoint assuming the parallel of no distortion is
   * the equator.  This converts from a Plate-Caree projection back to lat and
   * lon
   *
   * @param p
   * @return
   */
  @Override
  public MapPoint pointToMapPoint(Point p)
  {
    return new MapPoint(p.x/SCALING_FACTOR, p.y/SCALING_FACTOR);
  }

  public double yToLon(double y, MapPoint refPoint)
  {
    return y/(SCALING_FACTOR * Math.cos(Math.toRadians(refPoint.getLat())));
  }

  public double xToLat(double x, MapPoint refPoint)
  {
    return x/SCALING_FACTOR;
  }
}
