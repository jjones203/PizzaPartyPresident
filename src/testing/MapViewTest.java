package testing;

import gui.Camera;
import gui.EquirectangularConverter;
import gui.MapConverter;
import gui.views.MapView;
import IO.XMLparsers.StateParserTest;

/**
 * Created by winston on 1/28/15.
 * Phase_01
 * CS 351 spring 2015
 */
public class MapViewTest
{
  public static void main(String[] args)
  {
    Camera camera = new Camera(0, 0);
    
    MapConverter mapConverter = new EquirectangularConverter();
    MapView mapView = new MapView(StateParserTest.getStateRegions(), mapConverter);

    System.out.println(mapView.getGuiRegions().size());
  }

}
