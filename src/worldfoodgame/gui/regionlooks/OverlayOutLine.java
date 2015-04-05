package worldfoodgame.gui.regionlooks;

import worldfoodgame.gui.GUIRegion;

import java.awt.*;

/**
 * Created by winston on 4/4/15.
 *
 * Used to draw an outline of the region over a raster display.
 */
public class OverlayOutline
{
  private static Color outline = new Color(217, 208, 0);
  private static Color fill = new Color(255, 255, 255, 70);
  public static void draw(Graphics g, GUIRegion gRegion)
  {
    g.setColor(outline);
    g.drawPolygon(gRegion.getPoly());

    g.setColor(fill);
    g.fillPolygon(gRegion.getPoly());
  }
}
