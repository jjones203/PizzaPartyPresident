package cameratest;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

/**
 * Created by winston on 1/24/15.
 * CameraTest a simple camera controller class.
 * controlls:
 *  arrows pan,
 *  SHIFT + up or down arrow zooms in and out.
 *
 * to test, run this file.
 * CS 351 spring 2015
 */
public class CamraController extends KeyAdapter implements ActionListener
{
  private final static int CAMERA_STEP = 4;
  private final static double ZOOM_STEP = .04;
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

  private CameraMockup camer;

  public CamraController(CameraMockup camer)
  {
    this.camer = camer;
    Timer timer = new Timer(5, this);
    timer.start();
  }

  @Override
  public void actionPerformed(ActionEvent e)
  {
    if (isDOWNdepressed && isSHIFTdepressed)
    {
      camer.zoomOut(ZOOM_STEP);
      return;
    }
    if (isUPdepressed && isSHIFTdepressed)
    {
      camer.zoomIn(ZOOM_STEP);
      return;
    }
    if (isDOWNdepressed)  camer.moveUp(CAMERA_STEP);
    if (isUPdepressed)    camer.moveDown(CAMERA_STEP);
    if (isLEFTdepressed)  camer.moveRight(CAMERA_STEP);
    if (isRIGHTdepressed) camer.moveLeft(CAMERA_STEP);

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
        System.out.println("what what?");
    }
  }

  // update -> issue the collnad based on what is depressed

  public static void main(String[] args)
  {
    int HEIGHT = 800;
    int WIDTH = 800;
    ArrayList<Polygon> shapes = new ArrayList<>();
    Polygon p = new Polygon();
    p.addPoint(200, 200);
    p.addPoint(200, 400);
    p.addPoint(400, 200);

    shapes.add(p);

    Polygon p2 = new Polygon();
    p2.addPoint(-50, -50);
    p2.addPoint(-50, 50);
    p2.addPoint(50, 50);
    p2.addPoint(50, -15);

    shapes.add(p2);


    RandoPolygonGen polys = new RandoPolygonGen(1l, new Point(-800, -800), new Point(800, 800));

    for (int i = 0; i < 100; i++)
    {
      shapes.add(polys.genRandpolygon(12));
    }




    CameraMockup cam = new CameraMockup(shapes);

    CamraController controller = new CamraController(cam);

    JFrame frame = new JFrame();
    frame.setSize(800, 800);
    frame.add(cam);
    frame.setVisible(true);
    frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    frame.addKeyListener(controller);


  }

}
