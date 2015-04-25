package worldfoodgame.gui.TradeWindow;

import javax.swing.*;
import worldfoodgame.model.Player;
import worldfoodgame.model.Country;
import java.util.*;
/**
 * Created by Tim on 4/24/15.
 */

public class TradeAndImportFrame extends JFrame
{
  private Player player;
  private Collection<Country> countries;
  private LinkedList<ContinentState> savedStates;
  private double savedImportBudget;

  public TradeAndImportFrame(Player player, Collection<Country> countries)
  {
    this.player = player;
    this.countries = countries;
    savedImportBudget = player.getImportBudget();
    saveInitialStates();
  }

  private void saveInitialStates()
  {
    double [] tempActual = new double [5];
    double [] tempNeed = new double [5];
    
  }

  public void reset()
  {

  }

  public class ContinentState
  {
    private String name;
    private double [] actualCrops;
    private double [] neededCrops;

    public ContinentState(String name, double [] tempActual, double [] tempNeed)
    {
      this.name = name;
      actualCrops = tempActual;
      neededCrops = tempNeed;
    }
  }
}
