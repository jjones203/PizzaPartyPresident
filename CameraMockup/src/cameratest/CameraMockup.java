package cameratest;

import javax.swing.*;
import java.awt.*;
import java.util.Collection;

/**
 * Created by winston on 1/24/15.
 * CameraTest a basic proof of concept for a GUI camera that can zoom and pan.
 * CS 351 spring 2015
 */
public class CameraMockup extends JPanel
{
  private Collection<Polygon> shapes;
  private double zoomLevel = .8;
  public final static double ZOOM_STEP = .02;


  public CameraMockup(Collection<Polygon> shaped)
  {
    this.shapes = shaped;
  }


  private void shiftPolys(int dx, int dy)
  {
    for (Polygon p : shapes)
    {
      p.translate(dx, dy);
    }
  }

  public void moveRight(int x)
  {
    shiftPolys(x, 0);
    repaint();
  }

  public void moveLeft(int x)
  {
    shiftPolys(-x, 0);
    repaint();
  }

  public void moveUp(int x)
  {
    shiftPolys(0, -x);
    repaint();
  }

  public void moveDown(int x)
  {
    shiftPolys(0, x);
    repaint();
  }

  public void zoomIn(double x)
  {
    zoomLevel += x;
    repaint();
  }

  public void zoomOut(double x)
  {
    zoomLevel -= x;
    repaint();
  }

  @Override
  public void paint(Graphics g)
  {
    Graphics2D g2d = (Graphics2D) g;

//    g2d.setRenderingHint(
//        RenderingHints.KEY_ANTIALIASING,
//        RenderingHints.VALUE_ANTIALIAS_ON
//    );

    g2d.translate(getWidth() / 2, getHeight() / 2);
    g2d.scale(zoomLevel, zoomLevel);

    for (Polygon polygon : shapes)
    {
      g2d.draw(polygon);
//      g2d.fill(polygon);
    }

  }


}
