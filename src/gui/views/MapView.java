package gui.views;

import gui.Camera;
import gui.MapConverter;
import model.Region;

import java.awt.geom.Rectangle2D;
import java.util.Collection;
import java.util.LinkedList;

/**
 * Created by winston on 1/27/15.
 * Phase_01
 * CS 351 spring 2015
 * <p/>
 * Manages how the regions are displayed and rendered.
 */
public class MapView
{
  private CAM_DISTANCE lastDistance;
  private MapConverter mpConverter;
  private Collection<GUIRegion> guiRegions;
  private ActiveRegion activeRegionView = new ActiveRegion();
  private PassiveRegion passiveRegionView = new PassiveRegion();

  private RegionView ActiveWithName = new RegionNameView(activeRegionView, 5000);
  private RegionView PassiveWithName = new RegionNameView(passiveRegionView, 5000);

  private RegionView ActiveSmallText = new RegionNameView(activeRegionView, 1000);
  private RegionView PasiveSmallText = new RegionNameView(passiveRegionView, 1000);


  public MapView(Collection<Region> regions, MapConverter mpConverter)
  {
    this.mpConverter = mpConverter;
    guiRegions = wrapRegions(regions);
  }

  public Collection<GUIRegion> getGuiRegions()
  {
    return guiRegions;
  }

  public void setGuiRegions(Collection<GUIRegion> guiRegions)
  {
    this.guiRegions = guiRegions;
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

  public Collection<GUIRegion> getRegionsInview(Camera camera)
  {
//    Collection<GUIRegion> regionsInview = new LinkedList<>();

//    Rectangle2D inViewBox = camera.getViewBounds();

//    for (GUIRegion guir : guiRegions)
//    {
//      if (guir.getPoly().intersects(inViewBox)) regionsInview.add(guir);
//    }
//
//    for (GUIRegion guir : regionsInview)
//    {
//      if (guir.isActive()) guir.setLook(activeRegionView);
//    }
//


    if (lastDistance == calcDistance(camera)) return getGuiRegions();

    lastDistance = calcDistance(camera); // else set last to current.

    switch (lastDistance)
    {
      case CLOSE_UP:
        System.out.println("CLOSE UP");
        setRegionsActivePassiveViews(ActiveSmallText, PasiveSmallText);
        break;

      case MEDIUM:
        System.out.println("CLOSE UP/MEDIUM");
        setRegionsActivePassiveViews(ActiveWithName, PassiveWithName);
        break;

      case LONG:
        setRegionsActivePassiveViews(ActiveWithName, ActiveWithName);
        break;

      default:
        System.err.println(lastDistance + "not handeled by getRegionsInview");
    }

    return getGuiRegions();
  }

  /*
   * sets the regions to the respective views as a
   * function of their active state
   */
  private void setRegionsActivePassiveViews(RegionView active, RegionView passive)
  {
    for (GUIRegion region : getGuiRegions())
    {
      if (region.isActive()) region.setLook(active);
      else region.setLook(passive);
    }
  }

  /*
   * Creates discrete view steps to handel region presentation rules.
   */
  private CAM_DISTANCE calcDistance(Camera camera)
  {
    int height = camera.getHeight();
    if (height < 5)
    {
      return CAM_DISTANCE.CLOSE_UP;
    }
    else if (height < 10)
    {
      return CAM_DISTANCE.MEDIUM;
    }
    else
    {
      return CAM_DISTANCE.LONG;
    }
  }

  enum CAM_DISTANCE
  {
    CLOSE_UP, MEDIUM, LONG
  }

}
