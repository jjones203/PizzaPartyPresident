package worldfoodgame.gui.regionlooks;

import worldfoodgame.gui.ColorsAndFonts;
import worldfoodgame.gui.GUIRegion;
import worldfoodgame.model.World;

import java.awt.*;

/**
 * Created by winston on 4/4/15.
 */
class MortalityRate implements RegionView
{
  private static float CEILING_RATE = 20f;
  @Override
  public void draw(Graphics g, GUIRegion gRegion)
  {
    double mortalityRate = gRegion
      .getCountry()
      .getMortalityRate(World.getWorld().getCurrentYear());

    float visRatio = (float) (mortalityRate / CEILING_RATE);

    if (visRatio > 1) visRatio = 1; // to keep things in color bounds.

    Color fillCol = Color.getHSBColor(0f, .9f, 1 - visRatio);

    if (gRegion.isActive()) fillCol = fillCol.darker();

    g.setColor(fillCol);
    g.fillPolygon(gRegion.getPoly());

    g.setColor(ColorsAndFonts.ACTIVE_REGION_OUTLINE);
    g.drawPolygon(gRegion.getPoly());

  }
}
