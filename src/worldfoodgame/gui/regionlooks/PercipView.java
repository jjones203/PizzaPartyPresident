package worldfoodgame.gui.regionlooks;

import worldfoodgame.gui.GUIRegion;
import worldfoodgame.gui.displayconverters.EquirectangularConverter;
import worldfoodgame.gui.displayconverters.MapConverter;
import worldfoodgame.model.LandTile;
import worldfoodgame.model.World;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

/**
 * Created by winston on 3/31/15.
 */
public class PercipView implements RegionView, RasterDataView
{
  private static boolean DEBUG = true;

  private static int TILE_SIZE = 6;
  private static MapConverter converter = new EquirectangularConverter();
  private static DefaultLook defaultLook = new DefaultLook();

  private static int calculatedYear = 0;

  private BufferedImage precipitationData;


  @Override
  public void draw(Graphics g, GUIRegion gRegion)
  {
    defaultLook.draw(g, gRegion);

    boolean imageOutDated =
         calculatedYear != World.getWorld().getCurrentYear()
      || precipitationData == null;

    if (imageOutDated)
    {
      Graphics2D g2d = (Graphics2D) g;
      precipitationData = makeImage(g2d.getTransform());
      calculatedYear = World.getWorld().getCurrentYear();
    }
  }

  private BufferedImage makeImage(AffineTransform affineTransform)
  {
    int width = 900 * 4;
    int height = 450 * 3;

    BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

    Graphics2D g2d = image.createGraphics();
//    g2d.setTransform(affineTransform);

    g2d.translate(width/2, height/2);

    System.out.println("starting game tiles!");

    int counter = 0;

    for (LandTile tile : World.getWorld().getAllTheLand())
    {
      Point point = converter.mapPointToPoint(tile.getCenter());
      float peripRatio = tile.getRainfall() / 1_000;

      if (peripRatio > 1) peripRatio = 1;

      Color color = new Color(0.09019608f, 0.28627452f, 0.5019608f, peripRatio);

      g2d.setColor(color);
      g2d.fillOval(point.x - TILE_SIZE / 2, point.y - TILE_SIZE / 2, TILE_SIZE, TILE_SIZE);


      if (DEBUG)
      {
        counter++;
        if (counter % 1_000 == 0)
        {
          System.out.print(".");
          if (counter % 50_000 == 0)
          {
            counter = 0;
            System.out.println();
          }
        }
      }
    }

    System.out.println("finished generating percip image");

    return image;
  }

  @Override
  public BufferedImage getRasterImage()
  {
    return precipitationData;
  }
}
