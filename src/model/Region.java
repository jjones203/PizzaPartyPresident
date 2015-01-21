package model;


import java.util.List;


/**
 * Created by winston on 1/20/15.
 */
public class Region
{
  private List<MapPoint> perimeter;
  private String name;

  public String getName()
  {
    return name;
  }

  public void setName(String name)
  {
    this.name = name;
  }

  public List<MapPoint> getPerimeter()
  {
    return perimeter;
  }

  public void setPerimeter(List<MapPoint> perimeter)
  {
    this.perimeter = perimeter;
  }

  @Override
  public String toString()
  {
    return "Region{" +
        "name='" + name + '\'' +
        '}';
  }
}
