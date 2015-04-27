package worldfoodgame.catastrophes;

import javax.swing.JOptionPane;
import worldfoodgame.temp.Continent;


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


  public Blight()
  {
    continent = getRandContinent();
    setStory();
    initCatastrophe();
    popUpDialog();
  }


  @Override
  // Displays window that explains to user what catastrophe occurred
  public void popUpDialog()
  {
    JOptionPane.showMessageDialog(null,blightStory,"A Global Catastrophe has Struck!",JOptionPane.WARNING_MESSAGE);
  }


  @Override
  // Performs catastrophe changes i.e. by changing crop values,etc.
  public void initCatastrophe()
  {
    // TODO Auto-generated method stub

  }

  
  private Continent getRandContinent()
  {
    // TODO Auto-generated method stub
    return new Continent();
  }
  
  private void setStory()
  {
    // TODO Auto-generated method stub    
  }
}