package gui;

import gui.views.MapView;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;

/**
 * @author david
 *         created: 2015-01-27
 *         <p/>
 *         description:
 */
public class CamController 
        extends KeyAdapter 
        implements ActionListener, MouseListener, MouseWheelListener
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

  private Camera cam;
  private MapView mapView;


  public CamController(Camera camera, MapView mapView)
  {
    this.cam = camera;
    this.mapView = mapView;
  }


  @Override
  public void actionPerformed(ActionEvent e)
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
    if (isDOWNdepressed)  cam.translateRelativeToView(0, CAMERA_STEP);
    if (isUPdepressed)    cam.translateRelativeToView(0, -CAMERA_STEP);
    if (isLEFTdepressed)  cam.translateRelativeToView(-CAMERA_STEP, 0);
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
        System.out.println(e.getKeyCode());
    }
  }

  @Override
  public void keyTyped(KeyEvent e)
  {
    super.keyTyped(e);
  }

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
        System.out.println(e.getKeyCode());
    }
  }

  @Override
  public void mouseClicked(MouseEvent e)
  {
    Point loc = e.getPoint();
    System.out.println(loc);
    Point2D mapClick = new Point2D.Double();
    AffineTransform a = cam.getTransform();
    
    try
    {
      a.invert();
    } catch (NoninvertibleTransformException e1)
    {
      /* should not happen, all transforms are simple... */
      e1.printStackTrace();
      System.exit(1);
    }
    
    a.transform(loc, mapClick);

    if(e.isControlDown())
    {
      cam.centerAbsolute(mapClick.getX(), mapClick.getY());
    }

  }

  @Override
  public void mousePressed(MouseEvent e)
  {

  }

  @Override
  public void mouseReleased(MouseEvent e)
  {

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

  }
}
