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
 *harvest/year.
 */
public class PlanningPointsAllocationPanel extends JPanel
{
  private PlanningPointsInteractableRegion playerRegion;
  private Collection<PlanningPointsInteractableRegion> otherRegions;
  private int playerPlanningPoints;
  private final JFrame FRAME = new JFrame("PlanningPointsAllocation");
  private final int HEIGHT=620;
  private final int WIDTH=620;
  
  public PlanningPointsAllocationPanel
  (
      PlanningPointsInteractableRegion playerRegion,
      Collection<PlanningPointsInteractableRegion> otherRegions,
      int playerPlanningPoints
  )
  {
    this.playerRegion = playerRegion;
    this.otherRegions = otherRegions;
    this.playerPlanningPoints = playerPlanningPoints;
    
    buildGUI();
  }
  
  private void buildGUI()
  {
    FRAME.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    FRAME.setLocation(200, 200);
    FRAME.setResizable(false);
    
    JPanel bgPanel = new JPanel();
    bgPanel.setLayout(new GridLayout(2,0));
    bgPanel.setBackground(ColorsAndFonts.GUI_BACKGROUND);
    bgPanel.setPreferredSize(new Dimension(WIDTH,HEIGHT));
    bgPanel.setFocusable(true);
    
    PlanningPointsHeaderPanel header = new PlanningPointsHeaderPanel(0);
    bgPanel.add(header);
    FRAME.setContentPane(bgPanel);
    FRAME.pack();
    FRAME.setVisible(true);
    
    
    header.addPoints(15);
  }

  /*public static void main(String[] args)
  {
    Collection<PlanningPointsInteractableRegion> otherTestConts = new ArrayList<PlanningPointsInteractableRegion>();
    PlanningPointsInteractableRegion myCont = new TestContinent(EnumContinentNames.N_AMERICA.toString());
    PlanningPointsInteractableRegion c1 = new TestContinent(EnumContinentNames.AFRICA.toString());
    PlanningPointsInteractableRegion c2 = new TestContinent(EnumContinentNames.ASIA.toString());
    PlanningPointsInteractableRegion c3 = new TestContinent(EnumContinentNames.EUROPE.toString());
    PlanningPointsInteractableRegion c4= new TestContinent(EnumContinentNames.MIDDLE_EAST.toString());
    PlanningPointsInteractableRegion c5= new TestContinent(EnumContinentNames.OCEANIA.toString());
    PlanningPointsInteractableRegion c6= new TestContinent(EnumContinentNames.S_AMERICA.toString());
    otherTestConts.add(c1);
    otherTestConts.add(c2);
    otherTestConts.add(c3);
    otherTestConts.add(c4);
    otherTestConts.add(c5);
    otherTestConts.add(c6);
    new PlanningPointsAllocationPanel(myCont,otherTestConts,100);
  }*/
}

//for testing

class TestContinent implements PlanningPointsInteractableRegion
{
  private String name;
  TestContinent(String name)
  {
    this.name=name;
  }
  

  public String getContName()
  {
    return name;
  }

  @Override
  public int getGMOResistancePlanningPoints()
  {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public int getWaterEfficiencyPlanningPoints()
  {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public int getYieldEfficiencyPlanningPoints()
  {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public int getTradeEfficiencyPlanningPoints()
  {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public void addGMOResistancePlanningPoints(int numPoints)
  {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void addWaterEfficiencyPlanningPoints(int numPoints)
  {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void addYieldEfficiencyPlanningPoints(int numPoints)
  {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void addTradeEfficiencyPlanningPoints(int numPoints)
  {
    // TODO Auto-generated method stub
    
  }

  @Override
  public PlanningPointsLevel getGMOResistanceLevel()
  {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public PlanningPointsLevel getWaterEfficiencyLevel()
  {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public PlanningPointsLevel getYieldEfficiencyLevel()
  {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public PlanningPointsLevel getTradeEfficiencyLevel()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
}

