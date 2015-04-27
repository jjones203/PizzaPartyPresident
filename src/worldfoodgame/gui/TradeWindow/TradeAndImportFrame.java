package worldfoodgame.gui.TradeWindow;

import javax.swing.*;

import worldfoodgame.common.EnumCropType;
import worldfoodgame.gui.hud.infopanel.GraphLabel;
import worldfoodgame.gui.hud.infopanel.LabelFactory;
import worldfoodgame.gui.hud.infopanel.SingleCountryHandeler;
import worldfoodgame.model.Player;
import worldfoodgame.model.Country;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

/**
 * Created by Tim on 4/24/15. Needs a english/metric, reset, and continue button.
 * Needs 6 tabs with pictures and gradient colors for either deficient or surplus,
 * so the more green, the higher the surplus.
 */

public class TradeAndImportFrame extends JFrame implements ActionListener
{
  private static final Dimension CONT_DIM = new Dimension(620, 270);
  private static final Dimension PLAYER_DIM = new Dimension(620, 220);
  private static final Dimension TRADE_DIM = new Dimension(620, 150);
  private static final Dimension BUTTON_DIM = new Dimension(620, 100);
  private Player player;
  private Collection<Country> countries; //Soon to be continents
  private LinkedList<ContinentState> savedStates;
  private double savedImportBudget;
  private ArrayList<SingleCountryHandeler> handlers = new ArrayList<>();
  private ArrayList<LabelFactory> labelFactories = new ArrayList<>();
  private ArrayList<ContinentPanel> continentPanels = new ArrayList<>();
  private ContinentTabPanel continentTabPanel;
  private PlayerPanel playerPanel;
  private TradeBar tradeBar;
  private ButtonPanel buttonPanel;

  public TradeAndImportFrame(Player player, Collection<Country> countries, int year)
  {
    this.player = player;
    this.countries = countries;
    savedImportBudget = player.getImportBudget();
    continentTabPanel = new ContinentTabPanel(countries, handlers, CONT_DIM);
    playerPanel = new PlayerPanel(player, PLAYER_DIM, this);
    tradeBar = new TradeBar(TRADE_DIM, this);
    buttonPanel = new ButtonPanel(BUTTON_DIM, this);
    for (Country c : countries)
    {
      SingleCountryHandeler tempSH = new SingleCountryHandeler(c, year);
      LabelFactory temp = new LabelFactory(tempSH);
      handlers.add(tempSH);
      labelFactories.add(temp);
      if (c == player.getCountry())
      {
        //System.out.println("Setting player's country.");
        playerPanel.setLabelFactory(temp);
      }
      else
      {
        continentPanels.add(new ContinentPanel(c, tempSH, temp, this));
      }
    }
    saveInitialStates();
    setLayout(new GridLayout(3, 0));
    add(continentTabPanel);
    for (ContinentPanel cP : continentPanels)
    {
      continentTabPanel.addTab(cP.getCountry().getName(), cP);
    }
    add(tradeBar);
    add(playerPanel);
    add(buttonPanel);
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

  public void trade()
  {
    //redraws
  }

  public void newPlayerCrop(GraphLabel gl)
  {
    tradeBar.setPlayerBar(gl);
  }

  public void newContinentCrop(GraphLabel gl)
  {
    tradeBar.setContinentBar(gl);
  }

  public void updateUnits()
  {

  }

  @Override
  public void actionPerformed(ActionEvent e)
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
