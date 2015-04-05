package worldfoodgame.gui.hud.infopanel;

import worldfoodgame.common.EnumCropType;
import worldfoodgame.model.Country;

/**
 * Created by winston on 3/29/15.
 */
public class SingleCountryHandeler extends CountryDataHandler
{
  private Country country;
  private int year;

  public SingleCountryHandeler(Country country, int year)
  {
    this.country = country;
    this.year = year;
  }

  @Override
  public String getName()
  {
    return country.getName();
  }

  @Override
  public double getPopulation()
  {
    return country.getPopulation(year);
  }

  @Override
  public double getMedianAge()
  {
    return country.getMedianAge(year);
  }

  @Override
  public double getBirthRate()
  {
    return country.getBirthRate(year);
  }

  @Override
  public double getMortalityRate()
  {
    return country.getMortalityRate(year);
  }

  @Override
  public double getMigrationRate()
  {
    return country.getMigrationRate(year);
  }

  @Override
  public double getUndernourished()
  {
    return country.getUndernourished(year);
  }

  @Override
  public double getLandTotal()
  {
    return CountryDataHandler.activeConverter.convert2Display(country.getLandTotal(year));
  }

  @Override
  public double getArableOpen()
  {
    return CountryDataHandler.activeConverter.convert2Display(country.getArableLand(year));
  }

  @Override
  public double getProduction(EnumCropType type)
  {
    return country.getCropProduction(year,type);
  }

  @Override
  public double getImports(EnumCropType type)
  {
    return country.getCropImport(year, type);
  }

  @Override
  public double getExports(EnumCropType type)
  {
    return country.getCropExport(year, type);
  }

  @Override
  public double getLand(EnumCropType type)
  {
    return CountryDataHandler.activeConverter.convert2Display(country.getCropLand(year, type));
  }

  @Override
  public void setLand(EnumCropType type, double kilom)
  {
    double val = CountryDataHandler.activeConverter.convert2ModelSpace(kilom);
    country.updateCropLand(year, type, val);
  }

  @Override
  public double getNeed(EnumCropType type)
  {
    return country.getCropNeedPerCapita(type);
  }

  @Override
  public double getOpenLand()
  {
    return CountryDataHandler.activeConverter.convert2Display(country.getArableLandUnused(year));
  }
}
