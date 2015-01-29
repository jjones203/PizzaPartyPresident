package gui.views;

import java.awt.*;

import static model.RegionAttributes.PLANTING_ATTRIBUTES.HAPPINESS;

/**
 * Created by winston on 1/27/15.
 * Phase_01
 * CS 351 spring 2015
 */
public class RegionHappyView implements RegionView
{
  RegionView view;

  public RegionHappyView(RegionView view)
  {
    this.view = view;
  }

  @Override
  public void draw(Graphics g, GUIRegion gRegion)
  {
    //todo this is for testing
//    view.draw(g, gRegion); // ignores base view
    double happinessLevel = gRegion.getRegion()
                                   .getAttributes()
                                   .getAttribute(HAPPINESS);

    happinessLevel = happinessLevel * 12.75; // rough scaling factor testing only magic number!

    Color happyCollor = new Color((int) happinessLevel, (int)happinessLevel, 0);

    g.setColor(happyCollor);
    g.fillPolygon(gRegion.getPoly());

  }
}
