package worldfoodgame.gui.regionlooks;

import worldfoodgame.gui.ColorsAndFonts;
import worldfoodgame.gui.GUIRegion;
import worldfoodgame.model.World;

import java.awt.*;

/**
 * Visualizes world wide Malnutrition.
 * Created by winston on 4/5/15.
 */
 class Malnutrition implements RegionView
{
  private float HUE = .0555f;
  @Override
  public void draw(Graphics g, GUIRegion gRegion)
  {

    double malNur = gRegion
      .getCountry()
      .getUndernourished(World.getWorld().getCurrentYear());


    float nurDx = (float) malNur * 2.5f;

    if (nurDx > 1)
    {
      nurDx = 1;
    }

    Color color = Color.getHSBColor(HUE, .7f, nurDx);
    g.setColor(color);
    g.fillPolygon(gRegion.getPoly());

    g.setColor(ColorsAndFonts.PASSIVE_REGION_OUTLINE);
    g.drawPolygon(gRegion.getPoly());
  }
}
