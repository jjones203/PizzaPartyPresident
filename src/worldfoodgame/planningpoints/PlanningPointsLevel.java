package worldfoodgame.planningpoints;

import worldfoodgame.common.EnumCropType;
/**
 * 
 * @author Stephen Stromberg on 4/25/14
 * 
 * Enum's to describe a region's resistance to GMO,
 * Yield Efficiency, Water Efficiency, and Trade efficiency.
 *  The helper methods quantify these enums to a double between 
 *  [0 and 1] to be used as tolerances in model equations.
 *
 */
public enum PlanningPointsLevel
{
  //Tier 1 is lowest
  Tier1, Tier2, Tier3,Tier4,Tier5;
  //tier 5 is highest
  
  
  /**
   * Think of this in terms of how resistant the population
   * is to GMO practices. While this is not necessarily bad, 
   * (which may seem implied from the tier level nomenclature)
   * a lower Tier in GMOResistance just does not allow a country the 
   * choice to plant GMO if he/she wants to.
   * 
   * @param level is a region's current tier for GMOResistance
   * @return a double inclusive [0 1] representing GMoResistance 
   * to implement in a model equation. A resistance of 1 is the
   * most resistant, while 0 is the least
   */
  public static double getGMOResistance(PlanningPointsLevel level)
  {
    double resistance=0;
    switch(level)
    {
      case Tier1: 
        resistance=1;
      break;
      case Tier2: 
        resistance=.8;
      break;
      case Tier3: 
        resistance=.6;
      break;
      case Tier4: 
        resistance=.4;
      break;
      case Tier5: 
        resistance=.2;
      break;
      default: 
        
      break;
    }
    return resistance;
  }
  
  /**
   * Think of this in terms of technology for harvesting. A low 
   * efficiency may represent 
   * 
   * @param level is a region's current tier for YieldEffeciency
   * @return a double inclusive [0 1] representing YieldEffeciency
   * to implement in a model equation. A efficiency of 1 is the
   * most efficient, while 0 is the least.
   */
  public static double getYieldEfficiency(PlanningPointsLevel level)
  {
    double effeciency=.2;
    switch(level)
    {
      case Tier1: 
        effeciency=.2;
      break;
      case Tier2: 
        effeciency=.4;
      break;
      case Tier3: 
        effeciency=.6;
      break;
      case Tier4: 
        effeciency=.8;
      break;
      case Tier5: 
        effeciency=1;
      break;
      default: 
        
      break;
    }
    return effeciency;
  }
  
  /**
   * Think of this in terms of water technology. A country
   * with a low tier in water technology may have very limited
   * irrigation techniques, while one with more efficiency 
   * may have futuristic de-salinzation plants
   * 
   * @param level is a region's current tier for WaterEffeciency
   * @return a double inclusive [0 1] representing WaterEffeciency
   * to implement in a model equation. A efficiency of 1 is the
   * most efficient, while 0 is the least.
   */
  public static double getWaterEfficiency(PlanningPointsLevel level)
  {
    double effeciency=.2;
    switch(level)
    {
      case Tier1: 
        effeciency=.2;
      break;
      case Tier2: 
        effeciency=.4;
      break;
      case Tier3: 
        effeciency=.6;
      break;
      case Tier4: 
        effeciency=.8;
      break;
      case Tier5: 
        effeciency=1;
      break;
      default: 
        
      break;
    }
    return effeciency;
  }
  
   
  /**
   * Think if this in terms of trade technology. A lower tier in trade
   * may represent a country with very bad infrastructure for
   * trading.
   * 
   * @param level is a region's current tier for TradeEffeciency
   * @return a double inclusive [0 1] representing TradeEffeciency
   * to implement in a model equation. A efficiency of 1 is the
   * most efficient, while 0 is the least.
   */
  public static double getTradeEfficiency(PlanningPointsLevel level)
  {
    double effeciency=.2;
    switch(level)
    {
      case Tier1: 
        effeciency=.2;
      break;
      case Tier2: 
        effeciency=.4;
      break;
      case Tier3: 
        effeciency=.6;
      break;
      case Tier4: 
        effeciency=.8;
      break;
      case Tier5: 
        effeciency=1;
      break;
      default: 
        
      break;
    }
    return effeciency;
  }
  
}
