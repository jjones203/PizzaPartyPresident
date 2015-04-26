package worldfoodgame.planningpoints;

/**
 * 
 * @author Stephen Stromberg on 4/25/12
 * 
 * Represents a region that has all methods 
 * necessary for the MS3 implementation
 * of planning points. Thee non getter methods are
 * to be called from an instance of the
 * PlanningPointsAllocationPanel. 
 * 
 * The continent class that implements this 
 * should have integers representing
 * the number of planning points invested into 
 * each category. It will not necessarily
 * have to hold the ENUM types as the ENUM
 * class has a conversion from points to
 * ENUM that can be used in the getters.
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
  // should be bounded by [0 and PlanningPointsConstants.MAX_POINTS]
  public void addGMOResistancePlanningPoints(int numPoints);
  public void addWaterEfficiencyPlanningPoints(int numPoints);
  public void addYieldEfficiencyPlanningPoints(int numPoints);
  public void addTradeEfficiencyPlanningPoints(int numPoints);
  
  /**
   * The following should be straightforward to implement with my
   * helper methods within the PlanningPointsLevel enum class.
   * 
   * Just call the static method pointsToLevel with the number of
   * points within each field, this will tell you which tier you are
   * on for a given topic. Then use this tier in the getter 
   * methods defined below to get the actual numerical representation
   * of these topics for implementing with a model.
   * 
   * These getter's will ultimately be used by anyone dealing with the model
   * equations, all they need to do is call any of the static helper methods
   * found in PlanningPointsLevel with the returned level to get a 
   * quantifiable factor to implement within an equation.
   */
  public PlanningPointsLevel getGMOResistanceLevel();
  public PlanningPointsLevel getWaterEfficiencyLevel();
  public PlanningPointsLevel getYieldEfficiencyLevel();
  public PlanningPointsLevel getTradeEfficiencyLevel();
  
}
