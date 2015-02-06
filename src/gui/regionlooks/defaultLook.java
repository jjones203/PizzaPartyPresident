package gui.regionlooks;

import gui.ColorsAndFonts;
import gui.GUIRegion;

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
class defaultLook implements RegionView
{
  private boolean drawBorders;



  @Override
  public void draw(Graphics g, GUIRegion gRegion)
  {
    if (gRegion.isActive())
    {
      Polygon poly = gRegion.getPoly();

      g.setColor(ColorsAndFonts.ACTIVE_REGION);
      g.fillPolygon(poly);

      g.setColor(ColorsAndFonts.ACTIVE_REGION_OUTLINE);
      g.drawPolygon(poly);
    }
    else
    {
      Polygon poly = gRegion.getPoly();

      g.setColor(ColorsAndFonts.PASSIVE_REGION);
      g.fillPolygon(poly);

      g.setColor(ColorsAndFonts.PASSIVE_REGION_OUTLINE);
      g.drawPolygon(poly);
    }
  }
}
