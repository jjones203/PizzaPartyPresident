package model;

import java.awt.*;

/**
 * Created by winston on 1/20/15.
 */
public class MapPoint
{
  private double lat;
  private double lon;

  public MapPoint(double lat, double lon)
  {
    this.lat = lat;
    this.lon = lon;
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

  @Override
  public String toString()
  {
    return "MapPoint{" +
            "lat=" + lat +
            ", lon=" + lon +
            '}';
  }
}
