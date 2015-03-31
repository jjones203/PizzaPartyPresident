import worldfoodgame.IO.CountryCSVLoader;
import worldfoodgame.IO.CropZoneDataIO;
import worldfoodgame.IO.XMLparsers.CountryXMLparser;
import worldfoodgame.gui.Camera;
import worldfoodgame.gui.MapPane;
import worldfoodgame.gui.WorldPresenter;
import worldfoodgame.gui.displayconverters.EquirectangularConverter;
import worldfoodgame.gui.displayconverters.MapConverter;
import worldfoodgame.gui.hud.WorldFeedPanel;
import worldfoodgame.gui.hud.infopanel.InfoPanel;
import worldfoodgame.model.Country;
import worldfoodgame.model.CropZoneData;
import worldfoodgame.model.Region;
import worldfoodgame.model.World;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;
import java.util.Collection;

/**
 * Main entry point for the 'game'. Handles loading data and all configurations.
 * @author david winston
 *         created: 2015-02-04
 *         <p/>
 *         description:
 */
public class Game
{
  private static boolean DEBUG = true;

  public static final String MODEL_DATA_PATH = "resources/ne_10m_admin_1_states_provinces.kml";
  public static final String BG_DATA_PATH = "resources/ne_110m_land.kml";
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
   * set it ALL up.
   */
  private void init()
  {
//    Collection<Region> background = KMLParser.getRegionsFromFile(BG_DATA_PATH);
    Collection<Region> modelRegions = new CountryXMLparser().getRegionList();

    Collection<Country> noDataCountries = CountryXMLparser.RegionsToCountries(modelRegions);

    CropZoneData cropZoneData = CropZoneDataIO.parseFile(CropZoneDataIO.DEFAULT_FILE, noDataCountries);

    // add data from csv to noDataCountries
    CountryCSVLoader csvLoader = new CountryCSVLoader(noDataCountries);
    noDataCountries = csvLoader.getCountriesFromCSV();


    Calendar startingDate = Calendar.getInstance();
    startingDate.set(Calendar.YEAR,  2014);

    World.makeWorld(modelRegions, noDataCountries, cropZoneData.allTiles(), startingDate);

    World world = World.getWorld();
    MapConverter converter = new EquirectangularConverter();

    worldPresenter = new WorldPresenter(converter, world);
//    worldPresenter.setBackgroundRegions(background);



    Camera cam = new Camera(converter);
    mapPane = new MapPane(cam, worldPresenter);
    mapPane.setGrid(converter.getLatLonGrid());

    infoPanel = new InfoPanel(worldPresenter);


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
    frame.add(mapPane, BorderLayout.CENTER);
    frame.add(infoPanel, BorderLayout.SOUTH);
    frame.addKeyListener(mapPane);
    frame.pack();
    frame.setResizable(false);
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