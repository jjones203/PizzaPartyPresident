package gui.regionlooks;

import gui.ColorsAndFonts;
import gui.GUIRegion;

import java.awt.*;

/**
 * Created by winston on 1/27/15.
 * Phase_01
 * CS 351 spring 2015
 */
public class RegionNameDraw
{
  public static void draw(Graphics g, GUIRegion gRegion)
  {
    Graphics2D g2d = (Graphics2D) g;

    g2d.setRenderingHint(
      RenderingHints.KEY_TEXT_ANTIALIASING,
      RenderingHints.VALUE_TEXT_ANTIALIAS_ON
    );

    g2d.setFont(ColorsAndFonts.NAME_VIEW);
    g2d.setColor(ColorsAndFonts.REGION_NAME_FONT_C);

    g2d.drawString(gRegion.getName(),
        (int)gRegion.getPoly().getBounds().getCenterX(),
        (int)gRegion.getPoly().getBounds().getCenterY());
  }
}
