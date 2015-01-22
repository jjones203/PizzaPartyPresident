package gui;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * @author david
 *         created: 2015-01-21
 *         <p/>
 *         description:
 *          Infopane is the superclass of all information panels that may be
 *          seen on the GameCanvas
 *
 *
 *
 */


/*
 * I could see this going down a silly road of self-developed layout-managery
 * type classes to handle pane positioning, if not static.
 */
public abstract class Infopane
{
  private Dimension size;

  private Point loc; /* within the GameCanvas */

  /* may not need a class variable here.
   * Could instantiate and blit a new img with each render */
  private BufferedImage imgBuf;


  public Infopane(int x, int y, int w, int h)
  {
  }

  public abstract void render(Graphics2D g2);

}
