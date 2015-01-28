package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * @author david
 *         created: 2015-01-27
 *         <p/>
 *         description:
 */
public class CamController extends KeyAdapter implements ActionListener
{
  private final static int CAMERA_STEP = 4;
  private final static int ZOOM_STEP = 1;
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


  public CamController(Camera camera)
  {
    this.cam = camera;
  }


  @Override
  public void actionPerformed(ActionEvent e)
  {
    System.out.println("ACTIVE fired from " + getClass().getName());

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
    if (isDOWNdepressed)  cam.translateRelativeToHeight(0, CAMERA_STEP);
    if (isUPdepressed)    cam.translateRelativeToHeight(0, -CAMERA_STEP);
    if (isLEFTdepressed)  cam.translateRelativeToHeight(-CAMERA_STEP, 0);
    if (isRIGHTdepressed) cam.translateRelativeToHeight(CAMERA_STEP, 0);
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

}
