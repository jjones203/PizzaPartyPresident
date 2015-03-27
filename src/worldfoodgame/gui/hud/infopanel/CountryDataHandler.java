package worldfoodgame.gui.hud.infopanel;

import worldfoodgame.IO.CountryCSVLoader;
import worldfoodgame.IO.XMLparsers.CountryXMLparser;
import worldfoodgame.common.EnumCropType;
import worldfoodgame.model.Country;

import java.util.*;

/**
 * Created by winston on 3/13/15.
 * <p/>
 * Stores and handles country data interactions for a given country at a given
 * year.
 */
public class CountryDataHandler extends Observable
{
  private Collection<Country> derivedFrom;

  String name;

  public double
    population, medianAge, birthRate,
    mortalityRate, migrationRate, undernourished,
    landTotal, arableOpen;

  public HashMap<EnumCropType, Double> production = new HashMap<>();
  HashMap<EnumCropType, Double> imports = new HashMap<>();
  HashMap<EnumCropType, Double> exports = new HashMap<>();
  HashMap<EnumCropType, Double> land = new HashMap<>();
  HashMap<EnumCropType, Double> need = new HashMap<>();

  private CountryDataHandler()
  {
    // to keep things secret/safe.
  }


  public double getCultivatedLand()
  {
    double sum = 0;

    for (EnumCropType type : EnumCropType.values())
    {
      sum += land.get(type);
    }
    return sum;
  }

  // todo add write method, that takes the values in the data handler and writes them to the country.

  public static CountryDataHandler getTestData()
  {
    CountryDataHandler dataHandler = new CountryDataHandler();

    dataHandler.name = "";
    dataHandler.population = 0;
    dataHandler.medianAge = 0;
    dataHandler.birthRate = 0;
    dataHandler.mortalityRate = 0;
    dataHandler.migrationRate = 0;
    dataHandler.undernourished = 0; // percent

    dataHandler.production.put(EnumCropType.CORN, 0.0);
    dataHandler.production.put(EnumCropType.WHEAT, 0.0);
    dataHandler.production.put(EnumCropType.RICE, 0.0);
    dataHandler.production.put(EnumCropType.SOY, 0.0);
    dataHandler.production.put(EnumCropType.OTHER_CROPS, 0.0);

    dataHandler.imports.put(EnumCropType.CORN, 0.0);
    dataHandler.imports.put(EnumCropType.WHEAT, 0.0);
    dataHandler.imports.put(EnumCropType.RICE, 0.0);
    dataHandler.imports.put(EnumCropType.SOY, 0.0);
    dataHandler.imports.put(EnumCropType.OTHER_CROPS, 0.0);

    dataHandler.exports.put(EnumCropType.CORN, 0.0);
    dataHandler.exports.put(EnumCropType.WHEAT, 0.0);
    dataHandler.exports.put(EnumCropType.RICE, 0.0);
    dataHandler.exports.put(EnumCropType.SOY, 0.0);
    dataHandler.exports.put(EnumCropType.OTHER_CROPS, 0.0);

    dataHandler.land.put(EnumCropType.CORN, 0.0);
    dataHandler.land.put(EnumCropType.WHEAT, 0.0);
    dataHandler.land.put(EnumCropType.RICE, 0.0);
    dataHandler.land.put(EnumCropType.SOY, 0.0);
    dataHandler.land.put(EnumCropType.OTHER_CROPS, 0.0);

    dataHandler.landTotal = 0.0;
    dataHandler.arableOpen = 0.0;


    return dataHandler;
  }

  public void setland(EnumCropType type, double percent)
  {
    land.put(type, percent);
  }

  public double getOpenLand()
  {
    return arableOpen - getCultivatedLand();
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
    CountryDataHandler dataHandler = new CountryDataHandler();
    dataHandler.derivedFrom = new ArrayList<>();
    dataHandler.derivedFrom.add(country);

    dataHandler.name = country.getName();
    dataHandler.landTotal = country.getLandTotal(year);
    dataHandler.population = country.getPopulation(year);
    dataHandler.medianAge = country.getMedianAge(year);
    dataHandler.birthRate = country.getBirthRate(year);
    dataHandler.mortalityRate = country.getMortalityRate(year);
    dataHandler.migrationRate = country.getMigrationRate(year);
    dataHandler.undernourished = country.getUndernourished(year);
    dataHandler.arableOpen = country.getArableLand(year);

    for (EnumCropType type : EnumCropType.values())
    {
      dataHandler.land.put(type, country.getCropLand(year, type));
      dataHandler.imports.put(type, country.getCropImport(year, type));
      dataHandler.exports.put(type, country.getCropExport(year, type));
      dataHandler.production.put(type, country.getCropProduction(year, type));
      dataHandler.need.put(type, country.getCropNeedPerCapita(type));
    }

    return dataHandler;
  }


  private static CountryDataHandler summationData(List<Country> activeCountries, int year)
  {
    System.err.println("summationData is not implemented yet!");
    return extractData(activeCountries.get(0), year);
  }

  @Override
  public String toString()
  {
    return "CountryDataHandler{" +
      "arableOpen=" + arableOpen +
      ", derivedFrom=" + derivedFrom +
      ", name='" + name + '\'' +
      ", population=" + population +
      ", medianAge=" + medianAge +
      ", birthRate=" + birthRate +
      ", mortalityRate=" + mortalityRate +
      ", migrationRate=" + migrationRate +
      ", undernourished=" + undernourished +
      ", landTotal=" + landTotal +
      ", production=" + production +
      ", imports=" + imports +
      ", exports=" + exports +
      ", land=" + land +
      ", need=" + need +
      '}';
  }

  // for testing.
  public static void main(String[] args)
  {
    Collection<Country> countries = new CountryXMLparser().getCountries();
    CountryCSVLoader csvLoader = new CountryCSVLoader(countries);
    countries = csvLoader.getCountriesFromCSV();

    for (Country country : countries)
    {
      System.out.println(extractData(country, 2014));
    }

  }
}
