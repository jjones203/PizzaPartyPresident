package worldfoodgame.catastrophes;

import java.util.Collection;
import java.util.Random;

import javax.swing.JOptionPane;

import worldfoodgame.common.EnumCropType;
import worldfoodgame.common.EnumGrowMethod;
import worldfoodgame.model.Continent;


/****************************************
 * Blight catastrophe
 *  Disease wipes out crops and
 *  decreases production of the remaining
 *  
 * created on 04/25/15
 * @author Valarie 
 ****************************************/
public class Blight extends Catastrophe
{
  private Continent continent;
  private String blightStory;
  private int year;


  public Blight(Collection<Continent> continents, int year)
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
    JOptionPane.showMessageDialog(null,blightStory,"A Global Catastrophe has Struck!",JOptionPane.WARNING_MESSAGE);
  }


  @Override
  // Wipes out a crop entirely and weakens others
  protected void initCatastrophe()
  {  
    EnumCropType randomCrop = selectRandomCrop();    
    weakenCrops();   
    wipeOutCrop(randomCrop);    
  }  


  @Override
  //Creates story for dialog pop-up
  protected void setStory()
  {
    // TODO Auto-generated method stub    
  }


  @Override
  public
  // Recovers continent to a less harsh post-catastrophe state 
  void recuperateAfterCatastrophe()
  {
   // Nothing to set
  }

  
  // Decreases yield for all crops
  private void weakenCrops()
  {
    for (EnumCropType crop:EnumCropType.values())
    {
      double convYield = continent.getCropYield(year, crop, EnumGrowMethod.CONVENTIONAL);
      double gmoYield = continent.getCropYield(year, crop, EnumGrowMethod.GMO);
      double orgYield = continent.getCropYield(year, crop, EnumGrowMethod.ORGANIC);

      continent.setCropYield(year, crop, EnumGrowMethod.CONVENTIONAL, convYield/2);
      continent.setCropYield(year, crop, EnumGrowMethod.GMO, gmoYield);
      continent.setCropYield(year, crop, EnumGrowMethod.ORGANIC, orgYield/3);
    }
  }

  
  // Selects a random crop type
  private EnumCropType selectRandomCrop()
  {
    Random ran = new Random();
    int cropIndex = ran.nextInt(EnumCropType.SIZE);
    EnumCropType[] cropArray = EnumCropType.values();

    EnumCropType type = cropArray[cropIndex];
    return type;
  }

  
  // Wipes out crop's yield to zero
  private void wipeOutCrop(EnumCropType randomCrop)
  {
    continent.setCropYield(year, randomCrop, EnumGrowMethod.CONVENTIONAL, 0);
    continent.setCropYield(year, randomCrop, EnumGrowMethod.GMO, 0);
    continent.setCropYield(year, randomCrop, EnumGrowMethod.ORGANIC, 0);
  }
}