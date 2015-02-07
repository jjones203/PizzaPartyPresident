package testing;

import IO.AreaXMLLoader;
import IO.XMLparsers.KMLParser;
import model.Region;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

/**
 * @author david
 *         created: 2015-01-28
 *         <p/>
 *         description:
 */
public class KMLParserTest
{
  
  public static Collection<Region> getRegions() throws ParserConfigurationException
  {
    Collection<Region> l = new ArrayList<>();

    try
    {
      AreaXMLLoader loader = new AreaXMLLoader(new KMLParser());
      l = loader.parseFile("resources/2008_cpi_large.xml");
    }
    catch (SAXException e)
    {
      e.printStackTrace();
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
    
    return l;
  }
}
