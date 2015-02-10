import IO.AreaXMLLoader;
import IO.XMLparsers.KMLParser;
import gui.*;
import gui.displayconverters.EquirectangularConverter;
import gui.displayconverters.MapConverter;
import gui.hud.InfoPanel;
import gui.hud.WorldFeedPanel;
import model.Region;
import model.World;
import IO.AttributeGenerator;

import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;

/**
 *
 * @author david
 *         created: 2015-02-04
 *         <p/>
 *         description:
 */
public class Game
{
  public static final String MODEL_DATA_PATH = "resources/ne_10m_admin_1_states_provinces.kml";
  public static final String BG_DATA_PATH = "resources/countries_world.xml";
  private MapPane mapPane;
  private InfoPanel infoPanel;
  private WorldPresenter worldPresenter;
  private WorldFeedPanel worldFeedPanel;
  private Timer worldTime;
  private final static int DEFAULT_TIME_SPEED = 2000;
  private Timer gameLoop;
  private JFrame frame;

  public Game()
  {
    init();
  }

  private void init()
  {

    Random random = new Random(234);
    AttributeGenerator randoAtts = new AttributeGenerator();

    Collection<Region> background = initBackgroundRegions(random, randoAtts);
    Collection<Region> modelRegions = initModelRegions(random, randoAtts);

    List<Region> allRegions = new ArrayList<>(modelRegions);
    allRegions.addAll(background);
    World world = new World(allRegions);

    MapConverter converter = new EquirectangularConverter();

    worldPresenter = new WorldPresenter(converter, world);
    worldPresenter.setBackgroundRegions(background);
    worldPresenter.setModelRegions(modelRegions);

    Camera cam = new Camera(converter);
    mapPane = new MapPane(cam, worldPresenter);

    infoPanel = new InfoPanel();
    infoPanel.setPresenter(worldPresenter);

    worldFeedPanel = new WorldFeedPanel(worldPresenter);
    worldPresenter.addObserver(worldFeedPanel);

    initFrame();
    setupControlls();
  }

  public void show()
  {
    frame.setVisible(true);
  }

  public void start()
  {
    gameLoop.start();
    worldTime.start();
  }

  public void pause()
  {
    gameLoop.stop();
    worldTime.stop();
  }


  public boolean isRunning()
  {
    return gameLoop.isRunning();
  }

  private void setupControlls()
  {
    worldTime = new Timer(DEFAULT_TIME_SPEED, new AbstractAction()
    {
      @Override
      public void actionPerformed(ActionEvent e)
      {
        worldPresenter.setWorldForward(1);
        worldFeedPanel.setDate(worldPresenter.getWorldDate());
      }
    });

    gameLoop = new Timer(20, new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent e)
      {
        mapPane.repaint();   // for graphics
        mapPane.update();    // for controls
        infoPanel.repaint(); // again for graphics.
      }
    });


    InputMap inputMap = mapPane.getInputMap();
    ActionMap actionMap = mapPane.getActionMap();

    inputMap.put(KeyStroke.getKeyStroke("8"), "defaultTime");
    actionMap.put("defaultTime", new AbstractAction()
    {
      @Override
      public void actionPerformed(ActionEvent e)
      {
        worldTime.setDelay(DEFAULT_TIME_SPEED);
      }
    });

    inputMap.put(KeyStroke.getKeyStroke("9"), "faster");
    actionMap.put("faster", new AbstractAction()
    {
      @Override
      public void actionPerformed(ActionEvent e)
      {
        worldTime.setDelay(DEFAULT_TIME_SPEED / 2);
      }
    });

    inputMap.put(KeyStroke.getKeyStroke("0"), "superfast");
    actionMap.put("superfast", new AbstractAction()
    {
      @Override
      public void actionPerformed(ActionEvent e)
      {
        worldTime.setDelay(DEFAULT_TIME_SPEED / 4);
      }
    });

    inputMap.put(KeyStroke.getKeyStroke("SPACE"), "pause");
    actionMap.put("pause", new AbstractAction()
    {
      @Override
      public void actionPerformed(ActionEvent e)
      {
        if (isRunning()) pause();
        else start();
      }
    });


    inputMap.put(KeyStroke.getKeyStroke("N"), "setName");
    actionMap.put("setName", new AbstractAction()
    {
      @Override
      public void actionPerformed(ActionEvent e)
      {
//        mapPane.setDrawRegionsNames(! mapPane.getDrawRegionsNames());
      }
    });
  }

  private void initFrame()
  {
    frame = new JFrame();
    frame.setLayout(new BorderLayout());
    frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    frame.add(worldFeedPanel, BorderLayout.NORTH);
    frame.add(mapPane, BorderLayout.CENTER);
    frame.add(infoPanel, BorderLayout.SOUTH);
    frame.addKeyListener(mapPane);
    frame.pack();
    frame.setResizable(false);
  }

  private Collection<Region> initModelRegions(Random random,
                                              AttributeGenerator randoAtts)
  {
    Collection<Region> modelMap = KMLParser.getRegionsFromFile(MODEL_DATA_PATH);
    modelMap.addAll(new AreaXMLLoader().getRegions()); // adds XML regions for area folder...
    for (Region r : modelMap)
    {
      randoAtts.setRegionAttributes(r, random);
    }
    return modelMap;
  }

  private Collection<Region> initBackgroundRegions(Random random,
                                                   AttributeGenerator randoAtts)
  {
    Collection<Region> BGRegions = KMLParser.getRegionsFromFile(BG_DATA_PATH);
    for (Region r : BGRegions)
    {
      randoAtts.setRegionAttributes(r, random);
    }
    return BGRegions;
  }

  public static void main(String[] args)
  {
    Game gameManager = new Game();
    gameManager.show();
    gameManager.start();
  }
}
