package worldfoodgame.gui.hud.infopanel;

import worldfoodgame.common.EnumCropType;
import worldfoodgame.gui.WorldPresenter;
import worldfoodgame.gui.hud.MiniViewBox;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by winston on 3/21/15.
 */
public class InfoPanel extends JPanel implements Observer
{
  public static final Dimension VIEW_BOX_DIM = new Dimension(220, 240);
  public static final Dimension TABP_DIM = new Dimension(620, 240);


  private final WorldPresenter worldPresenter;
  private CountryDataHandler dataHandler;
  private LabelFactory labelFactory;
//  private final static Dimension SIZE = new Dimension(1, 224);
  private TabbedPanel OuterTabbedPanel;
  private DemographicPanel demographicPanel;
  private LandPanel landPanel;

  private MiniViewBox viewBox;

  private TabbedPanel innerTabbedPanel;
  private HashMap<EnumCropType, CropPanel> cropPanels;

  public InfoPanel(WorldPresenter worldPresenter)
  {

    this.dataHandler = CountryDataHandler.getTestData();
    this.worldPresenter = worldPresenter;
    this.labelFactory = new LabelFactory(dataHandler);
//    worldPresenter.addObserver(this); // disabled for testing only.


    setLayout(new BorderLayout());
    viewBox = new MiniViewBox("MAP view:");
    viewBox.setPreferredSize(VIEW_BOX_DIM);
    add(viewBox, BorderLayout.WEST);

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
  }

  @Override
  public void update(Observable o, Object arg)
  {
    // this would change to convets an active list of countries
    // taken from the wordpresenter into a country data object
    // todo find a way to issure read and write commands from the GUI....
    dataHandler = CountryDataHandler.getData(
      worldPresenter.getActiveCountries(),
      worldPresenter.getYear());

//    dataHandler = CountryDataHandler.getTestData();
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


  public static void main(String[] args)
  {
    final JFrame frame = new JFrame();
    final InfoPanel infoPanel = new InfoPanel(null);
    frame.add(infoPanel);

    frame.pack();
    frame.setVisible(true);
    frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);


    new Timer(10, new AbstractAction()
    {
      @Override
      public void actionPerformed(ActionEvent e)
      {
        frame.repaint();
      }
    }).start();


    new Timer(4000, new AbstractAction()
    {
      @Override
      public void actionPerformed(ActionEvent e)
      {
        infoPanel.update(null, null);
        System.out.println("just updated!");
      }
    }).start();

  }

}
