package worldfoodgame.gui.regionlooks;

import worldfoodgame.gui.GUIRegion;
import worldfoodgame.gui.displayconverters.EquirectangularConverter;
import worldfoodgame.gui.displayconverters.MapConverter;
import worldfoodgame.model.LandTile;
import worldfoodgame.model.MapPoint;
import worldfoodgame.model.World;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;

/**
 * Created by winston on 3/31/15.
 */
public class PercipView implements RegionView, RasterDataView
{
  private static boolean DEBUG = false;

  private static int TILE_SIZE = 5;
  private static MapConverter converter = new EquirectangularConverter();
  private static DefaultLook defaultLook = new DefaultLook();
  private static HashMap<MapPoint, Point> mapPtoP = new HashMap<>();

  private static int calculatedYear = 0;

  private BufferedImage precipitationData;
  public static final Color RAIN_COLOR = new Color(0.09019608f, 0.28627452f, 0.5019608f);


  float[] blrmatrix = {
    0.111f, 0.111f, 0.111f,
    0.111f, 0.111f, 0.111f,
    0.111f, 0.111f, 0.111f,
  };


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
      precipitationData = makeImage();
      calculatedYear = World.getWorld().getCurrentYear();
    }
  }

  private BufferedImage makeImage()
  {
    int width = 900 * 4;
    int height = 450 * 3;

    BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

    Graphics2D g2d = image.createGraphics();

    g2d.translate(width/2, height/2);

    if (DEBUG) System.out.println("starting game tiles!");

    for (LandTile tile : World.getWorld().getAllTheLand())
    {
      Point point = getPoint(tile.getCenter());

      float peripRatio = tile.getRainfall() / 1_000;

      if (peripRatio > 1) peripRatio = 1;

      peripRatio *= .25f;

      if (peripRatio < 0.01) continue;

      g2d.setComposite(
        AlphaComposite.getInstance(AlphaComposite.SRC_OVER, peripRatio));

      g2d.setColor(RAIN_COLOR);
      g2d.fillOval(point.x - TILE_SIZE / 2, point.y - TILE_SIZE / 2, TILE_SIZE, TILE_SIZE);

      g2d.setComposite(
        AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
    }

    if (DEBUG) System.out.println("finished generating percip image");

    return image;
  }

  @Override
  public BufferedImage getRasterImage()
  {
    return precipitationData;
  }

  private Point getPoint(MapPoint mp)
  {
    Point point = mapPtoP.get(mp);

    if (point == null)
    {
      point = converter.mapPointToPoint(mp);
      mapPtoP.put(mp, point);
    }

    return point;
  }
}
