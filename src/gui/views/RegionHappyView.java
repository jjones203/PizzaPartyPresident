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
    double happinessLevel = gRegion.getRegion()
        .getAttributes()
        .getAttribute(HAPPINESS);

    Color happyCollor = new Color(
        (float) happinessLevel,
        (float) happinessLevel,
        0.0f);

    g.setColor(happyCollor);
    g.drawPolygon(gRegion.getPoly());

    view.draw(g, gRegion);
  }
}
