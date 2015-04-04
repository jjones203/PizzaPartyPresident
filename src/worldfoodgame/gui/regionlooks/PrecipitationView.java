package worldfoodgame.gui.regionlooks;

import worldfoodgame.model.LandTile;
import worldfoodgame.model.World;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * This view Handels the visualization of Global precipitation data.
 * <p/>
 * this class should only be created once, to adjust look and feel of the class
 * asjust the class constants.
 */
public class PrecipitationView extends RasterViz
{
  public static final float THRESHOLD_SCALE = .25f;
  public static final double LIMI_VISABILITY = 0.007;
  public static final Color RAIN_COLOR = new Color(0.09019608f, 0.28627452f, 0.5019608f);
  private static boolean DEBUG = false;

  private BufferedImage makeImage()
  {
    BufferedImage image = new BufferedImage(IMG_WIDTH, IMG_HEIGHT, BufferedImage.TYPE_INT_ARGB);
    Graphics2D g2d = image.createGraphics();
    g2d.translate(IMG_WIDTH / 2, IMG_HEIGHT / 2);

    g2d.setColor(RAIN_COLOR);

    if (DEBUG) System.out.println("starting game tiles!");

    for (LandTile tile : World.getWorld().dataTiles())
    {
      Point point = getPoint(tile.getCenter());

      float peripRatio = tile.getRainfall() / 1_000;

      if (peripRatio > 1) peripRatio = 1;

      peripRatio *= THRESHOLD_SCALE;

      if (peripRatio < LIMI_VISABILITY) continue; // skip

      g2d.setComposite(
        AlphaComposite.getInstance(AlphaComposite.SRC_OVER, peripRatio));

      g2d.fillRect(point.x - TILE_SIZE / 2, point.y - TILE_SIZE / 2, TILE_SIZE, TILE_SIZE);

      g2d.setComposite(
        AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
    }

    if (DEBUG) System.out.println("finished generating percip image");

    return image;
  }

  @Override
  public BufferedImage getRasterImage()
  {
    if (bufferedImage == null) bufferedImage = makeImage();
    return bufferedImage;
  }

}
