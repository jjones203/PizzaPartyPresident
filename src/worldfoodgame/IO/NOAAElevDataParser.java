package worldfoodgame.IO;

import java.awt.geom.Point2D;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import static worldfoodgame.IO.NOAAElevDataParser.INDICIES.*;
/**
 @author david
 created: 2015-03-05

 #####################
 IF YOU RUN THIS WITH THE NOAA DATA, IT WILL REQUIRE -Xmx4g AS A JVM OPTION JUST
 TO PARSE ONE FILE
 This will be fixed with the next version.
 #####################
 
 this should be a temporary class, used to parse the binary data files
 NOAA
 provides and write them to a more meaningful format

 files are expected to have names "[a-p]10g" with no extension.  The
 format
 and naming convention of these files is detailed in the PDF found at

 http://www.ngdc.noaa.gov/mgg/topo/report/globedocumentationmanual.pdf

 In brief:
 Each file is simply a binary file holding a number of 16-bit integers,
 each representing the elevation at a specific latitude and longitude
 defined by their place in the conceptual array defined by the number of
 rows and columns for that file.  The table below details the contents
 and
 bounds of each file

        Latitude      Longitude    Elevation     Data
 Name   min   max     min   max    min    max    cols   rows
 -----------------------------------------------------------
 A10G    50    90    -180   -90      1   6098    10800  4800
 B10G    50    90     -90     0      1   3940    10800  4800
 C10G    50    90       0    90    -30   4010    10800  4800
 D10G    50    90      90   180      1   4588    10800  4800
 E10G     0    50    -180   -90    -84   5443    10800  6000
 F10G     0    50     -90     0    -40   6085    10800  6000
 G10G     0    50       0    90   -407   8752    10800  6000
 H10G     0    50      90   180    -63   7491    10800  6000
 I10G   -50     0    -180   -90      1   2732    10800  6000
 J10G   -50     0     -90     0   -127   6798    10800  6000
 K10G   -50     0       0    90      1   5825    10800  6000
 L10G   -50     0      90   180      1   5179    10800  6000
 M10G   -90   -50    -180   -90      1   4009    10800  4800
 N10G   -90   -50     -90     0      1   4743    10800  4800
 O10G   -90   -50       0    90      1   4039    10800  4800
 P10G   -90   -50      90   180      1   4363    10800  4800


 description: */
public class NOAAElevDataParser
{
  public enum INDICIES
  {
    NAME(0), 
    MINLAT(1), MAXLAT(2), 
    MINLON(3), MAXLON(4), 
    MINEL(5), MAXEL(6), 
    COLS(7), ROWS(8);

    final int id;
    
    INDICIES(int id)
    {
      this.id = id;
    }
  }

  final String[][] fileDef = new String[][]
    {
      {"A10G", "50", "90", "-180", "-90", "1", "6098", "10800", "4800"},
      {"B10G", "50", "90", "-90", "0", "1", "3940", "10800", "4800"},
      {"C10G", "50", "90", "0", "90", "-30", "4010", "10800", "4800"},
      {"D10G", "50", "90", "90", "180", "1", "4588", "10800", "4800"},
      {"E10G", "0", "50", "-180", "-90", "-84", "5443", "10800", "6000"},
      {"F10G", "0", "50", "-90", "0", "-40", "6085", "10800", "6000"},
      {"G10G", "0", "50", "0", "90", "-407", "8752", "10800", "6000"},
      {"H10G", "0", "50", "90", "180", "-63", "7491", "10800", "6000"},
      {"I10G", "-50", "0", "-180", "-90", "1", "2732", "10800", "6000"},
      {"J10G", "-50", "0", "-90", "0", "-127", "6798", "10800", "6000"},
      {"K10G", "-50", "0", "0", "90", "1", "5825", "10800", "6000"},
      {"L10G", "-50", "0", "90", "180", "1", "5179", "10800", "6000"},
      {"M10G", "-90", "-50", "-180", "-90", "1", "4009", "10800", "4800"},
      {"N10G", "-90", "-50", "-90", "0", "1", "4743", "10800", "4800"},
      {"O10G", "-90", "-50", "0", "90", "1", "4039", "10800", "4800"},
      {"P10G", "-90", "-50", "90", "180", "1", "4363", "10800", "4800"}
    };

  int sqSize;
  String root;
  ArrayList<ElevPoint> allPoints = new ArrayList<>();

  /**
   @param rootPath
   absolute path to location of NOAA files.  See class comment
   @param outputCellSize
   */
  public NOAAElevDataParser(String rootPath, int outputCellSize)
  {
    root = rootPath;
    sqSize = outputCellSize;
  }

  /*
    reads all the files in the first field of the fileDef array
   */
  public List<ElevPoint> readAll()
  {
    for (String listing[] : fileDef)
    {
      allPoints.addAll(readFile(listing[NAME.ordinal()]));
    }
    return null;
  }

  /*
    reads a single file from the NOAA data and returns the List of ElevPoints
    parsed from that file
   */
  private List<ElevPoint> readFile(String filename)
  {
    
    List<ElevPoint> pts = new ArrayList<>();
    

    try(FileInputStream in = new FileInputStream(root + "/" + filename))
    {
      String[] listing = getListingByName(filename);
      byte[] bytes = new byte[2];

      int rows = Integer.parseInt(listing[ROWS.ordinal()]);
      int cols = Integer.parseInt(listing[COLS.ordinal()]);

      double minLat = Double.parseDouble(listing[MINLAT.ordinal()]);
      double minLon = Double.parseDouble(listing[MINLON.ordinal()]);

      double maxLat = Double.parseDouble(listing[MAXLAT.ordinal()]);
      double maxLon = Double.parseDouble(listing[MAXLON.ordinal()]);

      double latStep = (maxLat - minLat) / cols;
      double lonStep = (maxLon - minLon) / rows;
      
      int minElev = Integer.parseInt(listing[MINEL.ordinal()]);
      int maxElev = Integer.parseInt(listing[MAXEL.ordinal()]);

      int elev;
      double lat;
      double lon;
      int index;

      for (int row = 0; row < rows; row++)
      {
        for (int col = 0; col < cols; col++)
        {

          bytes[0] = (byte)in.read();
          bytes[1] = (byte)in.read();
          
          /* data is stored in big-endian two-byte integers;
             shift bits accordingly */
          elev = (bytes[1] << 8) & bytes[0];
          assert(elev > minElev && elev < maxElev);

          lat = row * latStep + minLat;
          lon = col * lonStep + minLon;

          pts.add(new ElevPoint(lon, lat, elev));
          if(pts.size()%1000 == 0) System.out.println(pts.size());
        }
      }
    } 
    catch (IOException e)
    {
      e.printStackTrace();
      System.exit(-1);
    }
    return pts;
  }

  /*
    given a filename, return the corresponding listing that defines that file's
    constraints
   */
  private String[] getListingByName(String filename) throws IOException
  {
    for(String listing[] : fileDef) 
    {
      if(filename.endsWith(listing[NAME.ordinal()])) return listing;
    }
    throw new IOException("listing not found");
  }

  /*
    temporary class used for storing an elevation point
   */
  static class ElevPoint
  {
    private int dataPoints = 0;
    
    public final double lon;
    public final double lat;
    public final double elev;

    public ElevPoint(double lon, double lat, double elev)
    {
      this.lon = lon;
      this.lat = lat;
      this.elev = elev;
    }

    public ElevPoint(Point2D loc, double elev)
    {
      this(loc.getX(), loc.getY(), elev);
    }


    
    public String toXMLString()
    {
      return String.format(
        "<ElevationPoint lon=\"%.6f\" lat=\"%.6f\" elev=\"%f\"/>",
        lon, lat, elev);
    }
  }

  
  /*
    testing
   */
  public static void main(String[] args)
  {
    NOAAElevDataParser p =
      new NOAAElevDataParser("/Users/david/cs351/Food_Project/all10",10);
    
    p.allPoints.addAll(p.readFile(p.fileDef[0][0]));
    
    System.out.println(p.allPoints.size());
    System.out.println(p.allPoints.get(0).toXMLString());
  }
}
