package worldfoodgame.gui.regionlooks;

import worldfoodgame.gui.ColorsAndFonts;
import worldfoodgame.gui.GUIRegion;

import java.awt.*;

/**
 * Created by winston on 3/21/15.
 */
class BackGroundLook implements RegionView
{
  @Override
  public void draw(Graphics g, GUIRegion gRegion)
  {
    Graphics2D g2 = (Graphics2D) g;
    g2.setColor(ColorsAndFonts.BACKGROUD_TEST_COLOR);


    LinearGradientPaint linePaint =
      new LinearGradientPaint(
        0,0,
        30,30,
        new float[]{0.0f, 1.0f},
        new Color[]{
          ColorsAndFonts.PASSIVE_REGION,
          ColorsAndFonts.PASSIVE_REGION.darker()},
        MultipleGradientPaint.CycleMethod.REPEAT);

    g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, .85f));

    g2.setPaint(linePaint);
    g2.fillPolygon(gRegion.getPoly());

    g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
  }
}
