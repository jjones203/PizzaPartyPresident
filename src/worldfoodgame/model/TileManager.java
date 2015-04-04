package worldfoodgame.model;


import sun.jvm.hotspot.CLHSDB;
import worldfoodgame.common.AbstractClimateData;
import static worldfoodgame.common.AbstractScenario.*;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

public class TileManager extends AbstractClimateData
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

  public static final LandTile NO_TILE = new LandTile(-180,0); /* in pacific */
  
  public static final int ROWS = 1500;
  public static final int COLS = 4000;
  public static final int EARTH_CIRC = 40_000;
  
  /* these are fairly rough estimates for the distance between two tiles on the
   X and Y axes.  Should be acceptable for our purposes */
  public static final double DX = COLS/EARTH_CIRC;
  public static final double DY = ROWS/(2*EARTH_CIRC);

  private World world;

  private LandTile[][] tiles = new LandTile[COLS][ROWS];


  private List<LandTile> countryTiles = new ArrayList<>();
  private List<LandTile> allTiles;
  private List<LandTile> dataTiles;

  public TileManager(World world)
  {
    this.world = world;
    for(LandTile[] arr : tiles) Arrays.fill(arr, NO_TILE);
  }

  
  public TileManager()
  {this(null);}

  /**
   Get the max temperature at a location, given a year.  If the year is the
   current year in the World, the temperature is the actual model temperature at
   that location.  Otherwise it is an interpolated estimate that will NOT include
   the noise due to randomization in the year-stepping model.
   
   @param lat [-90.0 to 90.0], South latitudes are < 0.
   @param lon [-180.0 to 180.0], West longitudes are < 0.
   @param year between current year and AbstractScenario.END_YEAR
   @return  the max temperature at the coordinates, either estimated or exact,
            depending on the year
   */
  @Override
  public float getTemperatureMax(float lat, float lon, int year)
  {
    if(year < world.getCurrentYear())
    {
      throw new IllegalArgumentException("year argument must be current year or later");
    }
    LandTile tile = getTile(lon, lat);
    if(tile == NO_TILE)
    {
      throw new LandTile.NoDataException(
        String.format("No data for longitude: %f, latitude: %f)", lon, lat));
    }
    if (year == world.getCurrentYear()) return tile.getMaxAnnualTemp();
    
    float cur = tile.getMaxAnnualTemp();
    float proj = tile.getProj_maxAnnualTemp();
    int slices = END_YEAR - world.getCurrentYear();
    int sliceNum = year - world.getCurrentYear();
    return LandTile.interpolate(cur, proj, slices) * sliceNum;
  }

  /**

   @param lat
   @param lon
   @return
   */
  public float getTemperatureMax(float lat, float lon) 
  {
    return getTemperatureMax(lat, lon, world.getCurrentYear());
  }

  /**
   Get the min temperature at a location, given a year.  If the year is the
   current year in the World, the temperature is the actual model temperature at
   that location.  Otherwise it is an interpolated estimate that will NOT include
   the noise due to randomization in the year-stepping model.

   @param lat [-90.0 to 90.0], South latitudes are < 0.
   @param lon [-180.0 to 180.0], West longitudes are < 0.
   @param year between current year and AbstractScenario.END_YEAR
   @return  the min temperature at the coordinates, either estimated or exact,
   depending on the year
   */
  @Override
  public float getTemperatureMin(float lat, float lon, int year)
  {
    if(year < world.getCurrentYear())
    {
      throw new IllegalArgumentException("year argument must be current year or later");
    }
    LandTile tile = getTile(lon, lat);
    if(tile == NO_TILE)
    {
      throw new LandTile.NoDataException(
        String.format("No data for longitude: %f, latitude: %f)", lon, lat));
    }
    if (year == world.getCurrentYear()) return tile.getMinAnnualTemp();

    float cur = tile.getMinAnnualTemp();
    float proj = tile.getProj_minAnnualTemp();
    int slices = END_YEAR - world.getCurrentYear();
    int sliceNum = year - world.getCurrentYear();
    return LandTile.interpolate(cur, proj, slices) * sliceNum;
  }

  /**
   Get the average daytime temperature at a location, given a year.  If the year
   is the current year in the World, the temperature is the actual model 
   temperature at that location.  Otherwise it is an interpolated estimate that
   will NOT include the noise due to randomization in the year-stepping model.

   @param lat [-90.0 to 90.0], South latitudes are < 0.
   @param lon [-180.0 to 180.0], West longitudes are < 0.
   @param year between current year and AbstractScenario.END_YEAR
   @return  the average daytime temperature at the coordinates, either estimated
   or exact, depending on the year
   */
  @Override
  public float getTemperatureDay(float lat, float lon, int year)
  {
    if(year < world.getCurrentYear())
    {
      throw new IllegalArgumentException("year argument must be current year or later");
    }
    LandTile tile = getTile(lon, lat);
    if(tile == NO_TILE)
    {
      throw new LandTile.NoDataException(
        String.format("No data for longitude: %f, latitude: %f)", lon, lat));
    }
    if (year == world.getCurrentYear()) return tile.getAvgDayTemp();

    float cur = tile.getAvgDayTemp();
    float proj = tile.getProj_avgDayTemp();
    int slices = END_YEAR - world.getCurrentYear();
    int sliceNum = year - world.getCurrentYear();
    return LandTile.interpolate(cur, proj, slices) * sliceNum;
  }

  /**
   Get the average nighttime temperature at a location, given a year.  If the year
   is the current year in the World, the temperature is the actual model 
   temperature at that location.  Otherwise it is an interpolated estimate that
   will NOT include the noise due to randomization in the year-stepping model.

   @param lat [-90.0 to 90.0], South latitudes are < 0.
   @param lon [-180.0 to 180.0], West longitudes are < 0.
   @param year between current year and AbstractScenario.END_YEAR
   @return  the average night temperature at the coordinates, either estimated 
   or exact, depending on the year
   */
  @Override
  public float getTemperatureNight(float lat, float lon, int year)
  {
    if(year < world.getCurrentYear())
    {
      throw new IllegalArgumentException("year argument must be current year or later");
    }
    LandTile tile = getTile(lon, lat);
    if(tile == NO_TILE)
    {
      throw new LandTile.NoDataException(
        String.format("No data for longitude: %f, latitude: %f)", lon, lat));
    }
    if (year == world.getCurrentYear()) return tile.getAvgNightTemp();

    float cur = tile.getAvgNightTemp();
    float proj = tile.getProj_avgNightTemp();
    int slices = END_YEAR - world.getCurrentYear();
    int sliceNum = year - world.getCurrentYear();
    return LandTile.interpolate(cur, proj, slices) * sliceNum;
  }

  /**
   Get the annual rainfall at a location, given a year.  If the year is the
   current year in the World, the rainfall is the actual model rainfall at
   that location.  Otherwise it is an interpolated estimate that will NOT include
   the noise due to randomization in the year-stepping model.

   @param lat [-90.0 to 90.0], South latitudes are < 0.
   @param lon [-180.0 to 180.0], West longitudes are < 0.
   @param year between current year and AbstractScenario.END_YEAR
   @return  the annual rainfall at the coordinates, either estimated or exact,
   depending on the year
   */
  @Override
  public float getRainfall(float lat, float lon, int year)
  {
    if(year < world.getCurrentYear())
    {
      throw new IllegalArgumentException("year argument must be current year or later");
    }
    LandTile tile = getTile(lon, lat);
    if(tile == NO_TILE)
    {
      throw new LandTile.NoDataException(
        String.format("No data for longitude: %f, latitude: %f)", lon, lat));
    }
    if (year == world.getCurrentYear()) return tile.getRainfall();

    float cur = tile.getRainfall();
    float proj = tile.getProj_rainfall();
    int slices = END_YEAR - world.getCurrentYear();
    int sliceNum = year - world.getCurrentYear();
    return LandTile.interpolate(cur, proj, slices) * sliceNum;
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


  /**
   Add a given tile to the data set.
   This should really only be used when reading in a new set of tiles from
   a file.
   
   @param tile  LandTile to add
   */
  public void putTile(LandTile tile)
  {
    double lon = tile.getLon();
    double lat = tile.getLat();
    tiles[lonToCol(lon)][latToRow(lat)] = tile;
  }


  /**
   Registers a tile as having been associated with a country.  Due to gaps in the
   country data, if a set of tiles covering all the land is desired, use dataTiles()
   @param tile  tile to register
   */
  public void registerCountryTile(LandTile tile)
  {
    countryTiles.add(tile);
  }


  /**
   Returns a Collection of tiles that have been registered with a country.
   This is dependent on the usage of registerCountryTile() at initial data
   creation. (Also maybe should be refactored to another location?)
   
   @return Collection of those LandTiles that have been registered with a Country
   */
  public Collection<LandTile> countryTiles()
  {
    return countryTiles;
  }

  /**
   Returns a Collection of the tiles held by this TileManager that actually
   contain data.  This, in effect, excludes tiles that would be over ocean and
   those at the extremes of latitude.  For all tiles, use allTiles();
   
   @return  a Collection holding only those tiles for which there exists raster
            data.
   */
  public Collection<LandTile> dataTiles()
  {
    if(null == dataTiles)
    {
      dataTiles = new ArrayList<>();
      for(LandTile t : allTiles())
      {
        if(NO_TILE != t) dataTiles.add(t);
      }
    }
    return dataTiles;
  }

  /**
   @return  all the tiles in this manager in a List
   */
  public List<LandTile> allTiles()
  {
    if(allTiles == null) for(LandTile[] arr : tiles) allTiles.addAll(Arrays.asList(arr));
    return allTiles;
  }


  /**
   remove a given tile from the underlying set of tiles.  This has the effect
   of placing a NO_TILE tile at the location of the given tile in the full
   projection (assuming the given tile was found)
   @param tile tile to remove
   @return  true if the tile was found and removed
   */
  public boolean removeTile(LandTile tile)
  {
    int col = lonToCol(tile.getLon());
    int row = latToRow(tile.getLat());

    /* check if tile is in the right position */
    boolean ret = tiles[col][row] == tile;

    if(!ret) /* only search if the tile is not in its proper place */
    {
      loop:
      for (int i = 0; i < tiles.length; i++)
      {
        for (int j = 0; j < tiles[i].length; j++)
        {
          if(ret = (tiles[i][j] == tile))
          {
            /* "remove" first instance of tile from underlying array */
            tiles[i][j] = NO_TILE;
            break loop;
          }
        }
      }
    }
    /* tile is in the right place, remove it */
    else tiles[col][row] = NO_TILE;

    /* pull all tiles from method ref, to guarantee the list exists */
    ret = ret || allTiles().remove(tile);
    ret = ret || countryTiles.remove(tile);

    /* ret is true iff the tile was a member of one of the underlying structures */
    return ret;
  }


  public void setWorld(World world)
  {
    this.world = world;
  }


  /* intialize a new tileset with proper latitude and longitude center points.
    This is really only used for making tiles for a new data set*/
  private void initTiles()
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


  /* check given row and column indices for validity */
  private boolean indicesInBounds(int row, int col)
  {
    return col > 0 && col < COLS && row > 0 && row < ROWS;
  }


  /* given a longitude line, return the column index corresponding to tiles
    containing that line */
  private int latToRow(double lat)
  {
    double sin = Math.sin(Math.toRadians(lat));
    return (int)Math.min((ROWS * (sin + 1) / 2), ROWS - 1);
  }


  /* given a longitude line, return the column index corresponding to tiles
    containing that line */
  private int lonToCol(double lon)
  {
    return (int)Math.min((COLS * (lon + 180) / 360), COLS - 1);
  }

  
  /* return the theoretical center latitude line of tiles in a given row */
  public double rowToLat(int row)
  {
    return Math.toDegrees(Math.asin(((double)row)/ROWS * 2 - 1)) + 180/(ROWS * 2.0);
  }

  /* return the theoretical center longitude line of tiles in a given column */
  public double colToLon(int col)
  {
    return 360 * ((double)col) / COLS - 180 + 360/(COLS * 2.0);
  }

  /* Used to create and write a new tile set.
     Be careful using this with the current data's filepath.  If a backup is not
     made, that data will be overwritten and must be re-generated from the raw
     data from www.worldclim.org (See BioClimDataParser)
   */
  private static void initNewTileSet(String filepath)
  {
    TileManager data = new TileManager();
    data.initTiles();
    try(FileOutputStream out = new FileOutputStream(filepath))
    {
      for(LandTile t : data.allTiles())
      {
        byte[] array = t.toByteBuffer().array();
        out.write(array);
      }
    } catch (IOException e)
    {
      e.printStackTrace();
    }

  }
  
  
}
