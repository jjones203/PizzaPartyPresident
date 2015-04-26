package worldfoodgame.planningpoints;

/**
 * 
 * @author Stephen Stromberg on 4/25/12
 * 
 * Represents a region that has all methods 
 * necessary for the MS3 implementation
 * of planning points. These methods are
 * to be called from an instance of the
 * PlanningPointsAllocationPanel.
 *
 */
public interface PlanningPointsInteractableRegion
{
  
  // used by planning points allocation panel
  public int getGMOResistancePlanningPoints();
  public int getWaterEfficiencyPlanningPoints();
  public int getYieldEfficiencyPlanningPoints();
  public int getTradeEfficiencyPlanningPoints();
  
  // used by planning points allocation panel
  // should be bounded by 0 and PlanningPointsConstants.MAX_POINTS
  public void addGMOResistancePlanningPoints(int numPoints);
  public void addWaterEfficiencyPlanningPoints(int numPoints);
  public void addYieldEfficiencyPlanningPoints(int numPoints);
  public void addTradeEfficiencyPlanningPoints(int numPoints);
  
  /**
   * The following should be straightforward to implement with my
   * helper methods within the PlanningPointsLevel enum class.
   * 
   * Just call the static method pointsToLevel with the number of
   * points within each field. 
   * 
   * These getter's will ultimately be used by anyone dealing with the model
   * equations, all they need to do is call any of the static helper methods
   * found in PlanningPointsLevel with the returned level to get a 
   * quantifiable factor to implement within an equation.
   * 
   * @return
   */
  public PlanningPointsLevel getGMOResistanceLevel();
  public PlanningPointsLevel getWaterEfficiencyLevel();
  public PlanningPointsLevel getYieldEfficiencyLevel();
  public PlanningPointsLevel getTradeEfficiencyLevel();
  
}
