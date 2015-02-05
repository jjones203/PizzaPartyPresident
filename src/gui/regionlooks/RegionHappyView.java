package gui.regionlooks;

import gui.ColorsAndFonts;
import gui.GUIRegion;

import java.awt.*;

import static model.RegionAttributes.PLANTING_ATTRIBUTES.HAPPINESS;

/**
 * Created by winston on 1/27/15.
 * Phase_01
 * CS 351 spring 2015
 *
 * Represents the happiness index of the regions.
 *
 *
 */
class RegionHappyView implements RegionView
{
  /**
   * Method extracts happiness index from region and displays it.
   * @param g
   * @param gRegion
   */
  @Override
  public void draw(Graphics g, GUIRegion gRegion)
  {
    if (gRegion == null || gRegion.getRegion().getAttributes() == null)
    {
      System.err.println("(!)GUIREGION or attribut set is null!");
      return;
    }

    double happinessLevel = gRegion.getRegion()
                                   .getAttributes()
                                   .getAttribute(HAPPINESS);

    // rough scaling factor => ColorSpace/ValueSpace
    double scalingFactor = 12.75;
    happinessLevel = happinessLevel * scalingFactor;

    Color color;

    if (gRegion.isActive())
    {
      color = ColorsAndFonts.ACTIVE_REGION;
    }
    else
    {
      color = new Color((int) happinessLevel, (int)happinessLevel, 0);
    }

    g.setColor(color);
    g.fillPolygon(gRegion.getPoly());

    g.setColor(ColorsAndFonts.PASSIVE_REGION_OUTLINE);
    g.drawPolygon(gRegion.getPoly());

  }
}
