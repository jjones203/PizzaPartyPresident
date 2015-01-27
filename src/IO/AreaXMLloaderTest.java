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
      System.err.println("ERROR IN CREATING XML LOADER");
      e.printStackTrace();
    }
    catch (SAXException e)
    {
      System.err.println("ERROR IN CREATING XML LOADER");
      e.printStackTrace();
    }


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