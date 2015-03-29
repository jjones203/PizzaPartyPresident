package worldfoodgame.gui.regionlooks;

import worldfoodgame.gui.ColorsAndFonts;
import worldfoodgame.gui.GUIRegion;

import java.awt.*;

/**
 * Created by winston on 1/27/15.
 * Phase_01
 * CS 351 spring 2015
 * <p/>
 * Represents the happiness index of the regions.
 */
class RegionHappyView implements RegionView
{
  /**
   * Method extracts happiness index from region and displays it.
   *
   * @param g
   * @param gRegion
   */
  @Override
  public void draw(Graphics g, GUIRegion gRegion)
  {
    Color color;
    if (gRegion.isActive())
    {
      color = ColorsAndFonts.ACTIVE_REGION;
    }
    else
    {
      int yearOfInterest = 2014; // todo find solution to this!
      double unhappyPeople = gRegion.getRegion().getCountry().getUnhappyPeople(yearOfInterest);
      double totalPopulation = gRegion.getRegion().getCountry().getPopulation(yearOfInterest);

      double ratio = 1.0 - (unhappyPeople / totalPopulation);

      color = new Color((float)ratio, (float)ratio, 0.0f);
    }

    g.setColor(color);
    g.fillPolygon(gRegion.getPoly());

    g.setColor(ColorsAndFonts.PASSIVE_REGION_OUTLINE);
    g.drawPolygon(gRegion.getPoly());
  }
}
