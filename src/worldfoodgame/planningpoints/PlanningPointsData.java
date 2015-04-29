package worldfoodgame.planningpoints;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlanningPointsData
{
  private static Map<PlanningPointsInteractableRegion, Integer> initialTradeEffPoints = new HashMap<PlanningPointsInteractableRegion, Integer>();
  private static Map<PlanningPointsInteractableRegion, Integer> initialYieldEffPoints = new HashMap<PlanningPointsInteractableRegion, Integer>();
  private static Map<PlanningPointsInteractableRegion, Integer> initialWaterEffPoints = new HashMap<PlanningPointsInteractableRegion, Integer>();
  private static Map<PlanningPointsInteractableRegion, Integer> initialGMOResistancePoints = new HashMap<PlanningPointsInteractableRegion, Integer>();
  
  private static Map<PlanningPointsInteractableRegion, Integer> additionalTradeEffPoints = new HashMap<PlanningPointsInteractableRegion, Integer>();
  private static Map<PlanningPointsInteractableRegion, Integer> additionalYieldEffPoints = new HashMap<PlanningPointsInteractableRegion, Integer>();
  private static Map<PlanningPointsInteractableRegion, Integer> additionalWaterEffPoints = new HashMap<PlanningPointsInteractableRegion, Integer>();
  private static Map<PlanningPointsInteractableRegion, Integer> additionalGMOResistancePoints = new HashMap<PlanningPointsInteractableRegion, Integer>();
  
  private static List <PlanningPointsInteractableRegion> allRegions = new ArrayList<PlanningPointsInteractableRegion>();
  private static int pointsToInvest;
  
  public static void initData(List<PlanningPointsInteractableRegion> regions, int yearlyPlanningPoints)
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
  }
  
  public static void addAdditionalPoints(PlanningPointsInteractableRegion region,PlanningPointCategory category, int pointsToAdd)
  {
    //System.out.println(region.toString()+""+category.toString());
    pointsToInvest+=pointsToAdd;
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
  
  public static void getOriginalIvestment(PlanningPointsInteractableRegion region,PlanningPointCategory category)
  {
    switch(category)
    {
    case GMOResistance:
      initialGMOResistancePoints.get(region);
      break;
    case WaterEfficiency:
      initialWaterEffPoints.get(region);
      break;
    case YieldEffeciency:
      initialYieldEffPoints.get(region);
      break;
    case TradeEfficiency:
      initialTradeEffPoints.get(region);
      break;
    default:
      System.out.println(region.toString()+" not recgnized");
      break;
    }
  }
  
  public static void getTempIvestment(PlanningPointsInteractableRegion region,PlanningPointCategory category)
  {
    switch(category)
    {
    case GMOResistance:
      additionalGMOResistancePoints.get(region);
      break;
    case WaterEfficiency:
      additionalWaterEffPoints.get(region);
      break;
    case YieldEffeciency:
      additionalYieldEffPoints.get(region);
      break;
    case TradeEfficiency:
      additionalTradeEffPoints.get(region);
      break;
    default:
      System.out.println(region.toString()+" not recgnized");
      break;
    }
  }
  
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
  
  public static int getPlanningPointsAvalable()
  {
    return pointsToInvest;
  }
  
  
  
}
