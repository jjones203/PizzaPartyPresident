package gui.views;

import gui.Camera;
import model.Map;
import model.Region;

import java.util.Collection;

/**
 * Created by winston on 1/27/15.
 * Phase_01
 * CS 351 spring 2015
 */
public class MapView
{
  private Map map;
  private Camera camera;

  private ActiveRegion activeRegion = new ActiveRegion();
  private PassiveRegion passiveRegion = new PassiveRegion();

  public MapView(Map map, Camera camera)
  {
    this.map = map;
    this.camera = camera;
  }

  public Collection<Region> getRegionsInview()
  {
    int height = camera.getHeight();
  }

}
