package worldfoodgame.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.awt.Color;

import worldfoodgame.common.AbstractScenario;
import worldfoodgame.common.EnumCropType;
import worldfoodgame.common.EnumGrowMethod;
import worldfoodgame.gui.ColorsAndFonts;
import worldfoodgame.planningpoints.PlanningPointCategory;
import worldfoodgame.planningpoints.PlanningPointConstants;
import worldfoodgame.planningpoints.PlanningPointsInteractableRegion;
import worldfoodgame.planningpoints.PlanningPointsLevel;

/**
 * @author jessica
 * @version 4/26/15
 * Modified by Ken Kressin to add Water Allowance information and methods used
 * by the computer trading optimizer.
 *
 * This class is designed to build and maintain the continents used in the game,
 * by aggregating the data and methods for all the countries encompassed by the
 * continent.
 */
public class Continent implements CropClimateData, PlanningPointsInteractableRegion
{
  private static boolean VERBOSE = true;

  private EnumContinentNames name;
  private EnumContinentShipPoints continentShipPoint;
  private MapPoint shipPoint;
  private int START_YEAR = AbstractScenario.START_YEAR;
  private int YEARS_OF_SIM = AbstractScenario.YEARS_OF_SIM;
  private double ANNUAL_TONS_PER_PERSON = CropClimateData.ANNUAL_TONS_PER_PERSON; 
  private List<Country> countries;
  private int numCountries;
  private List<LandTile> landTiles;
  private Color color;

  protected double waterAllowance;
  protected double avgRainfall;
  protected double continentRainfall;
  protected double GAL_CM_CUBED = 2641.720524;
  protected double continentLandTileNum;

  protected long[] population = new long[YEARS_OF_SIM];       //in people
  protected double[] waterUsed = new double[YEARS_OF_SIM];
  protected double[] undernourish = new double[YEARS_OF_SIM];  // percentage of population. 0.50 is 50%.

  protected double[][] conventionalYield = new double[EnumCropType.SIZE][YEARS_OF_SIM]; //metric tons per square kilometer
  protected double[][] organicYield = new double[EnumCropType.SIZE][YEARS_OF_SIM]; //metric tons per square kilometer
  protected double[][] gmoYield = new double[EnumCropType.SIZE][YEARS_OF_SIM]; //metric tons per square kilometer

  protected double[] pizzaPreference = new double[EnumCropType.SIZE]; // % of population wanting each kind of pizza
  protected double[][] totalCropNeed = new double[EnumCropType.SIZE][YEARS_OF_SIM]; /* total need in metric tons based on projected
                                                                                   population and pizzaPreference */

  protected double[][] cropProduction = new double[EnumCropType.SIZE][YEARS_OF_SIM]; //in metric tons.
  protected double[][] cropExport     = new double[EnumCropType.SIZE][YEARS_OF_SIM]; //in metric tons.
  protected double[][] cropImport     = new double[EnumCropType.SIZE][YEARS_OF_SIM]; //in metric tons.

  protected double landTotal;  //in square kilometers
  protected double[] landArable = new double[YEARS_OF_SIM];  //in square kilometers
  protected double[][] landCrop = new double[EnumCropType.SIZE][YEARS_OF_SIM];  //in square kilometers

  private double startAreaPlanted; // in sq km
  private double[] areaDeforested = new double[YEARS_OF_SIM]; // in sq km 

  protected double[][] cultivationMethod = new double[EnumGrowMethod.SIZE][YEARS_OF_SIM]; //percentage

  protected double approvalRating; 
  protected double diplomacyRating;
  protected double greenRating;

  //planning points
  private boolean isRealistic = true;
  private int GMOPlanningPoints=0;
  private int waterEff=0;
  private int yieldEff=0;
  private int tradeEff=0;

  // for initialization
  private double countriesOrganicTotal = 0;
  private double countriesGmoTotal = 0;
  private double countriesUndernourishedTotal = 0;

  private boolean DEBUG = false;


  /**
   * Continent constructor
   * @param name1 continent name
   */
  public Continent(EnumContinentNames name1)
  {

    this.name = name1;
    switch (name)
    {
      case N_AMERICA:
        color = ColorsAndFonts.N_AMERICA;
        shipPoint = continentShipPoint.N_AMERICA_SHIP_POINT;
        break;
      case S_AMERICA:
        color = ColorsAndFonts.S_AMERICA;
        shipPoint = continentShipPoint.S_AMERICA_SHIP_POINT;
        break;
      case AFRICA:
        color = ColorsAndFonts.AFRICA;
        shipPoint = continentShipPoint.AFRICA_SHIP_POINT;
        break;
      case ASIA:
        color = ColorsAndFonts.ASIA;
        shipPoint = continentShipPoint.ASIA_SHIP_POINT;
        break;
      case EUROPE:
        color = ColorsAndFonts.EUROPE;
        shipPoint = continentShipPoint.EUROPE_SHIP_POINT;
        break;
      case MIDDLE_EAST:
        color = ColorsAndFonts.M_EAST;
        shipPoint = continentShipPoint.MIDDLE_EAST_SHIP_POINT;
        break;
      case OCEANIA:
        color = ColorsAndFonts.OCEANIA;
        shipPoint = continentShipPoint.OCEANIA_SHIP_POINT;
        break;
      default:
        color = ColorsAndFonts.PASSIVE_REGION;

        break;
    }

    countries = new ArrayList<>();
    numCountries = 0;
    startAreaPlanted = 0;
    landTiles = new ArrayList<>();
  }

  public Color getColor()
  {
    return color;
  }

  /**
   * Add country to continent's country list and its data to continent totals
   * @param country the Country object being processed.
   */
  public void addCountry(Country country)
  {
    countries.add(country);
    numCountries++;
    // add population and arable land
    double arableLand = country.getArableLand(START_YEAR);
    /*
    if (country.getContinentName() == EnumContinentNames.ASIA)
    {
      System.out.println("In Continent.addCountry country "+country.getName()+" arable land "+country.getArableLand(START_YEAR));
    }
    */
    for (int i = 0; i < YEARS_OF_SIM; i++)
    {
      population[i] += country.getPopulation(i+START_YEAR);   // population projections are different for each year, so i+START_YEAR
      landArable[i] += arableLand;                            // no change in projected arable land, so use variable with start value
    }
    // add crop info
    for (EnumCropType crop:EnumCropType.values())
    {
      double areaPlanted = country.getCropLand(START_YEAR, crop);
      addToCropLand(START_YEAR, crop, areaPlanted);
      startAreaPlanted += areaPlanted;

      double cropProduced = country.getCropProduction(START_YEAR, crop);
      addToCropProduction(START_YEAR, crop, cropProduced);
      if(DEBUG)
      {
        System.out.println("Amount of " + crop.name + " produced is: " + cropProduced);
      }

      double cropImported = country.getCropImport(START_YEAR, crop);
      addToCropImports(START_YEAR, crop, cropImported);

      double cropExported = country.getCropExport(START_YEAR, crop);
      addToCropExports(START_YEAR, crop, cropExported);
      // calculate water allowance for the crop being instantiated
      country.setWaterAllowance(crop, cropProduced);
    }

    // add tiles
    continentLandTileNum += country.getLandTiles().size();
    landTiles.addAll(country.getLandTiles());

    // calculate average rainfall for the country

    double rain = 0.0;
    for(LandTile tile: country.getLandTiles())
    {
      rain += (double)tile.getRainfall();
    }
    continentRainfall += (rain * GAL_CM_CUBED);
    avgRainfall = (rain / continentLandTileNum) * GAL_CM_CUBED;

    waterAllowance += country.getWaterAllowance();

    if(DEBUG)
    {
    System.out.println("Avg rainfall is: " + avgRainfall + "gals per land tile."
                       + "\nCrop water allowance is: " + waterAllowance + "gallons.");
    }

    landTotal += country.getLandTotal(START_YEAR);
    waterAllowance -=  avgRainfall;
    if(DEBUG)
    {
      System.out.println("\tAdjusted water allowance is: " + waterAllowance);
    }

    countriesOrganicTotal += country.getMethodPercentage(START_YEAR, EnumGrowMethod.ORGANIC);
    countriesGmoTotal += country.getMethodPercentage(START_YEAR, EnumGrowMethod.GMO);
    countriesUndernourishedTotal += country.getUndernourished(START_YEAR);
  }

  /**
   * Initialize fields that depend on average of all countries' values
   */
  public void initializeData(World world)
  {
    if(DEBUG)
    {
    System.out.println("In continent.initializeData, name = "+toString()+" pop = "+getPopulation(START_YEAR));
    System.out.println("In continent.initializeData, name = "+toString()+" arable = "+getArableLand(START_YEAR));
    }

    setInitialPlanningPoints();
    // calculate yields
    for (EnumCropType crop:EnumCropType.values())
    {
      double totalProduced = getCropProduction(START_YEAR, crop);
      double totalLand = getCropLand(START_YEAR, crop);
      double conventionalYield = totalProduced/totalLand;
      initializeYield(crop, conventionalYield);
    }

    initializePizzaPreference();
    initializeTotalCropNeed();
    initializeLandUse();
    initializeTiles();

    if(DEBUG)
    {
      System.out.println("For continent: " + this.getName() + ", the water allowance is: " + this.getWaterAllowance());
    }
    // set continent fields using average of country values
    // set percentages for gmo, organic, conventional for START_YEAR
    double organicAvg = countriesOrganicTotal/numCountries;
    double gmoAvg = countriesGmoTotal/numCountries;
    double conventionalAvg = 0;
    if ((organicAvg + gmoAvg) < 0 || (organicAvg + gmoAvg) > 1)
    {
      if (VERBOSE) System.err.println("gmo + organic % for continent "+this.toString()+" not between 0 and 1");
    }
    else
    {
      conventionalAvg = 1 - (organicAvg + gmoAvg);
    }

    setMethodPercentage(EnumGrowMethod.ORGANIC, organicAvg);
    setMethodPercentage(EnumGrowMethod.GMO, gmoAvg);
    setMethodPercentage(EnumGrowMethod.CONVENTIONAL, conventionalAvg);

    // set undernourished value for START_YEAR
    setUndernourished(START_YEAR, countriesUndernourishedTotal/numCountries);
    calculateGreenRating(START_YEAR);
    calculateApprovalRating(START_YEAR); 
    calculateDiplomacyRating(START_YEAR, world);
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

  public double getTotalLand()
  {
    return landTotal;
  }

  public long getPopulation(int year)
  {
    return population[year - START_YEAR];
  }

  public double getUndernourished(int year)
  {
    return undernourish[year - START_YEAR];
  }

  public void setUndernourished(int year, double percentage)
  {
    if (percentage >= 0 && percentage <= 1)
    {
      undernourish[year - START_YEAR] = percentage;
    }
  }

  public void updateUndernourished(int year)
  {
    double surplusTons = 0;
    double deficitTons = 0;
    for (EnumCropType crop:EnumCropType.values())
    {
      double produced = getCropProduction(year, crop);
      double imported = getCropImport(year, crop);
      double exported = getCropExport(year, crop);
      double totalAvail = produced + imported - exported;
      double need = getTotalCropNeed(year, crop);
      double difference = totalAvail - need;
      // if more tons available than needed, there is surplus
      if (difference > 0) surplusTons += difference;
      // if more tons needed than available, there is deficit
      else if (difference < 0) deficitTons += -difference;
      // if totalAvail == need, do nothing
    }
    // calculate adjusted unmet need
    double unmetNeed = 0;
    // if there is deficit in any crop
    if (deficitTons > 0)
    {
      if (surplusTons > 0)
      {
        // get 1/2 credit for any pizza you have that isn't what people prefer
        unmetNeed = deficitTons - surplusTons/2;
        // if 1/2 credit more than deficit, unmet need is now 0
        if (unmetNeed < 0) unmetNeed = 0;
      }
      else unmetNeed = deficitTons;
    }
    double undernourished = unmetNeed/ANNUAL_TONS_PER_PERSON;
    double percent = undernourished/(getPopulation(year));
    setUndernourished(year,percent);
  }

  public double getWaterUsage(int year)
  {
    return waterUsed[year - START_YEAR];
  }

  public void setWaterUsage(int year, double gallonsWater)
  {
    if (gallonsWater < 0)
    {
      waterUsed[year - START_YEAR] = 0;
    }
    else if (gallonsWater < (waterAllowance + continentRainfall))
    {
      waterUsed[year - START_YEAR] = gallonsWater;
    }
    else
    {
      waterUsed[year - START_YEAR] = waterAllowance + continentRainfall;
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

  public double getTotalCropNeed(int year, EnumCropType crop)
  {
    return totalCropNeed[crop.ordinal()][year - START_YEAR];
  }

  // this should only be called during Continent initialization and therefore is private
  private void setTotalCropNeed(int year, EnumCropType crop, double metTons)
  {
    totalCropNeed[crop.ordinal()][year - START_YEAR] = metTons;
  }

  public double getStartAreaPlanted()
  {
    return startAreaPlanted;
  }

  public void setDeforestation(int year, double area)
  {
    areaDeforested[year - START_YEAR] = area;
  }

  public double getDeforestation(int year)
  {
    return areaDeforested[year - START_YEAR];
  }

  public double getWaterAllowance()
  {
    return waterAllowance;
  }

  /**
   * This method allows either the continent initialization or an outside class to
   * set or modify the continent's water allowance.  For instance, game designers
   * could use this method to reflect the "discovery" of new fresh water supplies
   * for the comntinent, such as desalinization, or mining asteroids...
   * @param waterAllowance the amount of water in gallons.
   */
  public void setWaterAllowance(double waterAllowance)
  {
    this.waterAllowance = waterAllowance;
  }

  /**
   * Allows the gui to pull the continent's annual rainfall, to use in planting crops.
   * This amount is part of the continent's annual water allowance, and can change
   * for droughts or floods.
   * @return  The total gallons of water provided to the continent annually.
   */
  public double getRainfall()
  {
    return continentRainfall;
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

  public double getNetCropAvailable(int year, EnumCropType crop)
  {

    double available = this.getCropProduction(year, crop)
        + this.getCropImport(year, crop)
        - this.getCropExport(year, crop);
    return available;
  }

  public double getSurplus(int year, EnumCropType crop)
  {
    return this.getCropProduction(year, crop) - this.getTotalCropNeed(year, crop);
  }

  /**
   * Designed to pull the MapPoint location of the shipping city defined in
   * ContinentShipData. shipPoint is set when the specific continent is initialized
   * by pulling the lat and long from enumContinentShipPoints.
   *
   * This method is used in the trading optimizer, "worldfoodgame.model.TradeOptimizer.java"
   * as well as in tradingRoutOverlay.
   * @return A MapPoint object holding the longitude and latitude of the continent's
   *         shipping city.
   */
  public MapPoint getCapitolLocation()
  {
    return shipPoint;
  }

  public double getLandTotal(int year)
  {
    return landTotal;
  }
  /*
   public double getTotalCropNeed(int year, EnumCropType crop)
   {
     return totalCropNeed[crop.ordinal()][year - START_YEAR];
   }
   */
  /**
   * Use getCropProduction(int year, EnumCropType crop) instead
   * @param year
   * @param crop
   * @return tons of crop produced in year
   */
  @Deprecated
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
      landArable[year - START_YEAR] = kilomsq;
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
   * @param crop    crop in question
   * @param kilomsq area to set
   */
  private void setCropLand(EnumCropType crop, double kilomsq)
  {
    if (kilomsq >= 0 && kilomsq <= getArableLand(START_YEAR))
    {
      for (int i = 0; i < (YEARS_OF_SIM); i++)
      {
        landCrop[crop.ordinal()][i] = kilomsq;
      }
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
    double limit = waterAllowance + continentRainfall - waterUsed[year - START_YEAR];
  
    double factor = this.getPlanningPointsFactor(PlanningPointCategory.WaterEfficiency);
    limit = limit*factor;
    limit = limit / crop.waterUse;
    
    double valueToSet;
    double waterValue;
    if ((currCropLand + delta) < 0)
    {
      valueToSet = 0;
      //decreasing water usage since currCropLand + delta should be negative
      waterValue = waterUsed[year - START_YEAR] + (currCropLand + delta)*crop.waterUse;
    }
    else if (delta > limit)
    {
      valueToSet = limit + currCropLand;
      waterValue = waterAllowance + continentRainfall;
    }
    else if (delta > unused)
    {
      valueToSet = unused + currCropLand;
      waterValue = valueToSet * crop.waterUse;
    }
    else
    {
      valueToSet = kilomsq;
      waterValue = valueToSet * crop.waterUse;
    }
    for (int i = year - START_YEAR; i < YEARS_OF_SIM; i++)
    {
      landCrop[crop.ordinal()][i] = valueToSet;
      setWaterUsage(i + START_YEAR, waterValue);
    }
  }

  /**
   * Set cultivation method %; use when initializing
   * @param method     cultivation method
   * @param percentage % land cultivated by method
   */
  public void setMethodPercentage(EnumGrowMethod method, double percentage)
  {
    if (percentage >= 0 && percentage <= 1)
    {
      for (int i = 0; i < (YEARS_OF_SIM); i++)
      {
        cultivationMethod[method.ordinal()][i] = percentage;
      }
    }
    else
    {
      if (VERBOSE)
      {
        System.err.println("Invalid argument for Continent.setMethodPercentage method");
      }
    }
  }

  public void updateMethodPercentage(int year, EnumGrowMethod method, double percentage)
  {
    // what % of land under other cultivation methods, this method
    double sumOtherMethods = 0;
    double currentThisMethod = 0;
    for (EnumGrowMethod growMethod:EnumGrowMethod.values())
    {
      if (growMethod == method) currentThisMethod =  getMethodPercentage(year,growMethod);
      else sumOtherMethods += getMethodPercentage(year,growMethod);
    }
    double delta = percentage - currentThisMethod;
    double maxPossible;
    // GMO research affects how much GMO you can plant
    if (method == EnumGrowMethod.GMO)
    {  
      double limit = getPlanningPointsFactor(PlanningPointCategory.GMOResistance); 
      maxPossible = Math.min(limit, 1 - sumOtherMethods);
    }
    else maxPossible = 1 - sumOtherMethods;
    double valueToSet;

    // if trying to decrease beyond 0, set to 0
    if ((currentThisMethod + delta) < 0)
    {
      valueToSet = 0;
    }
    // else if trying to increase by amount greater than maxPossible, set to maxPossible
    else if ((currentThisMethod + delta) > maxPossible)
    {
      valueToSet = maxPossible;
    }
    // else set to current + delta
    else
    {
      valueToSet = currentThisMethod + delta;
    }
    for (int i = year - START_YEAR; i < YEARS_OF_SIM; i++)
    {
      cultivationMethod[method.ordinal()][i] = valueToSet;
    }
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
   * @param method
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
   * @param method
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


  public boolean contains(Country country)
  {
     return countries.contains(country);
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
   
   private void initializePizzaPreference()
   {
     //System.out.println("In Continent.initializePizzaPreference "+this.toString());
     ArrayList<EnumCropType> cropsToSet = new ArrayList<EnumCropType>();
     cropsToSet.addAll(Arrays.asList(EnumCropType.values()));
     double limit = 1;
     double sumPercents = 0;
     while (cropsToSet.size() > 1)
     {
       Collections.shuffle(cropsToSet);
       EnumCropType crop = cropsToSet.get(0);
       double percent = Math.random()*limit;
       setPizzaPreference(crop,percent);
       sumPercents += percent;
       limit = 1 - sumPercents;
       cropsToSet.remove(0);
       //System.out.println(crop+" percent "+percent+" sumPercents "+sumPercents+" limit "+limit);
     }
     EnumCropType crop = cropsToSet.get(0);
     double remainingPercent = 1 - sumPercents;
     setPizzaPreference(crop,remainingPercent);
     cropsToSet.clear();
   }
   
   private void initializeTotalCropNeed()
   {
     for (EnumCropType crop:EnumCropType.values())
     {
       double percentPrefer = getPizzaPreference(crop);
       for (int year = START_YEAR; year < (START_YEAR+YEARS_OF_SIM); year++)
       {
         double population = getPopulation(year);
         double need = population * percentPrefer * ANNUAL_TONS_PER_PERSON;
         setTotalCropNeed(year, crop, need);
       }
     }
   }
  
   /* populate cropLand array so values don't go to 0 after START_YEAR */ 
   private void initializeLandUse()
   {
     for (EnumCropType crop:EnumCropType.values())
     {
       double cropLand = getCropLand(START_YEAR, crop);
       setCropLand(crop, cropLand);
     }
     
   }
   
   /* 
    * for non-player continents, plant all arable land starting at 2nd year of sim; divide according to
    * pizza preferences
    */
   public void initializeNonPlayerLandUse()
   {
     //System.out.println("In Continent.initializeNonPlayerLandUse for "+this.toString());
     double landAvail = getArableLand(START_YEAR);
     //System.out.println("Arable land is "+landAvail);
     for (EnumCropType crop:EnumCropType.values())
     {
       double percent = getPizzaPreference(crop);
       double areaToPlant = percent * landAvail;
       //System.out.println("For "+crop+" percent is "+percent+" area is "+areaToPlant);
       for (int i = 1; i < (YEARS_OF_SIM); i++)
       {
         landCrop[crop.ordinal()][i] = areaToPlant;
       }
     }
   }
   
   
   private void initializeTiles()
   {
     Collections.sort(landTiles, new LonComparator()); 
     int numArableTiles = (int) getArableLand(START_YEAR)/100;
     
     for (int i = 0; i < numArableTiles; i++)
     {
       LandTile tile = landTiles.get(i);
       tile.setArable(true);
     }
   }
   
   private void addToCropLand(int year, EnumCropType crop, double area)
   {
     landCrop[crop.ordinal()][year-START_YEAR] += area;
   }
   
   private void addToCropProduction(int year, EnumCropType crop, double production)
   {
     cropProduction[crop.ordinal()][year-START_YEAR] += production;
   }
   
   private void addToCropImports(int year, EnumCropType crop, double imports)
   {
     cropImport[crop.ordinal()][year-START_YEAR] += imports;
   }
   
   private void addToCropExports(int year, EnumCropType crop, double exports)
   {
     cropExport[crop.ordinal()][year-START_YEAR] += exports;
   }
   
   
   /********************************/
   /** Start Planning Points      **/
   /********************************/

  public int calculatePlanningPoints()
  {
    int points = 0;
    points = (int) (50*(approvalRating + diplomacyRating));
    return points;
  }
    
  @Override
  /**
   * called once on initialization
   */
  public void setInitialPlanningPoints()
  {
    GMOPlanningPoints=(int)(Math.random()*PlanningPointConstants.MAX_POINTS);
    waterEff=(int)(Math.random()*PlanningPointConstants.MAX_POINTS);
    yieldEff=(int)(Math.random()*PlanningPointConstants.MAX_POINTS);
    tradeEff=(int)(Math.random()*PlanningPointConstants.MAX_POINTS);
  }

  @Override
  /**
   * Called once when creating point allocation panel
   */
  public int getPlanningPointsInCategory(PlanningPointCategory category)
  {
    int pointsInCat=0;
    switch(category)
    {
      case GMOResistance: return GMOPlanningPoints;
      case WaterEfficiency: return waterEff;
      case YieldEffeciency: return yieldEff;
      case TradeEfficiency: return tradeEff;
      default:
        System.out.println(category.toString()+" not recgnized");
        break;
    }
    return 0;
  }

  @Override
  /**
   * called by allocation panel
   */
  public String getContName()
  {
    return this.getName().toString();
  }

  @Override
  /**
   * Adds numPoints to category. The category
   * is represented as an integer within
   * this class
   */
  public void setPlanningPointsInCategory(PlanningPointCategory category,
      int numPoints)
  {
    switch(category)
    {
      case GMOResistance:
        GMOPlanningPoints=numPoints;
        break;
      case WaterEfficiency:
        waterEff=numPoints;
        break;
      case YieldEffeciency:
        yieldEff=numPoints;
        break;
      case TradeEfficiency:
        tradeEff=numPoints;
        break;
      default:
        System.out.println(category.toString()+" not recgnized");
        break;
    }
  }

  @Override
  /**
   * Gets the factor usually [0 1] to be used in
   * calculations for a certain category.
   * See the PlanningPointsLevel
   * class for more information on the 
   * factors that are returned.
   */
  public double getPlanningPointsFactor(PlanningPointCategory category)
  {
    PlanningPointsLevel level=null;
    switch(category)
    {
      case GMOResistance:
        level=PlanningPointsLevel.pointsToLevel(GMOPlanningPoints);
        PlanningPointsLevel.getGMOResistance(level);
        break;
      case WaterEfficiency:
        level=PlanningPointsLevel.pointsToLevel(waterEff);
        PlanningPointsLevel.getWaterEfficiency(level);
        break;
      case YieldEffeciency:
        level=PlanningPointsLevel.pointsToLevel(yieldEff);
        PlanningPointsLevel.getYieldEfficiency(level);
        break;
      case TradeEfficiency:
        level=PlanningPointsLevel.pointsToLevel(tradeEff);
        PlanningPointsLevel.getTradeEfficiency(level);
        break;
      default:
        System.out.println(category.toString()+" not recgnized");
        break;
    }
    return 0;
  }
  /********************************/
  /** End Planning Points        **/
  /********************************/


  public void testGetterMethods(int year)
  {
    System.out.println("Year is "+year);
    System.out.println("Continent name is "+toString());
    System.out.println("Total land is "+getTotalLand());
    System.out.println("Population "+getPopulation(year));
    System.out.println("Hungry % is "+getUndernourished(year));
    for (EnumCropType crop:EnumCropType.values())
    {
      System.out.println("Pizza preference for "+crop+" is "+getPizzaPreference(crop));
    }
    for (EnumCropType crop:EnumCropType.values())
    {
      System.out.println("Total need for "+crop+" is "+getTotalCropNeed(year,crop));
    }
    for (EnumCropType crop:EnumCropType.values())
    {
      System.out.println("Production for "+crop+" is "+getCropProduction(year, crop));
    }
    for (EnumCropType crop:EnumCropType.values())
    {
      System.out.println("Imports for "+crop+" is "+getCropImport(year, crop));
    }
    for (EnumCropType crop:EnumCropType.values())
    {
      System.out.println("Exports for "+crop+" is "+getCropExport(year, crop));
    }
    System.out.println("Arable land is "+getArableLand(year));
    for (EnumCropType crop:EnumCropType.values())
    {
      System.out.println("Land for "+crop+" is "+getCropLand(year, crop));
    }
    for (EnumCropType crop:EnumCropType.values())
    {
      for (EnumGrowMethod method:EnumGrowMethod.values())
      {
        System.out.println("Yield for "+method+" "+crop+" is "+getCropYield(year, crop, method));
      }
    }
    for (EnumGrowMethod method:EnumGrowMethod.values())
    {
      System.out.println("Percent for "+method+" is "+getMethodPercentage(year, method));
    }
    System.out.println("Deforestation is "+getDeforestation(year));
    System.out.println("Water allowance is "+getWaterAllowance());
  }

  /**
   * @return the approvalRating
   */
  public double getApprovalRating()
  {
    return approvalRating;
  }


  /**
   *@param year   year in question
   **/
  public void calculateApprovalRating(int year)
  {
    this.approvalRating = 1 - .5*undernourish[year - START_YEAR] + .5*greenRating;
    
    if (this.approvalRating>1)
    {
      this.approvalRating = 1;
    }
  }


  /**
   * @return the diplomacyRating
   */
  public double getDiplomacyRating()
  {
    return diplomacyRating;
  }


  /**
   *
   * @param year
   * @param world
   */
  public void calculateDiplomacyRating(int year, World world)
  {
    double worldHunger = world.getWorldHungerPercent();
    double hungerFactor = 0;

    if (worldHunger > getUndernourished(year))
    {
      hungerFactor = worldHunger -  getUndernourished(year);
    }

    this.diplomacyRating  = 1 - .5*hungerFactor + .5*greenRating;
    
    if (this.diplomacyRating>1)
    {
      this.diplomacyRating = 1;
    }
  }


  public void calculateGreenRating(int year)
  {
    this.greenRating  = 1 - (areaDeforested[year - START_YEAR]/landTotal);
//    System.out.println("Green rating is "+greenRating);
  }


  /**
   * @return the greenRating
   */
  public double getGreenRating()
  {
    return greenRating;
  }

  // Yearly rating update
  public void updateRatings(int year, World world)
  {
    calculateGreenRating(year);
    calculateDiplomacyRating(year,world);
    calculateApprovalRating(year); 
  }

  /*
  public static void main(String[] args)
  {
    Continent testContinent = new Continent(EnumContinentNames.AFRICA);
    testContinent.initializePizzaPreference();
    for (EnumCropType crop:EnumCropType.values())
    {
      double percent = testContinent.getPizzaPreference(crop);
      System.out.println("For "+crop+" "+percent);
    }
  }*/


  /**
   * Class for sorting land tiles by longitude
   * (unsorted, they are grouped by country)
   * @author jessica
   */
  class LonComparator implements Comparator<LandTile>
  {
    public int compare(LandTile tile1, LandTile tile2)
    {
      double diff = tile1.getLon() - tile2.getLon();
      if (diff > 0) return 1;
      else if (diff < 0) return -1;
      else return 0;
    } 
  }

}

