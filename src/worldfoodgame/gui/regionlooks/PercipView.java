package worldfoodgame.gui.regionlooks;

import worldfoodgame.gui.GUIRegion;
import worldfoodgame.gui.displayconverters.EquirectangularConverter;
import worldfoodgame.gui.displayconverters.MapConverter;
import worldfoodgame.model.LandTile;
import worldfoodgame.model.World;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by winston on 3/31/15.
 */
public class PercipView implements RegionView, RasterDataView
{
  private static int TILE_SIZE = 500;
  private static MapConverter converter = new EquirectangularConverter();
  private static DefaultLook defaultLook = new DefaultLook();

  private static int calculatedYear = 0;

//  private BufferedImage precipitationData;

  private Graphics2D graphicsContext;

  @Override
  public void draw(Graphics g, GUIRegion gRegion)
  {
    defaultLook.draw(g, gRegion);

    boolean imageOutDated =
         calculatedYear != World.getWorld().getCurrentYear()
      || graphicsContext == null;

    if (imageOutDated)
    {
      graphicsContext = makeImage(g);
      calculatedYear = World.getWorld().getCurrentYear();
    }
  }

  private BufferedImage makeImage()
  {
    BufferedImage image = new BufferedImage(66000, 78000, BufferedImage.TYPE_INT_ARGB);

    Graphics2D g2d = image.createGraphics();
    System.out.println("do we get here?");

    for (LandTile tile : World.getWorld().getAllTheLand())
    {
      Point point = converter.mapPointToPoint(tile.getCenter());
      float peripRatio = tile.getRainfall() / 1_000;

      if (peripRatio > 1) peripRatio = 1;

      Color color = new Color(0.09019608f, 0.28627452f, 0.5019608f, peripRatio);

      g2d.setColor(color);
      g2d.fillOval(point.x - TILE_SIZE / 2, point.y - TILE_SIZE / 2, TILE_SIZE, TILE_SIZE);

    }


    // for testing

    g2d.setColor(Color.RED);
    g2d.fillOval(0,0, 10_000, 10_000);
    image.flush();

    return image;
  }

  @Override
  public BufferedImage getRasterImage()
  {
    return precipitationData;
  }
}
