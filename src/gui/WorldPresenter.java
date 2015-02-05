package gui;

import gui.regionlooks.RegionView;
import gui.regionlooks.RegionViewFactory;
import model.Region;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.*;
import java.util.List;

import static gui.Camera.CAM_DISTANCE;

/**
 * Created by winston on 1/27/15.
 * Phase_01
 * CS 351 spring 2015
 * <p/>
 * Manages how the regions are displayed and rendered.
 */
public class WorldPresenter extends Observable
{

  private boolean DEBUG = true;
  private CAM_DISTANCE lastDistance; // (!) only for debugging. //todo remove when finalized
  private MapConverter mpConverter;
  private Collection<GUIRegion> modelRegions;
  private Collection<GUIRegion> backgroundRegions;
  private ActiveRegionList activeRegions;


  private RegionViewFactory regionViewFactory;


  public WorldPresenter(MapConverter mpConverter)
  {
    this.modelRegions = new ArrayList<>();
    this.backgroundRegions = new ArrayList<>();
    this.mpConverter = mpConverter;
    this.regionViewFactory = new RegionViewFactory();
    this.activeRegions = new ActiveRegionList();
    this.lastDistance = CAM_DISTANCE.LONG;
  }

  /**
   * Sets the specified collection of regions to be background graphical regions.
   * This regions set does not have a baring on the logic or model of the game,
   * it is  used for aesthetic presentation.
   *
   * @param regions back ground set of regions. (regions show in LONG shots)
   */
  public void setBackgroundRegions(Collection<Region> regions)
  {
    RegionView background = regionViewFactory.getBackgroundMapView();
    backgroundRegions = wrapRegions(regions, background);
  }

  // could this be made private?
  public Collection<GUIRegion> getModelRegions()
  {
    return modelRegions;
  }

  /**
   * Set the given collection of regions as the model of the game. These
   * regions can be selected and inspected (in distinction to the background
   * regions).
   *
   * @param regions set of regions that constitute the model and logical
   *                entities of the game.
   */
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


  /**
   * Marks every region that intersects the specified rectangle as active.
   * used with the camera object to enable click and drag selection of map
   * regions.
   *
   * @param rect bounding rectangle for selection
   */
  public void selectAll(Rectangle2D rect)
  {
    activeRegions.clear();
    for (GUIRegion r : getIntersectingRegions(rect, modelRegions))
    {
      activeRegions.add(r);
    }
  }

  /**
   * Registers a single selection click at the given point.
   * If the point given correspond to a region, that region will become selected,
   * and any other regions that were selected will be de-selected.
   * <p/>
   * If there is no corresponding region nothing will happen.
   *
   * @param x x coord of click
   * @param y y coord of click
   */
  public void singleClickAt(double x, double y)
  {
    for (GUIRegion guir : getModelRegions())
    {
      if (guir.getPoly().contains(x, y))
      {
        if (activeRegions.contains(guir))
        {
          activeRegions.remove(guir);
        }
        else
        {
          activeRegions.clear();
          activeRegions.add(guir);
        }
        return; //for early loop termination.
      }
    }
  }

  /**
   * Registers a region append click at the specified point.
   * if there is a region at said point, its state will be toggled.
   * This method is called to incrementally build up a group of selected
   * regions, that need not be contiguous.
   *
   * @param x x coord.
   * @param y y coord.
   */
  public void appendClickAt(double x, double y)
  {
    for (GUIRegion guir : getModelRegions())
    {
      if (guir.getPoly().contains(x, y))
      {
        if (activeRegions.contains(guir))
        {
          activeRegions.remove(guir);
        }
        else
        {
          activeRegions.add(guir);
        }
        return; //for early loop termination.
      }
    }
  }


  /**
   * Given a Camera, this method returns all the GUI regions 'in view',
   * and adjusts the look to the appropriate level of detail.
   *
   * @param camera camera object used to extract 'height' and viewing angle on
   *               map.
   * @return all the regions in view, all set to the appropriate rendering rules.
   */
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


  /*
   * Returns all the regions in the given collection that
   * intersect the given rectangle
   */
  private List<GUIRegion> getIntersectingRegions(Rectangle2D r, Collection<GUIRegion> regions)
  {
    List<GUIRegion> regionsInR = new ArrayList<>();
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

  public GUIRegion getActiveRegion()
  {
    if (activeRegions.isEmpty() || activeRegions.size() > 1)
    {
      return null;
    }
    else
    {
      return activeRegions.get(0);
    }
  }

  /**
   * Private class  that manages and the active/passive state of the region.
   * also deals the marking changes
   */
  private class ActiveRegionList
  {
    /**
     * Returns the element at the specified position in this list.
     *
     * @param index index of the element to return
     * @return the element at the specified position in this list
     * @throws IndexOutOfBoundsException if the index is out of range
     *                                   (<tt>index &lt; 0 || index &gt;= size()</tt>)
     */
    public GUIRegion get(int index)
    {
      return activeRegions.get(index);
    }

    private List<GUIRegion> activeRegions;


    public ActiveRegionList()
    {
      activeRegions = new ArrayList<>();
    }

    /**
     * Returns the number of elements in this list.  If this list contains
     * more than <tt>Integer.MAX_VALUE</tt> elements, returns
     * <tt>Integer.MAX_VALUE</tt>.
     *
     * @return the number of elements in this list
     */
    public int size()
    {
      return activeRegions.size();
    }

    /**
     * Returns <tt>true</tt> if this list contains no elements.
     *
     * @return <tt>true</tt> if this list contains no elements
     */
    public boolean isEmpty()
    {
      return activeRegions.isEmpty();
    }

    public void add(GUIRegion region)
    {
      if (contains(region)) return; // already in the list.
      region.setActive(true);
      activeRegions.add(region);

      setChanged();
      notifyObservers();
    }


    public GUIRegion remove(GUIRegion region)
    {
      int index = activeRegions.indexOf(region);
      if (index == -1) return null;
      GUIRegion guir = activeRegions.remove(index);
      guir.setActive(false);

      setChanged();
      notifyObservers();

      return guir;
    }

    public boolean contains(GUIRegion region)
    {
      return activeRegions.contains(region);
    }

    public void clear()
    {
      for (GUIRegion r : activeRegions) r.setActive(false);
      activeRegions.clear();
      setChanged();
      notifyObservers();
    }
  }
}
