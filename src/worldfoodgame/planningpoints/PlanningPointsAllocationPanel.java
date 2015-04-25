package worldfoodgame.planningpoints;

import java.util.Collection;

import javax.swing.JFrame;

import worldfoodgame.model.Region;

/**
 * 
 * @author Stephen Stromberg on 4/25/15
 *
 *Represents the main gui interface that the player will
 *have for allocating planning points at the end of each
 *harvest/year.
 */
public class PlanningPointsAllocationPanel extends JFrame
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
    
    this.setTitle("Planning Points Allocation");
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.setLocation(200, 200);
    this.setResizable(false);
    //this.setContentPane(mainPanel);
    this.pack();
    this.setVisible(true);
    
  }
  
  
  

}
