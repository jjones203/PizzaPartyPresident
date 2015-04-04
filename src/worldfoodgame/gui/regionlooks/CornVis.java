package worldfoodgame.gui.regionlooks;

import worldfoodgame.model.LandTile;
import worldfoodgame.model.World;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by winston on 4/4/15.
 */
class CornVis extends RasterViz
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

    float lowerBound = 240.0f/360.0f;
    float upperBound = 161.0f/360.0f;


    for (LandTile tile : World.getWorld().dataTiles())
    {



      Color color = Color.red;
      g2d.setColor(color);

      Point point = getPoint(tile.getCenter());

      // add conditional here
      g2d.fillRect(point.x, point.y, 1, 1);
    }


    return image;
  }
}
