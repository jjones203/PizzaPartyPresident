package worldfoodgame.model;


import worldfoodgame.common.AbstractCountryBorderData;

import java.awt.geom.Path2D;
import java.util.List;


/**
 * Represent a homogeneous area. Defined by a perimeter and various planting
 * attributes. The class acts as a kind of container for the parsed XML data.
 *
 * @author winston riley
 */
public class AtomicRegion extends AbstractCountryBorderData implements Region
{
  private Country country;
  private List<MapPoint> perimeter;
  private String name;
  private RegionAttributes attributes;

  private Path2D path;


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
    if (path == null) path = makePath();
    return path.contains(mapPoint.getLon(), mapPoint.getLat());
  }

  // constructs a path object representing the Region.
  private Path2D makePath()
  {
    Path2D path = new Path2D.Double();
    for (MapPoint mapPoint :perimeter)
    {
      path.moveTo(mapPoint.getLon(), mapPoint.getLat());
    }
    path.clone();
    return path;
  }

  @Override
  public RegionAttributes getAttributes()
  {
    return attributes;
  }

  @Override
  public void setAttributes(RegionAttributes attributes)
  {
    this.attributes = attributes;
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
