package worldfoodgame.catastrophes;


public abstract class Catastrophe
{
  // Displays window that explains to user what catastrophe occurred
  public abstract void popUpDialog();
  
  
  // Performs catastrophe changes i.e. by changing crop values,etc.
  public abstract void initCatastrophe();  
  
}
