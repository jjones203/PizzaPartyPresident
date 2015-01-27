package model;


import java.util.List;


/**
 * Created by winston on 1/20/15.
 */
public class AtomicRegion implements Region
{
  private List<MapPoint> perimeter;
  private String name;
  private RegionAttributes attributes;

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
