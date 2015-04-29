package worldfoodgame.planningpoints;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import worldfoodgame.gui.ColorsAndFonts;

public class PlanningPointsInvestmentPanel extends JPanel
{
  private PlanningPointsInteractableRegion region;
  private PlanningPointCategory category;
  private JPanel startingLevelPanel=new JPanel();
  private PlanningPointsBarAndTextPanel barsAndText;
  private JPanel endingLevelPanel=new JPanel();
  private JLabel description=new JLabel();
  
  private JPanel containerPanel = new JPanel();
  
  
  public PlanningPointsInvestmentPanel(
      PlanningPointsInteractableRegion region,
      PlanningPointCategory category)
  {
    this.region = region;
    this.category = category;
    barsAndText = new PlanningPointsBarAndTextPanel(region,category);
    createPanel();
  }

  private void createPanel()
  {
    this.setLayout(new GridLayout(2, 1));
    this.setBackground(ColorsAndFonts.GUI_BACKGROUND);
    description.setText(category.toString());
    description.setForeground(Color.white);
    description.setHorizontalAlignment(SwingConstants.CENTER);
    containerPanel.setLayout(new GridLayout(1, 3));
    containerPanel.add(startingLevelPanel);
    containerPanel.add(barsAndText);
    containerPanel.add(endingLevelPanel);
    this.add(description);
    this.add(containerPanel);
  }
  
  
  

}
