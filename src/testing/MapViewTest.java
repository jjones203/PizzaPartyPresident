package testing;

import IO.AreaXMLloader;
import IO.XMLparsers.KMLParser;
import gui.*;
import gui.views.GUIRegion;
import gui.views.MapView;
import IO.XMLparsers.StateParserTest;
import model.Region;
import org.xml.sax.SAXException;
import testing.generators.AttributeGenerator;

import javax.swing.*;
import javax.xml.parsers.ParserConfigurationException;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.geom.AffineTransform;
import java.util.Collection;
import java.util.List;
import java.awt.geom.Line2D;
import java.util.Random;


/**
 * Created by winston on 1/28/15.
 * Phase_01
 * CS 351 spring 2015
 */
public class MapViewTest extends JPanel
{
  private static final boolean DEBUG = true;
  private Camera cam;
  private MapView mapView;
  private List<Line2D> grid;

  public static void main(String[] args)
  {
    AttributeGenerator randoAtts = new AttributeGenerator(new Random());
    final MapViewTest canvas = new MapViewTest();
    MapConverter mapConverter = new EquirectangularConverter();

    AreaXMLloader areaXMLloader = null;
    try
    {
      areaXMLloader = new AreaXMLloader("resources/areas");
    }
    catch (ParserConfigurationException e)
    {
      e.printStackTrace();
    }
    catch (SAXException e)
    {
      e.printStackTrace();
    }

    Collection<Region> worldz = StateParserTest.getStateRegions();
    worldz.addAll(KMLParser.getRegionsFromFile("resources/world.xml"));
    worldz.addAll(areaXMLloader.getRegions());


    //add random attributes for testing...
    for (Region r : worldz)
    {
      r.setAttributes(randoAtts.nextAttributeSet());
    }

    MapView mapView = new MapView(worldz, mapConverter);

    Point startPoint = new Point(
        mapView.getGuiRegions().iterator().next().getPoly().xpoints[0],
        mapView.getGuiRegions().iterator().next().getPoly().ypoints[0]
    );


    Camera camera = new Camera(startPoint.x, startPoint.y, mapConverter);
    CamController keyController = new CamController(camera);

    canvas.setCam(camera);
    canvas.setMapView(mapView);
    canvas.setSize(1000, 800);
    canvas.setGrid(((EquirectangularConverter)mapConverter).getLatLonGrid());
    canvas.setBackground(ColorSchemes.OCEANS);


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

  public MapView getMapView()
  {
    return mapView;
  }

  public void setMapView(MapView mapView)
  {
    this.mapView = mapView;
  }

  @Override
  public void paintComponent(Graphics g)
  {
    Graphics2D g2d = (Graphics2D) g;


    g2d.setTransform(cam.getTransform());

    g2d.setColor(Color.PINK);
    g2d.setStroke(new BasicStroke(20));
    g2d.draw(cam.getLims());
    g2d.setStroke(new BasicStroke(10));

    for (GUIRegion guir : mapView.getRegionsInview(cam))
    {
      guir.draw(g);
    }

    g2d.setColor(new Color(255, 255, 255, 100));
    for(Line2D l : grid) g2d.draw(l);



    /* do this last to ensure transform is no longer needed and dbg info
     * overlays on top */
    if(DEBUG)
    {
      g2d.setColor(Color.CYAN);
      g2d.setStroke(new BasicStroke(3));
      g2d.draw(cam.getViewBounds()); // camera view HUD
      g2d.setTransform(new AffineTransform());
      g2d.drawImage(cam.getDBGimg(), 5, 5, null);
    }
  }

}
