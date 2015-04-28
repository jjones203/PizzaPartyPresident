package worldfoodgame.planningpoints;

import java.awt.GridLayout;

import javax.swing.JPanel;

public class PlanningPointsInvestmentPanel extends JPanel
{
  private PlanningPointsInteractableRegion region;
  private PlanningPointCategory category;
  private int initialAmountInvested;
  private JPanel startingLevelPanel=new JPanel();
  private PlanningPointsBarAndTextPanel barsAndText = new PlanningPointsBarAndTextPanel();
  private JPanel endingLevelPanel=new JPanel();
  public PlanningPointsInvestmentPanel(
      PlanningPointsInteractableRegion region,
      PlanningPointCategory category, int initialAmountInvested)
  {
    this.region = region;
    this.category = category;
    this.initialAmountInvested = initialAmountInvested;
    
    createPanel();
  }

  private void createPanel()
  {
    this.setLayout(new GridLayout(1, 3));
    this.add(startingLevelPanel);
    this.add(barsAndText);
    this.add(endingLevelPanel);
  }
  
  
  

}
