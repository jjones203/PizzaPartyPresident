package IO;

import model.MapPoint;
import model.Region;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;

public class AreaXMLloaderTest
{
  public static void main(String[] args)
  {
    AreaXMLloader loader = null;
    try
    {
      loader = new AreaXMLloader("resources/areas");
    }
    catch (ParserConfigurationException e)
    {
      System.out.println("got here");
//      e.printStackTrace();
    }
    catch (SAXException e)
    {
//      e.printStackTrace();
    }

    assert loader != null;
    for (Region r : loader.getRegions())
    {
      System.out.println(r);
      for (MapPoint mp : r.getPerimeter())
      {
        System.out.println("\t" + mp);
      }
      System.out.println();
    }
  }
}