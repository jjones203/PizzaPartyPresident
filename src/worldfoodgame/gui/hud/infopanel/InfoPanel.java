package worldfoodgame.gui.hud.infopanel;

import worldfoodgame.common.EnumCropType;
import worldfoodgame.gui.WorldPresenter;
import worldfoodgame.gui.hud.MiniViewBox;
import worldfoodgame.gui.hud.PieChart;
import worldfoodgame.model.Country;
import worldfoodgame.model.Continent;
import worldfoodgame.model.Player;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;
import java.util.ArrayList;

/**
 * Created by winston on 3/21/15.
 */
public class InfoPanel extends JPanel implements Observer
{
  public static final Dimension CHART_DIM = new Dimension(220, 240);
  public static final Dimension TABP_DIM = new Dimension(620, 240);


  private WorldPresenter worldPresenter;
  private CountryDataHandler dataHandler;
  private LabelFactory labelFactory;
  private TabbedPanel OuterTabbedPanel;
  private DemographicPanel demographicPanel;
  private TierPanel tierPanel;
  private LandPanel landPanel;
  private Player player;

  //private MiniViewBox viewBox;
  private PieChart pieChart;

  private TabbedPanel innerTabbedPanel;
  private HashMap<EnumCropType, CropPanel> cropPanels;

  public InfoPanel(WorldPresenter worldPresenter, Player player)
  {
    this.player = player;
    this.dataHandler = CountryDataHandler.getNullData();
    this.worldPresenter = worldPresenter;
    this.labelFactory = new LabelFactory(dataHandler);
    worldPresenter.addObserver(this);

    setLayout(new BorderLayout());
    //viewBox = new MiniViewBox(" ");
    //viewBox.setPreferredSize(VIEW_BOX_DIM);
    //add(viewBox, BorderLayout.WEST);

    pieChart = new PieChart(" ");
    pieChart.setPreferredSize(CHART_DIM);
    add(pieChart, BorderLayout.WEST);

    OuterTabbedPanel = new TabbedPanel();
    OuterTabbedPanel.setPreferredSize(TABP_DIM);
    add(OuterTabbedPanel, BorderLayout.CENTER);

    demographicPanel = new DemographicPanel(labelFactory);
    OuterTabbedPanel.addTab("demographic", demographicPanel);

    landPanel = new LandPanel(labelFactory);
    OuterTabbedPanel.addTab("land", landPanel);

    innerTabbedPanel = new TabbedPanel();
    innerTabbedPanel.fontSize = 12f;
    cropPanels = new HashMap<>();
    for (EnumCropType type : EnumCropType.values())
    {
      cropPanels.put(type, new CropPanel(labelFactory, type));
      innerTabbedPanel.addTab(type.toString(), cropPanels.get(type));
    }

    OuterTabbedPanel.addTab("crops", innerTabbedPanel);
    OuterTabbedPanel.addTab("overlays", new OverlayPanel(worldPresenter));
    
    tierPanel = new TierPanel(labelFactory,worldPresenter);
    OuterTabbedPanel.addTab("planning points",tierPanel);

    add(new ProgressControlPanel(worldPresenter, this), BorderLayout.NORTH);
  }

  @Override
  public void update(Observable o, Object arg)
  {
    ArrayList<Continent> continentArrayList = worldPresenter.getActiveCont();
    ArrayList<Country> activeCountries = new ArrayList<>();
    
    
    for (Continent cont : continentArrayList)
    {
      activeCountries.addAll(cont.getCountries());
    }
    if (continentArrayList.size() == 0)
    {
      dataHandler = CountryDataHandler.getNullData();
      //viewBox.setTitle(" ");
      pieChart.setTitle(" ");
    }
    else if (continentArrayList.size() == 1)
    {
      //dataHandler = CountryDataHandler.getData(activeCountries, worldPresenter.getYear());  //try replacing this w/line below
      dataHandler = CountryDataHandler.getData(continentArrayList.get(0), worldPresenter.getYear());
      pieChart.setTitle(continentArrayList.get(0).getName().toString());
    }
    else
    {
      //dataHandler = CountryDataHandler.getData(activeCountries, worldPresenter.getYear());
      //viewBox.setTitle(dataHandler.getName());
      dataHandler = CountryDataHandler.getData(continentArrayList.get(0), worldPresenter.getYear());
      pieChart.setTitle(dataHandler.getName());
    }

    //viewBox.setDrawableRegions(worldPresenter.getActiveRegions());
    pieChart.setRegions(worldPresenter.getActiveRegions());

    labelFactory = new LabelFactory(dataHandler);
    if(continentArrayList.size() == 1)
    {
      //System.out.println("In InfoPanel.update continent is "+continentArrayList.get(0).toString());
      labelFactory.setContinent(continentArrayList.get(0));
      if (continentArrayList.get(0) == player.getContinent())
      {
        landPanel.setHasPlayer(true);
      }
      else
      {
        landPanel.setHasPlayer(false);
      }
    }
    else if (continentArrayList.size() > 1)
    {
      System.out.println("ERROR: Trying to select multiple continents!!");
      labelFactory.setContinent(continentArrayList.get(0));
    }
    landPanel.setLabelFactory(labelFactory);
    landPanel.redraw();

    demographicPanel.setLabelFactory(labelFactory);
    demographicPanel.redraw();
    
    tierPanel.setLabelFactory(labelFactory);
    tierPanel.redraw();

    for (EnumCropType type : EnumCropType.values())
    {
      CropPanel panel = cropPanels.get(type);
      panel.setLabelFactory(labelFactory);
      if(continentArrayList.size() == 1)
      {
        if (continentArrayList.get(0) == player.getContinent())
        {
          panel.setHasPlayer(true);
        }
        else
        {
          panel.setHasPlayer(false);
        }
      }
      panel.redraw();
    }
  }

  public void incrementYear()
  {
    pieChart.incrementYear();
  }
}
