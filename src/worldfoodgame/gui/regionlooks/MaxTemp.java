package worldfoodgame.gui.regionlooks;

import worldfoodgame.model.LandTile;
import worldfoodgame.model.World;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by winston on 4/4/15.
 */
class MaxTemp extends RasterViz
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

  private BufferedImage makeImage()
  {
    BufferedImage image = new BufferedImage(IMG_WIDTH, IMG_HEIGHT, BufferedImage.TYPE_INT_ARGB);
    Graphics2D g2d = image.createGraphics();
    g2d.translate(IMG_WIDTH / 2, IMG_HEIGHT / 2);


    float S = .5f;
    float L = .8f;

    float lowerBound = .2f;
    float upperBound = .0f;

    for (LandTile tile : World.getWorld().dataTiles())
    {
      double maxTmpRatio = tile.getMaxAnnualTemp() / 50 ;
      float scaled = (float) (maxTmpRatio * (upperBound - lowerBound)) + lowerBound;

      Color color = Color.getHSBColor(Math.abs(scaled), S, L);
      g2d.setColor(color);

      Point point = getPoint(tile.getCenter());
      g2d.fillRect(point.x, point.y, 1, scaleHeight(tile));
    }


    return image;
  }
}
