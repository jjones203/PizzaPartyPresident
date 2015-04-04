package worldfoodgame.model;


import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TileManager
{
  /*
    Tiles represent 100 sq. km areas on the globe, defined by the
    Lambert Equal Area Projection.
    Their dimensions are defined as rectangles within the
    projection, with the following base assumptions:
    Earth Surface Area : 600,000,000 sq. km
    Earth Circumference: 40,000 km

    This implies there must be 6 million tiles.
    Since the projection is cylindrical and linear in X, it is simple to divide
    the rectangular map it produces into 4000 columns, which implies 1500 rows
    to achieve 6 million total tiles.  Since the aspect ratio of the map is PI,
    these tiles are not too far from square.
   */

  public static final int ROWS = 1500;
  public static final int COLS = 4000;
  public static final LandTile NO_TILE = new LandTile(-180,0); /* in pacific */
  public LandTile[][] tiles = new LandTile[COLS][ROWS];


  public TileManager()
  {
    for(LandTile[] arr : tiles) Arrays.fill(arr, NO_TILE);
  }
  
  /**
   Get a tile by longitude and latitude
   
   @param lon degrees longitude
   @param lat degrees latitude
   @return the tile into which the specified longitude and latitude coordinates
   fall 
   */
  public LandTile getTile (double lon, double lat)
  {
    /* equal area projection is encapsulated here */
    int col = lonToCol(lon);
    int row = latToRow(lat);
    LandTile tile = tiles[col][row];
    return tile == null? NO_TILE : tile;
  }
  
  public void putTile(LandTile tile)
  {
    double lon = tile.getLon();
    double lat = tile.getLat();
    tiles[lonToCol(lon)][latToRow(lat)] = tile;
  }

  public List<LandTile> allTiles()
  {
    List<LandTile> ls = new ArrayList<>();
    for(LandTile[] arr : tiles) ls.addAll(Arrays.asList(arr));
    return ls;
  }

  /* initial setup of tile data */
  private void initTiles()
  {
    for (int col = 0; col < COLS; col++)
    {
      for (int row = 0; row < ROWS; row++)
      {
        double lon = colToLon(col) + 360/(COLS * 2.0);
        double lat = rowToLat(row) + 180/(ROWS * 2.0);
        tiles[col][row] = new LandTile(lon, lat);
      }
    }
  }

  private int latToRow(double lat)
  {
    double sin = Math.sin(Math.toRadians(lat));
    return (int)Math.min((ROWS * (sin + 1) / 2), ROWS - 1);
  }

  private int lonToCol(double lon)
  {
    return (int)Math.min((COLS * (lon + 180) / 360), COLS - 1);
  }

  private double rowToLat(int row)
  {
    return Math.toDegrees(Math.asin(((double)row)/ROWS * 2 - 1));
  }

  private double colToLon(int col)
  {
    return 360 * ((double)col) / COLS - 180;
  }

  public boolean removeTile(LandTile t)
  {
    int col = lonToCol(t.getLon());
    int row = latToRow(t.getLat());
    boolean ret = tiles[col][row] == t;
    tiles[col][row] = NO_TILE;
    return ret;
  }


  private static void initNewTileSet()
  {
    TileManager data = new TileManager();
    data.initTiles();
    try(FileOutputStream out = new FileOutputStream("resources/data/tiledata.bil"))
    {
      for(LandTile t : data.allTiles())
      {
        byte[] array = t.toByteBuffer().array();
        out.write(array);
      }
    } catch (FileNotFoundException e)
    {
      e.printStackTrace();
    } catch (IOException e)
    {
      e.printStackTrace();
    }

  }



  public static void main(String[] args)
  {

  }
}
