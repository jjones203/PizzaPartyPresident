package gui;

import model.MapPoint;
import model.Region;

import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

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
    return -lat * SCALING_FACTOR; /* silly, but keeps API consistent */
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
  public double lonToX(double lon)
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
    int x = (int) lonToX(mp.getLon()) * SCALING_FACTOR;
    int y = (int) latToY(mp.getLat()) * SCALING_FACTOR;
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
    return -y/(SCALING_FACTOR * Math.cos(Math.toRadians(refPoint.getLat())));
  }

  public double xToLat(double x, MapPoint refPoint)
  {
    return x/SCALING_FACTOR;
  }

  @Override
  public Polygon regionToPolygon(Region r)
  {

    Polygon poly = new Polygon();
    for(MapPoint mPoint : r.getPerimeter())
    {
      int x = (int) lonToX(mPoint.getLon());
      int y = (int) latToY(mPoint.getLat());
      poly.addPoint(x, y);
    }

    return poly;
  }


  /**
   * generates a projected grid of latitude and longitude lines converted to
   * the scaled cartesian space
   *
   * @return
   */
  public List <Line2D> getLatLonGrid()
  {

    List<Line2D> lines = new ArrayList<>();
    int maxLat = 90;
    int maxLon = 180;
    int inc = 5;

    for (int lon = -maxLon; lon <= maxLon; lon += inc)
    {
      double x = lonToX(lon);
      double y = latToY(maxLat);
      Line2D l = new Line2D.Double(x, y, x, -y);
      lines.add(l);
    }
    for (int lat = -maxLat; lat <= maxLat; lat+= inc)
    {
      double y = latToY(lat);
      double x = lonToX(maxLon);
      Line2D.Double l = new Line2D.Double(x, y, -x, y);
      lines.add(l);
    }
    return lines;
  }
}
