package worldfoodgame.planningpoints;

/**
 * 
 * @author Stephen Stromberg on 4/29/15
 * 
 * Describe the different categories
 * that the player can invest planning points into.
 *
 */
public enum PlanningPointCategory
{
  GMOResistance,WaterEfficiency,YieldEffeciency,TradeEfficiency;
  
  
  /**
   * Cause toString is important for
   * da user
   * 
   */
  public String toString()
  {
    String name = name();
    String retString;
    
    switch (name)
    {
      case "GMOResistance":
        retString = "GMO Research";
        break;
      case "WaterEfficiency":
        retString = "Water Technology";
        break;
      case "YieldEffeciency":
        retString = "Crop Yield";
        break;
      case "TradeEfficiency":
        retString = "Trade Infrastructure";
        break;
      default:
        String substring = name.substring(1).toLowerCase();
        retString = name.charAt(0)+substring;
        break;
    }
    return retString;
  }
}
