package worldfoodgame.gui;

import worldfoodgame.gui.displayconverters.MapConverter;
import worldfoodgame.gui.regionlooks.*;
import worldfoodgame.model.LandTile;
import worldfoodgame.model.MapPoint;
import worldfoodgame.model.TileManager;
import worldfoodgame.model.World;

import javax.swing.*;
import javax.swing.event.MouseInputListener;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import java.util.Collection;

/**
 * @author david
 *         created: 2015-02-03
 *         <p/>
 *         description:
 *         MapPane is a Swing JPanel encapsulating the view through a Camera
 *         object and its user control interface.  The MapPane communicates
 *         input events corresponding to its domain to the WorldPresenter
 *         to allow regions to be selected and displayed properly
 */


public class MapPane extends JPanel
  implements MouseWheelListener, MouseInputListener, KeyListener
{

  private final static double NAME_VIS_SCALE = 80;
  private final static double FLAG_VIS_SCALE = 250;
  private final static int CAMERA_STEP = 10;
  private final static double ZOOM_STEP = .05;
  private final static double SCROLL_FACTOR = .2;

  private final static int
    UP = 38,
    LEFT = 37,
    DOWN = 40,
    RIGHT = 39,
    SHIFT = 16;

  private boolean
    isUPdepressed,
    isDOWNdepressed,
    isLEFTdepressed,
    isRIGHTdepressed,
    isSHIFTdepressed;


  private boolean drawMultiSelect;
  private Point2D multiSelectFrom;
  private Rectangle2D dragRect;

  private WorldPresenter presenter;
  private Camera cam;
  private MapConverter converter;
  private Point2D dragFrom;

  private boolean doMultiSelect;

  private Collection<Line2D> grid;

  /**
   * Instantiate this MapPane with a Camera to provide transforms and a
   * WorldPresenter to provide an interface into the worldfoodgame.model space
   *
   * @param cam       Camera controlling the map transformations
   * @param presenter WorldPresenter to
   */
  public MapPane(Camera cam, WorldPresenter presenter)
  {
    this.cam = cam;
    this.presenter = presenter;
    converter = presenter.getConverter();
    
    /* 'this' handles most of its controls, for convenience */
    addMouseListener(this);
    addMouseWheelListener(this);
    addMouseMotionListener(this);
    addKeyListener(this);

    setBackground(ColorsAndFonts.OCEANS);

    setPreferredSize(cam.getTargetSize());
    setSize(getPreferredSize());
    setMinimumSize(getPreferredSize());
    setDoubleBuffered(true);
    setToolTipText("");
  }



  /**
   * Overridden paintComponent handles all map drawing.  Model-dependent drawing
   * is handled by the GUIRegions and their associated RegionViews.  Interface
   * dependent drawing logic is handled in this class
   *
   * @param g Graphics context to draw to
   */
  @Override
  protected void paintComponent(Graphics g)
  {
    super.paintComponent(g);

    Graphics2D g2 = (Graphics2D) g;
    g2.setTransform(cam.getTransform());

    // draws map grid
    g2.setColor(ColorsAndFonts.MAP_GRID);
    for (Line2D l : grid) g2.draw(l);

    // set and draw all the background regions
    Collection<GUIRegion> backgroundRegions = presenter.getBackgroundRegionsInView(cam);
    for (GUIRegion region : backgroundRegions) region.draw(g2);


    // draws all the 'logic' regions
    Collection<GUIRegion> regionsToDraw = presenter.getRegionsInView(cam);
    for (GUIRegion region : regionsToDraw) region.draw(g2);

    RegionView regionView = presenter.getCurrentOverlay().getRegionView();
    if (regionView instanceof RasterDataView)
    {
      BufferedImage image = ((RasterDataView) regionView).getRasterImage();
      g2.drawImage(image, (int) -converter.getWidth()/2, (int) -converter.getHeight()/2, null);

      if (presenter.getActiveRegions() != null)
      {
        for (GUIRegion guiRegion : presenter.getActiveRegions())
        {
          OverlayOutline.draw(g2, guiRegion);
        }
      }
    }


    // draws names and/or flags
    double screenArea = cam.getViewBounds().getWidth() * cam.getViewBounds().getWidth();
    for (GUIRegion region : regionsToDraw)
    {
      double visibleRaio = screenArea / region.getSurfaceArea();
      boolean isPrimaryAndActive = region.isActive() && region.isPrimaryRegion();

      if (visibleRaio < NAME_VIS_SCALE || isPrimaryAndActive)
      {
        RegionNameDraw.draw(g2, region);
      }
      if (visibleRaio < FLAG_VIS_SCALE || isPrimaryAndActive)
      {
        RegionFlagDraw.draw(g2, region);
      }
    }


    if (drawMultiSelect)
    {
      g2.setTransform(new AffineTransform()); /* reset transform! */
      drawDragRect(g2);
    }
  }

  /*
    draws the rectangle representing the multi-select rectangle using pleasant
    transparencies
   */
  private void drawDragRect(Graphics2D g2)
  {
    g2.setColor(ColorsAndFonts.SELECT_RECT_OUTLINE);
    g2.draw(dragRect);
    g2.setColor(ColorsAndFonts.SELECT_RECT_FILL);
    g2.fill(dragRect);
  }


  /**
   * Respond to any input events that may have triggered by input events
   */
  public void update()
  {
    if (isDOWNdepressed && isSHIFTdepressed)
    {
      cam.zoomOut(ZOOM_STEP);
    }
    else if (isUPdepressed && isSHIFTdepressed)
    {
      cam.zoomIn(ZOOM_STEP);
    }
    else if (isDOWNdepressed)
    {
      cam.translateRelativeToView(0, CAMERA_STEP);
    }
    else if (isUPdepressed)
    {
      cam.translateRelativeToView(0, -CAMERA_STEP);
    }
    else if (isLEFTdepressed)
    {
      cam.translateRelativeToView(-CAMERA_STEP, 0);
    }
    else if (isRIGHTdepressed)
    {
      cam.translateRelativeToView(CAMERA_STEP, 0);
    }
    else
    {
      // do no-thing...
    }
  }


  /**
   * Overridden KeyPressed sets flags that can be interpreted at the control-polling
   * rate.  Flags are unset in keyReleased
   *
   * @param e KeyEvent fired by a key press
   */
  @Override
  public void keyPressed(KeyEvent e)
  {

    switch (e.getKeyCode())
    {
      case UP:
        isUPdepressed = true;
        break;
      case LEFT:
        isLEFTdepressed = true;
        break;
      case DOWN:
        isDOWNdepressed = true;
        break;
      case RIGHT:
        isRIGHTdepressed = true;
        break;
      case SHIFT:
        isSHIFTdepressed = true;
        break;
      default:
        /* do nothing.  Other keys are interpreted by keybindings
         in the inputmap for better resolution and control */
    }
  }


  @Override
  public void keyTyped(KeyEvent e)
  { /*do nothing*/ }


  /**
   * Overridden keyReleased method unsets flags that indicate which key presses
   * must be responded to when controls are polled
   *
   * @param e KeyEvent fired by a key release
   */
  @Override
  public void keyReleased(KeyEvent e)
  {
    switch (e.getKeyCode())
    {
      case UP:
        isUPdepressed = false;
        break;
      case LEFT:
        isLEFTdepressed = false;
        break;
      case DOWN:
        isDOWNdepressed = false;
        break;
      case RIGHT:
        isRIGHTdepressed = false;
        break;
      case SHIFT:
        isSHIFTdepressed = false;
        break;
      default:
    }
  }


  /**
   * Overridden mouseClicked controls region selection, (multi/single) and can
   * attempt to center the map to a click location
   *
   * @param e MouseEvent fired by a mouse click
   */
  @Override
  public void mouseClicked(MouseEvent e)
  {
    Point2D mapClick = convertToMapSpace(e.getPoint());

    if (e.isControlDown())
    {
      cam.centerAbsolute(mapClick.getX(), mapClick.getY());
    }
    else
    {
      if (e.isShiftDown())
      {
        presenter.appendClickAt(mapClick.getX(), mapClick.getY(), cam);
      }
      else
      {
        presenter.singleClickAt(mapClick.getX(), mapClick.getY(), cam);
      }
    }
  }


  /**
   * Overridden mousePressed method initializes variables that define the behavior
   * directly after the MouseEvent fired, (e.g. whether the user is multi-selecting
   * or not)
   *
   * @param e MouseEvent fired by a mouse press
   */
  @Override
  public void mousePressed(MouseEvent e)
  {
    doMultiSelect = e.isShiftDown();
    if (doMultiSelect) presenter.setActivelyDragging(true);
    multiSelectFrom = e.getPoint();
    dragFrom = e.getPoint();
  }


  /**
   * Overridden mouseReleased method resets flags that control multi-select behavior
   *
   * @param e MouseEvent fired by a mouse release
   */
  @Override
  public void mouseReleased(MouseEvent e)
  {
    doMultiSelect = false;
    drawMultiSelect = false;
    presenter.setActivelyDragging(false);
  }


  @Override
  public void mouseEntered(MouseEvent e)
  { /* do nothing */ }


  /* create a rectangle defined by two corner points */
  private Rectangle2D rectFromCornerPoints(Point2D p1, Point2D p2)
  {
    double x = Math.min(p1.getX(), p2.getX());
    double y = Math.min(p1.getY(), p2.getY());
    double w = Math.abs(p1.getX() - p2.getX());
    double h = Math.abs(p1.getY() - p2.getY());
    return new Rectangle2D.Double(x, y, w, h);
  }


  @Override
  public void mouseExited(MouseEvent e)
  { /* do nothing */}


  /**
   * Overridden mouseWheelMoved controls zooming on the map
   *
   * @param e MouseWheelEvent fired by mouse wheel motion
   */
  @Override
  public void mouseWheelMoved(MouseWheelEvent e)
  {
    Point2D mapClick = convertToMapSpace(e.getPoint());

    /*todo: generalize conversion from wheel rotation to zoom */

    cam.zoomAbsolute(e.getPreciseWheelRotation() * SCROLL_FACTOR, mapClick.getX(), mapClick.getY());
  }


  /**
   * Overridden mouseDragged controls either camera panning (when no modifier key
   * is held) or multi-select (when shift is held)
   *
   * @param e MouseEvent fired by a mouse drag
   */
  @Override
  public void mouseDragged(MouseEvent e)
  {

    if (doMultiSelect)
    {
      drawMultiSelect = true;
      Point2D p1 = convertToMapSpace(multiSelectFrom);
      Point2D p2 = convertToMapSpace(e.getPoint());
      dragRect = rectFromCornerPoints(multiSelectFrom, e.getPoint());
      presenter.selectAll(rectFromCornerPoints(p1, p2), cam);
    }
    else
    {
      double dx = -(e.getPoint().getX() - dragFrom.getX());
      double dy = -(e.getPoint().getY() - dragFrom.getY());
      cam.translateRelativeToView(dx, dy);
      dragFrom = e.getPoint();
    }
  }


  public void setGrid(Collection<Line2D> lines)
  {
    grid = lines;

  }

  @Override
  public void mouseMoved(MouseEvent e)
  {/* do nothing */}

  @Override
  public String getToolTipText(MouseEvent event)
  {
    if(!doMultiSelect)
    {
      Point2D loc = convertToMapSpace(event.getPoint());
      MapPoint p = converter.pointToMapPoint(loc);
      LandTile tile = World.getWorld().getTile(p.getLon(), p.getLat());
      if(tile == TileManager.NO_DATA) return "";
      return tile.toolTipText();
    }
    else return "";
  }

  /* helper function converts a point in screen-space to a point in map-space
     this encapsulates what is almost certainly unnecessary error handling,
     given usage of the AffineTransforms (checks NoninvertibleTransformExceptions)
   */
  private Point2D convertToMapSpace(Point2D screenClick)
  {
    AffineTransform a = cam.getTransform();
    try
    {
      a.invert();
    }
    catch (NoninvertibleTransformException e)
    {
      e.printStackTrace();
      System.exit(1);
    }
    Point2D mapClick = new Point2D.Double();
    a.transform(screenClick, mapClick);
    return mapClick;
  }
}
