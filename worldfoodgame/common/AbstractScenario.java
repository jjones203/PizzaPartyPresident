package common;


public abstract class AbstractScenario
{
  public static final int START_YEAR = 2014;
  public static final int END_YEAR = 2050;
  public static final int YEARS_OF_SIM = (END_YEAR - START_YEAR) + 1;
  
  //Note: This class does not include a structure for the climate and 
  //      elevation data. 
  //      This is to allow the development team to choose the representation 
  //      that best fit the team's search and display implementations.
  // 
  //      Also absent from this class are abstract methods that may need to 
  //      use the climate or elevation data.
  
  protected double randomizationPercentage = 1.0; //Domain: [0.0, 1.0]
  protected double baseSeaLevelRisePerYear;
  
  protected int currentYear = START_YEAR;
  
  
  /** The cumulative total sea level rise from START_YEAR through the year 
   * corresponding to each element of seaLevelByYear. */
  protected double[] seaLevelByYear = new double[YEARS_OF_SIM];
  
  protected CropZoneData[] idealCropZone = new CropZoneData[EnumCropType.SIZE];
  
  public abstract CropZoneData.EnumCropZone getZone(EnumCropType crop,
      double minTemp, double maxTemp, double dayTemp, double nightTemp, double rain);
  
  
  
  /**
   * 1) Calculate sea level rise for the given year.
   * 2) Set the element of seaLevelByYear[] corresponding to the given year.
   * 
   * This method assumes that:
   * 1) randomizationPercentage has already been set.
   * 2) baseSeaLevelRisePerYear has already been set.
   * 3) year = START_YEAR OR seaLevelByYear[year-1] has already been set.
   *
   * @param year the year from START_YEAR through END_YEAR.
   * @return the calculated value of seaLevelByYear[year].
   */
  public abstract double calculateSeaLevelRise(int year);
  
  public int indexToYear(int i)
  {
    if (i<0 || i>=YEARS_OF_SIM) 
    { throw new IllegalArgumentException("worldfoodgame.common.indexToYear("+i+")");
    }
    return i+START_YEAR;
  }
  
  
  public int yearToIndex(int year)
  {
    if (year<START_YEAR || year>END_YEAR) 
    { throw new IllegalArgumentException("worldfoodgame.common.yearToIndex("+year+")");
    }
    return year-START_YEAR;
  }
}
