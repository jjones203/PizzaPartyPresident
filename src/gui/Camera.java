package gui;


import com.sun.prism.shader.DrawRoundRect_RadialGradient_PAD_Loader;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

/**

 */

public class Camera
{
  static final double MID_HEIGHT = 10;
  static final double MAX_HEIGHT = 12;
  static final double MIN_HEIGHT = 4;
  private static final double ASPECT_RATIO = 2;
  private static final double BASE_W = 1000;
  private static final double BASE_H = BASE_W /ASPECT_RATIO;
  private static final Font DBG_FONT = new Font("Courier", Font.PLAIN, 14);
  double maxHeight;
  double minHeight;
  
  double baseW;
  double baseH;
  private MapConverter converter;
  private Rectangle2D viewBounds;
  private Rectangle2D limitingRect;
  private double height;
  private double scale;
  
  
  public Camera(MapConverter converter)
  {
    this(0, 0, converter);
  }

  public Camera(double x, double y, MapConverter converter)
  {
    this(x, y, MIN_HEIGHT, converter);
  }

  public Camera(double x, double y, double initialHeight, MapConverter converter)
  {
    setHeight(initialHeight);
    setConverter(converter);
    viewBounds = new Rectangle2D.Double();
    setViewBounds(x, y, scale*BASE_W, scale*BASE_H);
    System.out.println(limitingRect);
  }


  public void centerAbsolute(double x, double y)
  {
    System.out.println("centering on (" + x + "," + y + ")");
    double w = viewBounds.getWidth();
    double h = viewBounds.getHeight();
    x = x - w/2;
    y = y - h/2;
    setViewBounds(x, y, w, h);
  }

  private void setViewBounds(double x, double y, double w, double h)
  {
    if(w > limitingRect.getWidth()) w = limitingRect.getWidth();
    if(h > limitingRect.getHeight()) h = limitingRect.getHeight();
    
    if(x + w > limitingRect.getMaxX()) x = limitingRect.getMaxX() - w;
    else if(x < limitingRect.getMinX()) x = limitingRect.getMinX();

    if(y + h > limitingRect.getMaxY()) y = limitingRect.getMaxY() - h;
    else if(y < limitingRect.getMinY()) y = limitingRect.getMinY();
    
    viewBounds.setFrame(x, y, w, h);
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
    setViewBounds(newX, newY, width, height);
  }

  public void translateRelativeToView(int dx, int dy)
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
    double oldScale = scale;
    setHeight(height + dZoom);
    double actualDZoom = height - oldH;
    double dScale = scale - oldScale;

    double oldX = viewBounds.getX();
    double oldY = viewBounds.getY();

    double newX = anchorX - Math.pow(2, actualDZoom) * (anchorX - oldX);
    double newY = anchorY - Math.pow(2, actualDZoom) * (anchorY - oldY);

    double newH = scale * BASE_H;
    double newW = scale * BASE_W;

    setViewBounds(newX, newY, newW, newH);
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

  /* maybe should be private.  Can't modify converter after instantiation */
  public void setConverter(MapConverter converter)
  {
    this.converter = converter;
    double minX = converter.lonToX(-180);
    double minY = converter.latToY(90);
    double maxX = converter.lonToX(180);
    double maxY = converter.latToY(-90);

    double maxW = maxX - minX;
    double maxH = maxY - minY;
    
    
    maxHeight = Math.log(maxW/BASE_W)/Math.log(2); /*TODO: Fix this wrongness */

    limitingRect = new Rectangle2D.Double(minX, minY, maxW, maxH);
  }

  public int getHeight()
  {
    return (int)height; /* TODO: resolve FP v INT dilemma */
  }

  public void setHeight(double height)
  {
    if(height < MIN_HEIGHT) height = MIN_HEIGHT;
    else if(height > maxHeight) height = maxHeight;
    this.height = height;

    scale = Math.pow(2, height);
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

    String topleft_str = String.format("top left: (%.2f,%.2f)",
            viewBounds.getX(), viewBounds.getY());
    String dimension_str = String.format("size: (%.2f,%.2f)",
            viewBounds.getWidth(), viewBounds.getHeight());
    String center_str = String.format("center: (%.2f,%.2f)",
            viewBounds.getCenterX(), viewBounds.getCenterY());
    String height_str = String.format("height: %.2f", height);
    String scale_str = String.format("scaling factor: %.2f", scale);


    g.setColor(Color.RED);
    g.setFont(DBG_FONT);
    
    g.drawString("Camera Debug Info", 15,20);
    g.drawString(topleft_str, 15, 40);
    g.drawString(center_str, 15, 60);
    g.drawString(dimension_str, 15, 80);
    g.drawString(height_str, 15, 100);
    g.drawString(scale_str, 15, 120);

    return img;
  }

  public CAM_DISTANCE getDistance()
  {
    if(height < 6) return CAM_DISTANCE.CLOSE_UP;
    if(height < 10) return CAM_DISTANCE.MEDIUM;
    return CAM_DISTANCE.LONG;
  }

  public Rectangle2D getLims()
  {
    return limitingRect;
  }
  
  public enum CAM_DISTANCE
  { 
    CLOSE_UP, MEDIUM, LONG
  }
}