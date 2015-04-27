package worldfoodgame.planningpoints;

import java.util.Collection;

import javax.swing.JFrame;
import javax.swing.JPanel;

import worldfoodgame.model.Region;

/**
 * 
 * @author Stephen Stromberg on 4/25/15
 *
 *Represents the main gui interface that the player will
 *have for allocating planning points at the end of each
 *harvest/year.
 */
public class PlanningPointsAllocationPanel extends JPanel
{
  private PlanningPointsInteractableRegion playerRegion;
  private Collection<PlanningPointsInteractableRegion> otherRegions;
  private int playerPlanningPoints;
  private final JFrame FRAME = new JFrame("PlanningPointsAllocation");
  
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
    
    FRAME.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    FRAME.setLocation(200, 200);
    FRAME.setResizable(false);
    FRAME.pack();
    FRAME.setVisible(true);
  }
  
 /* public static void main(String[] args)
  {
    //new PlanningPointsAllocationPanel();
  }*/
}

//for testing

class testContinent implements PlanningPointsInteractableRegion
{
  
  testContinent()
  {
    
  }

  @Override
  public int getGMOResistancePlanningPoints()
  {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public int getWaterEfficiencyPlanningPoints()
  {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public int getYieldEfficiencyPlanningPoints()
  {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public int getTradeEfficiencyPlanningPoints()
  {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public void addGMOResistancePlanningPoints(int numPoints)
  {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void addWaterEfficiencyPlanningPoints(int numPoints)
  {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void addYieldEfficiencyPlanningPoints(int numPoints)
  {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void addTradeEfficiencyPlanningPoints(int numPoints)
  {
    // TODO Auto-generated method stub
    
  }

  @Override
  public PlanningPointsLevel getGMOResistanceLevel()
  {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public PlanningPointsLevel getWaterEfficiencyLevel()
  {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public PlanningPointsLevel getYieldEfficiencyLevel()
  {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public PlanningPointsLevel getTradeEfficiencyLevel()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
}

