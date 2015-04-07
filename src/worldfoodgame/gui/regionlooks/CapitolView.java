package worldfoodgame.gui.regionlooks;

import worldfoodgame.gui.ColorsAndFonts;
import worldfoodgame.gui.GUIRegion;
import worldfoodgame.gui.displayconverters.EquirectangularConverter;
import worldfoodgame.gui.displayconverters.MapConverter;
import worldfoodgame.model.MapPoint;

import java.awt.*;
import java.awt.geom.AffineTransform;

/**
 * Created by winston on 3/29/15.
 * 
 * description:
 * CapitolView is a RegionView displaying "capitol" locations for the countries
 * of the world, as calculated in the Country class.
 */
class CapitolView implements RegionView
{
  private MapConverter converter = new EquirectangularConverter();
  private RegionView defaultLook = new DefaultLook();

  @Override
  public void draw(Graphics g, GUIRegion gRegion)
  {
    defaultLook.draw(g, gRegion);

    if (gRegion.isPrimaryRegion())
    {
      Graphics2D g2d = (Graphics2D) g;
    
      /* save and temporarily reset the graphics transform */
      AffineTransform at = g2d.getTransform();
      g2d.setTransform(new AffineTransform());
      
      MapPoint capitol = gRegion.getRegion().getCountry().getCapitolLocation();
      Point convertedPoint = converter.mapPointToPoint(capitol);

      /* find the location on the map of the capital */
      Point dst = new Point();
      at.transform(convertedPoint, dst);

      if (gRegion.isActive())
      {
        g2d.setColor(ColorsAndFonts.CAPITAL_ACTIVE);
      }
      else
      {
        g2d.setColor(ColorsAndFonts.CAPITAL_PASSIVE);
      }
      
      /* indicate the capital location with an ellipse independent of the transform */
      g2d.fillOval(dst.x, dst.y, 4, 4);

      g2d.setTransform(at);
    }
  }
}
