package model;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by winston on 1/23/15.
 * Phase_01
 * CS 351 spring 2015
 */
public class RegionAttributes
{

  public enum PLANTING_ATTRIBUTES
  {
    PLANTING_ZONE("Planting Zone"),
//    PERCENTAGE_CROPS, // this is a complex value?
    ANNUAL_RAINFALL("Annual Rainfall"),
    MONTHLY_RAINFALL("Monthly Rainfall"),
    AVE_MONTH_TEMP_HI("Average Month High temp"),
    AVE_MONTH_TEMP_LO("Average Month Low Temp"),
    POPULATION("Population"),
    COST_OF_CROPS("Cost"),
    PROFIT_FROM_CROPS("Profit"),
    ELEVATION("Elevation"),
    HAPPINESS("Happiness"),
    SOIL_TYPE("Soil Type");


    private String prettyPrint;

    private PLANTING_ATTRIBUTES(String prettyPrint)
    {
      this.prettyPrint = prettyPrint;
    }

    @Override
    public String toString()
    {
      return prettyPrint;
    }
  }

  private HashMap<PLANTING_ATTRIBUTES, Double> attSet = new HashMap<>();
  private HashMap<String, Double> crops = new HashMap<>();

  public Double getAttribute(PLANTING_ATTRIBUTES att)
  {
    return attSet.get(att);
  }

  public void setAttribute(PLANTING_ATTRIBUTES att, double x)
  {
    attSet.put(att, x);
  }

  public void addCrop(String name, double percentage)
  {
    crops.put(name, percentage);
  }

  public Double getCropP(String name)
  {
    Double res = crops.get(name);
    return res;
  }


  public Collection<String> getAllCrops()
  {
    return crops.keySet();
  }

  @Deprecated
  public Map<String, Double> getAllCropsPercentage()
  {
    return new HashMap<>(crops); // to keep things safe?
  }


  @Override
  public String toString()
  {
    return "RegionAttributes{" +
        "attSet=" + attSet +
        ", crops=" + crops +
        '}';
  }

  public static void main(String[] args)
  {
    RegionAttributes atts = new RegionAttributes();
    atts.setAttribute(PLANTING_ATTRIBUTES.ANNUAL_RAINFALL, 34);

    System.out.println(atts.getAttribute(PLANTING_ATTRIBUTES.ANNUAL_RAINFALL));
    System.out.println(atts.getAttribute(PLANTING_ATTRIBUTES.COST_OF_CROPS));

    atts.addCrop("corn", 45.0);
    atts.addCrop("Guinness", 55);

    System.out.println(atts.getCropP("corn"));

    for (String name : atts.getAllCropsPercentage().keySet())
    {
      double p = atts.getCropP(name);
      System.out.println(name + " at %" + p);
    }

    System.out.println(atts);

  }

}
