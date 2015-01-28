package gui;


import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

/**

 */

public class Camera
{
  private static final double ASPECT_RATIO = 2;

  /*arbitrary*/
  static final double MID_HEIGHT = 10;
  static final double MAX_HEIGHT = 20;
  static final double MIN_HEIGHT = 0;

  private static final double BASE_W = 1000;
  private static final double BASE_H = BASE_W /ASPECT_RATIO;

  private Rectangle2D viewBounds;
  private double height;
  private double scale;

  public static void main(String[] args)
  {
    Camera c = new Camera(0,0);
    System.out.println(c);
    System.out.println(c.getCenter());
    c.zoomOut(10);
    System.out.println(c);
    System.out.println(c.getCenter());
    c.zoomIn(10);
    System.out.println(c);
    System.out.println(c.getCenter());

  }


  public Camera(double x, double y)
  {
    this(x, y, MIN_HEIGHT);
  }

  public Camera(double x, double y, double initialHeight)
  {
    setHeight(initialHeight);

    double scale = Math.pow(2, height);
    viewBounds = new Rectangle2D.Double(x, y, BASE_W, BASE_H);
  }

  public Camera(Point p)
  {
    this(p.x, p.y);
  }

  public Point2D getCenter()
  {
    return new Point2D.Double(viewBounds.getCenterX(),viewBounds.getCenterY());
  }


  public void translateAbsolute(double dx, double dy)
  {
    double newX = viewBounds.getX() + dx;
    double newY = viewBounds.getY() + dy;
    double width = viewBounds.getWidth();
    double height = viewBounds.getHeight();
    viewBounds.setFrame(newX, newY, width, height);
  }

  public void translateRelativeToHeight(int dx, int dy)
  {
    translateAbsolute(dx * scale, dy * scale);
  }

  public void zoomIn(double zoomDiff)
  {
    double centerY = viewBounds.getCenterY();
    double centerX = viewBounds.getCenterX();
    zoomAbsolute(-zoomDiff, centerX, centerY);
  }

  public void zoomOut(double zoomDiff)
  {
    double centerY = viewBounds.getCenterY();
    double centerX = viewBounds.getCenterX();
    zoomAbsolute(zoomDiff, centerX, centerY);
  }

  private void zoomAbsolute(double dZoom, double anchorX, double anchorY)
  {
    setHeight(height + dZoom);

    double oldX = viewBounds.getX();
    double oldY = viewBounds.getY();


    double newX = anchorX - scale * (anchorX - oldX);
    double newY = anchorY - scale * (anchorY - oldY);

    double newH = scale * BASE_H;
    double newW = scale * BASE_W;

    viewBounds.setFrame(newX, newY, newW, newH);
  }

  private void zoomRelativeToView(double dZoom, double anchorX, double anchorY)
  {
    zoomAbsolute(dZoom, anchorX / scale, anchorY / scale);
  }

  public AffineTransform getTransform()
  {
    AffineTransform at = new AffineTransform();


    double shiftX = -viewBounds.getX();
    double shiftY = -viewBounds.getY();

    /* viewBounds are scaled, shift is in scaled terms? */
    at.scale(1 / scale, 1 / scale);
    at.translate(shiftX, shiftY);

    return at;
  }


  public int getHeight()
  {
    return (int)height; /* TODO: resolve FP v INT dilemma */
  }

  public Rectangle2D getViewBounds()
  {
    return viewBounds;
  }

  @Override
  public String toString()
  {
    return "Camera{" +
            ", viewBounds=" + viewBounds +
            ", height=" + height +
            '}';
  }

  public void setHeight(double height)
  {
    if(height < MIN_HEIGHT) height = MIN_HEIGHT;
    else if(height > MAX_HEIGHT) height = MAX_HEIGHT;
    this.height = height;

    scale = Math.pow(2, height);
  }
}