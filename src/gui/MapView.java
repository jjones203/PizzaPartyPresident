package gui;

import gui.regionlooks.*;
import model.Region;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import static gui.Camera.CAM_DISTANCE;

/**
 * Created by winston on 1/27/15.
 * Phase_01
 * CS 351 spring 2015
 * <p>
 * Manages how the regions are displayed and rendered.
 */
public class MapView
{

  private boolean DEBUG = true;
  private CAM_DISTANCE lastDistance = CAM_DISTANCE.LONG; // (!) only for debugging.
  private MapConverter mpConverter;
  private Collection<GUIRegion> modelRegions;
  private Collection<GUIRegion> backgroundRegions;

  private RegionViewFactory regionViewFactory;


  public MapView(MapConverter mpConverter)
  {
    modelRegions = new ArrayList<>();
    backgroundRegions = new ArrayList<>();
    this.mpConverter = mpConverter;
    regionViewFactory = new RegionViewFactory();
  }

  public void setBackgroundRegions(Collection<Region> regions)
  {
    RegionView background = regionViewFactory.getBackgroundMapView();
    backgroundRegions = wrapRegions(regions, background);
  }

  public Collection<GUIRegion> getModelRegions()
  {
    return modelRegions;
  }

  public void setModelRegions(Collection<Region> regions)
  {
    RegionView background = regionViewFactory.getLongView();
    modelRegions = wrapRegions(regions, background);
  }

  /*
   * Initialization method only called during the constructor.
   * transforms model regions into gui regions.
   */
  private Collection<GUIRegion> wrapRegions(Collection<Region> regions, RegionView regionView)
  {
    Collection<GUIRegion> guiRs = new ArrayList<>();

    for (Region region : regions)
    {
      GUIRegion guir = new GUIRegion(region, mpConverter, regionView);
      guiRs.add(guir);
    }
    return guiRs;
  }


  public void clickAt(double x, double y)
  {
    for (GUIRegion guir : getModelRegions())
    {
      if (guir.getPoly().contains(x, y))
      {
        // todo debug printing
        System.out.println("region clicked: " + guir.getName());
        toggleRegionState(guir);
      }
    }
  }

  /**
   * Toggles the active/passive state of the specified region.
   *
   * @param region region that will be modified.
   * @return boolean representing the active/passive state of the
   * region after the toggle.
   */
  public boolean toggleRegionState(GUIRegion region)
  {
    region.setActive(!region.isActive());
    return region.isActive();
  }

  public Collection<GUIRegion> getRegionsInview(Camera camera)
  {
    Rectangle2D inViewBox = camera.getViewBounds();
    Collection<GUIRegion> regionsInView = null;

    if (DEBUG && lastDistance != calcDistance(camera))
    {
      lastDistance = calcDistance(camera);
      System.out.println("currentCamer pos: " + lastDistance);
    }

    switch (calcDistance(camera))
    {
      case CLOSE_UP:
        // adds details region view to map only when the camera is close.
        regionsInView = getIntersectingRegions(inViewBox, modelRegions); // over write background image set
        setRegionLook(regionViewFactory.getCloseUpView(), regionsInView);
        break;

      case MEDIUM:
        regionsInView = getIntersectingRegions(inViewBox, backgroundRegions);
        setRegionLook(regionViewFactory.getMediumView(), regionsInView);
        break;

      case LONG:
        regionsInView = getIntersectingRegions(inViewBox, backgroundRegions);
        setRegionLook(regionViewFactory.getLongView(), regionsInView);
        break;

      default:
        System.err.println(calcDistance(camera) + " (!)Not handeled by getRegionsInview");
        System.exit(1);
    }

    return regionsInView;
  }

  /*
   * sets the regions to the respective views as a
   * function of their active state
   */
  private void setRegionLook(RegionView look, Collection<GUIRegion> guiRegions)
  {
    for (GUIRegion region : guiRegions)
    {
      region.setLook(look);
    }
  }

  /*
   * Creates discrete view steps to handel region presentation rules.
   */
  private CAM_DISTANCE calcDistance(Camera camera)
  {
    return camera.getDistance();
  }


  private List<GUIRegion> getIntersectingRegions(Rectangle2D r, Collection<GUIRegion> regions)
  {
    List<GUIRegion> regionsInR = new LinkedList<>();
    for (GUIRegion g : regions)
    {
      if (g.getPoly().intersects(r)) regionsInR.add(g);
    }

    return regionsInR;
  }

  public int countIntersectingRegions(Rectangle2D r)
  {
    return getIntersectingRegions(r, modelRegions).size()
              + getIntersectingRegions(r, backgroundRegions).size();


  }

  public int countIntersectingPoints(Rectangle2D r)
  {
    int sum = 0;
    for (GUIRegion g : modelRegions)
    {
      Polygon p = g.getPoly();
      int n = p.npoints;
      for (int i = 0; i < n; i++)
      {
        if (r.contains(p.xpoints[i], p.ypoints[i])) sum++;
      }
    }
    return sum;
  }


}
