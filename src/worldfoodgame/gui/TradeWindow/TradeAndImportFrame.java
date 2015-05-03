package worldfoodgame.gui.TradeWindow;

import javax.swing.*;

import worldfoodgame.common.EnumCropType;
import worldfoodgame.gui.hud.infopanel.GraphLabel;
import worldfoodgame.gui.hud.infopanel.LabelFactory;
import worldfoodgame.gui.hud.infopanel.CountryDataHandler;
import worldfoodgame.gui.hud.infopanel.GroupCountryHandler;
import worldfoodgame.model.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.awt.event.WindowEvent;

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
      savedStates.add(new ContinentState(c, tempActual));
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
    for (ContinentState cS : savedStates)
    {
      cS.reset();
    }
  }

  public void trade(Continent continent, EnumCropType contCrop, EnumCropType playerCrop)
  {
    int year = World.getWorld().getCurrentYear() - 1;
    double temp = 0;
    temp = player.getContinent().getCropProduction(year, playerCrop) - tradeBar.getCurrentTrade();
    if (temp < 0)
    {
      System.out.println("Trying to set player crop prod to less than 0...");
      temp = 0;
    }
    player.getContinent().setCropProduction(year, playerCrop, temp);
    player.getContinent().setCropProduction(year, contCrop,
            player.getContinent().getCropProduction(year, contCrop) + tradeBar.getCurrentTrade());
    continent.setCropProduction(year, playerCrop,
            continent.getCropProduction(year, playerCrop) + tradeBar.getCurrentTrade());
    temp = continent.getCropProduction(year, contCrop) - tradeBar.getCurrentTrade();
    if (temp < 0)
    {
      System.out.println("Trying to set continent crop prod to less than 0...");
      temp = 0;
    }
    continent.setCropProduction(year, contCrop, temp);
    for (ContinentPanel cP : continentPanels)
    {
      cP.redraw();
    }
    playerPanel.redraw();
  }

  public void newPlayerCrop(LabelFactory lf, EnumCropType crop)
  {
    tradeBar.setPlayerBar(lf, crop);
  }

  public void newContinentCrop(LabelFactory lf, EnumCropType crop)
  {
    tradeBar.setContinentBar(lf, crop);
  }

  public void updateUnits()
  {

  }

  public void endTrading()
  {
    this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
  }

  @Override
  public void actionPerformed(ActionEvent e)
  {

  }

  private class ContinentState
  {
    private Continent continent;
    private double [] actualCrops;

    public ContinentState(Continent continent, double [] tempActual)
    {
      this.continent = continent;
      actualCrops = tempActual;
    }

    public void reset()
    {
      continent.setCropProduction(World.getWorld().getCurrentYear()-1, EnumCropType.CORN, actualCrops[0]);
      continent.setCropProduction(World.getWorld().getCurrentYear()-1, EnumCropType.WHEAT, actualCrops[1]);
      continent.setCropProduction(World.getWorld().getCurrentYear()-1, EnumCropType.SOY, actualCrops[2]);
      continent.setCropProduction(World.getWorld().getCurrentYear()-1, EnumCropType.RICE, actualCrops[3]);
      continent.setCropProduction(World.getWorld().getCurrentYear()-1, EnumCropType.OTHER_CROPS, actualCrops[4]);
    }
  }
}
