package worldfoodgame.gui.TradeWindow;

import javax.swing.*;

import worldfoodgame.common.EnumCropType;
import worldfoodgame.gui.hud.infopanel.SingleCountryHandeler;
import worldfoodgame.model.Player;
import worldfoodgame.model.Country;
import java.util.*;
/**
 * Created by Tim on 4/24/15. Needs a english/metric, reset, and continue button.
 * Needs 6 tabs with pictures and gradient colors for either deficient or surplus,
 * so the more green, the higher the surplus.
 */

public class TradeAndImportFrame extends JFrame
{
  private Player player;
  private Collection<Country> countries; //Soon to be continents
  private LinkedList<ContinentState> savedStates;
  private double savedImportBudget;
  private ArrayList<SingleCountryHandeler> handlers;

  public TradeAndImportFrame(Player player, Collection<Country> countries, int year)
  {
    this.player = player;
    this.countries = countries;
    savedImportBudget = player.getImportBudget();
    for (Country c : countries)
    {
      handlers.add(new SingleCountryHandeler(c, year));
    }
    saveInitialStates();
  }

  private void saveInitialStates()
  {
    savedStates = new LinkedList<>();
    for (SingleCountryHandeler cH : handlers)
    {
      double[] tempActual = new double[5];
      tempActual[0] = cH.getProduction(EnumCropType.CORN);
      tempActual[1] = cH.getProduction(EnumCropType.WHEAT);
      tempActual[2] = cH.getProduction(EnumCropType.SOY);
      tempActual[3] = cH.getProduction(EnumCropType.RICE);
      tempActual[4] = cH.getProduction(EnumCropType.OTHER_CROPS);
      savedStates.add(new ContinentState(cH.getName(), tempActual));
    }
    /* This uses getNetCropAvailable. Is this wrong?
    for (Country cH : countries)
    {
      double [] tempActual = new double [5];
      tempActual[0] = cH.getNetCropAvailable(year, EnumCropType.CORN);
      tempActual[1] = cH.getNetCropAvailable(year, EnumCropType.WHEAT);
      tempActual[2] = cH.getNetCropAvailable(year, EnumCropType.SOY);
      tempActual[3] = cH.getNetCropAvailable(year, EnumCropType.RICE);
      tempActual[4] = cH.getNetCropAvailable(year, EnumCropType.OTHER_CROPS);
      savedStates.add(new ContinentState(c.getName(), tempActual));
    }*/
  }

  public void reset()
  {

  }

  private class ContinentState
  {
    private String name;
    private double [] actualCrops;

    public ContinentState(String name, double [] tempActual)
    {
      this.name = name;
      actualCrops = tempActual;
    }
  }
}
