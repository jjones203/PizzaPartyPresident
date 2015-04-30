package worldfoodgame.planningpoints;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author Stephen Stromberg on 4/29/15
 * 
 * This class is the back end of the allocation
 * panel. All GUI components access and update
 * via these static members. 
 *
 */
public class PlanningPointsData
{
  private static Map<PlanningPointsInteractableRegion, Integer> 
  initialTradeEffPoints = 
  new HashMap<PlanningPointsInteractableRegion, Integer>();
  private static Map<PlanningPointsInteractableRegion, Integer> 
  initialYieldEffPoints = 
  new HashMap<PlanningPointsInteractableRegion, Integer>();
  private static Map<PlanningPointsInteractableRegion, Integer> 
  initialWaterEffPoints = 
  new HashMap<PlanningPointsInteractableRegion, Integer>();
  private static Map<PlanningPointsInteractableRegion, Integer> 
  initialGMOResistancePoints = 
  new HashMap<PlanningPointsInteractableRegion, Integer>();
  
  private static Map<PlanningPointsInteractableRegion, Integer> 
  additionalTradeEffPoints = 
  new HashMap<PlanningPointsInteractableRegion, Integer>();
  private static Map<PlanningPointsInteractableRegion, Integer> 
  additionalYieldEffPoints = 
  new HashMap<PlanningPointsInteractableRegion, Integer>();
  private static Map<PlanningPointsInteractableRegion, Integer> 
  additionalWaterEffPoints = 
  new HashMap<PlanningPointsInteractableRegion, Integer>();
  private static Map<PlanningPointsInteractableRegion, Integer> 
  additionalGMOResistancePoints = 
  new HashMap<PlanningPointsInteractableRegion, Integer>();
  
  private static List <PlanningPointsInteractableRegion> 
  allRegions = new ArrayList<PlanningPointsInteractableRegion>();
  
  private static int pointsToInvest;
  private static PlanningPointsInteractableRegion activeRegion;
  private static boolean isRunning=false;
  
  /**
   * This is to be called once creating the allocation panel
   * @param regions starting with the player's area
   * @param yearlyPlanningPoints the number of points allotted to the player
   * at the beginning of the year
   */
  public static void initData(List<PlanningPointsInteractableRegion> regions,
      int yearlyPlanningPoints)
  {
    allRegions.clear();
    initialTradeEffPoints.clear();    
    initialYieldEffPoints.clear();
    initialWaterEffPoints.clear();
    initialGMOResistancePoints.clear();
    
    additionalTradeEffPoints.clear();    
    additionalYieldEffPoints.clear();
    additionalWaterEffPoints.clear();
    additionalGMOResistancePoints.clear();
    
    pointsToInvest=yearlyPlanningPoints;
    allRegions=regions;
    
    setActiveRegion(regions.get(0));
    
    for (PlanningPointsInteractableRegion r:regions)
    {
      initialTradeEffPoints.put(r, r.getTradeEfficiencyPlanningPoints());
      initialYieldEffPoints.put(r, r.getYieldEfficiencyPlanningPoints());
      initialWaterEffPoints.put(r, r.getWaterEfficiencyPlanningPoints());
      initialGMOResistancePoints.put(r, r.getGMOResistancePlanningPoints());
      
      additionalTradeEffPoints.put(r, 0);
      additionalYieldEffPoints.put(r, 0);
      additionalWaterEffPoints.put(r, 0);
      additionalGMOResistancePoints.put(r, 0);
    }
    isRunning=true;
  }
  
  /**
   * 
   * @param region to invest in
   * @param category to invest in
   * @param pointsToAdd number of points to invest
   */
  public static void addAdditionalPoints(
      PlanningPointsInteractableRegion region,
      PlanningPointCategory category, int pointsToAdd)
  {
    //planning points quantity check
    int pointsCheck=pointsToInvest-pointsToAdd;
    if(pointsCheck<0||pointsCheck>PlanningPointConstants.MAX_POINTS_PER_YEAR)
    {
      return;  
    }
    
    //planning points invested in specific category check
    int categoryCheck=getTempIvestment(region,category)+pointsToAdd;
    if(categoryCheck<0 || categoryCheck+getOriginalIvestment(region,category)
        >PlanningPointConstants.MAX_POINTS)
    {
      return;
    }
    
    pointsToInvest=pointsCheck;
    switch(category)
    {
    case GMOResistance:
      pointsToAdd+=additionalGMOResistancePoints.get(region);
      additionalGMOResistancePoints.put(region, pointsToAdd);
      break;
    case WaterEfficiency:
      pointsToAdd+=additionalWaterEffPoints.get(region);
      additionalWaterEffPoints.put(region, pointsToAdd);
      break;
    case YieldEffeciency:
      pointsToAdd+=additionalYieldEffPoints.get(region);
      additionalYieldEffPoints.put(region, pointsToAdd);
      break;
    case TradeEfficiency:
      pointsToAdd+=additionalTradeEffPoints.get(region);
      additionalTradeEffPoints.put(region, pointsToAdd);
      break;
    default:
      System.out.println(region.toString()+" not recgnized");
      break;
    }
  }
  
  /**
   * 
   * @param region to the original investment from
   * @param category to get original investment from
   * @return the integer represnting how many points 
   * were invested into this category and country
   * at the start of this round
   */
  public static int getOriginalIvestment(
      PlanningPointsInteractableRegion region, PlanningPointCategory category)
  {
    int originalInvestment=0;
    switch(category)
    {
    case GMOResistance:
      originalInvestment=initialGMOResistancePoints.get(region);
      break;
    case WaterEfficiency:
      originalInvestment=initialWaterEffPoints.get(region);
      break;
    case YieldEffeciency:
      originalInvestment=initialYieldEffPoints.get(region);
      break;
    case TradeEfficiency:
      originalInvestment=initialTradeEffPoints.get(region);
      break;
    default:
      System.out.println(region.toString()+" not recgnized");
      break;
    }
    return originalInvestment;
  }
  
  /**
   * 
   * @param region to the original investment from
   * @param category to get original investment from
   * @return the integer represnting how many points 
   * were invested by the user with the allocation panel
   */
  public static int getTempIvestment(
      PlanningPointsInteractableRegion region,PlanningPointCategory category)
  {
    int tempInvestment=0;
    switch(category)
    {
    case GMOResistance:
      tempInvestment=additionalGMOResistancePoints.get(region);
      break;
    case WaterEfficiency:
      tempInvestment=additionalWaterEffPoints.get(region);
      break;
    case YieldEffeciency:
      tempInvestment=additionalYieldEffPoints.get(region);
      break;
    case TradeEfficiency:
      tempInvestment=additionalTradeEffPoints.get(region);
      break;
    default:
      System.out.println(region.toString()+" not recgnized");
      break;
    }
    return tempInvestment;
  }
  
  /**
   * Called by submit button to set the
   * original investment plus the new investment
   * as the new original investment
   */
  public static void submitInvestment()
  {
    int initPoints,additionalPoints;
    for (PlanningPointsInteractableRegion r:allRegions)
    {
      initPoints = initialGMOResistancePoints.get(r);
      additionalPoints = additionalGMOResistancePoints.get(r);
      r.setGMOResistancePlanningPoints(initPoints+additionalPoints);
      
      initPoints = initialWaterEffPoints.get(r);
      additionalPoints = additionalWaterEffPoints.get(r);
      r.setWaterEfficiencyPlanningPoints(initPoints+additionalPoints);
      
      initPoints = initialYieldEffPoints.get(r);
      additionalPoints = additionalYieldEffPoints.get(r);
      r.setYieldEfficiencyPlanningPoints(initPoints+additionalPoints);
      
      
      initPoints = initialTradeEffPoints.get(r);
      additionalPoints = additionalTradeEffPoints.get(r);
      r.setTradeEfficiencyPlanningPoints(initPoints+additionalPoints);     
    }
    
  }
  
  /**
   * 
   * @param category to get tootltip text from
   * @return String of text describing this category
   */
  public static String getToolTipText(PlanningPointCategory category)
  {
    String description = null;
    switch(category)
    {
    case GMOResistance:
      description="Increases the ability to plant more GMO crops.";
      break;
    case WaterEfficiency:
      description="Decreases the amount of water "
          + "required for planting crops.";
      break;
    case YieldEffeciency:
      description="Increases yield from all crops";
      break;
    case TradeEfficiency:
      description="Increases priority of "
          + "shipping to an area during optimization";
      break;
    default: //this should never happen
      description="Something Broke. Blame David or Winston";
      break;
    }
    return description;
  }
  
  /**
   * 
   * @return total number of points free for investing
   */
  public static int getPlanningPointsAvalable()
  {
    return pointsToInvest;
  }
  
  /**
   * 
   * @param region that all GUI components
   * associate as the curent region to invest
   * into
   */
  public static void setActiveRegion(PlanningPointsInteractableRegion region)
  {
    activeRegion=region;
  }
  
  /**
   * 
   * @return the region for all GUI components to
   * interact with
   */
  public static PlanningPointsInteractableRegion getActiveRegion()
  {
    return activeRegion;
  }
  
  /**
   * sets the global boolean
   * to false which allows the
   * timers to stop running
   */
  public static void stopRunning()
  {
    isRunning=false;
  }
  /**
   * 
   * @return the global boolean for
   * all GUI timers
   */
  public static boolean getRunning()
  {
    return isRunning;
  }
}
