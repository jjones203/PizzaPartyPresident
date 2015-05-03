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

      continent.setCropYield(year, crop, EnumGrowMethod.CONVENTIONAL, convYield/50);
      continent.setCropYield(year, crop, EnumGrowMethod.GMO, gmoYield/50);
      continent.setCropYield(year, crop, EnumGrowMethod.ORGANIC, orgYield/50);

      //  System.out.println(crop+" has a conventional yield of "+convYield/50);
      //   System.out.println(crop+" has a GMO yield of "+gmoYield/50);
      //  System.out.println(crop+" has a organic yield of "+orgYield/50);
    }

    waterAllowance = continent.getWaterAllowance(); // currently has no effect since we haven't implemented water-influence on yield yet
    continent.setWaterAllowance(3*waterAllowance); // currently has no effect since we haven't implemented water-influence on yield yet
  }


  @Override
  //Creates story for dialog pop-up
  protected void setStory()
  {
    floodStory =  "The year started with a beautiful sunny day. This made the citizens of \n"+
        continent.getName()+" happy. The next day was just as beautiful. As was \n"+
        "the next day, and the next day, and the one after that. Smiles began to\n"+
        "diappear as plants shrivel and ordinances are called. "+ continent.getName()+
        "\n is experiencing a severe drought. As its countries try to preserve water\n"+
        "and well-being, citizens turn to the Pizza Party Presidents of world to see\n"+
        "how they will act. What will you do now that the world is watching?";
  }

  @Override
  // Recovers continent to a less harsh post-catastrophe state 
  public void recuperateAfterCatastrophe()
  {
    // Nothing to set
  }
}
