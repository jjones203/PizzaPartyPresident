package worldfoodgame.gui.regionlooks;

import worldfoodgame.model.LandTile;
import worldfoodgame.model.World;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by winston on 4/4/15.
 */
class MinTemp extends RasterViz
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
    BufferedImage image = new BufferedImage(IMG_WIDTH, IMG_HEIGHT, BufferedImage.TYPE_INT_ARGB);
    Graphics2D g2d = image.createGraphics();
    g2d.translate(IMG_WIDTH / 2, IMG_HEIGHT / 2);

    float S = .8f;
    float L = .8f;

    float lowerBound = 240.0f/360.0f;
    float upperBound = 225.0f/360.0f;


    for (LandTile tile : World.getWorld().dataTiles())
    {
      double minTempRatio = (tile.getMinAnnualTemp() + 45) / 80;
      float scaled = (float) (minTempRatio * (upperBound - lowerBound)) + lowerBound;

      if (minTempRatio < 0)
      {
        System.out.println("min temp is negative: " + tile.getMinAnnualTemp());
      }

      if (minTempRatio > 1)
      {
        System.out.println("min temp is about one: " + tile.getMinAnnualTemp());
        minTempRatio = 1;
      }

      Color color = Color.getHSBColor(Math.abs(scaled), S, L);
      g2d.setColor(color);

      Point point = getPoint(tile.getCenter());
      g2d.fillRect(point.x, point.y, 1, scaleHeight(tile));
    }


    return image;
  }
}
