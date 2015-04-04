package worldfoodgame.model;


import java.awt.geom.Point2D;

/**
 * Data class representing long and lat on a map.
 */
public class MapPoint
{
  private double lat;
  private double lon;

  public MapPoint(double lon, double lat)
  {
    this.lat = lat;
    this.lon = lon;
  }

  public MapPoint(MapPoint source)
  {
    this.lat = source.lat;
    this.lon = source.lon;
  }

  public double getLat()
  {
    return lat;
  }

  public void setLat(double lat)
  {
    this.lat = lat;
  }

  public double getLon()
  {
    return lon;
  }

  public void setLon(double lon)
  {
    this.lon = lon;
  }
  
  public double distance(MapPoint p)
  {
    return Point2D.distance(p.lon, p.lat, lon, lat);
  }
  
  public double distanceSq(MapPoint p)
  {
    return Point2D.distanceSq(p.lon, p.lat, lon, lat);
  }

  @Override
  public String toString()
  {
    return "MapPoint{" +
            "lat=" + lat +
            ", lon=" + lon +
            '}';
  }
}
