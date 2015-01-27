package gui.views;

import gui.Camera;
import gui.MapConverter;
import model.Map;
import model.Region;

import java.awt.geom.Rectangle2D;
import java.util.Collection;
import java.util.LinkedList;

/**
 * Created by winston on 1/27/15.
 * Phase_01
 * CS 351 spring 2015
 *
 * Manages how the regions are displayed and rendered.
 */
public class MapView
{

  enum CAM_DISTANCE
  {
    CLOSE_UP, MEDIUM, LONG
  }

  private Map map;
  private Camera camera;
  private MapConverter mpConverter;

  private CAM_DISTANCE distance;
  private Collection<GUIRegion> guiRegions;

  private ActiveRegion activeRegionView = new ActiveRegion();
  private PassiveRegion passiveRegionView = new PassiveRegion();


  public MapView(Map map, Camera camera, MapConverter mpConverter)
  {
    this.map = map;
    this.camera = camera;
    this.mpConverter = mpConverter;

    guiRegions = wrapRegions();
  }

  private Collection<GUIRegion> wrapRegions()
  {
    Collection<GUIRegion> guiRs = new LinkedList<>();

    for (Region region : map.getWorld())
    {
      GUIRegion guir = new GUIRegion(region, mpConverter, passiveRegionView);
      guiRs.add(guir);
    }
    return guiRs;
  }

  public void markRegionActive(GUIRegion region)
  {
    region.setActive(true);
  }

  public void deselectActiveRegion(GUIRegion region)
  {
    region.setActive(false);
  }

  public Collection<GUIRegion> getRegionsInview()
  {
    Collection<GUIRegion> regionsInview = new LinkedList<>();
    distance = calcDistance(camera);

    Rectangle2D inViewBox = camera.getViewBounds();

    for (GUIRegion guir : guiRegions)
    {
      if (guir.getPoly().intersects(inViewBox)) regionsInview.add(guir);
    }

    for (GUIRegion guir : regionsInview)
    {
      if (guir.isActive()) guir.setLook(activeRegionView);
    }

    //TODO work on this logic, specifically how to view might or might not link together....
    switch (distance)
    {
      case CLOSE_UP:
        for (GUIRegion r : regionsInview)
        {
          if (r.isActive())
          {
            r.setLook(new RegionNameView(activeRegionView));
          }
          else
          {
            r.setLook(new RegionNameView(passiveRegionView));
          }
        }
    }

    return regionsInview;
  }

  private CAM_DISTANCE calcDistance(Camera camera)
  {
    int height = camera.getHeight();
    if (height < 3)
    {
      return CAM_DISTANCE.CLOSE_UP;
    }
    else if (height < 6)
    {
      return CAM_DISTANCE.MEDIUM;
    }
    else
    {
      return CAM_DISTANCE.LONG;
    }
  }

}
