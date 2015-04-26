package worldfoodgame.catastrophes;

import javax.swing.JOptionPane;

import worldfoodgame.temp.Continent;

public class Drought extends Catastrophe
{
  private Continent continent;
  private String droughtStory;


  public Drought ()
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
    JOptionPane.showMessageDialog(null,droughtStory,"A Global Catastrophe has Struck!",JOptionPane.WARNING_MESSAGE);
  }


  @Override
  // Performs catastrophe changes i.e. by changing crop values,etc.
  public void initCatastrophe()
  {
    // TODO Auto-generated method stub

  }

  private Continent getRandContinent()
  {
    return new Continent();
  }

  private void setStory()
  {
    droughtStory =  "The year started with a beautiful sunny day. This made the citizens of \n"+
                     continent.getName()+" happy. The next day was just as beautiful. As was \n"+
                    "the next day, and the next day, and the one after that. Smiles began to\n"+
                    "diappear as plants shrivel and ordinances are called. "+ continent.getName()+
                    "\n is experiencing a severe drought. As its countries try to preserve water\n"+
                    "and well-being, citizens turn to the Pizza Party Presidents of world to see\n"+
                    "how they will act. What will you do now that the world is watching?";
  }
}

