import worldfoodgame.IO.AreaXMLLoader;
import worldfoodgame.IO.AttributeGenerator;
import worldfoodgame.IO.XMLparsers.CountryXMLparser;
import worldfoodgame.IO.XMLparsers.KMLParser;
import worldfoodgame.gui.Camera;
import worldfoodgame.gui.MapPane;
import worldfoodgame.gui.WorldPresenter;
import worldfoodgame.gui.displayconverters.EquirectangularConverter;
import worldfoodgame.gui.displayconverters.MapConverter;
import worldfoodgame.gui.hud.InfoPanel;
import worldfoodgame.gui.hud.WorldFeedPanel;
import worldfoodgame.model.Region;
import worldfoodgame.model.World;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;
import java.util.Random;

/**
 * Main entry point for the 'game'. Handles loading data and all configurations.
 * @author david winston
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

  /**
   * Constructor for game, handles all init logic.
   */
  public Game()
  {
    init();
  }

  /**
   * set it ALL up, I mean all of it.
   */
  private void init()
  {

    Random random = new Random();
    AttributeGenerator randoAtts = new AttributeGenerator();

    Collection<Region> background = initBackgroundRegions(random, randoAtts);
    Collection<Region> modelRegions = initModelRegions(random, randoAtts);


    // trying to tease out interaction between model and background regions
//    List<Region> allRegions = new ArrayList<>(modelRegions);
//    allRegions.addAll(background);
    World world = new World(modelRegions);
    MapConverter converter = new EquirectangularConverter();

    worldPresenter = new WorldPresenter(converter, world);
    worldPresenter.setBackgroundRegions(background);
    worldPresenter.setModelRegions(new CountryXMLparser().getRegionList());

    Camera cam = new Camera(converter);
    mapPane = new MapPane(cam, worldPresenter);
    mapPane.setGrid(converter.getLatLonGrid());

    infoPanel = new InfoPanel();
    infoPanel.setPresenter(worldPresenter);

    worldFeedPanel = new WorldFeedPanel(worldPresenter);
    worldPresenter.addObserver(worldFeedPanel);

    initFrame();
    setupControlls();
  }

  /**
   * sets the main game container to visible.
   */
  public void show()
  {
    frame.setVisible(true);
  }

  /**
   * Starts the game timers.
   */
  public void start()
  {
    gameLoop.start();
    worldTime.start();
  }

  /**
   * pauses the game.
   */
  public void pause()
  {
    gameLoop.stop();
    worldTime.stop();
  }


  /**
   * @return true if the game loop is running.
   */
  public boolean isRunning()
  {
    return gameLoop.isRunning();
  }

  /**
   * init and configures the timers and key bindings for the game.
   */
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
        worldTime.setDelay(DEFAULT_TIME_SPEED / 3);
      }
    });

    inputMap.put(KeyStroke.getKeyStroke("0"), "superfast");
    actionMap.put("superfast", new AbstractAction()
    {
      @Override
      public void actionPerformed(ActionEvent e)
      {
        worldTime.setDelay(DEFAULT_TIME_SPEED / 6);
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

  }

  /**
   * Handles constructing the frame and adding all the worldfoodgame.gui components in
   * their proper places.
   */
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

  /**
   * Loads all the worldfoodgame.model regions and sets their attributes according to the
   * given attribute generator.
   */
  private Collection<Region> initModelRegions(Random random,
                                              AttributeGenerator randoAtts)
  {
    Collection<Region> modelMap = KMLParser.getRegionsFromFile(MODEL_DATA_PATH);

    // adds XML regions for area folder...
    modelMap.addAll(new AreaXMLLoader().getRegions());
    for (Region r : modelMap)
    {
      randoAtts.setRegionAttributes(r, random);
    }
    return modelMap;
  }

  /**
   * Loads and inits attributes for all background regions.
   */
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

  //*******
  // MAIN *
  //*******
  public static void main(String[] args)
  {
    Game gameManager = new Game();
    gameManager.show();
    gameManager.start();
  }
}
