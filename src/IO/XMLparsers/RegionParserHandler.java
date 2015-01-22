package IO.XMLparsers;

/**
 * Created by winston on 1/20/15.
 */

import model.MapPoint;
import model.Region;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


//todo clean up and add comments...

/**
 * at this point this is being blindly developed from :
 * http://docs.oracle.com/javase/tutorial/jaxp/sax/parsing.html
 */
public class RegionParserHandler extends DefaultHandler
{
  private List<Region> regionList = new ArrayList<>();

  private Region tmpRegion;
  private List<MapPoint> tmpPerimeterSet;

  private boolean nameTag;


  public List<Region> getRegionList()
  {
    return regionList;
  }

  @Override
  public void startElement(String namespaceURI, String localName,
                           String qName, Attributes atts)
  throws SAXException
  {

    /*
     * entering a new area tag.
     * re-init tmp objects:
     *    1) tmpRegion
     *    2) peremterSet
     */
    if (qName.equals("area"))
    {
      tmpRegion = new Region();
      tmpPerimeterSet = new ArrayList<>();
    }
    /*
     * sets flag to extract content of the same tag.
     */
    else if (qName.equals("name")) nameTag = true;

    /*
     * because the vertex tag only has atts and no content, we do not need
     * to set a flag as we did above.
     */
    else if (qName.equals("vertex"))
    {
      // TODO add error checking around these two attributes.
      double lat = Double.parseDouble(atts.getValue("lat"));
      double lon = Double.parseDouble(atts.getValue("lon"));
      MapPoint mapPoint = new MapPoint(lat, lon);
      tmpPerimeterSet.add(mapPoint);
    }

  }

  @Override
  public void characters(char[] ch, int start, int length) throws SAXException
  {
    if (nameTag)
    {
      nameTag = false;
      tmpRegion.setName(new String(ch, start, length));
    }
  }

  @Override
  public void endElement(String uri, String localName, String qName)
  throws SAXException
  {
    if (qName.equals("area"))
    {
      // save and reset....
      tmpRegion.setPerimeter(tmpPerimeterSet);
      regionList.add(tmpRegion);
    }
  }


//  private static class MyErrorHandler implements ErrorHandler
//  {
//    private PrintStream out;
//
//    public MyErrorHandler(PrintStream out)
//    {
//      this.out = out;
//    }
//
//    private String getParseExceptionInfo(SAXParseException spe)
//    {
//      String systemId = spe.getSystemId();
//
//      if (systemId == null)
//      {
//        systemId = "null";
//      }
//
//      String info = "URI=" + systemId + " Line="
//              + spe.getLineNumber() + ": " + spe.getMessage();
//
//      return info;
//    }
//
//    public void warning(SAXParseException spe) throws SAXException
//    {
//      out.println("Warning: " + getParseExceptionInfo(spe));
//    }
//
//    public void error(SAXParseException spe) throws SAXException
//    {
//      String message = "Error: " + getParseExceptionInfo(spe);
//      throw new SAXException(message);
//    }
//
//    public void fatalError(SAXParseException spe) throws SAXException
//    {
//      String message = "Fatal Error: " + getParseExceptionInfo(spe);
//      throw new SAXException(message);
//    }
//  }


  //******//
  // MAIN //
  //******//
  public static void main(String[] args)
  {
    String fileName = "resources/areas/newMexicoTest.xml";
    SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
    RegionParserHandler handler = new RegionParserHandler();
    try
    {
      SAXParser saxParser = saxParserFactory.newSAXParser();
      saxParser.parse(new File(fileName), handler);
    } catch (ParserConfigurationException | SAXException | IOException e)
    {
      e.printStackTrace();
    }


    for (Region region : handler.getRegionList())
    {
      System.out.println(region);
      for (MapPoint mp : region.getPerimeter())
      {
        System.out.println("\t" + mp);
      }
    }
  }
}


//TODO make this more general so that it can be adapted to other attribut classes
//todo researhc and add error checking.

/* RESOURCES:
http://www.saxproject.org/quickstart.html

 */