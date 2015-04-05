package worldfoodgame.gui.regionlooks;

import worldfoodgame.gui.ColorsAndFonts;
import worldfoodgame.gui.GUIRegion;

import java.awt.*;

/**
 * Created by winston on 3/21/15.
 */
class BackGroundLook implements RegionView
{
  @Override
  public void draw(Graphics g, GUIRegion gRegion)
  {
    g.setColor(ColorsAndFonts.BACKGROUD_TEST_COLOR);
    g.fillPolygon(gRegion.getPoly());
  }
}
