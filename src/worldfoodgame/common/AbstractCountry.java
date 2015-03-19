package worldfoodgame.common;

import java.awt.image.BufferedImage;

public abstract class AbstractCountry
{
  protected String name;
  protected BufferedImage flag;
  protected double shippingLongitude;
  protected double shippingLatitude;
  
  protected static final int YEARS_OF_SIM = AbstractScenario.YEARS_OF_SIM;
  
  
  protected AbstractCountryBorderData border;
  protected AbstractCropLocations cropLocationsCurrentYear;
  protected AbstractCropLocations cropLocationsPreviousYear;
  
  //Note: Since there are only about 200 countries, it does not take much 
  //      space to maintain all annual country data values from the start of the 
  //      model through the current year of the model.
  //      This information may be useful in milestone III for
  //      a) displaying graphs
  //      b) improved prediction algorithms that use multi-year trends.
   
  
  //Note: This class does not include fields for unused arable land, annual 
  //      crop consumption for each country, annual unhappy people for each 
  //      country and other quantities which can be easily calculated 
  //      on demand form the included fields.
  
  //Note: Values below that are, in the countryData.cvs file, defined as type int 
  //      should be displayed to the user as type int. However, as some of these
  //      values may be adjusted from year-to-year by continuous functions, it 
  //      is useful to maintain their internal representations as doubles.
  
  //Note: As the start of milestone II, birthRate and migrationRate are assumed constant
  //      for each year of the model and medianAge is not used at all. 
  //      However, it might be that before the end of milestone II, we are given simple functions
  //      for these quantities: such as a constant rate of change of birth rate replacing the 
  //      constant birth rate or a data lookup table that has predefined values for each year.
  //      The development team should expect and plan for such mid-milestone updates.
  protected int[] population = new int[YEARS_OF_SIM];       //in people
  protected double[] medianAge = new double[YEARS_OF_SIM];  //in years
  protected double[] birthRate = new double[YEARS_OF_SIM];  //number of live births per 1,000 individuals per year.
  protected double[] mortalityRate = new double[YEARS_OF_SIM];   //number of deaths per 1,000 individuals per year.
  protected double[] migrationRate = new double[YEARS_OF_SIM];   //immigration - emigration per 1,000 individuals.
  protected double[] undernourished = new double[YEARS_OF_SIM];  // percentage of population. 0.50 is 50%.

  protected double[][] cropProduction = new double[EnumCropType.SIZE][YEARS_OF_SIM]; //in metric tons.
  protected double[][] cropExport     = new double[EnumCropType.SIZE][YEARS_OF_SIM]; //in metric tons.
  protected double[][] cropImport     = new double[EnumCropType.SIZE][YEARS_OF_SIM]; //in metric tons.
  
  protected double[] landTotal  = new double[YEARS_OF_SIM];  //in square kilometers
  protected double[] landArable = new double[YEARS_OF_SIM];  //in square kilometers
  protected double[][] landCrop = new double[EnumCropType.SIZE][YEARS_OF_SIM];  //in square kilometers
  
  //Note: in milestone II, the model does nothing with the cultivation method.
  protected double[][] cultivationMethod = new double[EnumGrowMethod.SIZE][YEARS_OF_SIM]; //percentage
  
  //In Milestone II, crop yield and per capita need are defined in the first year and assumed constant 
  //    throughout each year of the simulation.
  //    This will NOT be changed before the end of milestone II as it would require redefining the core of
  //    the model's calculations.
  protected double[] cropYield = new double[EnumCropType.SIZE]; //metric tons per square kilometer
  protected double[] cropNeedPerCapita = new double[EnumCropType.SIZE]; //metric tons per person per year.
}
