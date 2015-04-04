package worldfoodgame.gui.regionlooks;

import worldfoodgame.gui.GUIRegion;
import worldfoodgame.gui.displayconverters.EquirectangularConverter;
import worldfoodgame.gui.displayconverters.MapConverter;
import worldfoodgame.model.LandTile;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Collection;

/**
 * Created by winston on 3/31/15.
 */
public class PercipView implements RegionView, RasterDataView
{
  private static int TILE_SIZE = 500;
  private static RegionView view;

  private static MapConverter converter = new EquirectangularConverter();
  private static DefaultLook defaultLook = new DefaultLook();

  private static int calculatedYear = 0;


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
        Point point = converter.mapPointToPoint(tile.getCenter());

        float peripRatio = tile.getRainfall() / 1_000;

        if (peripRatio > 1) peripRatio = 1;


        Color transRed = new Color(69, 255, 206);

        g2d.setComposite(
          AlphaComposite.getInstance(AlphaComposite.SRC_OVER, peripRatio));

        g2d.setColor(transRed);
        g2d.fillOval(point.x - TILE_SIZE/2, point.y - TILE_SIZE/2, TILE_SIZE, TILE_SIZE);

        g2d.setComposite(
          AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
      }
    }
  }

  public static RegionView getView()
  {
    if (view == null) view = new PercipView();
    return view;
  }

  @Override
  public BufferedImage getRasterImage()
  {
    return null;
  }
}
