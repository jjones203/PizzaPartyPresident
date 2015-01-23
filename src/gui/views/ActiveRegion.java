package gui.views;

import gui.ColorScheme;

import java.awt.*;

/**
 * Created by winston on 1/23/15.
 * Phase_01
 * class representing the look of a Active or selected region.
 * CS 351 spring 2015
 */
public class ActiveRegion implements RegionView
{
  @Override
  public void draw(Graphics g, GUIRegion gRegion)
  {
    Polygon poly = gRegion.getPoly();
    g.setColor(ColorScheme.ACTIVE_REGION);
    g.fillPolygon(poly);

    g.setColor(ColorScheme.ACTIVE_REGION_OUTLINE);
    g.drawPolygon(poly);
  }
}
