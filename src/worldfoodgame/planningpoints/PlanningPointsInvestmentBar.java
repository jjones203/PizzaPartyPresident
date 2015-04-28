package worldfoodgame.planningpoints;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;

public class PlanningPointsInvestmentBar extends PlanningPointBarPanel
{
  int initalPoints;
  PlanningPointsInvestmentBar(int initialPoints)
  {
    super();
    this.initalPoints=initialPoints;
  }
  
  /**
   */
  @Override
  public void paintComponent(Graphics g)
  {
    super.paintComponent(g);
    
    
    g.setColor(BACKGROUND_BAR_COLOR);
    g.fillRect(0,this.getBounds().height/2-BAR_HEIGHT/2, BAR_LENGTH, BAR_HEIGHT);
    g.setColor(FOREGROUND_BAR_COLOR);
    g.fillRect(0,this.getBounds().height/2-BAR_HEIGHT/2, BAR_LENGTH-(int) (((double)numPoints/PlanningPointConstants.MAX_POINTS)*BAR_LENGTH), BAR_HEIGHT);
  }
 
  
  
}
