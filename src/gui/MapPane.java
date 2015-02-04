package gui;

import javax.swing.*;
import javax.swing.event.MouseInputListener;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;

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


  private boolean isInDrag;
  private Point2D draggedFrom;

  
  private WorldPresenter presenter;
  private Camera cam;
  

  public MapPane(Camera cam, WorldPresenter presenter)
  {
    this.cam = cam;
    this.presenter = presenter;
    addMouseListener(this);
    addMouseListener(this);
    addMouseMotionListener(this);
    addKeyListener(this);
  }
  

  @Override
  protected void paintComponent(Graphics g)
  {
    Graphics2D g2 = (Graphics2D) g;
    g2.setTransform(cam.getTransform());

    for (GUIRegion region : presenter.getRegionsInview(cam)) region.draw(g2);
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

    if (e.isControlDown())
    {
      cam.centerAbsolute(mapClick.getX(), mapClick.getY());
    }
    else
    {
      presenter.clickAt(mapClick.getX(), mapClick.getY());
    }
  }

  
  @Override
  public void mousePressed(MouseEvent e)
  {
    isInDrag = true;

  }

  
  @Override
  public void mouseReleased(MouseEvent e)
  {
    draggedFrom = e.getPoint();
    

  }

  
  @Override
  public void mouseEntered(MouseEvent e)
  {

  }

  
  @Override
  public void mouseExited(MouseEvent e)
  {

  }

  
  @Override
  public void mouseWheelMoved(MouseWheelEvent e)
  {
    Point2D mapClick = convertToMapSpace(e.getPoint());

    cam.zoomAbsolute(e.getPreciseWheelRotation() / 5, mapClick.getX(), mapClick.getY());

  }

  
  @Override
  public void mouseDragged(MouseEvent e)
  {
    isInDrag = true;
  }

  
  @Override
  public void mouseMoved(MouseEvent e)
  {

  }

  
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
