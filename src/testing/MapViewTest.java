package testing;

import IO.AreaXMLloader;
import IO.XMLparsers.KMLParser;
import gui.*;
import gui.GUIRegion;
import gui.WorldPresenter;
import model.Region;
import org.xml.sax.SAXException;
import testing.generators.AttributeGenerator;

import javax.swing.*;
import javax.xml.parsers.ParserConfigurationException;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;


/**
 * Created by winston on 1/28/15.
 * Phase_01
 * CS 351 spring 2015
 */
public class MapViewTest extends JPanel
{
  /* boolean controlling the fate of the universe. Modify at your own risk */
  private static final boolean DEBUG = true;

  private Camera cam;
  private WorldPresenter worldPresenter;
  private List<Line2D> grid;

  public static void main(String[] args)
  {
    AttributeGenerator randoAtts = new AttributeGenerator();
    final MapViewTest canvas = new MapViewTest();
    MapConverter mapConverter = new EquirectangularConverter();

    AreaXMLloader areaXMLloader = null;
    try
    {
      areaXMLloader = new AreaXMLloader();
    }
    catch (ParserConfigurationException e)
    {
      e.printStackTrace();
    }
    catch (SAXException e)
    {
      e.printStackTrace();
    }

    Collection<Region> backgroudMap = new ArrayList<>(KMLParser.getRegionsFromFile("resources/countries_world.xml"));
    Collection<Region> modelMap = new ArrayList<>(KMLParser.getRegionsFromFile("resources/ne_10m_admin_1_states_provinces.kml"));
    
    modelMap.addAll(areaXMLloader.getRegions());
    for (Region r : modelMap)
    {
      r.setAttributes(randoAtts.nextAttributeSet());
    }


    WorldPresenter worldPresenter = new WorldPresenter(mapConverter);
    worldPresenter.setBackgroundRegions(backgroudMap);
    worldPresenter.setModelRegions(modelMap);

    Camera camera = new Camera(0, 0, mapConverter);
    CamController keyController = new CamController(camera, worldPresenter);

    canvas.setCam(camera);
    canvas.setWorldPresenter(worldPresenter);
    canvas.setSize(1000, 800);
    canvas.setGrid(mapConverter.getLatLonGrid());
    canvas.setBackground(ColorsAndFonts.OCEANS);

    Timer timer = new Timer(10, keyController);
    timer.addActionListener(new AbstractAction()
    {
      @Override
      public void actionPerformed(ActionEvent e)
      {
        canvas.repaint();
      }
    });

    timer.start();


    JFrame frame = new JFrame();
    frame.setContentPane(canvas);
    frame.addKeyListener(keyController);
    canvas.addMouseListener(keyController);
    canvas.addMouseWheelListener(keyController);
    frame.pack();

    frame.setSize(1000, 800);
    frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    frame.setVisible(true);

  }


  public void setGrid(List<Line2D> grid)
  {
    this.grid = grid;
  }

  public Camera getCam()
  {
    return cam;
  }

  public void setCam(Camera cam)
  {
    this.cam = cam;
  }

  public WorldPresenter getWorldPresenter()
  {
    return worldPresenter;
  }

  public void setWorldPresenter(WorldPresenter worldPresenter)
  {
    this.worldPresenter = worldPresenter;
  }

  @Override
  public void paintComponent(Graphics g)
  {
    super.paintComponent(g); // todo this is important! we need to used with in what ever context draws the map.
    Graphics2D g2d = (Graphics2D) g;


    g2d.setTransform(cam.getTransform());

    g2d.setColor(Color.PINK);
    g2d.setStroke(new BasicStroke(20));
    g2d.draw(cam.getLims());
    g2d.setStroke(new BasicStroke(10));
    


    for (GUIRegion guir : worldPresenter.getRegionsInview(cam))
    {
      guir.draw(g);
    }

    g2d.setColor(ColorsAndFonts.MAP_GRID);
    for(Line2D l : grid) g2d.draw(l);



    /* do this last to ensure transform is no longer needed and dbg info
     * overlays on top */
    if(DEBUG)
    {

      g2d.setColor(Color.CYAN);
      g2d.setStroke(new BasicStroke(3));

      g2d.draw(cam.getViewBounds()); // camera view HUD

      g2d.setTransform(new AffineTransform());

      String regCount = String.format("Polygons in viewBounds: %d",
              worldPresenter.countIntersectingRegions(cam.getViewBounds()));
      String vertCount = String.format("Vertices in viewBounds: %d",
              worldPresenter.countIntersectingPoints(cam.getViewBounds()));
      
      g2d.setColor(Color.RED);
      g2d.setFont(new Font("Courier", Font.PLAIN, 14));
      g2d.drawString(vertCount, 15, 640);
      g2d.drawString(regCount, 15, 660);
      
      /* debug stats on BufImg */
      g2d.drawImage(cam.getDBGimg(), 0, 500, null);
    }
  }

}
