package worldfoodgame.IO;


import org.apache.commons.csv.CSVRecord;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVFormat;
import worldfoodgame.model.Country;
import worldfoodgame.common.EnumCropType;
import worldfoodgame.common.EnumGrowMethod;
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
  public static final int START_YEAR = 2014;
  
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
    // demographics
    country.setPopulation(START_YEAR, Integer.parseInt(record.get("population")));
    country.setMedianAge(START_YEAR,  Integer.parseInt(record.get("averageAge")));
    country.setBirthRate(START_YEAR, Double.parseDouble(record.get("birthRate")));
    country.setMortalityRate(START_YEAR, Double.parseDouble(record.get("mortality")));
    country.setMigrationRate(START_YEAR, Double.parseDouble(record.get("migration")));
    country.setUndernourished(START_YEAR, Double.parseDouble(record.get("undernourish"))/100); // divide int by 100
    
    // main crops
    setCropData(country, EnumCropType.CORN, record);
    setCropData(country, EnumCropType.WHEAT, record);
    setCropData(country, EnumCropType.RICE, record);
    setCropData(country, EnumCropType.SOY, record);
    
    // other crops
    country.setCropProduction(START_YEAR, EnumCropType.OTHER_CROPS, Double.parseDouble(record.get("otherProduction")));
    country.setCropExport(START_YEAR, EnumCropType.OTHER_CROPS, Double.parseDouble(record.get("otherExports")));
    country.setCropImport(START_YEAR, EnumCropType.OTHER_CROPS, Double.parseDouble(record.get("otherImports")));
    country.setCropLand(START_YEAR, EnumCropType.OTHER_CROPS, Double.parseDouble(record.get("otherLand")));
    
    // etc
    country.setArableLand(START_YEAR, Double.parseDouble(record.get("arableOpen")));
    country.setLandTotal(START_YEAR, Double.parseDouble(record.get("landArea")));
    
    // set grow method percentages
    for (EnumGrowMethod method : EnumGrowMethod.values()) setGrowMethodData(country, method, record);
  }
  
  private static void setCropData(Country country, EnumCropType crop, CSVRecord record)
  {
    String cropString = crop.toString().toLowerCase();
    country.setCropProduction(START_YEAR, crop, Double.parseDouble(record.get(cropString+"Production")));
    country.setCropExport(START_YEAR, crop, Double.parseDouble(record.get(cropString+"Exports")));
    country.setCropImport(START_YEAR, crop, Double.parseDouble(record.get(cropString+"Imports")));
    country.setCropLand(START_YEAR, crop, Double.parseDouble(record.get(cropString+"Land")));
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
    for (Country ctry:countries)
    {
      System.out.println(ctry.getName()+" "+ctry.getMethodPercentage(START_YEAR, EnumGrowMethod.ORGANIC));
    }
  }*/
}
