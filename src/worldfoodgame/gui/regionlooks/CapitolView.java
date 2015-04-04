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
  private RegionView defalt = new DefaultLook();

  @Override
  public void draw(Graphics g, GUIRegion gRegion)
  {
    defalt.draw(g, gRegion);

    if (gRegion.isPrimaryRegion())
    {
      Graphics2D g2d = (Graphics2D) g;
    
    /* save and temporarily reset the graphics transform */
      AffineTransform at = g2d.getTransform();
      g2d.setTransform(new AffineTransform());


      MapPoint capitol = gRegion.getRegion().getCountry().getCapitolLocation();
      Point convertedPoint = converter.mapPointToPoint(capitol);

      Point dst = new Point();
      at.transform(convertedPoint, dst);

      if (gRegion.isActive())
      {
        g2d.setColor(Color.GREEN);
      }
      else
      {
        g2d.setColor(Color.RED);
      }


      g2d.fillOval(dst.x, dst.y, 5, 5);

      g2d.setTransform(at);
    }
  }
}
