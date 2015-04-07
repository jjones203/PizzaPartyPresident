package worldfoodgame.gui.hud.infopanel;

import worldfoodgame.common.EnumCropType;
import worldfoodgame.model.Country;
import worldfoodgame.model.World;

import java.util.List;

/**
 * Holds a collection of Country object in the gui simultaneously. Manages the
 * logic for applying state changes to the allocated crops.
 *
 * All the method basically return either summations or averages.
 * Created by winston on 4/5/15.
 */
public class GroupCountryHandler extends CountryDataHandler
{

  private List<Country> countries;

  public GroupCountryHandler(List<Country> countries)
  {
    this.countries = countries;
  }

  @Override
  public String getName()
  {
    return "Summation Data";
  }

  @Override
  public double getPopulation()
  {
    double sum = 0;
    for (Country country : countries)
    {
      sum += country.getPopulation(World.getWorld().getCurrentYear());
    }
    return sum;
  }

  @Override
  public double getMedianAge()
  {
    double medianAge = 0;
    for (Country country : countries)
    {
      medianAge += country.getMedianAge(World.getWorld().getCurrentYear());
    }
    return medianAge / countries.size();
  }

  @Override
  public double getBirthRate()
  {
    double birthrate = 0;
    for (Country country : countries)
    {
      birthrate += country.getBirthRate(World.getWorld().getCurrentYear());
    }
    return birthrate / countries.size();
  }

  @Override
  public double getMortalityRate()
  {
    double mortalityRate = 0;
    for (Country country : countries)
    {
      mortalityRate += country.getMortalityRate(World.getWorld().getCurrentYear());
    }
    return mortalityRate / countries.size();
  }

  @Override
  public double getMigrationRate()
  {
    double migrationRate = 0;
    for (Country country : countries)
    {
      migrationRate += country.getMigrationRate(World.getWorld().getCurrentYear());
    }
    return migrationRate / countries.size();
  }

  @Override
  public double getUndernourished()
  {
    double undernourished = 0;
    for (Country country : countries)
    {
      undernourished += country.getUndernourished(World.getWorld().getCurrentYear());
    }
    return undernourished / countries.size();
  }

  @Override
  public double getLandTotal()
  {
    double landTotal = 0;
    for (Country country : countries)
    {
      landTotal += country.getLandTotal(World.getWorld().getCurrentYear());
    }
    return activeConverter.convert2Display(landTotal);
  }

  @Override
  public double getArable()
  {
    double arableLand = 0;
    for (Country country : countries)
    {
      arableLand += country.getArableLand(World.getWorld().getCurrentYear());
    }
    return activeConverter.convert2Display(arableLand);
  }

  @Override
  public double getProduction(EnumCropType type)
  {
    double tons = 0;
    for (Country country : countries)
    {
      tons += country.getCropProduction(World.getWorld().getCurrentYear(), type);
    }
    return tons;
  }

  @Override
  public double getImports(EnumCropType type)
  {
    double tons = 0;
    for (Country country : countries)
    {
      tons += country.getCropImport(World.getWorld().getCurrentYear(), type);
    }
    return tons;
  }

  @Override
  public double getExports(EnumCropType type)
  {
    double tons = 0;
    for (Country country : countries)
    {
      tons += country.getCropExport(World.getWorld().getCurrentYear(), type);
    }
    return tons;
  }

  @Override
  public double getCropLand(EnumCropType type)
  {
    double cropLand = 0;
    for (Country country : countries)
    {
      cropLand += country.getCropLand(World.getWorld().getCurrentYear(), type);
    }

    return activeConverter.convert2Display(cropLand);
  }

  @Override
  public void setLand(EnumCropType type, double kilom)
  {
    int year = World.getWorld().getCurrentYear();
    double STEP = getLandTotal() / 500; /* arbitrary, rough */

    double oldValue = getCropLand(type);
    double dx = activeConverter.convert2ModelSpace(kilom) - oldValue;

    STEP = dx > 0 ? STEP : -STEP;
    for (Country country : countries)
    {
      double currentVal = country.getCropLand(year, type);
      country.updateCropLand(year, type, currentVal + STEP);
    }

  }

  @Override
  public double getNeed(EnumCropType type)
  {
    double totalNeed = 0;
    for (Country country : countries)
    {
      totalNeed += country.getTotalCropNeed(World.getWorld().getCurrentYear(), type);
    }
    return totalNeed;
  }

  @Override
  public double getOpenLand()
  {
    double openLand = 0;
    for (Country country : countries)
    {
      openLand += country.getArableLandUnused(World.getWorld().getCurrentYear());
    }
    return openLand;
  }

}
