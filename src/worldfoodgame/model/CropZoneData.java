package worldfoodgame.model;


import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CropZoneData
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
  public LandTile[][] tiles = new LandTile[COLS][ROWS];

  public static final double ACCEPTABLE_THRESHOLD = 0.30;

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
    int col = (int)(COLS * (lon + 180) / 360);
    int row = (int)(ROWS * (Math.sin(lat) + 1)/2);
    return tiles[col][row];
  }

  public List<LandTile> allTiles()
  {
    List<LandTile> ls = new ArrayList<>();
    for(LandTile[] arr : tiles) ls.addAll(Arrays.asList(arr));
    return ls;
  }

  public void initTiles()
  {
    for (int col = 0; col < COLS; col++)
    {
      for (int row = 0; row < ROWS; row++)
      {
        double lon = colToLon(col);
        double lat = rowToLat(row);
        tiles[col][row] = new LandTile(lon, lat);
      }
    }
  }

  private double rowToLat(int row)
  {
    return Math.toDegrees(Math.asin(((double)row)/ROWS * 2 - 1));
  }

  private double colToLon(int col)
  {
    return 360 * ((double)col) / COLS - 180;
  }

  public enum EnumCropZone
  {
    IDEAL
      {
        public double productionRate() {return 1.0;}
      },

    ACCEPTABLE
      {
        public double productionRate() {return 0.60;}
      },

    POOR
      {
        public double productionRate() {return 0.25;}
      };
    public static final int SIZE = values().length;
    public abstract double productionRate();
  }

  public static void main(String[] args)
  {
    CropZoneData data = new CropZoneData();
    data.initTiles();
    try(FileOutputStream out = new FileOutputStream("resources/data/tiledata"))
    {
      for(LandTile t : data.allTiles())
      {
        out.write(t.toByteBuffer().array());
      }
    } catch (FileNotFoundException e)
    {
      e.printStackTrace();
    } catch (IOException e)
    {
      e.printStackTrace();
    }
  }
}
