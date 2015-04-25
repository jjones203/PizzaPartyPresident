package worldfoodgame.planningpoints;

import java.util.Collection;

import worldfoodgame.model.Region;

/**
 * 
 * @author Stephen Stromberg on 4/25/15
 *
 *Represents the main gui interface that the player will
 *have for allocating planning points at the end of each
 *harvest/year.
 */
public class PlanningPointsAllocationPanel
{
  private PlanningPointsInteractableRegion playerRegion;
  private Collection<PlanningPointsInteractableRegion> otherRegions;
  private int playerPlanningPoints;
  
  public PlanningPointsAllocationPanel
  (
      PlanningPointsInteractableRegion playerRegion,
      Collection<PlanningPointsInteractableRegion> otherRegions,
      int playerPlanningPoints
  )
  {
    this.playerRegion = playerRegion;
    this.otherRegions = otherRegions;
    this.playerPlanningPoints = playerPlanningPoints;
  }
  
  
  

}
