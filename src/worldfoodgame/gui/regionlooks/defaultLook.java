package worldfoodgame.gui.regionlooks;

import worldfoodgame.gui.ColorsAndFonts;
import worldfoodgame.gui.GUIRegion;

import java.awt.*;

/**
 * Created by winston on 1/23/15.
 * Phase_01
 * CS 351 spring 2015
 *
 * Class representing the look of a Active or selected region. With no
 * additional styling information.
 *
 * Depends on the ColorAndFontClass.
 */
class DefaultLook implements RegionView
{
  private boolean drawBorders;

  /**
   Draw a GUIRegion to a given graphics context
   @param g   Graphics context to draw to
   @param gRegion   GUIRegion to draw
   */
  @Override
  public void draw(Graphics g, GUIRegion gRegion)
  {
    Polygon poly = gRegion.getPoly();
    //Color outLine;
    Color fill;

    if (gRegion.getGUIContinent().isActive())
    {
      //outLine = ColorsAndFonts.ACTIVE_REGION_OUTLINE;
      //fill = ColorsAndFonts.ACTIVE_REGION;
      fill = gRegion.getCountry().getContinent().getColor();
    }
    else
    {
      //outLine = ColorsAndFonts.PASSIVE_REGION_OUTLINE;
      //fill = ColorsAndFonts.PASSIVE_REGION;
      fill = gRegion.getCountry().getContinent().getColor().darker();
    }

    g.setColor(fill);
    g.fillPolygon(poly);

    g.setColor(fill);
    g.drawPolygon(poly);
  }
}
