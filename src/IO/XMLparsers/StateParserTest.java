package IO.XMLparsers;

import IO.AreaXMLloader;
import model.MapPoint;
import model.Region;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;

import java.io.IOException;
import java.util.Collection;

public class StateParserTest
{
  public static void main(String[] args)
  {
    Collection<Region> regions = null;
    try
    {
      AreaXMLloader loader = new AreaXMLloader(new StateParser(), null);
      regions = loader.parseFile("resources/states.xml");
    }
    catch (ParserConfigurationException e)
    {
      e.printStackTrace();
    }
    catch (SAXException e)
    {
      e.printStackTrace();
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }

    for (Region region : regions)
    {
      System.out.println(region);
      for (MapPoint mp : region.getPerimeter())
      {
        System.out.println("\t" + mp);
      }
      System.out.println("\n");
    }

    System.out.println("all done!");
  }

}