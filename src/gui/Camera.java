package gui;


import gui.displayconverters.MapConverter;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;

/**
 
 @author david
 
 description:
  Camera class describes an object that moves relative to a map or world projection.
  It's primary use is in building an AffineTransform that takes everything within
  its view in map-space (see viewBounds rect) and translate it to screen space
  for viewing in some Window-type object.
  It provides an interface for motion and zooming both in terms of the projection
  and in terms of the window it is translating to.
 */

public class Camera
{
  private static final double ASPECT_RATIO = 2;
  private static final double BASE_W = 1_000;
  private static final double BASE_H = BASE_W / ASPECT_RATIO;
  
  final double MIN_HEIGHT = 0;

  /* determined by the scale of the Converter used to instantiate the Camera */
  final double MAX_HEIGHT;
  
  private Rectangle2D viewBounds;
  private Rectangle2D limitingRect;
  private double height;
  private double scale;

  /**
   Instantiate this Camera with a MapConverter upon which it will base its
   maximum height (according to the scale of the converter)
   @param converter MapConverter used to set height limits
   */
  public Camera(MapConverter converter)
  {
    this(0, 0, converter);
  }

  /**
   Instantiate this camera with an initial position in map-space and a converter
   to use to define the camera's height limits
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
   The units of the zoom are relative to the converter used to instantiate
   the camera.  The differential should be small enough that transforms
   produced by the camera are not jarring.
   @param zoomDiff amount to zoom in
   */
  public void zoomIn(double zoomDiff)
  {
    double centerY = viewBounds.getCenterY();
    double centerX = viewBounds.getCenterX();
    zoomAbsolute(-zoomDiff, centerX, centerY);
  }


  /**
   zoom out by a given amount.  This raises the "height" of the camera.
   The units of the zoom are relative to the converter used to instantiate
   the camera.  The differential should be small enough that transforms
   produced by the camera are not jarring.
   @param zoomDiff amount to zoom out
   */
  public void zoomOut(double zoomDiff)
  {
    double centerY = viewBounds.getCenterY();
    double centerX = viewBounds.getCenterX();
    zoomAbsolute(zoomDiff, centerX, centerY);
  }

  
  /**
   Adjusts the camera height (depending on sign of dZoom) and sets its
   viewbounds such that the anchor coordinates provided remain in the same
   position relative to the viewbounds
   @param dZoom     zoom differential
   @param anchorX   x coord to anchor camera at
   @param anchorY   y coord to anchor camera at
   */
  public void zoomAbsolute(double dZoom, double anchorX, double anchorY)
  {
    /* this zoom diff calculation is important: allows for proper scaling of the
     distance ratios from the anchor point*/
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

    /* viewBounds are in scaled terms so shift is also in scaled terms:
     * hence translate *after* scale  */
    at.scale(1 / scale, 1 / scale);
    at.translate(shiftX, shiftY);

    return at;
  }


  /**
   set Height of the camera.  This should be used both externally and internally
   as it guarantees the height bounds are honored
   @param height  height to set camera to
   */
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

  /**
   @return the Rectangle that defines the visible area the Camera is scaling to,
   in map-space
   */
  public Rectangle2D getViewBounds()
  {
    return viewBounds;
  }


  /**
   @return Meaningful String representation of the Camera
   (ViewBounds info, height)
   */
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
   @return The Dimension to which this camera is scaling
   */
  public Dimension getTargetSize()
  {
    return new Dimension((int)BASE_W, (int)BASE_H);
  }
  
  
  /**
    @return the CAM_DISTANCE enum based on the height or zoom of this camera
   */
  public CAM_DISTANCE getDistance()
  {
    if (height < 7) return CAM_DISTANCE.CLOSE_UP;
    if (height < 8) return CAM_DISTANCE.MEDIUM;
    return CAM_DISTANCE.LONG;
  }


  /**
   Enum binning the Camera's distance from the map (i.e. height) for
   less numerical dependence in external classes
   */
  public enum CAM_DISTANCE
  {
    CLOSE_UP, MEDIUM, LONG
  }
}