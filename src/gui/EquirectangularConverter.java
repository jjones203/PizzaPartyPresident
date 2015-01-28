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

/*      TODO Check if projection is flipping into graphics-land coords
 *          y(gfx) = -y(cart)
 */
public class EquirectangularConverter extends MapConverter
{

  private static final MapPoint DEFAULT_REF = new MapPoint(0,0);
  /* Converter scales projections using this value */
  public static final int SCALING_FACTOR = 10000;


  /**
   * Convert latitude to cartesian Y given a point of reference
   *
   * @param lat
   * @param refPoint
   * @return the latitude, scaled and projected in Y
   */
  public double latToY(double lat, MapPoint refPoint)
  {
    return lat * SCALING_FACTOR; /* silly, but keeps API consistent */
  }


  /**
   * Convert latitude to cartesian Y, assuming (0,0) is the point of reference
   * in the spherical coord system
   *
   * @param lat
   * @return the latitude, scaled and projected in Y
   */
  public double latToY(double lat){
    return latToY(lat, DEFAULT_REF);
  }


  /**
   * Convert longitude to cartesian X, given a reference point in spherical
   * coords
   *
   * @param lon   decimal longitude to convert
   * @param refPoint  mapPoint of reference
   * @return    longitude, scaled and projected in X
   */
  public double lonToX(double lon, MapPoint refPoint)
  {
    return lon * Math.cos(Math.toRadians(refPoint.getLat())) * SCALING_FACTOR;
  }


  /**
   * Convert longitude to cartesian X, assuming a reference point of (0,0) in
   * spherical coords
   * @param lon decimal longitude to convert
   * @return    longitude, scaled and projected in X
   */
  public double longToX(double lon)
  {
    return lonToX(lon, DEFAULT_REF);
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
