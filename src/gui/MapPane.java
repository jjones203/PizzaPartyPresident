package gui;

import javax.swing.*;
import javax.swing.event.MouseInputListener;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

/**
 @author david
 created: 2015-02-03

 description: */


public class MapPane extends JPanel
  implements MouseListener, MouseWheelListener, MouseInputListener, KeyListener
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


  private boolean multiSelect;
  private Point2D multiSelectFrom;
  private Rectangle2D dragRect;
  
  private WorldPresenter presenter;
  private Camera cam;
  private Point2D dragFrom;


  public MapPane(Camera cam, WorldPresenter presenter)
  {
    this.cam = cam;
    this.presenter = presenter;
    addMouseListener(this);
    addMouseWheelListener(this);
    addMouseMotionListener(this);
    addKeyListener(this);
    
    /* todo: sizing generalization */
    setPreferredSize(new Dimension(1000,500));
  }
  

  @Override
  protected void paintComponent(Graphics g)
  {
    Graphics2D g2 = (Graphics2D) g;

    if(multiSelect) drawDragRect(g2); /* BEFORE transform */
    g2.setTransform(cam.getTransform());

    
    for (GUIRegion region : presenter.getRegionsInview(cam)) region.draw(g2);
  }

  private void drawDragRect(Graphics2D g2)
  {
    g2.setColor(ColorsAndFonts.ACTIVE_REGION_OUTLINE);
    g2.draw(dragRect);
  }


  public void update()
  {

    if (isDOWNdepressed && isSHIFTdepressed)
    {
      cam.zoomOut(ZOOM_STEP);
      return;
    }
    if (isUPdepressed && isSHIFTdepressed)
    {
      cam.zoomIn(ZOOM_STEP);
      return;
    }
    if (isDOWNdepressed) cam.translateRelativeToView(0, CAMERA_STEP);
    if (isUPdepressed) cam.translateRelativeToView(0, -CAMERA_STEP);
    if (isLEFTdepressed) cam.translateRelativeToView(-CAMERA_STEP, 0);
    if (isRIGHTdepressed) cam.translateRelativeToView(CAMERA_STEP, 0);
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
    }
  }

  
  @Override
  public void keyTyped(KeyEvent e){ /*do nothing*/}

  
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

    System.out.println("click");
    if (e.isControlDown())
    {
      cam.centerAbsolute(mapClick.getX(), mapClick.getY());
    }
    else
    {
      presenter.singleClickAt(mapClick.getX(), mapClick.getY());
    }
  }

  
  @Override
  public void mousePressed(MouseEvent e)
  {
    multiSelectFrom = e.getPoint();
    dragFrom = e.getPoint();
  }

  
  @Override
  public void mouseReleased(MouseEvent e)
  {
    multiSelect = false;
  }

  
  @Override
  public void mouseEntered(MouseEvent e){ /* do nothing */ }

  
  private Rectangle2D rectFromCornerPoints(Point2D topLeft, Point2D botRight)
  {
    double x = Math.max(topLeft.getX(), botRight.getX());
    double y = Math.min(topLeft.getY(), botRight.getY());
    double w = Math.abs(topLeft.getX() - botRight.getX());
    double h = Math.abs(topLeft.getY() - botRight.getY());
    return new Rectangle2D.Double(x, y, w, h);
  }


  @Override
  public void mouseExited(MouseEvent e){ /* do nothing */}

  
  @Override
  public void mouseWheelMoved(MouseWheelEvent e)
  {
    Point2D mapClick = convertToMapSpace(e.getPoint());

    /*todo: generalize conversion from wheel rotation to zoom */
    System.out.println("whee!");
      
    cam.zoomAbsolute(e.getPreciseWheelRotation() / 5, mapClick.getX(), mapClick.getY());
  }

  
  @Override
  public void mouseDragged(MouseEvent e)
  {
    if(isSHIFTdepressed)
    {
      multiSelect = true;
      dragRect = rectFromCornerPoints(multiSelectFrom, e.getPoint());
      presenter.selectAll(dragRect);
    }
    else
    {
      double dx = e.getPoint().getX() - dragFrom.getX();
      double dy = e.getPoint().getY() - dragFrom.getY();
      cam.translateRelativeToView(dx, dy);
      dragFrom = e.getPoint();
    }
    
  }

  
  @Override
  public void mouseMoved(MouseEvent e)
  { /* todo something here? */  }

  
  private Point2D convertToMapSpace(Point2D screenClick)
  {
    AffineTransform a = cam.getTransform();

    try
    {
      a.invert();
    } catch (NoninvertibleTransformException e)
    {
      e.printStackTrace();
      System.exit(1);
    }
    Point2D mapClick = new Point2D.Double();
    a.transform(screenClick, mapClick);
    return mapClick;
  }
}
