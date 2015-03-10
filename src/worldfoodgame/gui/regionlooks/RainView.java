package worldfoodgame.gui.regionlooks;

import worldfoodgame.gui.ColorsAndFonts;
import worldfoodgame.gui.GUIRegion;
import worldfoodgame.model.RegionAttributes;
import static worldfoodgame.model.RegionAttributes.PLANTING_ATTRIBUTES.*;

import java.awt.*;

/**
 * Created by winston on 2/8/15.
 */
class RainView implements RegionView
{
  private static Color[] colors = ColorsAndFonts.RAIN_FALL;
  private static double LIMIT = colors.length / RegionAttributes.LIMITS.get(ANNUAL_RAINFALL);


  @Override
  public void draw(Graphics g, GUIRegion gRegion)
  {

    double rains = gRegion.getRegion().getAttributes().getAttribute(ANNUAL_RAINFALL);
    g.setColor(colors[(int) (rains * LIMIT)]);

    g.fillPolygon(gRegion.getPoly());

    g.setColor(ColorsAndFonts.PASSIVE_REGION_OUTLINE);
    g.drawPolygon(gRegion.getPoly());
  }
}
