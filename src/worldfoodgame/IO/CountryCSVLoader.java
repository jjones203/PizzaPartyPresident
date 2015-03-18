package worldfoodgame.IO;


import org.apache.commons.csv.CSVRecord;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVFormat;
import worldfoodgame.model.Country;
import worldfoodgame.common.EnumCropType;
import worldfoodgame.common.EnumGrowMethod;
import worldfoodgame.common.AbstractScenario;
import worldfoodgame.gui.CSVEditor;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import java.nio.charset.StandardCharsets;
import java.lang.Integer;
import java.lang.Double;

/**
 * CountryCSVLoader contains methods for parsing country data in csv file, creating list of
 * country objects. Uses Apache Commons CSV parser.
 * 3/15 TO DO: add error handling for both essential and nonessential fields
 * @author  jessica
 * @version Mar-15-2015
 */
public class CountryCSVLoader
{
  private static final String DATA_DIR_PATH = "resources/data/";
  private static final String DATA_FILE = "countryDataTest.csv";
  private static final int START_YEAR = AbstractScenario.START_YEAR;
  
  
  private ArrayList<Country> countries;
  private File csvFile;
  private List<CSVRecord> records;
  private String[] headers;
  
  /**
   * Constructor takes list to which countries will be added
   * @param countries   list of Country objects
   */
  public CountryCSVLoader(ArrayList<Country> countries)
  {
    this.countries = countries;
  }
  
  /**
   * Parses csv file with predetermined path; returns list containing both countries created from csv
   * file and countries in list with which loader was initialized. (Method can either populate an
   * empty list or append to existing list.)
   * @return copy ArrayList of countries
   */
  public ArrayList<Country> getCountriesFromCSV()
  {
    boolean parsedOk = false;
    while (parsedOk == false) parsedOk = parseCountries();
    return countries;
  }

  /**
   * Parses country data from DATA_FILE, adds countries to countries list.
   */ 
  private boolean parseCountries()
  {
    boolean parsedOk = false;
    ArrayList<Country> tempCountryList = new ArrayList<Country>();
    getRecords();

    for (CSVRecord record:records)
    { 
      Country country;
      if (record.getRecordNumber() == 1) continue; // skip line w/data types
      // if name in file, make country
      try
      {
        String name = record.get("country");
        if (!name.isEmpty()) country = new Country(name);
        else throw new CSVParsingException(record, this.csvFile);
        setEssentialFields(country,record);
        setNonessentialFields(country,record);
        tempCountryList.add(country);
        
      }
      // if name or essential fields empty, edit file
      catch (CSVParsingException exception)
      {
        //System.out.println("CSVLoader parseCountries - opening editor");
        callEditor(exception);
        return parsedOk;
      }
    }
    countries.addAll(tempCountryList);
    return true;
  }
  
   
  /**
   * Set population and land area
   * @param country   country object
   * @param record    country's CSVRecord
   */
  private void setEssentialFields(Country country, CSVRecord record)
  {
    try
    {
      country.setPopulation(START_YEAR, Integer.parseInt(record.get("population")));
      country.setLandTotal(START_YEAR, Double.parseDouble(record.get("landArea")));
    }
    catch (NumberFormatException e)
    {
      throw new CSVParsingException(record, this.csvFile);
    }
  }

  /**
   * Set fields other than name, population, and total land area.
   * If any field missing, its value must be determined.
   * @param country   country object
   * @param record    country's CSVRecord
   */
  private void setNonessentialFields(Country country, CSVRecord record)
  {
    Map<String,String> recordMap = record.toMap();
    setDemographicData(country,recordMap);    
    // crop data; note can't do this before setting population
    setCropData(country, recordMap);
    // grow method percentages
    setGrowMethodData(country, recordMap);
  }
  
  /**
   * Set averageAge, birthRate, mortality, migration, undernourish, and arableOpen fields.
   * @param country     country object
   * @param recordMap   map of strings (key=field name, value=field value) generated from
   *                    country's CSVRecord
   */
  private void setDemographicData(Country country, Map<String,String> recordMap)
  {
    String[] demographicFields = {"averageAge", "birthRate", "mortality", "migration", "undernourish",
                                  "arableOpen"};
    
    for (int i = 0; i < demographicFields.length; i++)
    {
      String field = demographicFields[i];
      String value = recordMap.get(field);
      /*if (value.isEmpty())
      {
        // get default value for field here
        value = "0";
        recordMap.put(field, value);
      }*/
      try
      {
        switch (field)
        {
          case "averageAge":
            country.setMedianAge(START_YEAR,  Integer.parseInt(value));
            break;
          case "birthRate":
            country.setBirthRate(START_YEAR, Double.parseDouble(value));
            break;
          case "mortality":
            country.setMortalityRate(START_YEAR, Double.parseDouble(value));
            break;
          case "migration":
            country.setMigrationRate(START_YEAR, Double.parseDouble(value));
            break;
          case "undernourish":
            country.setUndernourished(START_YEAR, Double.parseDouble(value)/100); //divide int by 100
            break;
          case "arableOpen":
            country.setArableLand(START_YEAR, Double.parseDouble(value));
            break;
          default: break;
        }
      }
      catch (NumberFormatException e)
      {
        // need to assign default value
        value = "0";
        recordMap.put(field, value);
        // loop again after reassigning value
        i--;
      }
    }
  }
  
  /**
   * Set production, exports, imports, and land fields for each crop type in EnumCropType.
   * @param country     country object
   * @param recordMap   map of strings (key=field name, value=field value) generated from
   *                    country's CSVRecord
   */
  private void setCropData(Country country, Map<String,String> recordMap)
  {  
    String[] cropFields = {"Production", "Exports", "Imports", "Land"};
    for (EnumCropType crop : EnumCropType.values()){
      Double production = null; // initialize as null objects to get rid of annoying error msg
      Double exports = null;
      Double imports = null;
      Double land = null;
      String cropString;
      if (crop == EnumCropType.OTHER_CROPS) cropString = "other"; 
      else cropString = crop.toString().toLowerCase();

      for (int i = 0; i < cropFields.length; i++)
      {
        // concatenate to cornProduction, cornExports, etc.
        String cropField = cropFields[i];
        String key = cropString + cropFields[i];
        String value = recordMap.get(key);

        /*if (value.isEmpty())
        {
          // get default value for field here
          value = "0";
          recordMap.put(key, value);
        }*/

        try
        {
          switch (cropField)
          {
            case "Production":
              production = Double.parseDouble(recordMap.get(key));
              break;
            case "Exports":
              exports = Double.parseDouble(recordMap.get(key));
              break;
            case "Imports":
              imports = Double.parseDouble(recordMap.get(key));
              break;
            case "Land":
              land  = Double.parseDouble(recordMap.get(key));
              break;
            default:
              break;
          }
        }
        catch (NumberFormatException e)
        {
          // need to assign default value
          value = "0";
          recordMap.put(key, value);
          // loop again after reassigning value
          i--;
        }
      }
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
  }
  
  /**
   * Set percentage for each method in EnumGrowMethod.
   * @param country     country object
   * @param recordMap   map of strings (key=field name, value=field value) generated from
   *                    country's CSVRecord
   */
  private void setGrowMethodData(Country country, Map<String,String> recordMap)
  {
    for (EnumGrowMethod method : EnumGrowMethod.values()) 
    {
      String methodString = method.toString().toLowerCase();
      String value = recordMap.get(methodString);
      /*if (value.isEmpty())
      {
        // set default value
        value = "0";
        recordMap.put(methodString, value);
      }*/
      
      try
      {
        country.setMethodPercentage(START_YEAR, method, Double.parseDouble(recordMap.get(methodString)));
      }
      catch (NumberFormatException e)
      {
        // set default value
        value = "0";
        country.setMethodPercentage(START_YEAR, method, Double.parseDouble(value));
      }
    }
  }
  
  /**
   * Creates CSVEditor when CSVParsingException thrown.
   * @param exception   the exception thrown
   */
  private void callEditor(CSVParsingException exception)
  {
    long fileLastModified = csvFile.lastModified();
    // local class for listening to editor window
    class EdListener extends WindowAdapter
    {
      boolean windowClosed;
      EdListener()
      {
        super();
        boolean windowClosed = false;
      } 
      public void windowClosed(WindowEvent e)
      {
        windowClosed = true;
      }
    }
    EdListener edListener = new EdListener();
    CSVEditor editor = new CSVEditor(headers, records, exception);
    editor.addWindowListener(edListener);
    while (csvFile.lastModified() == fileLastModified && edListener.windowClosed == false)
    {
      //wait for file to be edited
    }
  }
 
  /**
   * Parse the csv file specified by DATA_DIR_PATH+DATA_FILE. Use it
   * to populate a list of CSVRecords, assign list to records member variable.
   */
  private void getRecords()
  {
    records = new ArrayList<CSVRecord>();
    try
    {
      csvFile = new File(DATA_DIR_PATH+DATA_FILE);
      CSVFormat format;
      CSVParser parser;
      format = CSVFormat.DEFAULT.withHeader();
      parser = CSVParser.parse(csvFile, StandardCharsets.US_ASCII, format);
      headers = parser.getHeaderMap().keySet().toArray(new String[0]);
      records = parser.getRecords();
      parser.close();
    }
    catch (IOException e)
    {
      System.err.println("Country data file not found");
    }
  }
  
  
  
  /* for testing
  public static void main(String[] args)
  { 
    CountryCSVLoader testLoader = new CountryCSVLoader(new ArrayList<Country>());
   
    List<Country> countryList = new ArrayList<Country>();
    countryList = testLoader.getCountriesFromCSV();
    System.out.println("Testing - main method in CountryCSVLoader");
    for (Country ctry:countryList)
    {
      System.out.println(ctry.getName()+" "+ctry.getMethodPercentage(START_YEAR,EnumGrowMethod.ORGANIC));
    }
  }*/
  
  
}
