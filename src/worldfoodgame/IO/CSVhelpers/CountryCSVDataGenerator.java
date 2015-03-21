package worldfoodgame.IO.CSVhelpers;

import java.util.EnumMap;
import java.util.Map;

import worldfoodgame.common.AbstractScenario;
import worldfoodgame.common.EnumGrowMethod;
import worldfoodgame.model.Country;

//TODO: comments, demographic & crop methods
public class CountryCSVDataGenerator implements CountryCSVDefaultData
{
  private static final int START_YEAR = AbstractScenario.START_YEAR;
  
  
  
  public static void fixGrowMethods(Country country, Map<String,String> recordMap, int numError)
  {
    if (numError == 1)
    {
      fixGrowMethodSingleError(country, recordMap);
    }
    else
    {
      //assign world median values to all methods      
      country.setMethodPercentage(START_YEAR, EnumGrowMethod.ORGANIC, WORLD_ORGANIC);
      country.setMethodPercentage(START_YEAR, EnumGrowMethod.CONVENTIONAL, WORLD_CONVENTIONAL);
      country.setMethodPercentage(START_YEAR, EnumGrowMethod.GMO, WORLD_GMO);
    }
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
