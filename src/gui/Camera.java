package gui;


import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;

/**

 */

public class Camera
{
  private static final int MAX_HEIGHT = 10;
  private static final double ASPECT_RATIO = 2;

  private static final int VIEW_W = 1000;
  private static final int VIEW_H = (int)(VIEW_W/ASPECT_RATIO);

  private Point location;
  private Rectangle2D viewBounds;
  private int height;


  public Camera(int x, int y)
  {
    height = 0;
    location = new Point(x, y);
    viewBounds = new Rectangle2D.Double();
    updateViewBounds();
  }

  public Camera(Point p)
  {
    this(p.x, p.y);
  }

  public void setLocation(int x, int y)
  {
    location.setLocation(x,y);
    updateViewBounds();
  }

  public void setHeight(int height)
  {
    /* ensure height is within bounds */
    if (height > MAX_HEIGHT) height = MAX_HEIGHT;
    else if (height < 0) height = 0;
    this.height = height;
    updateViewBounds();
  }

  private void updateViewBounds()
  {
    int scale = 1 << height;
    int width = VIEW_W * scale;
    int height = VIEW_H * scale;
    int x = location.x - width/2;
    int y = location.y - height/2;

    viewBounds.setFrame(x, y, width, height);
  }

  public void translate(int dx, int dy)
  {
    setLocation(location.x + dx, location.y + dy);
  }

  public void translateRelativeToHeight(int dx, int dy)
  {
    translate(dx * (1<<height), dy * (1<<height));
  }

  public void zoomIn(int zoomDiff)
  {
    setHeight(height - zoomDiff);
  }

  public void zoomOut(int zoomDiff)
  {
    setHeight(height + zoomDiff);
  }

  public AffineTransform getTransform()
  {
    AffineTransform at = new AffineTransform();
    int scale = 1<<height;
    at.translate(viewBounds.getX(), viewBounds.getY());
    at.scale(scale, scale);
    return at;
  }


  public int getHeight()
  {
    return height;
  }

  public Rectangle2D getViewBounds()
  {
    return viewBounds;
  }

  @Override
  public String toString()
  {
    return "Camera{" +
            "location=" + location +
            ", viewBounds=" + viewBounds +
            ", height=" + height +
            '}';
  }
}