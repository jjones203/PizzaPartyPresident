package worldfoodgame.gui.regionlooks;

import worldfoodgame.gui.GUIRegion;

import java.awt.*;

/**
 * Created by winston on 4/4/15.
 */
public class OverlayOutLine
{
  public static void draw(Graphics g, GUIRegion gRegion)
  {
    g.setColor(Color.yellow);
    g.drawPolygon(gRegion.getPoly());
  }
}
