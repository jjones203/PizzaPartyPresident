package model;

import java.util.List;

/**
 * Created by winston on 1/23/15.
 * Phase_01
 * CS 351 spring 2015
 *
 */
public interface Region
{
  String getName();

  void setName(String name);

  List<MapPoint> getPerimeter();

  void setPerimeter(List<MapPoint> perimeter);

  public RegionAttributes getAttributes();

  public void setAttributes(RegionAttributes attributes);
}
