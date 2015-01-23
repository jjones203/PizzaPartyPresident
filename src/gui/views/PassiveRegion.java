package gui.views;

import gui.ColorSchemes;

import java.awt.*;

/**
 * Created by winston on 1/23/15.
 * Phase_01
 * CS 351 spring 2015
 */
public class PassiveRegion implements RegionView
{
  @Override
  public void draw(Graphics g, GUIRegion gRegion)
  {
    Polygon poly = gRegion.getPoly();

    g.setColor(ColorSchemes.PASSIVE_REGION);
    g.fillPolygon(poly);

    g.setColor(ColorSchemes.PASSIVE_REGION_OUTLINE);
    g.drawPolygon(poly);
  }
}
