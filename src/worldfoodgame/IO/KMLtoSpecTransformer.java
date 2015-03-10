package worldfoodgame.IO;

import worldfoodgame.IO.XMLparsers.KMLParser;
import worldfoodgame.model.MapPoint;
import worldfoodgame.model.Region;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

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
        writer.println(String.format("<vertex lat=\"%f\" lon=\"%f\"/>",
          point.getLat(), point.getLon()));
      }
      writer.println("</area>");
    }

    writer.println("</country>");
    writer.close();
  }

  public static void main(String[] args) throws IOException
  {
    Collection<Region> regions = KMLParser.getRegionsFromFile("resources/countries_world.xml");

    HashMap<String, List<List<MapPoint>>> complexSet = new HashMap<>();

    for (Region region : regions)
    {
      if (!complexSet.containsKey(region.getName()))
      {
        complexSet.put(region.getName(), new ArrayList<List<MapPoint>>());
      }

      complexSet.get(region.getName()).add(region.getPerimeter());
    }


    for (String name : complexSet.keySet())
    {
      writeOutCountry(name, complexSet.get(name));
    }
  }
}
