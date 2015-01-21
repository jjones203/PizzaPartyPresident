package model;

import java.awt.*;
import java.util.ArrayList;

/**
 * Created by winston on 1/20/15.
 */
public class Region
{
  private ArrayList<Point> permineter;
  private String name;


  public String getName()
  {
    return name;
  }

  public void setName(String name)
  {
    this.name = name;
  }

  public ArrayList<Point> getPermineter()
  {
    return permineter;
  }

  public void setPermineter(ArrayList<Point> permineter)
  {
    this.permineter = permineter;
  }
}
