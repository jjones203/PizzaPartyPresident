package worldfoodgame.IO.CSVhelpers;

import java.util.Map;

import worldfoodgame.common.AbstractScenario;
import worldfoodgame.common.EnumCropType;
import worldfoodgame.common.EnumGrowMethod;
import worldfoodgame.model.Country;

//TODO: comments, demographic & crop methods
/**
 * Class with static methods for filling in country data omitted
 * from CSV file.
 * @author jessica
 * @verion 21-Mar-2015
 */
public final class CountryCSVDataGenerator implements CountryCSVDefaultData
{
  private static final int START_YEAR = AbstractScenario.START_YEAR;
  
  /**
   * Assigns values to country's fields based on world median values
   * for demographics and ratio of arable land/total land.
   * @param country   country with missing data
   * @param field     field that needs to be assigned
   */
  public static void fixDemographic(Country country, String field)
  {
    switch (field)
    {
      case "averageAge":
        country.setMedianAge(START_YEAR, WORLD_AVG_AGE);
        break;
      case "birthRate":
        country.setBirthRate(START_YEAR, WORLD_BIRTH_RATE);
        break;
      case "mortality":
        country.setMortalityRate(START_YEAR, WORLD_MORTALITY);
        break;
      case "migration":
        country.setMigrationRate(START_YEAR, WORLD_MIGRATION);
        break;
      case "undernourish":
        country.setUndernourished(START_YEAR, WORLD_UNDERNOURISH/100); //divide int by 100
        break;
      case "arableOpen":
        country.setArableLand(START_YEAR, WORLD_PERCENT_ARABLE * country.getLandTotal(START_YEAR));
        break;
    }
  }
  
  /**
   * Assigns values to country's fields for a particular crop based on world median need for
   * that crop. Assumes country imports its entire need for the crop for the sake of simplicity and
   * also because countries for which agricultural data is unavailable tend to import much of their
   * food (e.g., Monaco, Bahrain, Singapore).
   * @param country   country with missing data   
   * @param crop      crop for which we need to fill in production, imports, exports, and land
   */
  public static void fixCropData(Country country, EnumCropType crop)
  {
    int population = country.getPopulation(START_YEAR);
    // get world median ton/capita
    int index = -1;
    for (int i = 0; i < CountryCSVDefaultData.cropTypes.length; i++)
    {
      if (crop == CountryCSVDefaultData.cropTypes[i])
      {
        index = i;
        break;
      }
    }
    double worldPerCap = CountryCSVDefaultData.cropTonPerCapita[index];
    double countryNeed = worldPerCap * country.getPopulation(START_YEAR);
    
    // set country's need to world per capita media need * population; assume all imported
    country.setCropProduction(START_YEAR, crop, 0);
    country.setCropExport(START_YEAR, crop, 0);
    country.setCropImport(START_YEAR, crop, countryNeed);
    country.setCropLand(START_YEAR, crop, 0);
    country.setCropYield(START_YEAR, crop, 0);
    country.setCropNeedPerCapita(START_YEAR, crop, worldPerCap);
  }
  
  /**
   * Assigns values to country's fields for organic, conventional, and GMO percentages when
   * one or more of them is missing or invalid, using world median values.
   * @param country     Country with missing or invalid data
   */
  public static void fixGrowMethods(Country country)
  {  
      //assign world median values to all methods      
      country.setMethodPercentage(START_YEAR, EnumGrowMethod.ORGANIC, WORLD_ORGANIC);
      country.setMethodPercentage(START_YEAR, EnumGrowMethod.CONVENTIONAL, WORLD_CONVENTIONAL);
      country.setMethodPercentage(START_YEAR, EnumGrowMethod.GMO, WORLD_GMO);
  }
  
  private static void fixGrowMethodSingleError(Country country, Map<String,String> recordMap)
  {
    double totalMethods = 0;
    EnumGrowMethod wrongMethod = null;
    for (EnumGrowMethod method : EnumGrowMethod.values()) 
    {
      try
      {

        String methodString = method.toString().toLowerCase();
        double value = Double.parseDouble(recordMap.get(methodString));

        if (value >= 0 && value <= 1)
        {
          totalMethods += value;
        }
        else wrongMethod = method;
      }
      catch (NumberFormatException e)
      {
        // set default value
        wrongMethod = method;  // if value is not a number, is error
        //country.setMethodPercentage(START_YEAR, method, Double.parseDouble(value));
      }
    }
    double correctValue = 1 - totalMethods;
    country.setMethodPercentage(START_YEAR, wrongMethod, correctValue);
  }
}
