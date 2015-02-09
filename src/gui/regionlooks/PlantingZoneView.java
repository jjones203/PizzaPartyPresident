package gui.regionlooks;

import gui.ColorsAndFonts;
import gui.GUIRegion;
import model.RegionAttributes;

import java.awt.*;

/**
 * Created by winston on 2/5/15.
 * <p/>
 * Over lay view. Expresses the Planting zone in the below divergent color
 * spectrum.
 */
public class PlantingZoneView implements RegionView
{
  // planting zone => 1 <= x <= 14
  private static Color[] PlantingZoneColors = ColorsAndFonts.PlantingZoneColors;

  public static Color getPlantingColor(double plantingValue)
  {
    try
    {
      return PlantingZoneColors[(int) plantingValue];
    }
    catch (Exception e)
    {
      System.err.println("BAD planting zone value in region");
    }
    return PlantingZoneColors[0];
  }

  @Override
  public void draw(Graphics g, GUIRegion gRegion)
  {
    double index = gRegion.getRegion()
        .getAttributes()
        .getAttribute(RegionAttributes.PLANTING_ATTRIBUTES.PLANTING_ZONE);


    if (gRegion.isActive())
    {
      g.setColor(getPlantingColor(index).brighter());
    }
    else
    {
      g.setColor(getPlantingColor(index));
    }

    g.fillPolygon(gRegion.getPoly());
    g.setColor(ColorsAndFonts.PASSIVE_REGION_OUTLINE);
    g.drawPolygon(gRegion.getPoly());
  }
}
