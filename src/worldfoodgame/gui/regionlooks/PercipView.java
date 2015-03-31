package worldfoodgame.gui.regionlooks;

import worldfoodgame.gui.GUIRegion;
import worldfoodgame.gui.displayconverters.EquirectangularConverter;
import worldfoodgame.gui.displayconverters.MapConverter;
import worldfoodgame.model.LandTile;

import java.awt.*;
import java.util.Collection;

/**
 * Created by winston on 3/31/15.
 */
public class PercipView implements RegionView
{
  private static RegionView view;

  private static MapConverter converter = new EquirectangularConverter();
  private static DefaultLook defaultLook = new DefaultLook();

  @Override
  public void draw(Graphics g, GUIRegion gRegion)
  {
    defaultLook.draw(g, gRegion);

    Graphics2D g2d = (Graphics2D) g;

    if (gRegion.isPrimaryRegion())
    {
      Collection<LandTile> tiles = gRegion.getRegion().getCountry().getLandTiles();

      for (LandTile tile : tiles)
      {

        if (tile.getRainfall() > 60)
        {
          Point point = converter.mapPointToPoint(tile.getCenter());
          Color transRed = new Color(0.3019608f, 1.0f, 0.49803922f);

//          g2d.setComposite(
//            AlphaComposite.getInstance(AlphaComposite.SRC_OVER, .2f));

          g2d.setColor(transRed);


          g2d.fillOval(point.x, point.y, 1000, 1000);
        }
      }
    }
  }

  public static RegionView getView()
  {
    if (view == null) view = new PercipView();
    return view;
  }
}
