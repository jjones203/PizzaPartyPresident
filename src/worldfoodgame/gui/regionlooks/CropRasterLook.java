package worldfoodgame.gui.regionlooks;

import worldfoodgame.model.LandTile;
import worldfoodgame.model.World;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by winston on 4/4/15.
 */
class CropRasterLook extends RasterViz
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


    for (LandTile tile : World.getWorld().dataTiles())
    {
      if (tile.getCurrentCrop() == null) continue;

      switch (tile.getCurrentCrop())
      {
        case CORN:
          g2d.setColor(Color.red);
          break;

        case OTHER_CROPS:
          g2d.setColor(Color.green);
          break;

        case RICE:
          g2d.setColor(Color.white);
          break;

        case SOY:
          g2d.setColor(Color.yellow);
          break;

        case WHEAT:
          g2d.setColor(Color.blue);
          break;

        default:
          System.out.println("got here some how?");
          break;
      }
      Point point = getPoint(tile.getCenter());
      g2d.fillRect(point.x, point.y, 1, 1);
    }


    return image;
  }
}
