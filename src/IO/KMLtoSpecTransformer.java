package IO;

import IO.XMLparsers.KMLParser;
import model.MapPoint;
import model.Region;

import java.io.*;
import java.util.*;

/**
 * Created by winston on 3/6/15.
 * temporary class, only in use to transform kml data into Spec data.
 */
@Deprecated
public class KMLtoSpecTransformer
{
  private static String baseDir = "/Users/winston/Desktop/countries/";

  private static void writeOutCountry(String name, List<List<MapPoint>> areaSet) throws FileNotFoundException
  {
    PrintWriter writer = new PrintWriter(baseDir + name +".xml");

    writer.println("<country>");
    writer.println(String.format("<name>%s</name>", name));


    for (List<MapPoint> area : areaSet)
    {
      if (area.size() == 1) continue;
      writer.println("<area>");
      for (MapPoint point : area)
      {
        writer.println(String.format("<vertex lat=\"%f\" lon=\"%f\"/>", point.getLat(), point.getLon()));
      }
      writer.println("</area>");
    }

    writer.println("</country>");
    writer.close();
  }

  public static void main(String[] args) throws IOException
  {
    Collection<Region> regions = KMLParser.getRegionsFromFile("resources/countries_world.xml");

    HashMap<String, List<List<MapPoint>>> complextSet = new HashMap<>();

    for (Region region : regions)
    {
      if (!complextSet.containsKey(region.getName()))
      {
        complextSet.put(region.getName(), new ArrayList<List<MapPoint>>());
      }

      complextSet.get(region.getName()).add(region.getPerimeter());
    }


    for (String name : complextSet.keySet())
    {
      writeOutCountry(name, complextSet.get(name));
    }
  }
}
