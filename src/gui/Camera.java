package gui;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

/**
 * @author david
 *         created: 2015-01-21
 *         <p/>
 *         description:
 */


public class Camera extends MouseAdapter /* for now */
{

  private static final double ASPECT_RATIO = 2d;
  private static final double HEIGHT_DEFAULT = 1000; /* arbitrary */

  /* 3D location: spherical coords */
  private double height; /* in m */
  private double lon; /* decimal degrees */
  private double lat; /* decimal degrees */

  /* defines visible rectangle in model-space, for polling from data */
  private Rectangle2D.Double viewBounds;

  /* buffer to draw to and/or return for rendering */
  private BufferedImage view;

  public Camera()
  {
    viewBounds = new Rectangle2D.Double();
    moveTo(0,0, HEIGHT_DEFAULT);
  }

  public void moveTo(Point2D p)
  {
    moveTo(p.getX(), p.getY(), height);
  }

  public void moveTo(double lon, double lat, double height)
  {
    this.lon = lon;
    this.lat = lat;
    this.height = height;
    updateViewBounds();
  }

  private void updateViewBounds()
  {

  }

  public void translate(double dLon, double dLat)
  {
    translate(dLon, dLat, 0);
  }

  private void translate(double dLon, double dLat, double i)
  {
    moveTo(lon + dLon, lat + dLat, height);
  }


  public void renderView(Graphics2D g2)
  {
    /* get regions in viewBounds */


  }


}
