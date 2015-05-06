package worldfoodgame.catastrophes;

import java.util.Collection;

import javax.swing.JOptionPane;

import worldfoodgame.common.EnumCropType;
import worldfoodgame.common.EnumGrowMethod;
import worldfoodgame.model.Continent;


/*********************
 * Flood catastrophe
 *  provides water surplus
 *  and wipes out crops
 *  
 * created on 04/25/15
 * @author Valarie
 ************************/
public class Flood extends Catastrophe
{
  private Continent continent;
  private String floodStory;
  private int year;
  private double waterAllowance;


  public Flood(Collection<Continent> continents, int year)
  {
    continent = getRandContinent(continents);
    this.year = year;
    setStory();
    initCatastrophe();
    popUpDialog();
  }

  @Override
  // Displays window that explains to user what catastrophe occurred
  protected void popUpDialog()
  {
    JOptionPane.showMessageDialog(null,floodStory,"A Global Catastrophe has Struck!",JOptionPane.WARNING_MESSAGE);
  }


  @Override
  // Nearly wipes at all crops, but increases water allowance
  protected void initCatastrophe()
  {
    for (EnumCropType crop:EnumCropType.values())
    {
      double convYield = continent.getCropYield(year, crop, EnumGrowMethod.CONVENTIONAL);
      double gmoYield = continent.getCropYield(year, crop, EnumGrowMethod.GMO);
      double orgYield = continent.getCropYield(year, crop, EnumGrowMethod.ORGANIC);

      continent.setCropYield(year, crop, EnumGrowMethod.CONVENTIONAL, convYield/4*convYield);
      continent.setCropYield(year, crop, EnumGrowMethod.GMO, gmoYield/4*gmoYield);
      continent.setCropYield(year, crop, EnumGrowMethod.ORGANIC, orgYield/4*orgYield);

      //      System.out.println(crop+" has a conventional yield of "+convYield/4*convYield);
      //      System.out.println(crop+" has a GMO yield of "+gmoYield/4*gmoYield);
      //      System.out.println(crop+" has a organic yield of "+ orgYield/4*orgYield);

    }

    waterAllowance = continent.getWaterAllowance(); // currently has no effect since we haven't implemented water-influence on yield yet
    continent.setWaterAllowance(3*waterAllowance); // currently has no effect since we haven't implemented water-influence on yield yet
  }


  @Override
  //Creates story for dialog pop-up
  protected void setStory()
  {
    floodStory = continent.getName()+" expected some sort of warning. A message or ark or something.\n"+
        "Instead they got clouds and relentless rain. It was less than 40 days, felt close \n"+
        "to 40 nights. The citizens of "+continent.getName()+" occupied themselves with board \n" +
        " games and warm drinks. When the rainbow finally arrived, the stir-crazy beings were free. But\n" +
        "the toppings were severly damaged. As infrastructure is repaired, citizens turn to \n" +
        "the Pizza Party Presidents of world to see how they will act. What will you do \n"+
        "now that the world is watching?";
  }

  @Override
  // Recovers continent to a less harsh post-catastrophe state 
  public void recuperateAfterCatastrophe()
  {
    // Nothing to set
  }
}
