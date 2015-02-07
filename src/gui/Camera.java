package gui;


import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

/**

 */

public class Camera
{
  private static final double ASPECT_RATIO = 2;
  private static final double BASE_W = 1200;
  private static final double BASE_H = BASE_W / ASPECT_RATIO;
  
  private static final Font DBG_FONT = new Font("Courier", Font.PLAIN, 14);
  
  final double MIN_HEIGHT = 4;
  final double MAX_HEIGHT;

  private Rectangle2D viewBounds;
  private Rectangle2D limitingRect;
  private double height;
  private double scale;
  private boolean hasMoved;


  public Camera(MapConverter converter)
  {
    this(0, 0, converter);
  }

  /**
   Wraps base constructor setting camera to a Maximum height by default
   Location and MapConverter to use can be specified

   @param x
   x coord of initial location
   @param y
   y coord of initial location
   @param converter
   MapConverter to use for scaling rules
   */
  public Camera(double x, double y, MapConverter converter)
  {
    double minX = converter.lonToX(-180);
    double minY = converter.latToY(90);
    double maxX = converter.lonToX(180);
    double maxY = converter.latToY(-90);

    double maxW = maxX - minX;
    double maxH = maxY - minY;
    
    MAX_HEIGHT = Math.log(maxW/BASE_W)/Math.log(2);
    limitingRect = new Rectangle2D.Double(minX, minY, maxW, maxH);
    
    setHeight(MAX_HEIGHT);
    viewBounds = new Rectangle2D.Double();
    setViewBounds(x, y, scale * BASE_W, scale * BASE_H);
    
  }


  /**
   Center the camera on given x and y coordinates in absolute MapSpace
   If the center would cause the Camera to move past the limits of the map,
   it will be positioned flush against the boundaries prevent such motion

   @param x
   x coord to center on
   @param y
   y coord to center on
   */
  public void centerAbsolute(double x, double y)
  {
    double w = viewBounds.getWidth();
    double h = viewBounds.getHeight();

    x = x - w / 2;
    y = y - h / 2;
    setViewBounds(x, y, w, h);
  }

  /**
   Method wraps any repositioning or resizing of the viewBounds rectangle. If
   the viewBounds would exceed its limiting rectangle in any direction, it is
   positioned flush against the edge(s) it would have crossed int the
   repositioning
   
   @param x
   new x coord of viewBounds
   @param y
   new y coord of viewBounds
   @param w
   new width of viewBounds
   @param h
   new height of viewBounds
   */
  private void setViewBounds(double x, double y, double w, double h)
  {

    if (w > limitingRect.getWidth()) w = limitingRect.getWidth();
    if (h > limitingRect.getHeight()) h = limitingRect.getHeight();

    if (x + w > limitingRect.getMaxX())
    {
      x = limitingRect.getMaxX() - w;
    }
    else if (x < limitingRect.getMinX()) x = limitingRect.getMinX();

    if (y + h > limitingRect.getMaxY())
    {
      y = limitingRect.getMaxY() - h;
    }
    else if (y < limitingRect.getMinY()) y = limitingRect.getMinY();

    viewBounds.setFrame(x, y, w, h);
  }

  
  /**
   Translate the camera, in absolute terms, a given differential in x and y
   
   @param dx
   difference in x to move 
   @param dy
   difference in y to move
   */
  public void translateAbsolute(double dx, double dy)
  {
    double newX = viewBounds.getX() + dx;
    double newY = viewBounds.getY() + dy;
    double width = viewBounds.getWidth();
    double height = viewBounds.getHeight();
    setViewBounds(newX, newY, width, height);
  }

  /**
   Translate the camera in terms relative to the screen it is produces transforms
   for.  Scaling is handled automatically
   * @param dx
   difference in x to move, in DisplaySpace
   @param dy
 difference in y to move, in DisplaySpace

   */
  public void translateRelativeToView(double dx, double dy)
  {
    translateAbsolute(dx * scale, dy * scale);
  }


  /**
   zoom in by a given amount.  This lowers the "height" of the camera.
   The units are semi-arbitrary.  They are a function of the scaling factor
   in the MapConverter associated with this Camera
   
   @param zoomDiff
   amount to zoom it
   */
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

  /**
   Moves the camera closer to the map, with an anchor point in the absolute
   coordinate system of the map
   
   @param dZoom
   @param anchorX
   @param anchorY
   */
  public void zoomAbsolute(double dZoom, double anchorX, double anchorY)
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

    setViewBounds(newX, newY, newW, newH);
  }


  /**
    Moves the camera closer to the map, with an anchor point relative to the 
    screen's coordinate system
   
   @param dZoom     difference in the zoom or height of the camera
   @param anchorX   x coord of the anchor point
   @param anchorY   y coord of the anchor point 
   */
  private void zoomRelativeToView(double dZoom, double anchorX, double anchorY)
  {
    zoomAbsolute(dZoom, anchorX / scale, anchorY / scale);
  }

  
  /**
   @return the AffineTransform that transforms the view of the map to the screen
      based on this Camera's location
   */
  public AffineTransform getTransform()
  {
    AffineTransform at = new AffineTransform();
    
    double shiftX = -viewBounds.getX();
    double shiftY = -viewBounds.getY();

    /* viewBounds are scaled, shift is in scaled terms */
    at.scale(1 / scale, 1 / scale);
    at.translate(shiftX, shiftY);

    return at;
  }

  
  public void setHeight(double height)
  {
    if (height < MIN_HEIGHT)
    {
      height = MIN_HEIGHT;
    }
    else if (height > MAX_HEIGHT){
      height = MAX_HEIGHT;
      
    }
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


  /**
   @return a BufferedImage containing debug info for the camera  
   */
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

    g.drawString("Camera Debug Info", 15, 20);
    g.drawString(topleft_str, 15, 40);
    g.drawString(center_str, 15, 60);
    g.drawString(dimension_str, 15, 80);
    g.drawString(height_str, 15, 100);
    g.drawString(scale_str, 15, 120);

    return img;
  }
  
  /**
    @return the CAM_DISTANCE enum based on the height or zoom of this camera
   */
  public CAM_DISTANCE getDistance()
  {
    if (height < 6) return CAM_DISTANCE.CLOSE_UP;
    if (height < 8) return CAM_DISTANCE.MEDIUM;
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