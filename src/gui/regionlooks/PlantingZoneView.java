package gui.regionlooks;

import gui.GUIRegion;
import model.RegionAttributes;

import java.awt.*;

/**
 * Created by winston on 2/5/15.
 *
 * Over lay view. Expresses the Planting zone in the below divergent color
 * spectrum.
 */
class PlantingZoneView implements RegionView
{
  // planting zone => 1 <= x <= 14
  private Color[] PlantingColors = {
      new Color(0xFBFF1E),
      new Color(0x67001f),
      new Color(0x67001f),
      new Color(0x67001f),
      new Color(0xb2182b),
      new Color(0xd6604d),
      new Color(0xf4a582),
      new Color(0xfddbc7),
      new Color(0xf7f7f7),
      new Color(0xd1e5f0),
      new Color(0x92c5de),
      new Color(0x4393c3),
      new Color(0x2166ac),
      new Color(0x053061),
      new Color(0x053061),
  };

  @Override
  public void draw(Graphics g, GUIRegion gRegion)
  {
    double index = gRegion.getRegion()
        .getAttributes()
        .getAttribute(RegionAttributes.PLANTING_ATTRIBUTES.PLANTING_ZONE);

    g.setColor(PlantingColors[(int) index]);
    g.fillPolygon(gRegion.getPoly());
  }
}
