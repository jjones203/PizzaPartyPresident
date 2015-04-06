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
 * Created by winston on 4/3/15.
 */
public abstract class RasterViz implements RegionView, RasterDataView
{
  protected static int TILE_SIZE = 6;
  protected static MapConverter converter = new EquirectangularConverter();
  protected static DefaultLook defaultLook = new DefaultLook();
  protected static final int IMG_HEIGHT = (int) converter.getHeight();
  protected static final int IMG_WIDTH = (int) converter.getWidth();

  // this is static so that all subclasses can share the same mapping.
  // no need for copies
  private static HashMap<MapPoint, Point> mapPtoP = new HashMap<>();

  protected int calculatedYear = 0; // init value
  protected BufferedImage bufferedImage;

  @Override
  public void draw(Graphics g, GUIRegion gRegion)
  {
    defaultLook.draw(g, gRegion);

    boolean imageOutDated = calculatedYear != World.getWorld().getCurrentYear()
                         || bufferedImage == null;


    if (imageOutDated)
    {
      bufferedImage = makeImage();
      calculatedYear = World.getWorld().getCurrentYear();
    }
  }

  @Override
  public abstract BufferedImage getRasterImage();

  protected abstract BufferedImage makeImage();

  protected Point getPoint(MapPoint mp)
  {
    Point point = mapPtoP.get(mp);

    if (point == null)
    {
      point = converter.mapPointToPoint(mp);
      mapPtoP.put(mp, point);
    }

    return point;
  }

  // simple linear function to transform the pixel height to keep track with the
  // map projection.
  protected int scaleHeight(LandTile landTile)
  {
    double lat = landTile.getLat();
    return (int) Math.floor( (Math.abs(lat) * 7 / 90) + 1);
  }

}
