package worldfoodgame.gui.regionlooks;

import worldfoodgame.gui.ColorsAndFonts;
import worldfoodgame.gui.GUIRegion;
import worldfoodgame.model.World;

import java.awt.*;

/**
 * Created by winston on 3/31/15.
 */
class PopulationView implements RegionView
{
  @Override
  public void draw(Graphics g, GUIRegion gRegion)
  {

    int population = gRegion
      .getCountry()
      .getPopulation(World.getWorld().getCurrentYear());

    float popRatio = (float) (population / 500_000_000.0);

    Color fillColor;

    if (popRatio > 1)
    {
      fillColor = new Color(1f, .01f, .01f);
    }
    else
    {
      float offset = 1 - popRatio;
      fillColor = new Color(1f, offset, offset);
    }

    if (gRegion.isActive()) fillColor = fillColor = fillColor.darker();

    g.setColor(fillColor);
    g.fillPolygon(gRegion.getPoly());

    g.setColor(ColorsAndFonts.ACTIVE_REGION_OUTLINE);
    g.drawPolygon(gRegion.getPoly());
  }
}
