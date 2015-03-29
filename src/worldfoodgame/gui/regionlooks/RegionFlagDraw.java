package worldfoodgame.gui.regionlooks;

import worldfoodgame.gui.FlagLoader;
import worldfoodgame.gui.GUIRegion;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.image.BufferedImage;

/**
 * Created by winston on 3/29/15.
 */
public class RegionFlagDraw
{
  /**
   * Draws the flag of the specified country.
   * @param g
   * @param gRegion
   */
  public static void draw(Graphics g, GUIRegion gRegion)
  {

    Graphics2D g2d = (Graphics2D) g;

    g2d.setRenderingHint(
      RenderingHints.KEY_TEXT_ANTIALIASING,
      RenderingHints.VALUE_TEXT_ANTIALIAS_ON
    );

    /* save and temporarily reset the graphics transform */
    AffineTransform at = g2d.getTransform();
    g2d.setTransform(new AffineTransform());

    /* font in terms of screen-space */

    /* find ~center of region in map-space */
    int x = (int) gRegion.getPoly().getBounds().getCenterX();
    int y = (int) gRegion.getPoly().getBounds().getCenterY();


    Point src = new Point(x, y);
    Point dst = new Point();

    try
    {
      /* convert center point to screen space for drawing */
      AffineTransform atINV = new AffineTransform(at);
      atINV.invert();
      at.transform(src, dst);
    }
    catch (NoninvertibleTransformException e)
    { /* should never happen: all map transforms should be invertible */
      e.printStackTrace();
    }

    BufferedImage flag = FlagLoader.getFlagLoader()
      .getFlag(gRegion.getRegion().getCountry().getName());


    if (flag != null)
    {
      // magic numbers are padding to position the flag
      int imgX = dst.x - flag.getWidth() - 3;
      int imgY = dst.y - flag.getHeight() + 3;
      g2d.drawImage(flag, imgX, imgY, null);
    }
    else
    {
      System.err.println("could not load flag: " + gRegion.getRegion().getName());
    }

    /* reset the transform for any RegionViews depending on it for proper rendering */
    g2d.setTransform(at);
  }
}
