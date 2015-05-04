package worldfoodgame.catastrophes;

import java.util.Collection;

import javax.swing.JOptionPane;

import worldfoodgame.common.EnumCropType;
import worldfoodgame.common.EnumGrowMethod;
import worldfoodgame.model.Continent;


/****************************************
 * Drought catastrophe
 *  decreases stored water due to lack
 *  of rain and wipes out crops
 *  
 *  created on 04/25/15
 * @author Valarie
 ****************************************/
public class Drought extends Catastrophe
{
  private Continent continent;
  private String droughtStory;
  private int year;
  private double waterAllowance;


  public Drought (Collection<Continent> continents, int year)
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
    JOptionPane.showMessageDialog(null,droughtStory,"A Global Catastrophe has Struck!",JOptionPane.WARNING_MESSAGE);
  }


  @Override
  // Decreases water for the year
  protected void initCatastrophe()
  {    
    // Cuts yield to a third, temporary effect until water is implemented
    for (EnumCropType crop:EnumCropType.values())
    {
      double convYield = continent.getCropYield(year, crop, EnumGrowMethod.CONVENTIONAL);
      double gmoYield = continent.getCropYield(year, crop, EnumGrowMethod.GMO);
      double orgYield = continent.getCropYield(year, crop, EnumGrowMethod.ORGANIC);
      
      continent.setCropYield(year, crop, EnumGrowMethod.CONVENTIONAL, convYield/(4*convYield));
      continent.setCropYield(year, crop, EnumGrowMethod.GMO, gmoYield/(3*gmoYield));
      continent.setCropYield(year, crop, EnumGrowMethod.ORGANIC, orgYield/(5*orgYield));
      
      System.out.println(crop+" has a conventional yield of "+convYield/(4*convYield));
      System.out.println(crop+" has a GMO yield of "+gmoYield/(3*gmoYield));
      System.out.println(crop+" has a organic yield of "+ orgYield/(5*orgYield));
    }
    
    waterAllowance = continent.getWaterAllowance(); // currently has no effect since we haven't implemented water-influence on yield yet
    continent.setWaterAllowance(waterAllowance/3); // currently has no effect since we haven't implemented water-influence on yield yet
  }


  @Override
  //Creates story for dialog pop-up
  protected void setStory()
  {
    droughtStory =  "The year started with a beautiful sunny day. This made the citizens of \n"+
        continent.getName()+" happy. The next day was just as beautiful. As was \n"+
        "the next day, and the next day, and the one after that. Smiles began to\n"+
        "diappear as plants shrivel and ordinances are called. "+ continent.getName()+
        "\n is experiencing a severe drought. As its countries try to preserve water\n"+
        "and well-being, citizens turn to the Pizza Party Presidents of world to see\n"+
        "how they will act. What will you do now that the world is watching?";
  }


  @Override
  // Recovers continent to usual water allowance
  public void recuperateAfterCatastrophe()
  {
    continent.setWaterAllowance(waterAllowance); // currently has no effect since we haven't implemented water-influence on yield yet   
  }
}

