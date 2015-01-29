package gui;


import java.awt.*;
import java.awt.font.LineMetrics;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.text.AttributedCharacterIterator;

/**

 */

public class Camera
{
  private static final double ASPECT_RATIO = 2;

  /*arbitrary*/
  static final double MID_HEIGHT = 10;
  static final double MAX_HEIGHT = 12;
  static final double MIN_HEIGHT = 4;

  private static final double BASE_W = 1000;
  private static final double BASE_H = BASE_W /ASPECT_RATIO;

  private Rectangle2D viewBounds;
  private double height;
  private double scale;

  private static final Font DBG_FONT = new Font("Courier", Font.PLAIN, 14);

  public Camera()
  {
    this(0,0);
  }

  public Camera(double x, double y)
  {
    this(x, y, MIN_HEIGHT);
  }

  public Camera(double x, double y, double initialHeight)
  {
    setHeight(initialHeight);

    viewBounds = new Rectangle2D.Double(x, y, scale*BASE_W, scale*BASE_H);
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
    double oldH = height;
    setHeight(height + dZoom);
    double actualDZoom = height - oldH;

    double oldX = viewBounds.getX();
    double oldY = viewBounds.getY();

    double newX = anchorX - Math.pow(2, actualDZoom) * (anchorX - oldX);
    double newY = anchorY - Math.pow(2, actualDZoom) * (anchorY - oldY);

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
    String s = String.format("ViewBounds:" +
                    "%n\ttop left: (%f, %f)" +
                    "%n\tcenter: (%f, %f)" +
                    "%n\theight: %f" +
                    "%n\twidth: %f" +
                    "%nCamera height: %f",
            viewBounds.getX(), viewBounds.getX(),
            viewBounds.getCenterX(), viewBounds.getCenterY(),
            viewBounds.getHeight(), viewBounds.getWidth(),
            height);

    return s;
  }

  public BufferedImage getDBGimg()
  {
    BufferedImage img = new BufferedImage(400, 200, BufferedImage.TYPE_4BYTE_ABGR);
    Graphics2D g = (Graphics2D) img.getGraphics();

    String topleft_str = String.format("top left: (%.4f,%.4f)",
            viewBounds.getX(), viewBounds.getY());
    String dimension_str = String.format("size: (%.4f,%.4f)",
            viewBounds.getWidth(), viewBounds.getHeight());
    String center_str = String.format("center: (%.4f,%.4f)",
            viewBounds.getCenterX(), viewBounds.getCenterY());


    g.setColor(Color.RED);
    g.setFont(DBG_FONT);
    g.drawString("Camera Debug Info", 15,20);
    g.drawString(topleft_str, 15, 40);
    g.drawString(center_str, 15, 60);
    g.drawString(dimension_str, 15, 80);

    return img;
  }

  public void setHeight(double height)
  {
    if(height < MIN_HEIGHT) height = MIN_HEIGHT;
    else if(height > MAX_HEIGHT) height = MAX_HEIGHT;
    this.height = height;

    scale = Math.pow(2, height);
  }
}