package worldfoodgame.model;

import worldfoodgame.common.AbstractCountry;
import worldfoodgame.common.AbstractScenario;
import worldfoodgame.common.EnumCropType;
import worldfoodgame.common.EnumGrowMethod;
import worldfoodgame.gui.displayconverters.EquirectangularConverter;
import worldfoodgame.gui.displayconverters.MapConverter;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Country class extends AbstractCountry, includes methods for accessing its
 * fields.
 * Created by winston on 3/9/15.
 * Edited by Jessica on 3/14/15: getName, setLandTotal, methodPercentage methods
 *
 * @version 22-Mar-2015
 */
public class Country extends AbstractCountry
{
  private static MapConverter converter = new EquirectangularConverter();
  private int START_YEAR = AbstractScenario.START_YEAR;
  private List<Region> regions;
  private MapPoint capitolLocation;

  /**
   * returns the point representing the shipping location of that country.
   *
   * (!) note: this method can only be called after the Country's regions have
   * been set.
   * @return map point representing the lat and lon location of the Country's
   * capitol.
   */
  public MapPoint getCapitolLocation()
  {
    if (capitolLocation == null)
    {
      capitolLocation = calCapitalLocation();
    }
    return capitolLocation;
  }

  // generate the capital by finding the center of the largest landmass.
  // this method can only be called after the Country's regions have been set.
  private MapPoint calCapitalLocation()
  {
    if (regions == null) throw new RuntimeException("(!) regions not set!");
    if (regions.isEmpty()) throw new RuntimeException("(!) no regions !");

    int maxArea = 0;
    Polygon largest = converter.regionToPolygon(regions.get(0));

    for (Region region : regions)
    {
      Polygon poly = converter.regionToPolygon(region);
      int area = poly.getBounds().width * poly.getBounds().height;
      if (area > maxArea) largest = poly;
    }

    int x = (int) largest.getBounds().getCenterX();
    int y = (int) largest.getBounds().getCenterY();

    return converter.pointToMapPoint(new Point(x, y));
  }


  public void addRegion(Region region)
  {
    if (regions == null) regions = new ArrayList<>();
    regions.add(region);
  }

  public List<Region> getRegions()
  {
    return regions;
  }

  public Country(String name)
  {
    this.name = name;
  }

  public String getName()
  {
    return name;
  }

  public int getPopulation(int year)
  {
    return population[year - START_YEAR];
  }

  public void setPopulation(int year, int n)
  {
    if (n >= 0)
    {
      population[year - START_YEAR] = n;
    }
    else
    {
      System.err.println("Invalid argument for Country.setPopulation method");
    }
  }

  /**
   * Updates population for given year based on formula in spec
   * @param year    year for which to calculate population
   */
  public void updatePopulation(int year)
  {
    int priorPop = population[year - START_YEAR -1];
    double changePer1K = birthRate[year - START_YEAR] + migrationRate[year - START_YEAR] - mortalityRate[year - START_YEAR];
    Double popNow = priorPop + changePer1K * priorPop/1000;
    int popInt = popNow.intValue();
    population[year - START_YEAR] = popInt;
  }
  
  public double getMedianAge(int year)
  {
    return medianAge[year - START_YEAR];
  }

  /**
   * Populate medianAge array with given age; assumes median age remains constant.
   * @param years   median age
   */
  public void setMedianAge(double years)
  {
    if (years >= 0)
    {
      for (int i = 0; i < medianAge.length; i++) medianAge[i] = years;
    }
    else
    {
      System.err.println("Invalid argument for Country.setMedianAge method");
    }
  }

  public double getBirthRate(int year)
  {
    return birthRate[year - START_YEAR];
  }

  /**
   * Populate birthRate array with given rate; assumes rate remains constant.
   * @param permille  births/1000 people
   */
  public void setBirthRate(double permille)
  {
    if (permille >= 0 && permille <= 1000)
    {
      for (int i = 0; i < birthRate.length; i++) birthRate[i] = permille;
    }
    else
    {
      System.err.println("Invalid argument for Country.setBirthRate method");
    }
  }

  public double getMortalityRate(int year)
  {
    return mortalityRate[year - START_YEAR];
  }

  public void setMortalityRate(int year, double permille)
  {
    if (permille >= 0 && permille <= 1000)
    {
      mortalityRate[year - START_YEAR] = permille;
    }
    else
    {
      System.err.println("Invalid argument for Country.setMortalityRate method");
    }
  }

  /**
   * Updates mortality rate for given year based on formula given in spec.
   * @param year    year for which we are updating mortality rate
   */
  public void updateMortalityRate(int year)
  {
    double hungryStart = undernourished[START_YEAR] * population[START_YEAR];
    int popNow = population[year - START_YEAR -1];
    double hungryNow = popNow * undernourished[year - START_YEAR -1];
    double hungryChange = hungryNow - hungryStart;
    double mortalityNow = (mortalityRate[START_YEAR] + 0.2 * hungryChange)/(popNow/1000); 
    mortalityRate[year - START_YEAR] = mortalityNow;
  }
  
  public double getMigrationRate(int year)
  {
    return migrationRate[year - START_YEAR];
  }

  /**
   * Populate migrationRate array with given rate; assumes rate remains constant.
   * @param permille    migration/1000 people
   */
  public void setMigrationRate(double permille)
  {
    if (permille >= -1000 && permille <= 1000)
    {
      for (int i = 0; i < migrationRate.length; i++) migrationRate[i] = permille;
    }
    else
    {
      System.err.println("Invalid argument for Country.setMigrationRate method");
    }
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
    else
    {
      System.err.println("Invalid argument for Country.setUndernourished method");
    }
  }

  public double getCropProduction(int year, EnumCropType crop)
  {
    return cropProduction[crop.ordinal()][year - START_YEAR];
  }

  public void setCropProduction(int year, EnumCropType crop, double metTons)
  {
    if (metTons >= 0)
    {
      cropProduction[crop.ordinal()][year - START_YEAR] = metTons;
    }
    else
    {
      System.err.println("Invalid argument for Country.setCropProduction method");
    }
  }

  public double getCropExport(int year, EnumCropType crop)
  {
    return cropExport[crop.ordinal()][year - START_YEAR];
  }

  public void setCropExport(int year, EnumCropType crop, double metTons)
  {
    if (metTons >= 0)
    {
      cropExport[crop.ordinal()][year - START_YEAR] = metTons;
    }
    else
    {
      System.err.println("Invalid argument for Country.setCropExport method");
    }
  }

  public double getCropImport(int year, EnumCropType crop)
  {
    return cropImport[crop.ordinal()][year - START_YEAR];
  }

  public void setCropImport(int year, EnumCropType crop, double metTons)
  {
    if (metTons >= 0)
    {
      cropImport[crop.ordinal()][year - START_YEAR] = metTons;
    }
    else
    {
      System.err.println("Invalid argument for Country.setCropImport method");
    }
  }

  public double getLandTotal(int year)
  {
    return landTotal[year - START_YEAR];
  }

  public void setLandTotal(int year, double kilomsq)
  {
    if (kilomsq > 0)
    {
      landTotal[year - START_YEAR] = kilomsq;
    }
    else
    {
      System.err.println("Invalid argument for Country.setLandTotal method");
    }
  }

  public double getArableLand(int year)
  {
    return landArable[year - START_YEAR];
  }

  public void setArableLand(int year, double kilomsq)
  {
    if (kilomsq >= 0)
    {
      landArable[year - START_YEAR] = kilomsq;
    }
    else
    {
      System.err.println("Invalid argument for Country.setArableLand method for country " + getName());
    }
  }

  public double getCropLand(int year, EnumCropType crop)
  {
    return landCrop[crop.ordinal()][year - START_YEAR];
  }


  // todo this still needs to be corrected
  public void setCropLand(int year, EnumCropType crop, double kilomsq)
  {
    if (kilomsq >= 0)
    {
      landCrop[crop.ordinal()][year - START_YEAR] = kilomsq;
    }
    else
    {
      System.err.println("Invalid argument for Country.setCropLand method");
    }
  }


  public double getMethodPercentage(int year, EnumGrowMethod method)
  {
    return cultivationMethod[method.ordinal()][year - START_YEAR];
  }

  public void setMethodPercentage(int year, EnumGrowMethod method, double percentage)
  {
    if (percentage >= 0)
    {
      cultivationMethod[method.ordinal()][year - START_YEAR] = percentage;
    }
    else
    {
      System.err.println("Invalid argument for Country.setMethodPercentage method");
    }
  }

  /* passing year might be useful in the next milestone? */

  public double getCropYield(int year, EnumCropType crop)
  {
    return cropYield[crop.ordinal()];
  }

  public void setCropYield(int year, EnumCropType crop, double tonPerSqKilom)
  {
    cropYield[crop.ordinal()] = tonPerSqKilom;
  }

  public double getCropNeedPerCapita(EnumCropType crop)
  {
    return cropNeedPerCapita[crop.ordinal()];
  }

  /**
   * Method for calculating & setting crop need
   *
   * @param crop                  EnumCropType
   * @param tonsConsumed          2014 production + imports - exports
   * @param percentUndernourished 2014 % of population undernourished
   */
  public void setCropNeedPerCapita(EnumCropType crop, double tonsConsumed, double percentUndernourished)
  {
    double population = getPopulation(START_YEAR);
    double tonPerPerson = tonsConsumed / (population - 0.5 * percentUndernourished * population);
    cropNeedPerCapita[crop.ordinal()] = tonPerPerson;
  }

  /**
   * Method for setting crop need when already known (e.g., when copying).
   *
   * @param crop         EnumCropType
   * @param tonPerPerson 2014 ton/person
   */
  public void setCropNeedPerCapita(EnumCropType crop, double tonPerPerson)
  {
    cropNeedPerCapita[crop.ordinal()] = tonPerPerson;
  }

  /**
   * Calculates number of unhappy people in country for a given year based on formula in specifications.
   * For START_YEAR, returns undernourished population to avoid null pointer.
   *
   * @param year
   * @return number of unhappy people for that year
   */
  public double getUnhappyPeople(int year)
  {
    int currentPop = getPopulation(year);
    double formulaResult;
    if (year == START_YEAR)
    {
      return currentPop * getUndernourished(year);
    }
    else
    {
      double numUndernourish = getUndernourished(year) * currentPop;
      double numDeaths = getMortalityRate(year) * currentPop;
      double changeUndernourish = numUndernourish - (getPopulation(year - 1) * getUndernourished(year - 1));
      double changeDeaths = numDeaths - (getPopulation(year - 1) * getMortalityRate(year - 1));
      formulaResult = 5 * numUndernourish + 2 * changeUndernourish + 10 * numDeaths + 5 * changeDeaths;
    }
    return Math.min(currentPop, formulaResult);
  }
  
  /**
   * Calculate great circle distance from country's capitolLocation to another MapPoint.
   * Formula from http://www.gcmap.com/faq/gccalc
   * @param   otherCapitol
   * @return  great circle distance in km
   */
  public double getShippingDistance(MapPoint otherCapitol)
  {
    double radianConversion = (Math.PI)/180;
    double lon1 = capitolLocation.getLon() * radianConversion;
    double lat1 = capitolLocation.getLat() * radianConversion;
    double lon2 = otherCapitol.getLon() * radianConversion;
    double lat2 = otherCapitol.getLat() * radianConversion;
    double theta = lon2 - lon1;
    double dist = Math.acos(Math.sin(lat1) * Math.sin(lat2) + Math.cos(lat1) * Math.cos(lat2) * Math.cos(theta));
    if (dist < 0) dist = dist + Math.PI;
    dist = dist * 6371.2; 
    return dist;
  }
  
  /**
   * returns the number of tons needed of crop type for the specified year.
   *
   * @param year year in question
   * @param type type of crop
   * @return tons of crop needed
   */
  public double getSurplus(int year, EnumCropType type)
  {
    return this.getCropProduction(year, type) - this.getPopulation(year) * this.getCropNeedPerCapita(type);
  }

  
}
