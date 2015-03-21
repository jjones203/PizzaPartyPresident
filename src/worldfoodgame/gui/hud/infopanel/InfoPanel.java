package worldfoodgame.gui.hud.infopanel;

import worldfoodgame.common.EnumCropType;
import worldfoodgame.gui.WorldPresenter;
import worldfoodgame.gui.hud.MiniViewBox;

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
  private final WorldPresenter worldPresenter;
  private CountryDataHandler dataHandler;
  private final static Dimension SIZE = new Dimension(1, 224);
  private TabbedPanel OuterTabbedPanel;
  private DemographicPanel demographicPanel;
  private LandPanel landPanel;

  private MiniViewBox viewBox;

  private TabbedPanel innerTabbedPanel;
  private HashMap<EnumCropType, CropPanel> cropPanels;

  public InfoPanel(WorldPresenter worldPresenter)
  {
    // todo, add getNullData() as a way to init these components
    this.dataHandler = CountryDataHandler.getTestData();
    this.worldPresenter = worldPresenter;
//    worldPresenter.addObserver(this); // disabled for testing only.

    // init
    LabelFactory labelFactory = new LabelFactory(dataHandler);
    viewBox = new MiniViewBox("");
    add(viewBox);

    OuterTabbedPanel = new TabbedPanel();
    add(OuterTabbedPanel);
    
    demographicPanel = new DemographicPanel(labelFactory);
    OuterTabbedPanel.addTab("demographic", demographicPanel);

    landPanel = new LandPanel(labelFactory);
    OuterTabbedPanel.addTab("land", landPanel);

    innerTabbedPanel = new TabbedPanel();
    cropPanels = new HashMap<>();
    for (EnumCropType type : EnumCropType.values())
    {
      cropPanels.put(type, new CropPanel(labelFactory, type));
      innerTabbedPanel.addTab(type.toString(), cropPanels.get(type));
    }

    OuterTabbedPanel.addTab("crops", innerTabbedPanel);

  }

  @Override
  public void update(Observable o, Object arg)
  {
    // this would change to convets an active list of countries
    // taken from the wordpresenter into a country data object
    // todo find a way to issure read and write commands from the GUI....
    dataHandler = CountryDataHandler.getTestData();

    LabelFactory labelFactory = new LabelFactory(dataHandler);
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


  public static void main(String[] args)
  {
    JFrame frame = new JFrame();
    InfoPanel infoPanel = new InfoPanel(null);
    frame.add(infoPanel);

    frame.pack();
    frame.setVisible(true);


  }

}
