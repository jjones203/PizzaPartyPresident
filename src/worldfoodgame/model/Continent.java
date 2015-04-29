 package worldfoodgame.model;

 import java.util.ArrayList;
 import java.util.Collection;
 import java.util.List;
 import java.awt.Color;

 import worldfoodgame.common.AbstractScenario;
 import worldfoodgame.common.EnumCropType;
 import worldfoodgame.common.EnumGrowMethod;
 import worldfoodgame.gui.ColorsAndFonts;

 /**
 * @author jessica
 * @version 4/26/15
 */
public class Continent implements CropClimateData
{
  private static boolean VERBOSE = false;
  
  private EnumContinentNames name;
  private int START_YEAR = AbstractScenario.START_YEAR;
  private int YEARS_OF_SIM = AbstractScenario.YEARS_OF_SIM;
  private List<Country> countries;
  private int numCountries;
  private Collection<LandTile> landTiles;
  private Color color;

  protected double waterAllowance;
  protected int[] population = new int[YEARS_OF_SIM];       //in people
  protected double[] undernourished = new double[YEARS_OF_SIM];  // percentage of population. 0.50 is 50%.
  
  protected double[][] conventionalYield = new double[EnumCropType.SIZE][YEARS_OF_SIM]; //metric tons per square kilometer
  protected double[][] organicYield = new double[EnumCropType.SIZE][YEARS_OF_SIM]; //metric tons per square kilometer
  protected double[][] gmoYield = new double[EnumCropType.SIZE][YEARS_OF_SIM]; //metric tons per square kilometer
  
  protected double[] pizzaPreference = new double[EnumCropType.SIZE]; // % of population wanting each kind of pizza
  
  protected double[][] cropProduction = new double[EnumCropType.SIZE][YEARS_OF_SIM]; //in metric tons.
  protected double[][] cropExport     = new double[EnumCropType.SIZE][YEARS_OF_SIM]; //in metric tons.
  protected double[][] cropImport     = new double[EnumCropType.SIZE][YEARS_OF_SIM]; //in metric tons.
  
  protected double landTotal;  //in square kilometers
  protected double[] landArable = new double[YEARS_OF_SIM];  //in square kilometers
  protected double[][] landCrop = new double[EnumCropType.SIZE][YEARS_OF_SIM];  //in square kilometers
  
  protected double[][] cultivationMethod = new double[EnumGrowMethod.SIZE][YEARS_OF_SIM]; //percentage
  
  protected int approvalRating;
  protected int diplomacyRating;
  
  
  /**
   * Continent constructor
   * @param name continent name
   */
  public Continent(EnumContinentNames name)
  {
    this.name = name;
    switch (name)
    {
      case N_AMERICA:
        color = ColorsAndFonts.N_AMERICA;
        break;
      case S_AMERICA:
        color = ColorsAndFonts.S_AMERICA;
        break;
      case AFRICA:
        color = ColorsAndFonts.AFRICA;
        break;
      case ASIA:
        color = ColorsAndFonts.ASIA;
        break;
      case EUROPE:
        color = ColorsAndFonts.EUROPE;
        break;
      case MIDDLE_EAST:
        color = ColorsAndFonts.M_EAST;
        break;
      case OCEANIA:
        color = ColorsAndFonts.OCEANIA;
        break;
      default:
        color = ColorsAndFonts.PASSIVE_REGION;
        break;
    }
    countries = new ArrayList<>();
    numCountries = 0;
    landTiles = new ArrayList<>();
  }

  public Color getColor()
  {
    return color;
  }

  /**
   * Add country to continent's country list and its data to continent totals
   * @param country
   */
  public void addCountry(Country country)
  {
    countries.add(country);
    numCountries++;
    // add population
    for (int i = 0; i < YEARS_OF_SIM; i++)
    {
      population[i] += country.getPopulation(i+START_YEAR);
    }
    
    // add tiles
    landTiles.addAll(country.getLandTiles());
  }
  
  /**
   * Initialize fields that depend on average of all countries' values
   */
  public void initializeData()
  {
    // using old crop data, get avg yield from countries, assign to continent
    double[] cropYieldTotals = new double[EnumCropType.SIZE];
    double organicTotal = 0;
    double gmoTotal = 0;
    double undernourishedTotal = 0;

    // loop through countries, total values
    for (Country country:countries)
    {
      for (EnumCropType crop:EnumCropType.values())
      { 
        // total yields so we can average them
        cropYieldTotals[crop.ordinal()] += country.getCropYield(START_YEAR, crop);
      }
      waterAllowance += country.getWaterAllowance();

      organicTotal += country.getMethodPercentage(START_YEAR, EnumGrowMethod.ORGANIC);
      gmoTotal += country.getMethodPercentage(START_YEAR, EnumGrowMethod.GMO);
      undernourishedTotal += country.getUndernourished(START_YEAR);
    }
    
    // set continent fields using average of country values
    for (EnumCropType crop:EnumCropType.values())
    {
      int cropIndex = crop.ordinal();
      // continent's conventional yield is average of country's yields
      double conventionalYield = cropYieldTotals[cropIndex]/numCountries;
      initializeYield(crop, conventionalYield);
    }
    
    // set percentages for gmo, organic, conventional
    double organicAvg = organicTotal/numCountries;
    double gmoAvg = gmoTotal/numCountries;
    setMethodPercentage(START_YEAR, EnumGrowMethod.ORGANIC, organicAvg);
    setMethodPercentage(START_YEAR, EnumGrowMethod.GMO, gmoAvg);
    if ((organicAvg + gmoAvg) < 0 || (organicAvg + gmoAvg) > 1)
    {
      if (VERBOSE) System.err.println("gmo + organic % for continent "+this.toString()+" not between 0 and 1");
    }
    else
    {
      double conventionalAvg = 1 - (organicAvg + gmoAvg);
      setMethodPercentage(START_YEAR, EnumGrowMethod.CONVENTIONAL, conventionalAvg);
    }
    // set undernourished
    setUndernourished(START_YEAR, undernourishedTotal/numCountries);
  }
  
  public EnumContinentNames getName()
  {
    return name;
  }
  
  public String toString()
  {
    return name.toString();
  }
  
  public List<Country> getCountries()
  {
    return countries;
  }
  
  public Collection<LandTile> getLandTiles()
  {
    return landTiles;
  }
  
  public int getPopulation(int year)
  {
    return population[year - START_YEAR];
  }
  
  public double getUndernourished(int year)
  {
    return undernourished[year - START_YEAR];
  }
  
  public void setUndernourished(int year, double percentage)
  {
    if (percentage >= 0 && percentage <= 1)
    {
      undernourished[year - START_YEAR] = percentage;
    }
  }
  
  public double getPizzaPreference(EnumCropType pizzaType)
  {
    return pizzaPreference[pizzaType.ordinal()];
  }
  
  public void setPizzaPreference(EnumCropType pizzaType, double percent)
  {
    if (percent >= 0 && percent <= 1)
    {
      pizzaPreference[pizzaType.ordinal()] = percent;
    }
  }

  public double getWaterAllowance()
  {
    return waterAllowance;
  }
  
  /**
   * @param waterAllowance the waterAllowance to set
   */
  public void setWaterAllowance(double waterAllowance)
  {
    this.waterAllowance = waterAllowance;
  }

  /**
   * @param year year in question
   * @param crop crop in question
   * @return tons produced
   */
  public double getCropProduction(int year, EnumCropType crop)
  {
    return cropProduction[crop.ordinal()][year - START_YEAR];
  }

   /**
    * @param year    year in question
    * @param crop    crop in question
    * @param metTons tons produced
    */
   public void setCropProduction(int year, EnumCropType crop, double metTons)
   {
     if (metTons >= 0)
     {
       cropProduction[crop.ordinal()][year - START_YEAR] = metTons;
     }
     else
     {
       if (VERBOSE)
       {
         System.err.println("Invalid argument for Continent.setCropProduction method");
       }
     }
   }

   /**
    * @param year year in question
    * @param crop crop in question
    * @return tons exported
    */
   public double getCropExport(int year, EnumCropType crop)
   {
     return cropExport[crop.ordinal()][year - START_YEAR];
   }

   /**
    * @param year    year in question
    * @param crop    crop in question
    * @param metTons tons exported
    */
   public void setCropExport(int year, EnumCropType crop, double metTons)
   {
     if (metTons >= 0)
     {
       cropExport[crop.ordinal()][year - START_YEAR] = metTons;
     }
     else
     {
       if (VERBOSE)
       {
         System.err.println("Invalid argument for Continent.setCropExport method");
       }
     }
   }

   /**
    * @param year year in question
    * @param crop crop in question
    * @return tons imported
    */
   public double getCropImport(int year, EnumCropType crop)
   {
     return cropImport[crop.ordinal()][year - START_YEAR];
   }

   /**
    * @param year    year in question
    * @param crop    crop in question
    * @param metTons tons imported
    */
   public void setCropImport(int year, EnumCropType crop, double metTons)
   {
     if (metTons >= 0)
     {
       cropImport[crop.ordinal()][year - START_YEAR] = metTons;
     }
     else
     {
       if (VERBOSE)
       {
         System.err.println("Invalid argument for Continent.setCropImport method");
       }
     }
   }

   public double getLandTotal(int year)
   {
     return landTotal;
   }

   public double getTotalCropNeed(int year, EnumCropType crop)
   {
     double temp = 0;
     for (Country c : countries)
     {
       temp = temp + c.getTotalCropNeed(year, crop);
     }
     return temp;
   }

   public double getProduction(int year, EnumCropType crop)
   {
     double temp = 0;
     for (Country c : countries)
     {
       temp = temp + c.getCropProduction(year, crop);
     }
     return temp;
   }
   
   /**
    * @param year
    * @return area of arable land
    */
   public double getArableLand(int year)
   {
     return landArable[year - START_YEAR];
   }

   /**
    * @param year
    * @param kilomsq area of arable land
    */
   public void setArableLand(int year, double kilomsq)
   {
     if (kilomsq >= 0)
     {
       for (int i = 0; i < (YEARS_OF_SIM); i++) landArable[i] = kilomsq;
     }
     else
     {
       if (VERBOSE)
       {
         System.err.println("Invalid argument for Continent.setArableLand method for " + getName());
       }
     }
   }
   
   /**
    * Returns area available for planting: arable land - sum(area used for each crop)
    *
    * @param year year to check
    * @return arable area unused
    */
   public double getArableLandUnused(int year)
   {
     double used = 0;
     for (EnumCropType crop : EnumCropType.values())
     {
       used += getCropLand(year, crop);
     }
     double unused = getArableLand(year) - used;
     return unused;
   }
   

   /**
    * @param year year in question
    * @param crop crop in question
    * @return square km planted with crop
    */
   public double getCropLand(int year, EnumCropType crop)
   {
     return landCrop[crop.ordinal()][year - START_YEAR];
   }


   /**
    * Set crop land value; use this method when initializing
    *
    * @param year    year in question
    * @param crop    crop in question
    * @param kilomsq area to set
    */
   public void setCropLand(int year, EnumCropType crop, double kilomsq)
   {
     if (kilomsq >= 0 && kilomsq <= getArableLand(year))
     {
       for (int i = 0; i < (YEARS_OF_SIM); i++)
         landCrop[crop.ordinal()][i] = kilomsq;
     }
     else
     {
       if (VERBOSE)
       {
         System.err.println("Invalid argument for Continent.setCropLand method for continent " + getName() + " crop " + crop);
       }
     }
   }

   /**
    * Sets area to be planted with given crop in given year based on user input
    *
    * @param year    year in question
    * @param crop    crop in question
    * @param kilomsq number square km user wants to plant with that crop
    */
   public void updateCropLand(int year, EnumCropType crop, double kilomsq)
   {
     double unused = getArableLandUnused(year);
     double currCropLand = getCropLand(year, crop);
     double delta = kilomsq - currCropLand;
     double valueToSet;

     // if trying to decrease beyond 0, set to 0
     if ((currCropLand + delta) < 0)
     {
       valueToSet = 0;
     }
     // else if trying to increase by amount greater than available, set to current + available
     else if (delta > unused)
     {
       valueToSet = currCropLand + unused;
     }
     // else set to curr + delta
     else
     {
       valueToSet = currCropLand + delta;
     }
     for (int i = year - START_YEAR; i < YEARS_OF_SIM; i++)
       landCrop[crop.ordinal()][i] = valueToSet;
   }

   /**
    * Returns conventional crop yield; 
    * use getCropYield(int year, EnumCropType crop, EnumGrowMethod method) instead
    * @param year
    * @param crop
    * @return yield for crop
    */
   @Deprecated
   public double getCropYield(int year, EnumCropType crop)
   {
     return conventionalYield[crop.ordinal()][year-START_YEAR];
   }
   
   /**
    * Returns specified crop yield
    * @param year
    * @param crop
    * @param grow method
    * @return yield for crop
    */
   public double getCropYield(int year, EnumCropType crop, EnumGrowMethod method)
   {
     switch (method)
     {
       case CONVENTIONAL:
         return conventionalYield[crop.ordinal()][year-START_YEAR];
       case GMO:
         return gmoYield[crop.ordinal()][year-START_YEAR];
       case ORGANIC:
         return organicYield[crop.ordinal()][year-START_YEAR];
       default:
         if (VERBOSE) System.err.println("Invalid method argument for Continent.getCropYield");
         return -1;
     }
   }
   

   /**
    * Sets conventional yield for year and crop; 
    * use setCropYield(int year, EnumCropType crop, EnumGrowMethod method, double tonPerSqKilom) instead
    * @param year          (passing year might be useful in the next milestone?)
    * @param crop
    * @param tonPerSqKilom yield for crop
    */
   @Deprecated
   public void setCropYield(int year, EnumCropType crop, double tonPerSqKilom)
   {
     conventionalYield[crop.ordinal()][year-START_YEAR] = tonPerSqKilom;
   }
   
   /**
    * Sets specified crop yield
    * @param year          (passing year might be useful in the next milestone?)
    * @param crop
    * @param grow method
    * @param tonPerSqKilom yield for crop
    */
   public void setCropYield(int year, EnumCropType crop, EnumGrowMethod method, double tonPerSqKilom)
   {
     switch (method)
     {
       case CONVENTIONAL:
         conventionalYield[crop.ordinal()][year-START_YEAR] = tonPerSqKilom;
         break;
       case GMO:
         gmoYield[crop.ordinal()][year-START_YEAR] = tonPerSqKilom;
         break;
       case ORGANIC:
         organicYield[crop.ordinal()][year-START_YEAR] = tonPerSqKilom;
         break;
       default:
         if (VERBOSE) System.err.println("Invalid method argument for Continent.setCropYield");
         break;
     }
   }
   
   /**
    * @param year   year in question
    * @param method cultivation method
    * @return % land cultivated by method
    */
   public double getMethodPercentage(int year, EnumGrowMethod method)
   {
     return cultivationMethod[method.ordinal()][year - START_YEAR];
   }

   /**
    * @param year       year in question
    * @param method     cultivation method
    * @param percentage % land cultivated by method
    */
   public void setMethodPercentage(int year, EnumGrowMethod method, double percentage)
   {
     if (percentage >= 0)
     {
       cultivationMethod[method.ordinal()][year - START_YEAR] = percentage;
     }
     else
     {
       if (VERBOSE)
       {
         System.err.println("Invalid argument for Continent.setMethodPercentage method");
       }
     }
   }

   public boolean contains(Country country)
   {
     for (Country c : countries)
     {
       if (c == country)
       {
         return true;
       }
     }
     return false;
   }
   
   private void initializeYield(EnumCropType crop, double startYield)
   {
     // assign calculated yield for year 0 to conventional; adjust for gmo and organic
     setCropYield(START_YEAR, crop, EnumGrowMethod.CONVENTIONAL, startYield);
     setCropYield(START_YEAR, crop, EnumGrowMethod.GMO, startYield * GMO_YIELD_PERCENT);
     setCropYield(START_YEAR, crop, EnumGrowMethod.ORGANIC, startYield * ORGANIC_YIELD_PERCENT);
     
     // set remaining years' yield to decline to account for climate change
     for (EnumGrowMethod method:EnumGrowMethod.values())
     {
       for (int year = START_YEAR + 1; year < START_YEAR + YEARS_OF_SIM; year++)
       {
         double priorYield = getCropYield(year-1, crop, method);
         double adjustedYield = priorYield * (1 - ANNUAL_YIELD_DECLINE);
         setCropYield(year, crop, method, adjustedYield);
       }
     }
   }
 }

