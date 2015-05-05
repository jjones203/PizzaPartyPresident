package worldfoodgame.planningpoints;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import worldfoodgame.gui.ColorsAndFonts;
import worldfoodgame.model.Continent;
import worldfoodgame.model.EnumContinentNames;
import worldfoodgame.model.Region;

/**
 * 
 * @author Stephen Stromberg on 4/25/15
 *
 *Represents the main gui interface that the player will
 *have for allocating planning points at the end of each
 *harvest/year. This works closely in conjunction with the
 *PlanningPointsData class which has static members to
 *communicate to all GUI components
 */
public class PlanningPointsAllocationPanel extends JPanel
{
  private List<PlanningPointsInteractableRegion> allRegions;
  private JFrame FRAME;
  
  /**
   * 
   * @param otherRegions all regions with player's first
   * @param playerPlanningPoints points allotted for player to invest
   */
  public PlanningPointsAllocationPanel
  (
      Collection<Continent> otherRegions,
      int playerPlanningPoints,
      JFrame frame
  )
  {
    FRAME = frame;
    this.allRegions = 
        new ArrayList<PlanningPointsInteractableRegion>(otherRegions);
    PlanningPointsData.initData(allRegions, playerPlanningPoints);
    buildGUI();
  }
  
  private void buildGUI()
  {
    FRAME.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    FRAME.setLocation(100, 100);
    FRAME.setResizable(false);
    
    JPanel bgPanel = new JPanel();
    bgPanel.setLayout(new BoxLayout(bgPanel, BoxLayout.Y_AXIS));
    bgPanel.setBackground(ColorsAndFonts.GUI_BACKGROUND);
    bgPanel.setPreferredSize(new Dimension(
        PlanningPointConstants.WINDOW_WIDTH,
        PlanningPointConstants.WINDOW_HEIGHT));
    bgPanel.setFocusable(true);
    
    PlanningPointsHeaderPanel header = new PlanningPointsHeaderPanel();
    bgPanel.add(header);
    
    PlanningPointContinentSelector selector =
        new PlanningPointContinentSelector(allRegions);
    bgPanel.add(selector);
    
    for(PlanningPointCategory category: PlanningPointCategory.values())
    {
      PlanningPointsInvestmentPanel investment = 
          new PlanningPointsInvestmentPanel(category);
      bgPanel.add(investment);
    }
    
    PlanningPointsFooterPanel footer = 
        new PlanningPointsFooterPanel(FRAME, this);
    bgPanel.add(footer);
    
    FRAME.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    FRAME.setContentPane(bgPanel);
    FRAME.pack();
    FRAME.setVisible(true);
  }

  public void closeInvesting()
  {
    FRAME.dispatchEvent(new WindowEvent(FRAME, WindowEvent.WINDOW_CLOSING));
  }
}
  /**
   * 
   * testing
   */
  /*public static void main(String[] args)
  {
    List<PlanningPointsInteractableRegion> otherTestConts = new ArrayList<PlanningPointsInteractableRegion>();
    PlanningPointsInteractableRegion myCont = new TestContinent(EnumContinentNames.N_AMERICA.toString());
    PlanningPointsInteractableRegion c1 = new TestContinent(EnumContinentNames.AFRICA.toString());
    PlanningPointsInteractableRegion c2 = new TestContinent(EnumContinentNames.ASIA.toString());
    PlanningPointsInteractableRegion c3 = new TestContinent(EnumContinentNames.EUROPE.toString());
    PlanningPointsInteractableRegion c4= new TestContinent(EnumContinentNames.MIDDLE_EAST.toString());
    PlanningPointsInteractableRegion c5= new TestContinent(EnumContinentNames.OCEANIA.toString());
    PlanningPointsInteractableRegion c6= new TestContinent(EnumContinentNames.S_AMERICA.toString());
    otherTestConts.add(myCont);
    otherTestConts.add(c1);
    otherTestConts.add(c2);
    otherTestConts.add(c3);
    otherTestConts.add(c4);
    otherTestConts.add(c5);
    otherTestConts.add(c6);
    new PlanningPointsAllocationPanel(otherTestConts,70);
  }
}

//for testing
/**
 * 
 * @author Stephen
 * for testing
 */
/*class TestContinent implements PlanningPointsInteractableRegion
{
  private String name;
  private int GMOPlanningPoints;
  private int waterEff;
  private int yieldEff;
  private int tradeEff;
  TestContinent(String name)
  {
    this.name=name;
    setInitialPlanningPoints();
  }
  

  public String getContName()
  {
    return name;
  }


  @Override
  public void setInitialPlanningPoints()
  {
    GMOPlanningPoints=(int)(Math.random()*PlanningPointConstants.MAX_POINTS);
    waterEff=(int)(Math.random()*PlanningPointConstants.MAX_POINTS);
    yieldEff=(int)(Math.random()*PlanningPointConstants.MAX_POINTS);
    tradeEff=(int)(Math.random()*PlanningPointConstants.MAX_POINTS);
  }


  @Override
  public double getPlanningPointsFactor(PlanningPointCategory category)
  {
    PlanningPointsLevel level=null;
    switch(category)
    {
      case GMOResistance:
        level=PlanningPointsLevel.pointsToLevel(GMOPlanningPoints);
        PlanningPointsLevel.getGMOResistance(level);
      break;
      case WaterEfficiency:
        level=PlanningPointsLevel.pointsToLevel(waterEff);
        PlanningPointsLevel.getWaterEfficiency(level);
      break;
      case YieldEffeciency:
        level=PlanningPointsLevel.pointsToLevel(yieldEff);
        PlanningPointsLevel.getYieldEfficiency(level);
      break;
      case TradeEfficiency:
        level=PlanningPointsLevel.pointsToLevel(tradeEff);
        PlanningPointsLevel.getTradeEfficiency(level);
      break;
      default:
        System.out.println(category.toString()+" not recgnized");
      break;
    }
    
    return 0;
    
  }

  @Override
  public int getPlanningPointsInCategory(PlanningPointCategory category)
  {
    switch(category)
    {
      case GMOResistance: return GMOPlanningPoints;
      case WaterEfficiency: return waterEff;
      case YieldEffeciency: return yieldEff;
      case TradeEfficiency: return tradeEff;
      default:
        System.out.println(category.toString()+" not recgnized");
      break;
    }
    return 0;
  }

  @Override
  public void setPlanningPointsInCategory(PlanningPointCategory category,int numPoints)
  {
    switch(category)
    {
      case GMOResistance:
        GMOPlanningPoints=numPoints;
      break;
      case WaterEfficiency:
        waterEff=numPoints;
      break;
      case YieldEffeciency:
        yieldEff=numPoints;
      break;
      case TradeEfficiency:
        tradeEff=numPoints;
      break;
      default:
        System.out.println(category.toString()+" not recognized");
      break;
    }
  }

 
}*/

