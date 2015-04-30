package worldfoodgame.planningpoints;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;

import worldfoodgame.gui.ColorsAndFonts;
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
 *communicate to alll GUI components
 */
public class PlanningPointsAllocationPanel extends JPanel
{
  private List<PlanningPointsInteractableRegion> allRegions;
  private final JFrame FRAME = new JFrame("Planning Points Allocation");
  
  /**
   * 
   * @param otherRegions all regions with player's first
   * @param playerPlanningPoints points allotted for player to invest
   */
  public PlanningPointsAllocationPanel
  (
      List<PlanningPointsInteractableRegion> otherRegions,
      int playerPlanningPoints
  )
  {
    this.allRegions = otherRegions;
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
    bgPanel.setPreferredSize(new Dimension(PlanningPointConstants.WINDOW_WIDTH,PlanningPointConstants.WINDOW_HEIGHT));
    bgPanel.setFocusable(true);
    
    PlanningPointsHeaderPanel header = new PlanningPointsHeaderPanel();
    bgPanel.add(header);
    
    PlanningPointContinentSelector selector = new PlanningPointContinentSelector(allRegions);
    bgPanel.add(selector);
    
    for(PlanningPointCategory category: PlanningPointCategory.values())
    {
      PlanningPointsInvestmentPanel investment = new PlanningPointsInvestmentPanel(category);
      bgPanel.add(investment);
    }
    
    PlanningPointsFooterPanel footer = new PlanningPointsFooterPanel(FRAME);
    bgPanel.add(footer);
    
    
    FRAME.setContentPane(bgPanel);
    FRAME.pack();
    FRAME.setVisible(true);
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
  }*/
}

//for testing
/**
 * 
 * @author Stephen
 * for testing
 */
class TestContinent implements PlanningPointsInteractableRegion
{
  private String name;
  private int GMOplanningPoints;
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
  public int getGMOResistancePlanningPoints()
  {
    // TODO Auto-generated method stub
    return GMOplanningPoints;
  }


  @Override
  public int getWaterEfficiencyPlanningPoints()
  {
    // TODO Auto-generated method stub
    return waterEff;
  }


  @Override
  public int getYieldEfficiencyPlanningPoints()
  {
    // TODO Auto-generated method stub
    return yieldEff;
  }


  @Override
  public int getTradeEfficiencyPlanningPoints()
  {
    // TODO Auto-generated method stub
    return tradeEff;
  }


  @Override
  public void setGMOResistancePlanningPoints(int numPoints)
  {
    // TODO Auto-generated method stub
    GMOplanningPoints=numPoints;
  }


  @Override
  public void setWaterEfficiencyPlanningPoints(int numPoints)
  {
    // TODO Auto-generated method stub
    waterEff=numPoints;
  }


  @Override
  public void setYieldEfficiencyPlanningPoints(int numPoints)
  {
    // TODO Auto-generated method stub
    yieldEff=numPoints;
  }


  @Override
  public void setTradeEfficiencyPlanningPoints(int numPoints)
  {

    tradeEff=numPoints;
  }


  @Override
  public PlanningPointsLevel getGMOResistanceLevel()
  {
    //PlanningPointsLevel level = PlanningPointsLevel.pointsToLevel(GMOplanningPoints);
    
    //PlanningPointsLevel.getGMOResistance(level);

    
    return null; //PlanningPointsLevel.pointsToLevel(GMOplanningPoints);
  }


  @Override
  public PlanningPointsLevel getWaterEfficiencyLevel()
  {
    return null;
  }


  @Override
  public PlanningPointsLevel getYieldEfficiencyLevel()
  {

    return null;
  }


  @Override
  public PlanningPointsLevel getTradeEfficiencyLevel()
  {

    return null;
  }


  @Override
  public void setInitialPlanningPoints()
  {
    GMOplanningPoints=(int)(Math.random()*PlanningPointConstants.MAX_POINTS);
    waterEff=(int)(Math.random()*PlanningPointConstants.MAX_POINTS);
    yieldEff=(int)(Math.random()*PlanningPointConstants.MAX_POINTS);
    tradeEff=(int)(Math.random()*PlanningPointConstants.MAX_POINTS);
    // TODO Auto-generated method stub
    
  }

 
}

