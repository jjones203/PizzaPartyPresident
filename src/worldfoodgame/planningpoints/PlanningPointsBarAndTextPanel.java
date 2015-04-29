package worldfoodgame.planningpoints;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import worldfoodgame.gui.ColorsAndFonts;

public class PlanningPointsBarAndTextPanel extends JPanel implements DynamicTextInteractable
{
  private InteractableLable decreaseInvestment = new InteractableLable(" - ",this,false,100,Color.WHITE,Color.RED);
  private PlanningPointsInvestmentBar investmentBar = new PlanningPointsInvestmentBar(0);
  private InteractableLable increaseInvestment = new InteractableLable(" + ",this,true,100,Color.WHITE,Color.RED);
  
  private PlanningPointsInteractableRegion region;
  private PlanningPointCategory category;
  
  PlanningPointsBarAndTextPanel(PlanningPointsInteractableRegion region,PlanningPointCategory category)
  {
    this.region=region;
    this.category=category;

    initPanel();
  }
  
  private void initPanel()
  {
    this.setLayout(new GridLayout(1, 3));
    this.setBackground(ColorsAndFonts.GUI_BACKGROUND);
    this.add(decreaseInvestment);
    decreaseInvestment.setHorizontalAlignment(SwingConstants.RIGHT);
    //this.add(investmentBar);
    this.add(increaseInvestment);
    
    
    this.setBackground(ColorsAndFonts.GUI_BACKGROUND);
    //this.add(new JLabel("test"),BorderLayout.PAGE_START);
  }

  @Override
  public void interact(boolean positive)
  {
    if(positive)
    {
      PlanningPointsData.addAdditionalPoints(region, category, 1);
    }
    else
    {
      PlanningPointsData.addAdditionalPoints(region, category, -1);
    }
    
    
  }

}