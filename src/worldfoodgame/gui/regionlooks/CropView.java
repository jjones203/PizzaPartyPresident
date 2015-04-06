package worldfoodgame.gui.regionlooks;

import java.awt.Color;
import java.awt.Graphics;

import worldfoodgame.common.EnumCropType;
import worldfoodgame.gui.ColorsAndFonts;
import worldfoodgame.gui.GUIRegion;
import worldfoodgame.model.World;

/**
 * Overlay for % of land planted with given crop
 * @author jessica
 */
public class CropView implements RegionView
{
  EnumCropType crop;
  float hueVal;
  
  public CropView(EnumCropType crop, float hueVal)
  {
    this.crop = crop;
    this.hueVal = hueVal;
  }

  @Override
  public void draw(Graphics g, GUIRegion gRegion)
  {
    Color color;
    if (gRegion.isActive())
    {
      color = ColorsAndFonts.ACTIVE_REGION;
    }
    else
    {
      int yearOfInterest = World.getWorld().getCurrentYear();
      double cropLand = gRegion.getRegion().getCountry().getCropLand(yearOfInterest,crop);
      double arableLand = gRegion.getRegion().getCountry().getArableLand(yearOfInterest);

      double ratio = cropLand/arableLand;
      color = new Color(hueVal, (float)ratio, (float)ratio);
    }

    g.setColor(color);
    g.fillPolygon(gRegion.getPoly());

    g.setColor(ColorsAndFonts.PASSIVE_REGION_OUTLINE);
    g.drawPolygon(gRegion.getPoly());

  }
  
  
}
