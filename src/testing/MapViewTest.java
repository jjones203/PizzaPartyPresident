package testing;

import gui.CamController;
import gui.Camera;
import gui.EquirectangularConverter;
import gui.MapConverter;
import gui.views.GUIRegion;
import gui.views.MapView;
import IO.XMLparsers.StateParserTest;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * Created by winston on 1/28/15.
 * Phase_01
 * CS 351 spring 2015
 */
public class MapViewTest extends JPanel
{

  private Camera cam;
  private MapView mapView;

  public static void main(String[] args)
  {
    final MapViewTest canvas = new MapViewTest();

    Camera camera = new Camera(0, 0);
    CamController keyController = new CamController(camera);

    MapConverter mapConverter = new EquirectangularConverter();
    MapView mapView = new MapView(StateParserTest.getStateRegions(), mapConverter);

    canvas.setCam(camera);
    canvas.setMapView(mapView);
    canvas.setSize(1000, 800);


    Timer timer = new Timer(30, keyController);
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

    frame.setSize(1000, 800);
    frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    frame.setVisible(true);


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
  public void paintComponents(Graphics g)
  {
    Graphics2D g2d = (Graphics2D) g;

    g2d.setStroke(new BasicStroke(10));

    g2d.draw(new Rectangle(0, 0, 1000, 1000));


    g2d.setTransform(cam.getTransform());
    g2d.draw(cam.getViewBounds()); // camera view HUD


    for (GUIRegion guir : mapView.getGuiRegions())
    {
      guir.draw(g);
    }
  }

}
