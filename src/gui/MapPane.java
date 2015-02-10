package gui;

import gui.regionlooks.RegionNameDraw;
import gui.regionlooks.RegionViewFactory;

import javax.swing.*;
import javax.swing.event.MouseInputListener;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.Collection;

/**
 * @author david
 *         created: 2015-02-03
 *         <p/>
 *         description:
 */


public class MapPane extends JPanel
  implements MouseWheelListener, MouseInputListener, KeyListener
{

  private final static int CAMERA_STEP = 10;
  private final static double ZOOM_STEP = .05;
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
  private Point2D dragFrom;

  private boolean doMultiSelect;

  // for key binding
  private Action happyOverlay = new AbstractAction()
  {
    @Override
    public void actionPerformed(ActionEvent e)
    {
      presenter.setCurrentOverlay(RegionViewFactory.Overlay.HAPPINESS);
    }
  };

  private Action defaultOverlay = new AbstractAction()
  {
    @Override
    public void actionPerformed(ActionEvent e)
    {
      presenter.setCurrentOverlay(RegionViewFactory.Overlay.NONE);
    }
  };

  private Action plantingZoneOverlay = new AbstractAction()
  {
    @Override
    public void actionPerformed(ActionEvent e)
    {
      presenter.setCurrentOverlay(RegionViewFactory.Overlay.PLANTING_ZONE);
    }
  };
  // test key binding
  private Action stepWorld = new AbstractAction()
  {
    @Override
    public void actionPerformed(ActionEvent e)
    {
      presenter.setWorldForward(1);
      System.out.println(presenter.getWorldDate());
    }
  };
  
  /**
   * @param cam
   * @param presenter
   */
  public MapPane(Camera cam, WorldPresenter presenter)
  {
    this.cam = cam;
    this.presenter = presenter;
    addMouseListener(this);
    addMouseWheelListener(this);
    addMouseMotionListener(this);
    addKeyListener(this);
    setBackground(ColorsAndFonts.OCEANS);

    /* todo: sizing generalization */
    setPreferredSize(new Dimension(1000, 500));
    setSize(getPreferredSize());
    setMinimumSize(getPreferredSize());
    setDoubleBuffered(true);

    // set up keybindings.
    getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("1"), "default");
    getActionMap().put("default", defaultOverlay);

    getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("2"), "happy");
    getActionMap().put("happy", happyOverlay);

    getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("3"), "planting");
    getActionMap().put("planting", plantingZoneOverlay);

    getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("shift pressed 4"), "step");
    getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("4"), "step");
    getActionMap().put("step", stepWorld);
  }


  @Override
  protected void paintComponent(Graphics g)
  {
    super.paintComponent(g);

    Graphics2D g2 = (Graphics2D) g;
    g2.setTransform(cam.getTransform());

    Collection<GUIRegion> regionsToDraw = presenter.getRegionsInView(cam);

    for (GUIRegion region : regionsToDraw) region.draw(g2);

    if (cam.getDistance() != Camera.CAM_DISTANCE.LONG)
    {
      for (GUIRegion region : regionsToDraw) RegionNameDraw.draw(g2, region);
    }

    if (drawMultiSelect)
    {
      g2.setTransform(new AffineTransform()); /* reset transform! */
      drawDragRect(g2);
    }
  }

  private void drawDragRect(Graphics2D g2)
  {
    g2.setColor(ColorsAndFonts.SELECT_RECT_OUTLINE);
    g2.draw(dragRect);
    g2.setColor(ColorsAndFonts.SELECT_RECT_FILL);
    g2.fill(dragRect);
  }


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
//        System.out.println("unhandeled key press: " + e.getKeyCode());

    }
  }


  @Override
  public void keyTyped(KeyEvent e)
  { /*do nothing*/ }




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
      if (e.isMetaDown())
      {
        presenter.appendClickAt(mapClick.getX(), mapClick.getY(), cam);
      }
      else
      {
        presenter.singleClickAt(mapClick.getX(), mapClick.getY(), cam);
      }
    }
  }


  @Override
  public void mousePressed(MouseEvent e)
  {
    doMultiSelect = isSHIFTdepressed;
    if (doMultiSelect) presenter.setActivelyDragging(true);
    multiSelectFrom = e.getPoint();
    dragFrom = e.getPoint();
  }


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


  @Override
  public void mouseWheelMoved(MouseWheelEvent e)
  {
    Point2D mapClick = convertToMapSpace(e.getPoint());

    /*todo: generalize conversion from wheel rotation to zoom */

    cam.zoomAbsolute(e.getPreciseWheelRotation() / 5, mapClick.getX(), mapClick.getY());
  }


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


  @Override
  public void mouseMoved(MouseEvent e)
  { /* todo something here? */ }


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
