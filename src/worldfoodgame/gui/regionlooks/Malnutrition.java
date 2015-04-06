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
  private static float HUE = .0555f;
  private static float LUMINOSITY = .7f;
  private static float SCALE = 2.5f;
  private static float OFFSET = 0.01f;

  @Override
  public void draw(Graphics g, GUIRegion gRegion)
  {

    double malNur = gRegion
      .getCountry()
      .getUndernourished(World.getWorld().getCurrentYear());


    float nurDx = (float) malNur * SCALE;

    if (nurDx > 1) nurDx = 1;

    Color color = Color.getHSBColor(HUE, nurDx, LUMINOSITY - (nurDx/50));

    if (gRegion.isActive())
    {
      color = color.brighter();
    }

    g.setColor(color);
    g.fillPolygon(gRegion.getPoly());

    g.setColor(ColorsAndFonts.PASSIVE_REGION_OUTLINE);
    g.drawPolygon(gRegion.getPoly());
  }
}
