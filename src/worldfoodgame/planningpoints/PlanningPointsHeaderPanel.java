package worldfoodgame.planningpoints;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class PlanningPointsHeaderPanel extends JPanel
{
  private int numPoints;
  private JLabel title;
  private PlanningPointBarPanel planningPointsIndicator;
  
  PlanningPointsHeaderPanel(int numPoints)
  {
    if(numPoints>PlanningPointConstants.MAX_POINTS_PER_YEAR || numPoints<0) 
    {
      System.out.println("number of poiints problem");
    }
    
    this.numPoints = numPoints;
    this.setLayout(new GridLayout(0, 2));
    this.setBackground(Color.LIGHT_GRAY);
    title=new JLabel("Planning Points Remaining: "+numPoints+" ");
    title.setHorizontalAlignment(SwingConstants.RIGHT);
    planningPointsIndicator = new PlanningPointBarPanel();
    addGUIComponents();
    
    
  }
  
  private void addGUIComponents()
  {
    this.add(title);
    this.add(planningPointsIndicator);
    planningPointsIndicator.updateBar(numPoints);
  }
  
  public void addPoints(int pointsToAdd)
  {
    numPoints+=pointsToAdd;
    numPoints=Math.min(PlanningPointConstants.MAX_POINTS_PER_YEAR, numPoints);
    title.setText("Planning Points Remaining: "+numPoints+" ");
    planningPointsIndicator.updateBar(numPoints);
  }
  
  public void subtractPoints(int pointsToSubtract)
  {
    numPoints-=pointsToSubtract;
    numPoints=Math.max(0, numPoints);
    title.setText("Planning Points Remaining: "+numPoints+" ");
    planningPointsIndicator.updateBar(numPoints);
  }
  
  public int getPoints()
  {
    return numPoints;
  }

}
