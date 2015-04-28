package worldfoodgame.catastrophes;

import java.util.Collection;
import java.util.Random;

import worldfoodgame.model.Continent;


/******************************************************
 * Generic class whose extensions represent random
 * calamities that can befall the world an affect 
 * crop production and ratings
 * 
 * created on 04/25/15
 * @author Valarie
 **************************************************/
public abstract class Catastrophe
{
  // Displays window that explains to user what catastrophe occurred
  protected abstract void popUpDialog();


  // Performs catastrophe changes i.e. by changing crop values,etc.
  protected abstract void initCatastrophe();  


  // Creates story for dialog pop-up
  protected abstract void setStory();


  // Recovers continent to a less harsh post-catastrophe state 
  public abstract void recuperateAfterCatastrophe();
  
  
  // Chooses random continent for catastrophe to occur
  protected Continent getRandContinent(Collection<Continent> continents)
  {    
    // referenced http://stackoverflow.com/questions/21092086/get-random-element-from-collection
    Random ran = new Random();
    int i = ran.nextInt(continents.size());
    Continent c = (Continent) continents.toArray()[i];  

    return c;
  }

}
