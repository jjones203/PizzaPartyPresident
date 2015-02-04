package testing;

import IO.XMLparsers.KMLParser;
import gui.*;
import model.Region;
import model.World;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;

/**
 @author david
 created: 2015-02-04

 description: */
public class MapPaneTest
{

 public static void main(String[] args)
 {

  Collection<Region> backgroundRegions = KMLParser.getRegionsFromFile("resources/countries_world.xml");
  Collection<Region> modelMap = KMLParser.getRegionsFromFile("resources/ne_10m_admin_1_states_provinces.kml");
  
  World world = new World(modelMap);
  
  MapConverter converter = new EquirectangularConverter();
  WorldPresenter presenter = new WorldPresenter(converter);
  presenter.setBackgroundRegions(backgroundRegions);
  presenter.setModelRegions(modelMap);
  
  Camera cam = new Camera(converter);
  final MapPane mapPane = new MapPane(cam, presenter);
  
  
  JFrame win = new JFrame();
  win.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
  win.add(mapPane);
  win.addKeyListener(mapPane);
  win.pack();
  win.setVisible(true);
  
  
  new Timer(30, new ActionListener()
  {
   @Override
   public void actionPerformed(ActionEvent e)
   {
    mapPane.repaint();
    mapPane.update();
   }
  }).start();
 }
}
