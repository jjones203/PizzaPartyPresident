package worldfoodgame.gui.hud.infopanel;

import worldfoodgame.common.EnumCropType;
import worldfoodgame.gui.WorldPresenter;
import worldfoodgame.gui.hud.MiniViewBox;
import worldfoodgame.gui.hud.PieChart;
import worldfoodgame.model.Country;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by winston on 3/21/15.
 */
public class InfoPanel extends JPanel implements Observer
{
  public static final Dimension CHART_DIM = new Dimension(220, 240);
  public static final Dimension TABP_DIM = new Dimension(620, 240);


  private final WorldPresenter worldPresenter;
  private CountryDataHandler dataHandler;
  private LabelFactory labelFactory;
  private TabbedPanel OuterTabbedPanel;
  private DemographicPanel demographicPanel;
  private LandPanel landPanel;

  private MiniViewBox viewBox;
  private PieChart pieChart;

  private TabbedPanel innerTabbedPanel;
  private HashMap<EnumCropType, CropPanel> cropPanels;

  public InfoPanel(WorldPresenter worldPresenter)
  {

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

    add(new ProgressControlPanel(worldPresenter), BorderLayout.NORTH);
  }

  @Override
  public void update(Observable o, Object arg)
  {
    java.util.List<Country> countryList = worldPresenter.getActiveCountries();

    if (countryList.size() == 0)
    {
      dataHandler = CountryDataHandler.getNullData();
      //viewBox.setTitle(" ");
      pieChart.setTitle(" ");
    }
    else
    {
      dataHandler = CountryDataHandler.getData(countryList, worldPresenter.getYear());
      //viewBox.setTitle(dataHandler.getName());
      pieChart.setTitle(dataHandler.getName());
    }

    //viewBox.setDrawableRegions(worldPresenter.getActiveRegions());
    pieChart.setRegions(worldPresenter.getActiveRegions());

    labelFactory = new LabelFactory(dataHandler);

    landPanel.setLabelFactory(labelFactory);
    landPanel.redraw();

    demographicPanel.setLabelFactory(labelFactory);
    demographicPanel.redraw();

    for (EnumCropType type : EnumCropType.values())
    {
      CropPanel panel = cropPanels.get(type);
      panel.setLabelFactory(labelFactory);
      panel.redraw();
    }
  }


}
