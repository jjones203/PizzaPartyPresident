package gui.regionlooks;

import gui.ColorSchemes;
import gui.GUIRegion;

import java.awt.*;

/**
 * Created by winston on 1/27/15.
 * Phase_01
 * CS 351 spring 2015
 */
class RegionNameView implements RegionView
{
//  private static final Font FONT = new Font("TimesRoman", Font.PLAIN, 1000);
  private RegionView view;
  private int fontSize;
  public RegionNameView(RegionView view, int fontSize)
  {
    this.view = view;
    this.fontSize = fontSize;
  }

  @Override
  public void draw(Graphics g, GUIRegion gRegion)
  {
    view.draw(g, gRegion);

    Graphics2D g2d = (Graphics2D) g;
    g2d.setFont(new Font("TimesRoman", Font.PLAIN, fontSize));
    g2d.setColor(ColorSchemes.REGION_NAME_FONT_C);

    g2d.drawString(gRegion.getName(),
        (int)gRegion.getPoly().getBounds().getCenterX(),
        (int)gRegion.getPoly().getBounds().getCenterY()
        );
  }
}
