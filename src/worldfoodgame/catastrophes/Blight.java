package worldfoodgame.catastrophes;

import java.util.Collection;
import javax.swing.JOptionPane;
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


  public Blight(Collection<Continent> continents)
  {
    continent = getRandContinent(continents);
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
  // Performs catastrophe changes i.e. by changing crop values,etc.
  protected void initCatastrophe()
  {
    // TODO Auto-generated method stub

  }  

  
  @Override
  //Creates story for dialog pop-up
  protected void setStory()
  {
    // TODO Auto-generated method stub    
  }


  @Override
  // Recovers continent to a less harsh post-catastrophe state 
  public void recuperateAfterCatastrophe()
  {
    // TODO Auto-generated method stub
    
  }
}