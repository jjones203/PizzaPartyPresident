package gui.regionlooks;

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
   * Method extracts the relevant
   * @param g
   * @param gRegion
   */
  @Override
  public void draw(Graphics g, GUIRegion gRegion)
  {
    double happinessLevel = gRegion.getRegion()
                                   .getAttributes()
                                   .getAttribute(HAPPINESS);

    // rough scaling factor => ColorSpace/ValueSpace
    double scalingFactor = 12.75;
    happinessLevel = happinessLevel * scalingFactor;

    Color happyCollor = new Color((int) happinessLevel, (int)happinessLevel, 0);

    g.setColor(happyCollor);
    g.fillPolygon(gRegion.getPoly());

  }
}
