package worldfoodgame.model;

import java.util.List;

/**
 * Created by winston on 1/23/15.
 * Phase_01
 * CS 351 spring 2015
 *
 *
 * this Interface was useless.
 */
public interface Region
{
  String getName();

  void setName(String name);

  List<MapPoint> getPerimeter();

  void setPerimeter(List<MapPoint> perimeter);

  void setCountry(Country country);

  boolean containsMapPoint(MapPoint mapPoint);
  Country getCountry();

}
