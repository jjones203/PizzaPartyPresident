package gui.views;

import java.awt.*;

/**
 * Created by winston on 1/27/15.
 * Phase_01
 * CS 351 spring 2015
 */
public class RegionNameView implements RegionView
{
  private RegionView view;
  public RegionNameView(RegionView view)
  {
    this.view = view;
  }

  @Override
  public void draw(Graphics g, GUIRegion gRegion)
  {

    Graphics2D g2d = (Graphics2D) g;
    g2d.drawString(gRegion.getName(),
        gRegion.getPoly().getBounds().x,
        gRegion.getPoly().getBounds().y
        );

    //TODO this might need work.

    view.draw(g, gRegion);
  }
}
