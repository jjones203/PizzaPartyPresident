package IO;

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
      e.printStackTrace();
    }
    catch (SAXException e)
    {
      e.printStackTrace();
    }

    assert loader != null;
    for (Region r : loader.getRegions())
    {
      System.out.print("from here: \n\t");
      System.out.println(r);
    }
  }
}