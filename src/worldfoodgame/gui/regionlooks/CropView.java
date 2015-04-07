package worldfoodgame.gui.regionlooks;

import worldfoodgame.common.EnumCropType;
import worldfoodgame.gui.ColorsAndFonts;
import worldfoodgame.gui.GUIRegion;
import worldfoodgame.model.World;

import java.awt.*;

/**
 * Overlay for % of land planted with given crop
 *
 * @author jessica
 */
public class CropView implements RegionView
{
  private float LUMINOSITY = .7f;
  private EnumCropType crop;
  private float hueVal;

  public CropView(EnumCropType crop, float hueVal)
  {
    this.crop = crop;
    this.hueVal = hueVal;
  }

  @Override
  public void draw(Graphics g, GUIRegion gRegion)
  {
    int year = World.getWorld().getCurrentYear();

    float ratio = (float) gRegion.getCountry().getCropLand(year, crop)
                / (float) gRegion.getCountry().getArableLand(year);

    ratio *= 100;

    if (ratio > 1) ratio = 1;

    Color color = Color.getHSBColor(hueVal, ratio, LUMINOSITY);

    if (gRegion.isActive()) color = color.brighter();

    g.setColor(color);
    g.fillPolygon(gRegion.getPoly());

    g.setColor(ColorsAndFonts.PASSIVE_REGION_OUTLINE);
    g.drawPolygon(gRegion.getPoly());
  }
}
