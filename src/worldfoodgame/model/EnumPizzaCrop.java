package worldfoodgame.model;



public enum EnumPizzaCrop implements CropClimateData
{
  
  TOMATOES("tomatoes", WHEAT_MAX_TEMP, WHEAT_MIN_TEMP, WHEAT_DAY_TEMP, WHEAT_NIGHT_TEMP,
           WHEAT_MAX_RAIN, WHEAT_MIN_RAIN, WHEAT_WATER_USE),
  PEPPERONI("pepperoni"),
  PEPPERS("bell peppers", CORN_MAX_TEMP, CORN_MIN_TEMP, CORN_DAY_TEMP, CORN_NIGHT_TEMP,
          CORN_MAX_RAIN, CORN_MIN_RAIN, CORN_WATER_USE),
  MUSHROOMS("'shrooms", SOY_MAX_TEMP, SOY_MIN_TEMP, SOY_DAY_TEMP, SOY_NIGHT_TEMP,
            SOY_MAX_RAIN, SOY_MIN_RAIN, SOY_WATER_USE),
  PINEAPPLE("pineapples", RICE_MAX_TEMP, RICE_MIN_TEMP, RICE_DAY_TEMP, RICE_NIGHT_TEMP,
            RICE_MAX_RAIN, RICE_MIN_RAIN, RICE_WATER_USE);

  public final String name;
  public final Integer maxTemp; // use Integer because need to be able to assign null
  public final Integer minTemp;
  public final Integer dayTemp;
  public final Integer nightTemp;
  public final Integer maxRain;
  public final Integer minRain;
  public final Long waterUse;


  EnumPizzaCrop(String name, int maxTemp, int minTemp, int dayTemp, int nightTemp, int maxRain, int minRain, long waterUse)
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

  EnumPizzaCrop(String name)
  {
    this.name = name;
    this.maxTemp = null;
    this.minTemp = null;
    this.dayTemp = null;
    this.nightTemp = null;
    this.maxRain = null;
    this.minRain = null;
    this.waterUse = null;
  }
  

  public String toString()
  {
    return name;
  }

  public static final int SIZE = values().length;

}
