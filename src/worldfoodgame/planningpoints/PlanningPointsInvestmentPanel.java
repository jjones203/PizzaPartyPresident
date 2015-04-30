package worldfoodgame.planningpoints;


import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import worldfoodgame.gui.ColorsAndFonts;

/**
 * 
 * @author Stephen Stromberg on 4/29/15
 *
 * Basic panel holding all components
 * necessary for user feedback for
 * investing into a category
 */
public class PlanningPointsInvestmentPanel extends JPanel
{
  private PlanningPointCategory category;
  
  private JPanel startingLevelPanel=new JPanel();
  private PlanningPointsBarAndTextPanel barsAndText;
  private JPanel endingLevelPanel=new JPanel();
  private JLabel description=new JLabel();
  private JLabel startDescrib = new JLabel("Current Level");
  private JLabel endDescrib = new JLabel("Next Level");
  private JLabel startTier = new JLabel();
  private JLabel endTier = new JLabel();
  
  private JPanel containerPanel = new JPanel();
  private JPanel descriptionPanel = new JPanel();
  
  /**
   * 
   * @param category to invest into
   */
  public PlanningPointsInvestmentPanel(
      PlanningPointCategory category)
  {
    this.category = category;
    barsAndText = 
        new PlanningPointsBarAndTextPanel(category,startTier,endTier);
    createPanel();
  }

  private void createPanel()
  { 
    startDescrib.setForeground(Color.white);
    endDescrib.setForeground(Color.white);
    startDescrib.setHorizontalAlignment(SwingConstants.CENTER);
    endDescrib.setHorizontalAlignment(SwingConstants.CENTER);
    
    descriptionPanel.setLayout(new GridLayout(1,3));
    descriptionPanel.add(startDescrib);
    descriptionPanel.add(description);
    descriptionPanel.add(endDescrib);
    descriptionPanel.setBackground(ColorsAndFonts.GUI_BACKGROUND);
    this.setLayout(new GridLayout(2, 1));
    this.setBackground(ColorsAndFonts.GUI_BACKGROUND);
    
    description.setText(category.toString());
    description.setForeground(Color.white);
    description.setHorizontalAlignment(SwingConstants.CENTER);
    description.setToolTipText(PlanningPointsData.getToolTipText(category));
    containerPanel.setLayout(new GridLayout(1, 3));
    startingLevelPanel.add(startTier);
    startingLevelPanel.setBackground(ColorsAndFonts.GUI_BACKGROUND);
    endingLevelPanel.setBackground(ColorsAndFonts.GUI_BACKGROUND);
    endingLevelPanel.add(endTier);
    containerPanel.add(startingLevelPanel);
    containerPanel.add(barsAndText);
    containerPanel.add(endingLevelPanel);
    
    this.setBorder(BorderFactory.createLineBorder(Color.WHITE));
    
    this.add(descriptionPanel);
    this.add(containerPanel);
  }
  
  
  

}
