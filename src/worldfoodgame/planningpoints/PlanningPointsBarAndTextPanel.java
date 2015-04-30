package worldfoodgame.planningpoints;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import worldfoodgame.gui.ColorsAndFonts;

/**
 * 
 * @author Stephen Stromberg on 4/29/15
 * The nested Panel which holds the increment,
 * decrement, and bar representation
 * of planning points invested by the
 * current country into a specific category
 *
 */
public class PlanningPointsBarAndTextPanel extends JPanel
                                          implements DynamicTextInteractable
{
  private PlanningPointCategory category;
  
  private JPanel container;
  private JLabel pointsInvested;
  private InteractableLable decreaseInvestment;
  private PlanningPointsInvestmentBar investmentBar;
  private InteractableLable increaseInvestment;
  
  /**
   * 
   * @param category to invest into
   * @param startTier the label which will hold the icon of the start tier
   * @param endTier the label which will hold the icon of the end tier
   */
  PlanningPointsBarAndTextPanel(
      PlanningPointCategory category, JLabel startTier,JLabel endTier)
  {
    super();
    this.category=category;
    container=new JPanel();
    pointsInvested = new JLabel();
    decreaseInvestment = 
        new InteractableLable(" - ",this,false,60,Color.WHITE,Color.RED);
    increaseInvestment = 
        new InteractableLable(" + ",this,true,60,Color.WHITE,Color.RED);
    investmentBar = 
        new PlanningPointsInvestmentBar(
            category,pointsInvested,startTier,endTier);
    investmentBar.setPreferredSize(new Dimension(150,50));
    buildPanel();
  }
  
  private void buildPanel()
  {
    this.setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
    this.setBackground(ColorsAndFonts.GUI_BACKGROUND);
    container.setBackground(ColorsAndFonts.GUI_BACKGROUND);
    pointsInvested.setForeground(Color.WHITE);
    
    container.add(decreaseInvestment);
    container.add(investmentBar);
    container.add(increaseInvestment);
    
    this.add(pointsInvested);
    this.add(container);

  }

  @Override
  /**
   * Defined in interface. If labels are clicked
   * these methods will call the static methods 
   * common among GUI components to change 
   * amounts invested 
   */
  public void interact(boolean positive)
  {
    PlanningPointsInteractableRegion activeRegion = 
        PlanningPointsData.getActiveRegion();
    if(positive)
    {
      PlanningPointsData.addAdditionalPoints(activeRegion, category, 1);
    }
    else
    {
      PlanningPointsData.addAdditionalPoints(activeRegion, category, -1);
    }
  }

}