package model;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by winston on 1/23/15.
 * Phase_01
 * CS 351 spring 2015
 * <p/>
 * Class representing all the different attributes and crops that can be
 * associated with a given region. Essentially a data class.
 */
public class RegionAttributes
{

  /**
   * static map to represent all the limits of all the different attributes.
   */
  public final static Map<PLANTING_ATTRIBUTES, Double> LIMITS = new HashMap<>();

  static
  {
    LIMITS.put(PLANTING_ATTRIBUTES.PLANTING_ZONE, 13.0);
    LIMITS.put(PLANTING_ATTRIBUTES.ANNUAL_RAINFALL, 460.0);
    LIMITS.put(PLANTING_ATTRIBUTES.MONTHLY_RAINFALL, 10.0);
    LIMITS.put(PLANTING_ATTRIBUTES.POPULATION, 100_000.0);
    LIMITS.put(PLANTING_ATTRIBUTES.AVE_MONTH_TEMP_HI, 136.0);
    LIMITS.put(PLANTING_ATTRIBUTES.AVE_MONTH_TEMP_LO, -126.0);
    LIMITS.put(PLANTING_ATTRIBUTES.ELEVATION, 20_000.0);
    LIMITS.put(PLANTING_ATTRIBUTES.SOIL_TYPE, 14.0);
    LIMITS.put(PLANTING_ATTRIBUTES.PROFIT_FROM_CROPS, 1_000_000.0);
    LIMITS.put(PLANTING_ATTRIBUTES.COST_OF_CROPS, 1_000_000.0);
    LIMITS.put(PLANTING_ATTRIBUTES.HAPPINESS, 1.0);
  }

  private HashMap<PLANTING_ATTRIBUTES, Double> attSet = new HashMap<>();
  private HashMap<String, Double> crops = new HashMap<>();

  /**
   * Produces the value associated with the given attribute.
   *
   * @param att enum representing the given attribute.
   * @return double. Value associated with attribute,
   */
  public Double getAttribute(PLANTING_ATTRIBUTES att)
  {
    return attSet.get(att);
  }

  /**
   * Set the given attribute to the given value.
   *
   * @param att attribute to set.
   * @param x   value to store.
   */
  public void setAttribute(PLANTING_ATTRIBUTES att, double x)
  {
    attSet.put(att, x);
  }


  /**
   * Set crop growth by name
   *
   * @param name   name of crop
   * @param amount amount of crop grown, units arbitrary
   */
  public void setCrop(String name, double amount)
  {
    crops.put(name, amount);
  }

  /**
   * Get crop percentage by name
   *
   * @param name
   * @return
   */
  public double getCropP(String name)
  {
    if (!crops.containsKey(name)) return 0;

    double totalCrops = 0;
    for (String crop : crops.keySet())
    {
      totalCrops += crops.get(crop);
    }
    return crops.get(name) / totalCrops;
  }

  /**
   * Get crop growth by name
   *
   * @param name
   * @return
   */
  public double getCropGrowth(String name)
  {
    if (!crops.containsKey(name)) return 0;
    return crops.get(name);
  }


  public Collection<String> getAllCrops()
  {
    return crops.keySet();
  }


  @Override
  public String toString()
  {
    return "RegionAttributes{" +
      "attSet=" + attSet +
      ", crops=" + crops +
      '}';
  }


  /**
   * enum describing a set of potential planting-related attributes a region
   * might have.
   */
  public enum PLANTING_ATTRIBUTES
  {
    PLANTING_ZONE("Planting Zone"),
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

}
