package worldfoodgame.gui.regionlooks;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by winston on 4/4/15.
 */
public class MaxTemp extends RasterViz
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



    return null;
  }
}
