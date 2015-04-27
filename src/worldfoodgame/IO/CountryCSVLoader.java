package worldfoodgame.IO;


import org.apache.commons.csv.CSVRecord;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVFormat;
import worldfoodgame.model.Country;
import worldfoodgame.model.EnumContinentNames;
import worldfoodgame.IO.CSVhelpers.CSVParsingException;
import worldfoodgame.IO.CSVhelpers.CountryCSVDataGenerator;
import worldfoodgame.common.EnumCropType;
import worldfoodgame.common.EnumGrowMethod;
import worldfoodgame.common.AbstractScenario;
import worldfoodgame.gui.CSVEditor;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.EnumMap;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.nio.charset.StandardCharsets;
import java.lang.Integer;
import java.lang.Double;

/**
 * CountryCSVLoader contains methods for parsing country data in csv file, creating list of
 * country objects. Uses Apache Commons CSV parser.
 * @author  jessica
 * @version Mar-21-2015
 */
public class CountryCSVLoader
{
  private static final String DATA_DIR_PATH = "resources/data/";
  private static final String DATA_FILE = "countryData2.csv";
  private static final int START_YEAR = AbstractScenario.START_YEAR;
  
  
  private Collection<Country> countries;        // collection populated by parsing csv
  private Collection<Country> countriesToMerge; // collection passed in (i.e., after parsing xml)
  private File csvFile;
  private List<CSVRecord> records;
  private String[] headers;
  
  /**
   * Constructor takes list of country objects that need data from csv file (previously created from xml file)
   */
  public CountryCSVLoader(Collection<Country> countriesToMerge)
  {
    this.countriesToMerge = countriesToMerge;
    countries = new ArrayList<Country>();
  }
  
  /**
   * Parses csv file with predetermined path; uses data from csv file to populate fields of
   * country objects passed in constructor.
   * @return  same country objects passed in, with fields populated from csv data (if possible)
   */
  public Collection<Country> getCountriesFromCSV()
  {
    boolean parsedOk = false;
    // create collection of countries from CSV
    while (parsedOk == false) parsedOk = parseCountries();
    // merge those countries with the ones passed in
    for (Country xmlCountry:countriesToMerge)
    {
      String xmlCountryName = xmlCountry.getName();
      boolean countryFound = false;
      Iterator<Country> csvItr = countries.iterator();
      while (csvItr.hasNext())
      {
        Country csvCountry = csvItr.next();
        if (xmlCountryName.equals(csvCountry.getName()))
        {
          // copy data from csv country object into xml country object
          xmlCountry = copyCountryData(xmlCountry, csvCountry);
          // remove country from csv list after copying it
          csvItr.remove();
          countryFound = true;
          break;
        }
      }
      if (countryFound == false)
      {
        //todo add method (String nameOf Country) -> offending xml file, -> load in XML editor.
        System.err.print("CSV data not found for country " + xmlCountryName + "\n");
      }
    }
    
    // if not all csv data copied, print message
    if (countries.isEmpty() == false)
    {
      for (Country country:countries)
      {
        System.err.print("XML data not found for country "+country.getName()+"\n");
      }
    }
    return countriesToMerge;
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
        if (!name.isEmpty())
        {
          country = new Country(name);
        }
        else throw new CSVParsingException("country", record, this.csvFile);
        setEssentialFields(country,record);
        setNonessentialFields(country,record);
        setPopulationProjections(country,record);
        tempCountryList.add(country);
        
      }
      // if name or essential fields empty, edit file
      catch (CSVParsingException exception)
      {
        callEditor(exception);
        return parsedOk;
      }
    }
    countries.addAll(tempCountryList);
    return true;
  }
  
   
  /**
   * Set population, land area, continent
   * @param country   country object
   * @param record    country's CSVRecord
   */
  private void setEssentialFields(Country country, CSVRecord record)
  {
    try
    {
      int value = Integer.parseInt(record.get("population"));
      if (value > 0) country.setPopulation(START_YEAR, value);
      else throw new IllegalArgumentException();
    }
    catch (IllegalArgumentException e)
    {
      throw new CSVParsingException("population", record, this.csvFile);
    }
    try
    {
      double value = Double.parseDouble(record.get("landArea"));
      if (value > 0) country.setLandTotal(START_YEAR, value);
      else throw new IllegalArgumentException();
    }
    catch (IllegalArgumentException e)
    {
      throw new CSVParsingException("landArea", record, this.csvFile);
    }
    try
    {
      String value = record.get("continent");
      EnumContinentNames continentName = EnumContinentNames.findContinentName(value);
      if (continentName != null) country.setContinentName(continentName);
      else throw new IllegalArgumentException();
    }
    catch (IllegalArgumentException e)
    {
      throw new CSVParsingException("continent", record, this.csvFile);
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
      try
      {
        switch (field)
        {
          case "averageAge":
            int intValue = Integer.parseInt(value);
            if (intValue > 0) country.setMedianAge(intValue);
            else throw new IllegalArgumentException(); 
            break;
          case "birthRate":
            double numValue = Double.parseDouble(value);
            if (numValue >= 0 && numValue < 2000) country.setBirthRate(numValue);
            else throw new IllegalArgumentException();
            break;
          case "mortality":
            numValue = Double.parseDouble(value);
            if (numValue >= 0 && numValue <= 1000) country.setMortalityRate(START_YEAR, numValue);
            else throw new IllegalArgumentException();
            break;
          case "migration":
            numValue = Double.parseDouble(value);
            if (numValue >= -1000 && numValue <= 1000) country.setMigrationRate(numValue);
            else throw new IllegalArgumentException();
            break;
          case "undernourish":
            numValue = Double.parseDouble(value);
            if (numValue >= 0 && numValue <= 100) country.setUndernourished(START_YEAR, numValue/100); //divide int by 100
            else throw new IllegalArgumentException();
            break;
          case "arableOpen":
            numValue = Double.parseDouble(value);
            if (numValue >= 0 && numValue <= country.getLandTotal(START_YEAR)) country.setArableLand(START_YEAR, numValue);
            else throw new IllegalArgumentException();
            break;
          default: ;
        }
      }
      catch (IllegalArgumentException e)
      {
        // need to assign default value
        CountryCSVDataGenerator.fixDemographic(country, field);
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
    cropLoop:
    for (EnumCropType crop : EnumCropType.values()){
      Double production = null; // initialize as null objects to get rid of annoying error msg
      Double exports = null;
      Double imports = null;
      Double land = null;
      String cropString;
      /*if (crop == EnumCropType.OTHER_CROPS) cropString = "other"; 
      else cropString = crop.toString().toLowerCase();*/
      cropString = crop.getOldName();
      for (int i = 0; i < cropFields.length; i++)
      {
        // concatenate to cornProduction, cornExports, etc.
        String cropField = cropFields[i];
        String key = cropString + cropFields[i];
        try
        {
          Double value = Double.parseDouble(recordMap.get(key));
          if (value < 0) throw new IllegalArgumentException();
          switch (cropField)
          {
            case "Production":
               production = value;
              break;
            case "Exports":
              exports = value;
              
              break;
            case "Imports":
              imports = value;
              break;
            case "Land":
              if (value <= country.getLandTotal(START_YEAR)) land  = value;
              else throw new IllegalArgumentException();
              break;
            default:
              break;
          }
        }
        catch (IllegalArgumentException e)
        {
          CountryCSVDataGenerator.fixCropData(country, crop);
          continue cropLoop;
        }
      }
      double yield = production/land;
      double tonsConsumed = production + imports - exports;
      // set values
      country.setCropProduction(START_YEAR, crop, production);
      country.setCropExport(START_YEAR, crop, exports);
      country.setCropImport(START_YEAR, crop, imports);
      country.setCropLand(START_YEAR, crop, land);
      country.setCropYield(START_YEAR, crop, yield);
      country.setCropNeedPerCapita(crop, tonsConsumed, country.getUndernourished(START_YEAR));
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
    double sum = 0;
    for (EnumGrowMethod method : EnumGrowMethod.values()) 
    {
      try
      {
        double value = 0;
        String methodString = method.toString().toLowerCase();
        value = Double.parseDouble(recordMap.get(methodString));
        if (value >= 0 && value <= 1)
        {
          country.setMethodPercentage(START_YEAR, method, value);
          sum += value;
        }
        else throw new IllegalArgumentException();
      }
      catch (IllegalArgumentException e) 
      {
        CountryCSVDataGenerator.fixGrowMethods(country);
        return;
      }
    }
    if (sum != 1) CountryCSVDataGenerator.fixGrowMethods(country);
  }

  /**
   * Set population values for 2015-2050.
   * @param country     country object
   * @param recordMap   map of strings (key=field name, value=field value) generated from
   *                    country's CSVRecord
   */
  private void setPopulationProjections(Country country, CSVRecord record)
  {
    for (int year = 2015; year <= 2050; year++)
    {
      int value = 0;
      String yearStr = Integer.toString(year);
      value = Integer.parseInt(record.get(yearStr));
      value = value * 1000; // projections are in 1000's
      country.setPopulation(year, value);
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
  
  private Country copyCountryData(Country countryFinal, Country countryTemp)
  {
    //countryTemp values
     int population = countryTemp.getPopulation(START_YEAR);
     double medianAge = countryTemp.getMedianAge(START_YEAR);
     double birthRate = countryTemp.getBirthRate(START_YEAR);
     double mortalityRate = countryTemp.getMortalityRate(START_YEAR);
     double migrationRate = countryTemp.getMigrationRate(START_YEAR);
     double undernourished = countryTemp.getUndernourished(START_YEAR);
     double landTotal = countryTemp.getLandTotal(START_YEAR);
     double landArable = countryTemp.getArableLand(START_YEAR);
     EnumContinentNames continentName = countryTemp.getContinentName();
     
     // copy everything
     countryFinal.setPopulation(START_YEAR, population);
     countryFinal.setMedianAge(medianAge);
     countryFinal.setBirthRate(birthRate);
     countryFinal.setMortalityRate(START_YEAR, mortalityRate);
     countryFinal.setMigrationRate(migrationRate);
     countryFinal.setUndernourished(START_YEAR, undernourished);
     countryFinal.setLandTotal(START_YEAR, landTotal);
     countryFinal.setArableLand(START_YEAR, landArable);
     countryFinal.setContinentName(continentName);
     
     copyCropValues(countryFinal, countryTemp);
    
     for (EnumGrowMethod method:EnumGrowMethod.values())
     {
       double percentage = countryTemp.getMethodPercentage(START_YEAR,method);
       countryFinal.setMethodPercentage(START_YEAR, method, percentage);
     }
     
     int estPopulation;
     for (int year = 2015; year <= 2050; year++)
     {
        estPopulation = countryTemp.getPopulation(year);
        countryFinal.setPopulation(year, estPopulation);
     }
     
     return countryFinal;
  }
  
  private void copyCropValues(Country countryFinal, Country countryTemp)
  {
    for (EnumCropType crop:EnumCropType.values())
    {
      double production = countryTemp.getCropProduction(START_YEAR, crop);
      double imports = countryTemp.getCropImport(START_YEAR, crop);
      double exports = countryTemp.getCropExport(START_YEAR, crop);
      double land = countryTemp.getCropLand(START_YEAR, crop);
      double yield = countryTemp.getCropYield(START_YEAR, crop);
      double need = countryTemp.getCropNeedPerCapita(crop);

      countryFinal.setCropProduction(START_YEAR, crop, production);
      countryFinal.setCropImport(START_YEAR, crop, imports);
      countryFinal.setCropExport(START_YEAR, crop, exports);
      countryFinal.setCropLand(START_YEAR, crop, land);
      countryFinal.setCropYield(START_YEAR, crop, yield);
      countryFinal.setCropNeedPerCapita(crop, need);
    }
  }
  
  
  
  /* for testing
  public static void main(String[] args)
  { 
    ArrayList<Country> fakeXmlList = new ArrayList<Country>();
    fakeXmlList.add(new Country("Afghanistan"));
    fakeXmlList.add(new Country("Albania"));
    fakeXmlList.add(new Country("Algeria"));
    //fakeXmlList.add(new Country("Vatican City"));
    CountryCSVLoader testLoader = new CountryCSVLoader(fakeXmlList);
    Collection<Country> countryList;
    //List<Country> countryList = new ArrayList<Country>();
    countryList = testLoader.getCountriesFromCSV();
    System.out.println("Testing - main method in CountryCSVLoader");
    for (Country ctry:countryList)
    {
      //System.out.println(ctry.getName()+" "+ctry.getMethodPercentage(START_YEAR,EnumGrowMethod.ORGANIC));
      System.out.println(ctry.getName()+" "+ctry.getPopulation(2015));
      System.out.println(ctry.getContinentName().toString());
      //System.out.println(ctry.getName()+" "+ctry.getCropProduction(START_YEAR,EnumCropType.WHEAT));
    }
  }*/
  
  
}
