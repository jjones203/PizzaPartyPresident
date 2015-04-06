package worldfoodgame.model;


import worldfoodgame.common.AbstractCountryBorderData;
import worldfoodgame.gui.displayconverters.EquirectangularConverter;
import worldfoodgame.gui.displayconverters.MapConverter;

import java.awt.*;
import java.util.List;


/**
 * Represent a homogeneous area. Defined by a perimeter and various planting
 * attributes. The class acts as a kind of container for the parsed XML data.
 *
 * @author winston riley
 */
public class AtomicRegion extends AbstractCountryBorderData implements Region
{
  //todo clean up mapconverters
  private static MapConverter mapConverter = new EquirectangularConverter();
  private Country country;
  private List<MapPoint> perimeter;
  private String name;

  private Polygon mapSpacePoly;


  @Override
  public Country getCountry()
  {
    return country;
  }

  @Override
  public void setCountry(Country country)
  {
    this.country = country;
    country.addRegion(this);
  }

  @Override
  public boolean containsMapPoint(MapPoint mapPoint)
  {
    if (mapSpacePoly == null) mapSpacePoly = mapConverter.regionToPolygon(this);

    Point point = mapConverter.mapPointToPoint(mapPoint);
    return mapSpacePoly.contains(point);
  }


  @Override
  public String getName()
  {
    return name;
  }


  @Override
  public void setName(String name)
  {
    this.name = name;
  }


  @Override
  public List<MapPoint> getPerimeter()
  {
    return perimeter;
  }


  @Override
  public void setPerimeter(List<MapPoint> perimeter)
  {
    this.perimeter = perimeter;
  }


  public String toString()
  {
    return "AtomicRegion{" +
      "name='" + name + '\'' +
      '}';
  }
}
