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

  enum CAM_DISTANCE
  {
    CLOSE_UP, MEDIUM, LONG
  }

  private Map map;
  private Camera camera;

  private CAM_DISTANCE distance;

  private ActiveRegion activeRegion = new ActiveRegion();
  private PassiveRegion passiveRegion = new PassiveRegion();

  public MapView(Map map, Camera camera)
  {
    this.map = map;
    this.camera = camera;
  }

  public Collection<Region> getRegionsInview()
  {
    distance = getDistance(camera);
  }

  private CAM_DISTANCE getDistance(Camera camera)
  {
    int height = camera.getHeight();
    if (height < 3) return CAM_DISTANCE.CLOSE_UP;
    else if (height < 6) return CAM_DISTANCE.MEDIUM;
    else return CAM_DISTANCE.LONG;
  }

}
