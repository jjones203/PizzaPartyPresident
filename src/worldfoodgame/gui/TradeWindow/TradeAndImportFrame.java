package worldfoodgame.gui.TradeWindow;

import javax.swing.*;

import worldfoodgame.common.EnumCropType;
import worldfoodgame.gui.hud.infopanel.GraphLabel;
import worldfoodgame.gui.hud.infopanel.LabelFactory;
import worldfoodgame.gui.hud.infopanel.CountryDataHandler;
import worldfoodgame.gui.hud.infopanel.GroupCountryHandler;
import worldfoodgame.model.*;
import worldfoodgame.planningpoints.PlanningPointCategory;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.awt.event.WindowEvent;

/**
 * Created by Tim on 4/24/15. Contains functionality for both Trading and Donating.
 * It's a super frame!
 */
public class TradeAndImportFrame extends JFrame
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
  private JPanel mainPanel;
  private boolean isTrade = true;

  /**
   * Constructor sets the label factories for the continents and initializes all of the panels,
   * adding tabs for all of the non-player continents.
   * @param player      The player
   * @param continents  All of the game's continents
   * @param year        The current year of the game
   * @param isTrade     Whether the frame should be trading or donating
   */
  public TradeAndImportFrame(Player player, ArrayList<Continent> continents, int year, boolean isTrade)
  {
    this.player = player;
    this.continents = continents;
    this.isTrade = isTrade;
    savedImportBudget = player.getImportBudget();
    continentTabPanel = new ContinentTabPanel(countries, handlers, CONT_DIM, isTrade, this);
    playerPanel = new PlayerPanel(player, PLAYER_DIM, this);
    tradeBar = new TradeBar(TRADE_DIM, this, isTrade);
    buttonPanel = new ButtonPanel(BUTTON_DIM, this, isTrade);
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
        playerPanel.setLabelFactory(temp);
      }
      else
      {
        continentPanels.add(new ContinentPanel(c, temp, this, isTrade));
      }
    }
    saveInitialStates(year - 1);
    mainPanel = new JPanel();
    add(mainPanel);
    if (isTrade)
    {
      setTitle("Trade Window");
    }
    else
    {
      setTitle("Donate Window");
    }
    mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
    mainPanel.add(playerPanel);
    mainPanel.add(tradeBar);
    mainPanel.add(continentTabPanel);
    for (ContinentPanel cP : continentPanels)
    {
      continentTabPanel.addTab(cP.getContinent().getName().toString(), cP);
    }
    mainPanel.add(buttonPanel);
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
  }

  /**
   * Resets trades/donations to before any were made.
   */
  public void reset()
  {
    for (ContinentState cS : savedStates)
    {
      cS.reset();
    }
    for (ContinentPanel cP : continentPanels)
    {
      cP.redraw();
    }
    playerPanel.redraw();
    tradeBar.redraw();
  }

  /**
   * Make a trade between select continent and player's continent.
   * @param continent   The continent to trade with
   * @param contCrop    The continent's crop to trade
   * @param playerCrop  The player's crop to trade
   */
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
    temp = player.getContinent().getCropProduction(year, contCrop) + tradeBar.getCurrentTrade();
    player.getContinent().setCropProduction(year, contCrop,
            player.getContinent().getPlanningPointsFactor(PlanningPointCategory.TradeEfficiency) * temp);
    temp = continent.getCropProduction(year, playerCrop) + tradeBar.getCurrentTrade();
    continent.setCropProduction(year, playerCrop,
            continent.getPlanningPointsFactor(PlanningPointCategory.TradeEfficiency) * temp);
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

  /**
   * Called by playerPanel to set a new label in the trade panel
   * @param lf    Label factory for player's continent
   * @param crop  The crop to trade
   */
  public void newPlayerCrop(LabelFactory lf, EnumCropType crop)
  {
    tradeBar.setPlayerBar(lf, crop);
  }

  /**
   * Called by a continentPanel to set a new label in the trade panel
   * @param lf    Label factory for a continent
   * @param crop  The crop to trade
   */
  public void newContinentCrop(LabelFactory lf, EnumCropType crop)
  {
    tradeBar.setContinentBar(lf, crop);
  }

  /**
   * Sets a new continent for a donation
   * @param continent Continent to donate to
   */
  public void newContinent(Continent continent)
  {
    tradeBar.setContinent(continent);
  }

  /**
   * Donate a crop from the player to another continent.
   * @param continent The continent to donate to
   * @param crop      The crop to donate
   */
  public void donate(Continent continent, EnumCropType crop)
  {
    int year = World.getWorld().getCurrentYear() - 1;
    double temp = 0;
    temp = player.getContinent().getCropProduction(year, crop) - tradeBar.getCurrentTrade();
    if (temp < 0)
    {
      System.out.println("Trying to set player crop prod to less than 0...");
      temp = 0;
    }
    player.getContinent().setCropProduction(year, crop, temp);
    temp = continent.getCropProduction(year, crop) + tradeBar.getCurrentTrade();
    if (temp < 0)
    {
      System.out.println("Trying to set continent crop prod to less than 0...");
      temp = 0;
    }
    continent.setCropProduction(year, crop,
            continent.getPlanningPointsFactor(PlanningPointCategory.TradeEfficiency) * temp);
    for (ContinentPanel cP : continentPanels)
    {
      cP.redraw();
    }
    playerPanel.redraw();
  }

  /**
   * Close the window.
   */
  public void endTrading()
  {
    this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
  }

  /**
   * A class to save the state of each continent before trading/donating
   */
  private class ContinentState
  {
    private Continent continent;
    private double [] actualCrops;

    /**
     * Constructor that saves the continent
     * @param continent   Continent to be saved
     * @param tempActual  Array of crop production
     */
    public ContinentState(Continent continent, double [] tempActual)
    {
      this.continent = continent;
      actualCrops = tempActual;
    }

    /**
     * Resets the continent productions of each crop.
     */
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
