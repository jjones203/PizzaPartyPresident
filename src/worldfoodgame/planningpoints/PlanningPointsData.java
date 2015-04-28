package worldfoodgame.planningpoints;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlanningPointsData
{
  public static Map<PlanningPointsInteractableRegion, Integer> initialTradeEffPoints = new HashMap<PlanningPointsInteractableRegion, Integer>();
  public static Map<PlanningPointsInteractableRegion, Integer> initialYieldEffPoints = new HashMap<PlanningPointsInteractableRegion, Integer>();
  public static Map<PlanningPointsInteractableRegion, Integer> initialWaterEffPoints = new HashMap<PlanningPointsInteractableRegion, Integer>();
  public static Map<PlanningPointsInteractableRegion, Integer> initialGMOResistancePoints = new HashMap<PlanningPointsInteractableRegion, Integer>();
  
  public static Map<PlanningPointsInteractableRegion, Integer> additionalTradeEffPoints = new HashMap<PlanningPointsInteractableRegion, Integer>();
  public static Map<PlanningPointsInteractableRegion, Integer> additionalYieldEffPoints = new HashMap<PlanningPointsInteractableRegion, Integer>();
  public static Map<PlanningPointsInteractableRegion, Integer> additionalWaterEffPoints = new HashMap<PlanningPointsInteractableRegion, Integer>();
  public static Map<PlanningPointsInteractableRegion, Integer> additionalGMOResistancePoints = new HashMap<PlanningPointsInteractableRegion, Integer>();
  
  public static void initData(List<PlanningPointsInteractableRegion> regions)
  {
    initialTradeEffPoints.clear();    
    initialYieldEffPoints.clear();
    initialWaterEffPoints.clear();
    initialGMOResistancePoints.clear();
    
    additionalTradeEffPoints.clear();    
    additionalYieldEffPoints.clear();
    additionalWaterEffPoints.clear();
    additionalGMOResistancePoints.clear();
    
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
  
  
  
}
