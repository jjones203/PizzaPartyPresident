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
  public static DISPLAY_UNIT activeConverter = DISPLAY_UNIT.METRIC;

  /**
   * this is used when there in nothing selected in the info panel,
   * representing a kind of null data.
   * @return zero data set.
   */
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
      public double getArable()
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
      public double getCropLand(EnumCropType type)
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


  /**
   Create a CountryDataHandler to handle the data of the List of countries
   passed to it.
   
   @param activeCountries List of countries the Handler is need for
   @param year            year the Handler will be managing
   @return      A CountryDataHandler with accessors to data from the countries given
   */
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

  /* given a country and a year, extracts a data handler */
  protected static CountryDataHandler extractData(Country country, int year)
  {
    return new SingleCountryHandeler(country, year);
  }

  /* given a  list of countries and a year, extracts a data handler */
  private static CountryDataHandler summationData(List<Country> activeCountries, int year)
  {
    return new GroupCountryHandler(activeCountries);
  }


  /* all the acessor method... */
  public abstract String getName();
  public abstract double getPopulation();
  public abstract double getMedianAge();
  public abstract double getBirthRate();
  public abstract double getMortalityRate();
  public abstract double getMigrationRate();
  public abstract double getUndernourished();
  public abstract double getLandTotal();
  public abstract double getArable();
  public abstract double getProduction(EnumCropType type);
  public abstract double getImports(EnumCropType type);
  public abstract double getExports(EnumCropType type);
  public abstract double getCropLand(EnumCropType type);
  public abstract void setLand(EnumCropType type, double p);
  public abstract double getNeed(EnumCropType type);
  public abstract double getOpenLand();

  public String landUnite()
  {
    return activeConverter.getDisplayLabel();
  }




  /**
   * The enum is the converter for the display. handels the logic on converting
   * between the model unite space and the user specifiec gui space.
   */
  public enum DISPLAY_UNIT
  {
    METRIC("km"),
    US("mi");

    private String displayLabel;

    DISPLAY_UNIT(String displayLabel)
    {
      this.displayLabel = displayLabel;
    }

    /**
     * Converts the square kilometers the the currently specified Unite.
     * @param klms
     * @return
     */
    public double convert2Display(double klms)
    {
      double val = -1;
      switch (this)
      {
        case METRIC:
          val = klms;
          break;
        case US:
          val = klms * 0.38610; //todo check this.
          break;
        default:
          throw new RuntimeException("non-exhaustive pattern!");
      }
      return val;
    }


    /**
     * Converts FROM the display unite to the Model space, which is metric.
     * @param length
     * @return
     */
    public double convert2ModelSpace(double length)
    {
      double val = -1;
      switch (this)
      {
        case METRIC:
          val = length;
          break;
        case US:
          val = length / 0.38610; //todo check this.
          break;
        default:
          throw new RuntimeException("non-exhaustive pattern!");
      }
      return  val;
    }

    public String getDisplayLabel()
    {
      return displayLabel;
    }
  }
}


