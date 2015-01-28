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
  Camera camera = new Camera();
  MapConverter mapConverter = new EquirectangularConverter();
  MapView mapView = new MapView(StateParserTest.getStateRegions(), mapConverter);



}
