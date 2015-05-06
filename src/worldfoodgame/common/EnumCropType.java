package worldfoodgame.common;

import worldfoodgame.model.CropClimateData;
// Added water use variables to the class, and changed the constructors accordingly
public enum EnumCropType implements CropClimateData
{
  WHEAT("tomatoes", WHEAT_MAX_TEMP, WHEAT_MIN_TEMP, WHEAT_DAY_TEMP, WHEAT_NIGHT_TEMP, WHEAT_MAX_RAIN, WHEAT_MIN_RAIN, WHEAT_WATER_USE),
  
  RICE("pineapples", RICE_MAX_TEMP, RICE_MIN_TEMP, RICE_DAY_TEMP, RICE_NIGHT_TEMP, RICE_MAX_RAIN, RICE_MIN_RAIN, RICE_WATER_USE),
  
  CORN("peppers", CORN_MAX_TEMP, CORN_MIN_TEMP, CORN_DAY_TEMP, CORN_NIGHT_TEMP, CORN_MAX_RAIN, CORN_MIN_RAIN, CORN_WATER_USE),
  
  SOY("'shrooms", SOY_MAX_TEMP, SOY_MIN_TEMP, SOY_DAY_TEMP, SOY_NIGHT_TEMP, SOY_MAX_RAIN, SOY_MIN_RAIN, SOY_WATER_USE),
  
  OTHER_CROPS("pepperoni"); // use constructor that sets fields to null because will vary by country
  
  public final String name;
  public final Integer maxTemp; // use Integer because need to be able to assign null
  public final Integer minTemp;
  public final Integer dayTemp;
  public final Integer nightTemp;
  public final Integer maxRain;
  public final Integer minRain;
  public final Double waterUse;
  EnumCropType(String name, int maxTemp, int minTemp, int dayTemp, int nightTemp, int maxRain, int minRain, double waterUse)
  {
    this.name = name;
    this.maxTemp = maxTemp;
    this.minTemp = minTemp;
    this.dayTemp = dayTemp;
    this.nightTemp = nightTemp;
    this.maxRain = maxRain;
    this.minRain = minRain;
    this.waterUse = waterUse;
  }
  
  EnumCropType(String name)
  {
    this.name = name;
    this.maxTemp = null;
    this.minTemp = null;
    this.dayTemp = null;
    this.nightTemp = null;
    this.maxRain = null;
    this.minRain = null;
    this.waterUse = 1.0e6; // a made-up value to represent pepperoni water use.
    //this.waterUse = null;
  }
  /**
   * This method allows us to get the old crop names when doing things like parsing
   * csv files.  Use in place of the toString method when you need the old names.
   * @return  The original crop toString names from Milestone 2
   */
  public String getOldName()
  {
    switch(name)
    {
      case "tomatoes":
      {
        return "wheat";
      }
      case "pineapples":
      {
        return "rice";
      }
      case "peppers":
      {
        return "corn";
      }
      case "'shrooms":
      {
        return "soy";
      }
      default:
      {
        return "other";
      }
    }
  }


  public String toString()
  {
    return name;
  }
  
  public static final int SIZE = values().length;
  
}
