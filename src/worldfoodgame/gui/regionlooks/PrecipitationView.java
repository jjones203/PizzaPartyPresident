package worldfoodgame.gui.regionlooks;

import worldfoodgame.model.LandTile;
import worldfoodgame.model.World;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * This view Handles the visualization of Global precipitation data.
 * <p/>
 * this class should only be created once, to adjust look and feel of the class
 * asjust the class constants.
 */
class PrecipitationView extends RasterViz
{
  @Override
  public BufferedImage getRasterImage()
  {
    if (bufferedImage == null)
    {
      bufferedImage = makeImage();
    }
    return bufferedImage;
  }

  protected BufferedImage makeImage()
  {
    System.out.println("making an image for: " + World.getWorld().getCurrentYear());
    BufferedImage image = new BufferedImage(IMG_WIDTH, IMG_HEIGHT, BufferedImage.TYPE_INT_ARGB);
    Graphics2D g2d = image.createGraphics();
    g2d.translate(IMG_WIDTH / 2, IMG_HEIGHT / 2);

    float S = .5f;
    float L = .8f;

    float lowerBound = .130555556f;
    float upperBound = .663888889f;

    for (LandTile tile : World.getWorld().getAllCountrifiedTiles())
    {
      double percipRatio = (Math.log(tile.getRainfall()) - 2) / 10;

      float scaled = (float) (percipRatio * (upperBound - lowerBound)) + lowerBound;

      Color color = Color.getHSBColor(Math.abs(scaled), S, L);
      g2d.setColor(color);

      Point point = getPoint(tile.getCenter());

      int height = scaleHeight(tile);
      g2d.fillRect(point.x, point.y, 1, height);
    }

    return image;
  }

}
