package worldfoodgame.gui.hud.infopanel;

import worldfoodgame.common.EnumCropType;
import worldfoodgame.model.Country;

import java.util.List;

/**
 * Created by winston on 3/13/15.
 * <p/>
 * Stores and handles country data interactions for a given country at a given
 * year.
 */
public abstract class CountryDataHandler
{

  // todo add write method, that takes the values in the data handler and writes them to the country.

  public static CountryDataHandler getNullData()
  {
    return new CountryDataHandler()
    {
      @Override
      public String getName()
      {
        return "";
      }

      @Override
      public double getPopulation()
      {
        return 0;
      }

      @Override
      public double getMedianAge()
      {
        return 0;
      }

      @Override
      public double getBirthRate()
      {
        return 0;
      }

      @Override
      public double getMortalityRate()
      {
        return 0;
      }

      @Override
      public double getMigrationRate()
      {
        return 0;
      }

      @Override
      public double getUndernourished()
      {
        return 0;
      }

      @Override
      public double getLandTotal()
      {
        return 0;
      }

      @Override
      public double getArableOpen()
      {
        return 0;
      }

      @Override
      public double getProduction(EnumCropType type)
      {
        return 0;
      }

      @Override
      public double getImports(EnumCropType type)
      {
        return 0;
      }

      @Override
      public double getExports(EnumCropType type)
      {
        return 0;
      }

      @Override
      public double getLand(EnumCropType type)
      {
        return 0;
      }

      @Override
      public void setLand(EnumCropType type, double p)
      {

      }
      @Override
      public double getNeed(EnumCropType type)
      {
        return 0;
      }

      @Override
      public double getOpenLand()
      {
        return 0;
      }
    };
  }

  public static CountryDataHandler getData(List<Country> activeCountries, int year)
  {
    if (activeCountries.size() == 1)
    {
      return extractData(activeCountries.get(0), year);
    }
    else
    {
      return summationData(activeCountries, year);
    }
  }

  private static CountryDataHandler extractData(Country country, int year)
  {

    return new SingleCountryHandeler(country, year);
  }


  private static CountryDataHandler summationData(List<Country> activeCountries, int year)
  {
    System.err.println("summationData is not implemented yet!");
    return extractData(activeCountries.get(0), year);
  }



  public abstract String getName();
  public abstract double getPopulation();
  public abstract double getMedianAge();
  public abstract double getBirthRate();
  public abstract double getMortalityRate();
  public abstract double getMigrationRate();
  public abstract double getUndernourished();
  public abstract double getLandTotal();
  public abstract double getArableOpen();
  public abstract double getProduction(EnumCropType type);
  public abstract double getImports(EnumCropType type);
  public abstract double getExports(EnumCropType type);

  public abstract double getLand(EnumCropType type);


  public abstract void setLand(EnumCropType type, double p);

  public abstract double getNeed(EnumCropType type);
  public abstract double getOpenLand();
}


