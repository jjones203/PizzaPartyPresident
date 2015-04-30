package worldfoodgame.planningpoints;

import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

/**
 * 
 * @author Stephen Stromberg 4/29/15
 * 
 * Header panel of allocation panel 
 * that holds the planning
 * points indicator and label
 *
 */
public class PlanningPointsHeaderPanel extends JPanel
{
  private JLabel title;
  private PlanningPointBarPanel planningPointsIndicator;
  
  PlanningPointsHeaderPanel()
  {
    this.setLayout(new GridLayout(0, 2));
    this.setBackground(Color.LIGHT_GRAY);
    this.setBorder(new EmptyBorder(10, 10, 10, 10) );
    title=new JLabel();
    title.setHorizontalAlignment(SwingConstants.RIGHT);
    planningPointsIndicator = new PlanningPointBarPanel(title);
    this.add(title);
    this.add(planningPointsIndicator);
  }
}
