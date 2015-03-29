package worldfoodgame.gui.regionlooks;

import worldfoodgame.gui.GUIRegion;
import worldfoodgame.gui.displayconverters.EquirectangularConverter;
import worldfoodgame.gui.displayconverters.MapConverter;
import worldfoodgame.model.MapPoint;

import java.awt.*;
import java.awt.geom.AffineTransform;

/**
 * Created by winston on 3/29/15.
 */
class CapitolView implements RegionView
{
  private MapConverter converter = new EquirectangularConverter();
  private RegionView defalt = new defaultLook();

  @Override
  public void draw(Graphics g, GUIRegion gRegion)
  {
    defalt.draw(g, gRegion);

    Graphics2D g2d = (Graphics2D) g;

    g2d.setRenderingHint(
      RenderingHints.KEY_TEXT_ANTIALIASING,
      RenderingHints.VALUE_TEXT_ANTIALIAS_ON
    );

    /* save and temporarily reset the graphics transform */
    AffineTransform at = g2d.getTransform();
    g2d.setTransform(new AffineTransform());


    MapPoint capitol = gRegion.getRegion().getCountry().getCapitolLocation();
    Point convertedPoint = converter.mapPointToPoint(capitol);


    Point dst = new Point();

    at.transform(convertedPoint, dst);

    g2d.setColor(Color.CYAN);
    g2d.fillOval(dst.x, dst.y, 10, 10);

    g2d.setTransform(at);

  }
}
