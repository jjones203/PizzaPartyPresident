package worldfoodgame.gui.TradeWindow;

import javax.swing.*;

import worldfoodgame.common.EnumCropType;
import worldfoodgame.gui.hud.infopanel.GraphLabel;
import worldfoodgame.gui.hud.infopanel.LabelFactory;
import worldfoodgame.gui.hud.infopanel.CountryDataHandler;
import worldfoodgame.gui.hud.infopanel.GroupCountryHandler;
import worldfoodgame.model.EnumContinentNames;
import worldfoodgame.model.Player;
import worldfoodgame.model.Country;
import worldfoodgame.model.Continent;

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
  private Collection<Continent> continents;
  private LinkedList<ContinentState> savedStates;
  private double savedImportBudget;
  private ArrayList<CountryDataHandler> handlers = new ArrayList<>();
  private ArrayList<LabelFactory> labelFactories = new ArrayList<>();
  private ArrayList<ContinentPanel> continentPanels = new ArrayList<>();
  private ContinentTabPanel continentTabPanel;
  private PlayerPanel playerPanel;
  private TradeBar tradeBar;
  private ButtonPanel buttonPanel;

  public TradeAndImportFrame(Player player, ArrayList<Continent> continents, int year)
  {
    this.player = player;
    this.continents = continents;
    savedImportBudget = player.getImportBudget();
    continentTabPanel = new ContinentTabPanel(countries, handlers, CONT_DIM);
    playerPanel = new PlayerPanel(player, PLAYER_DIM, this);
    tradeBar = new TradeBar(TRADE_DIM, this);
    buttonPanel = new ButtonPanel(BUTTON_DIM, this);
    GroupCountryHandler tempGH;
    LabelFactory temp;
    for (Continent c : continents)
    {
      tempGH = new GroupCountryHandler(c.getCountries());
      temp = new LabelFactory(tempGH);
      handlers.add(tempGH);
      labelFactories.add(temp);
      if (c == player.getContinent())
      {
        //System.out.println("Setting player's country.");
        playerPanel.setLabelFactory(temp);
      }
      else
      {
        continentPanels.add(new ContinentPanel(c, temp, this));
      }
    }
    saveInitialStates(year);
    //setLayout(new GridLayout(3, 0));
    setLayout(new BorderLayout());
    add(continentTabPanel, BorderLayout.WEST);
    for (ContinentPanel cP : continentPanels)
    {
      continentTabPanel.addTab(cP.getContinent().getName().toString(), cP);
    }
    add(tradeBar, BorderLayout.CENTER);
    add(playerPanel, BorderLayout.EAST);
    add(buttonPanel, BorderLayout.SOUTH);
  }

  private void saveInitialStates(int year)
  {
    savedStates = new LinkedList<>();
    for (Continent c : continents)
    {
      double[] tempActual = new double [5];
      tempActual[0] = c.getCropProduction(year, EnumCropType.CORN);
      tempActual[1] = c.getCropProduction(year, EnumCropType.WHEAT);
      tempActual[2] = c.getCropProduction(year, EnumCropType.SOY);
      tempActual[3] = c.getCropProduction(year, EnumCropType.RICE);
      tempActual[4] = c.getCropProduction(year, EnumCropType.OTHER_CROPS);
      savedStates.add(new ContinentState(c.getName(), tempActual));
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
    private EnumContinentNames name;
    private double [] actualCrops;

    public ContinentState(EnumContinentNames name, double [] tempActual)
    {
      this.name = name;
      actualCrops = tempActual;
    }
  }
}
