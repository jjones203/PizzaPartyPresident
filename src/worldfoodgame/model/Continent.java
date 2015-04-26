package worldfoodgame.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import worldfoodgame.common.AbstractScenario;
import worldfoodgame.common.EnumCropType;

public class Continent
{
  protected String name;
  private int START_YEAR = AbstractScenario.START_YEAR;
  private int YEARS_OF_SIM = AbstractScenario.YEARS_OF_SIM;
  private List<Country> countries;
  private Collection<LandTile> landTiles;
  
  protected int[] population = new int[YEARS_OF_SIM];       //in people
  protected double[] undernourished = new double[YEARS_OF_SIM];  // percentage of population. 0.50 is 50%.
  
  protected double[] cropYield = new double[EnumCropType.SIZE]; //metric tons per square kilometer
  protected double[] cropNeedPerCapita = new double[EnumCropType.SIZE]; //metric tons per person per year.
  
  protected double[][] cropProduction = new double[EnumPizzaCrop.SIZE][YEARS_OF_SIM]; //in metric tons.
  protected double[][] cropExport     = new double[EnumPizzaCrop.SIZE][YEARS_OF_SIM]; //in metric tons.
  protected double[][] cropImport     = new double[EnumPizzaCrop.SIZE][YEARS_OF_SIM]; //in metric tons.
  
  protected double landTotal;  //in square kilometers
  protected double[] landArable = new double[YEARS_OF_SIM];  //in square kilometers
  protected double[][] landCrop = new double[EnumPizzaCrop.SIZE][YEARS_OF_SIM];  //in square kilometers
  
  
  /**
   * Continent constructor
   *
   * @param name continent name
   */
  public Continent(String name)
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
  
  
}
