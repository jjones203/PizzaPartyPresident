package worldfoodgame.gui.regionlooks;

import worldfoodgame.gui.ColorsAndFonts;
import worldfoodgame.model.Continent;
import worldfoodgame.model.LandTile;
import worldfoodgame.model.World;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Collection;

/**
 * Created by winston on 4/4/15.
 * Raster-type RegionView used for visualizing the crop planting locations across
 * the globe.  This is more for debug purposes than for actual gameplay.
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

  protected BufferedImage makeImage()
  {
    BufferedImage image = new BufferedImage(IMG_WIDTH, IMG_HEIGHT, BufferedImage.TYPE_INT_ARGB);
    Graphics2D g2d = image.createGraphics();
    g2d.translate(IMG_WIDTH / 2, IMG_HEIGHT / 2);

    //for (LandTile tile : World.getWorld().getAllCountrifiedTiles())
    for (Continent cont:World.getWorld().getContinents())
    {
      Collection<LandTile> tiles = cont.getLandTiles(); 
      for (LandTile tile : tiles)
      {
        if (tile.isArable() == false)
        {
          g2d.setColor(ColorsAndFonts.NONARABLE_COLOR);
        }
        else if (tile.getCurrentCrop() == null)
        {
          g2d.setColor(ColorsAndFonts.UNPLANTED_COLOR);
        }

        else
        {
          switch (tile.getCurrentCrop())
          {
            case CORN:
              g2d.setColor(ColorsAndFonts.CORN_COLOR);
              break;

            case OTHER_CROPS:
              g2d.setColor(ColorsAndFonts.OTHER_CROP_COLOR);
              break;

            case RICE:
              g2d.setColor(ColorsAndFonts.RICE_COLOR);
              break;

            case SOY:
              g2d.setColor(ColorsAndFonts.SOY_COLOR);
              break;

            case WHEAT:
              g2d.setColor(ColorsAndFonts.WHEAT_COLOR);
              break;

            default:
              System.err.println("non exhaustive case in CropRasterLook switch");
              break;
          }
        }
        Point point = getPoint(tile.getCenter());
        g2d.fillRect(point.x, point.y, 1, scaleHeight(tile));
      }
      
    }
    return image;
  }
}
