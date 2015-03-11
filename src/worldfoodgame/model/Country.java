package worldfoodgame.model;

import worldfoodgame.common.AbstractCountry;
import worldfoodgame.common.AbstractScenario;
import worldfoodgame.common.EnumCropType;

import java.util.List;

/**
 * DRAFT!
 * Created by winston on 3/9/15.
 */
public class Country extends AbstractCountry
{
  private int START_YEAR = AbstractScenario.START_YEAR;
  private List<AtomicRegion> landMasses;

  public Country(String name)
  {
    this.name = name;
  }

  public int getPopulation(int year)
  {
    return population[year - START_YEAR];
  }

  public void setPopulation(int year, int n)
  {
    population[year - START_YEAR] = n;
  }

  public double getMedianAge(int year)
  {
    return medianAge[year - START_YEAR];
  }

  public void setMedianAge(int year, double years)
  {
    medianAge[year - START_YEAR] = years;
  }

  public double getBirthRate(int year)
  {
    return birthRate[year - START_YEAR];
  }

  public void setBirthRate(int year, double permille)
  {
    birthRate[year - START_YEAR] = permille;
  }

  public double getMortalityRate(int year)
  {
    return mortalityRate[year - START_YEAR];
  }

  public void setMortalityRate(int year, double permille)
  {
    mortalityRate[year - START_YEAR] = permille;
  }

  public double getMigrationRate(int year)
  {
    return migrationRate[year - START_YEAR];
  }

  public void setMigrationRate(int year, double permille)
  {
    migrationRate[year - START_YEAR] = permille;
  }

  public double getUndernourished(int year)
  {
    return undernourished[year - START_YEAR];
  }

  public void setUndernourished(int year, double percentage)
  {
    undernourished[year - START_YEAR] = percentage;
  }

  public double getCropProduction(int year, EnumCropType crop)
  {
    return cropProduction[crop.ordinal()][year - START_YEAR];
  }

  public void setCropProduction(int year, EnumCropType crop, double metTons)
  {
    cropProduction[crop.ordinal()][year - START_YEAR] = metTons;
  }

  public double getCropExport(int year, EnumCropType crop)
  {
    return cropExport[crop.ordinal()][year - START_YEAR];
  }

  public void setCropExport(int year, EnumCropType crop, double metTons)
  {
    cropExport[crop.ordinal()][year - START_YEAR] = metTons;
  }

  public double getCropImport(int year, EnumCropType crop)
  {
    return cropImport[crop.ordinal()][year - START_YEAR];
  }

  public void setCropImport(int year, EnumCropType crop, double metTons)
  {
    cropImport[crop.ordinal()][year - START_YEAR] = metTons;
  }

  public double getLandTotal(int year)
  {
    return landTotal[year - START_YEAR];
  }

  /* do we need to write land total? */

  public double getArableLand(int year)
  {
    return landArable[year - START_YEAR];
  }

  public void setArableLand(int year, double kilomsq)
  {
    landArable[year - START_YEAR] = kilomsq;
  }

  public double getCropLand(int year, EnumCropType crop)
  {
    return landCrop[crop.ordinal()][year - START_YEAR];
  }

  public void setCropLand(int year, EnumCropType crop, double kilomsq)
  {
    landCrop[crop.ordinal()][year - START_YEAR] = kilomsq;
  }


  public double getMethodPercentage(int year, EnumCropType crop)
  {
    return cultivationMethod[crop.ordinal()][year - START_YEAR];
  }

  public void setMethodPercentage(int year, EnumCropType crop, double percentage)
  {
    cultivationMethod[crop.ordinal()][year - START_YEAR] = percentage;
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

  public double getCropNeedPerCapita(int year, EnumCropType crop)
  {
    return cropNeedPerCapita[crop.ordinal()];
  }

  public void setCropNeedPerCapita(int year, EnumCropType crop, double tonPerPerson)
  {
    cropNeedPerCapita[crop.ordinal()] = tonPerPerson;
  }
}
