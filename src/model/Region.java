package model;


import java.util.List;


/**
 * Created by winston on 1/20/15.
 */
public class Region
{
  private List<MapPoint> permineter;
  private String name;

  public String getName()
  {
    return name;
  }

  public void setName(String name)
  {
    this.name = name;
  }

  public List<MapPoint> getPermineter()
  {
    return permineter;
  }

  public void setPermineter(List<MapPoint> permineter)
  {
    this.permineter = permineter;
  }

  @Override
  public String toString()
  {
    return "Region{" +
            "name='" + name + '\'' +
            '}';
  }
}
