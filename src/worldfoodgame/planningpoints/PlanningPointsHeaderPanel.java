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
  private JLabel title;
  private PlanningPointBarPanel planningPointsIndicator;
  
  PlanningPointsHeaderPanel()
  {
    this.setLayout(new GridLayout(0, 2));
    this.setBackground(Color.LIGHT_GRAY);
    title=new JLabel("Planning Points Remaining: "+PlanningPointsData.getPlanningPointsAvalable()+" ");
    title.setHorizontalAlignment(SwingConstants.RIGHT);
    planningPointsIndicator = new PlanningPointBarPanel(title);
    addGUIComponents();    
  }
  
  private void addGUIComponents()
  {
    this.add(title);
    this.add(planningPointsIndicator);
  }
}
