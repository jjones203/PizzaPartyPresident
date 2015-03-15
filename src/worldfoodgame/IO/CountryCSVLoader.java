package worldfoodgame.IO;


import org.apache.commons.csv.CSVRecord;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVFormat;
import worldfoodgame.model.Country;
import worldfoodgame.common.EnumCropType;
import worldfoodgame.common.EnumGrowMethod;
import worldfoodgame.common.AbstractScenario;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.nio.charset.StandardCharsets;
import java.lang.Integer;
import java.lang.Double;

/**
 * CountryCSVLoader contains methods for parsing country data in csv file, creating list of
 * country objects. Uses Apache Commons CSV parser.
 * @author  jessica
 * @version Mar-14-2015
 */
public class CountryCSVLoader
{
  private final static String DATA_DIR_PATH = "resources/data/";
  private final static String DATA_FILE = "countryDataTest.csv";
  public static final int START_YEAR = AbstractScenario.START_YEAR;
  
  /**
   * Static method gets country data from DATA_FILE, populates and returns list
   * of countries.
   * @return list of country objects generated from CSV file
   */
  public static List<Country> getCountries()
  {
    List<Country> countryList = new ArrayList<Country>();
    try
    {
      File csvFile = new File(DATA_DIR_PATH+DATA_FILE);
      CSVFormat format = CSVFormat.DEFAULT.withHeader();
      CSVParser parser = CSVParser.parse(csvFile, StandardCharsets.US_ASCII, format);
      
      for (CSVRecord record:parser)
      {
        if (parser.getCurrentLineNumber() == 2) continue; // skip line w/data types
        String name = record.get("country");
        Country country = new Country(name);
        parseCountryRecord(country, record);
        countryList.add(country);
      }
      
      parser.close();
    }
    catch (IOException e)
    {
      System.err.println("Country data file not found");
    }
    return countryList;
  }
  
  private static void parseCountryRecord(Country country, CSVRecord record)
  {
    // demographics from csv
    country.setPopulation(START_YEAR, Integer.parseInt(record.get("population")));
    country.setMedianAge(START_YEAR,  Integer.parseInt(record.get("averageAge")));
    country.setBirthRate(START_YEAR, Double.parseDouble(record.get("birthRate")));
    country.setMortalityRate(START_YEAR, Double.parseDouble(record.get("mortality")));
    country.setMigrationRate(START_YEAR, Double.parseDouble(record.get("migration")));
    country.setUndernourished(START_YEAR, Double.parseDouble(record.get("undernourish"))/100); // divide int by 100
    
    // crop data; note can't do this before setting population
    for (EnumCropType crop : EnumCropType.values()) setCropData(country, crop, record);
    
    // land
    country.setArableLand(START_YEAR, Double.parseDouble(record.get("arableOpen")));
    country.setLandTotal(START_YEAR, Double.parseDouble(record.get("landArea")));
    
    // grow method percentages
    for (EnumGrowMethod method : EnumGrowMethod.values()) setGrowMethodData(country, method, record);
  }
  
  private static void setCropData(Country country, EnumCropType crop, CSVRecord record)
  {
    String cropString;
    if (crop == EnumCropType.OTHER_CROPS) cropString = "other"; 
    else cropString = crop.toString().toLowerCase();
    // get values from csv
    double production = Double.parseDouble(record.get(cropString+"Production"));
    double exports = Double.parseDouble(record.get(cropString+"Exports"));
    double imports = Double.parseDouble(record.get(cropString+"Imports"));
    double land = Double.parseDouble(record.get(cropString+"Land"));
    // calculate yield and need
    double yield = production/land;
    double need = (production + imports - exports)/country.getPopulation(START_YEAR);
    // set values
    country.setCropProduction(START_YEAR, crop, production);
    country.setCropExport(START_YEAR, crop, exports);
    country.setCropImport(START_YEAR, crop, imports);
    country.setCropLand(START_YEAR, crop, land);
    country.setCropYield(START_YEAR, crop, yield);
    country.setCropNeedPerCapita(START_YEAR, crop, need);
  }
    
  private static void setGrowMethodData(Country country, EnumGrowMethod method, CSVRecord record)
  {
    String methodString = method.toString().toLowerCase();
    country.setMethodPercentage(START_YEAR, method, Double.parseDouble(record.get(methodString)));
  }
 
  /* for testing
  public static void main(String[] args)
  {
    List<Country> countries = getCountries();
    System.out.println("Testing - main method in CountryCSVLoader");
    for (Country ctry:countries)
    {
      System.out.println(ctry.getName()+" "+ctry.getCropYield(START_YEAR,EnumCropType.WHEAT));
    }
  }*/
}
