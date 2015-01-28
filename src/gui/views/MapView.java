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

//  private Map map;
  private MapConverter mpConverter;

  private CAM_DISTANCE distance;
  private Collection<GUIRegion> guiRegions;

  private ActiveRegion activeRegionView = new ActiveRegion();
  private PassiveRegion passiveRegionView = new PassiveRegion();

  private RegionView ActiveWithName = new RegionNameView(activeRegionView);
  private RegionView PassiveWithName = new RegionNameView(passiveRegionView);


  public MapView(Collection<Region> regions, MapConverter mpConverter)
  {
    this.mpConverter = mpConverter;
    guiRegions = wrapRegions(regions);
  }

  private Collection<GUIRegion> wrapRegions(Collection<Region> regions)
  {
    Collection<GUIRegion> guiRs = new LinkedList<>();

    for (Region region : regions)
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

  public Collection<GUIRegion> getRegionsInview(Rectangle2D inViewBox)
  {
    Collection<GUIRegion> regionsInview = new LinkedList<>();

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
            r.setLook(ActiveWithName);
          }
          else
          {
            r.setLook(PassiveWithName);
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
