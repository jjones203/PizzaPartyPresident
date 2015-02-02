package gui.regionlooks;

import gui.ColorsAndFonts;
import gui.GUIRegion;

import java.awt.*;

/**
 * Created by winston on 1/23/15.
 * Phase_01
 * class representing the look of a Active or selected region.
 * CS 351 spring 2015
 */
class defaultLook implements RegionView
{
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
