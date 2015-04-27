package worldfoodgame.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import worldfoodgame.common.AbstractScenario;
import worldfoodgame.common.EnumCropType;
import worldfoodgame.common.EnumGrowMethod;

/**
 * @author jessica
 * @version 4/26/15
 */
public class Continent
{
  private static boolean VERBOSE = false;
  
  private EnumContinentNames name;
  private int START_YEAR = AbstractScenario.START_YEAR;
  private int YEARS_OF_SIM = AbstractScenario.YEARS_OF_SIM;
  private List<Country> countries;
  private Collection<LandTile> landTiles;
  
  protected int[] population = new int[YEARS_OF_SIM];       //in people
  protected double[] undernourished = new double[YEARS_OF_SIM];  // percentage of population. 0.50 is 50%.
  
  protected double[] cropYield = new double[EnumPizzaCrop.SIZE]; //metric tons per square kilometer
  protected double[] pizzaPreference = new double[EnumPizzaCrop.SIZE]; // % of population wanting each kind of pizza
  
  protected double[][] cropProduction = new double[EnumPizzaCrop.SIZE][YEARS_OF_SIM]; //in metric tons.
  protected double[][] cropExport     = new double[EnumPizzaCrop.SIZE][YEARS_OF_SIM]; //in metric tons.
  protected double[][] cropImport     = new double[EnumPizzaCrop.SIZE][YEARS_OF_SIM]; //in metric tons.
  
  protected double landTotal;  //in square kilometers
  protected double[] landArable = new double[YEARS_OF_SIM];  //in square kilometers
  protected double[][] landCrop = new double[EnumPizzaCrop.SIZE][YEARS_OF_SIM];  //in square kilometers
  
  protected double[][] cultivationMethod = new double[EnumGrowMethod.SIZE][YEARS_OF_SIM]; //percentage
  
  /**
   * Continent constructor
   *
   * @param name continent name
   */
  public Continent(EnumContinentNames name)
  {
    this.name = name;
    this.countries = new ArrayList<>();
    this.landTiles = new ArrayList<>();
  }

  public void addCountry(Country country)
  {
    // add population
    for (int i = 0; i < YEARS_OF_SIM; i++)
    {
      population[i] += country.getPopulation(i+START_YEAR);
    }
    
    // add land
    landTotal += country.getLandTotal(START_YEAR);
    landArable[0] += country.getArableLand(START_YEAR);
    
    // add tiles
    landTiles.addAll(country.getLandTiles());
  }
  
  public EnumContinentNames getName()
  {
    return name;
  }
  
  public String getNameString()
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
  
  public double getPizzaPreference(EnumPizzaCrop pizzaType)
  {
    return pizzaPreference[pizzaType.ordinal()];
  }
  
  public void setPizzaPreference(EnumPizzaCrop pizzaType, double percent)
  {
    if (percent >= 0 && percent <= 1)
    {
      pizzaPreference[pizzaType.ordinal()] = percent;
    }
  }
  
  /**
   * @param year year in question
   * @param crop crop in question
   * @return tons produced
   */
  public double getCropProduction(int year, EnumPizzaCrop crop)
  {
    return cropProduction[crop.ordinal()][year - START_YEAR];
  }

  /**
   * @param year    year in question
   * @param crop    crop in question
   * @param metTons tons produced
   */
  public void setCropProduction(int year, EnumPizzaCrop crop, double metTons)
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
  public double getCropExport(int year, EnumPizzaCrop crop)
  {
    return cropExport[crop.ordinal()][year - START_YEAR];
  }

  /**
   * @param year    year in question
   * @param crop    crop in question
   * @param metTons tons exported
   */
  public void setCropExport(int year, EnumPizzaCrop crop, double metTons)
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
  public double getCropImport(int year, EnumPizzaCrop crop)
  {
    return cropImport[crop.ordinal()][year - START_YEAR];
  }

  /**
   * @param year    year in question
   * @param crop    crop in question
   * @param metTons tons imported
   */
  public void setCropImport(int year, EnumPizzaCrop crop, double metTons)
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
    for (EnumPizzaCrop crop : EnumPizzaCrop.values())
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
  public double getCropLand(int year, EnumPizzaCrop crop)
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
  public void setCropLand(int year, EnumPizzaCrop crop, double kilomsq)
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
  public void updateCropLand(int year, EnumPizzaCrop crop, double kilomsq)
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
}
