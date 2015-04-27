package worldfoodgame.catastrophes;


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
  public abstract void popUpDialog();
  
  
  // Performs catastrophe changes i.e. by changing crop values,etc.
  public abstract void initCatastrophe();  
  
}
