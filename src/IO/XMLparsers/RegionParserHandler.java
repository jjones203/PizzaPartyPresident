package IO.XMLparsers;

/**
 * Created by winston on 1/20/15.
 * ${PROJECT_NAME}
 * CS 351 spring 2015
 */

import model.AtomicRegion;
import model.MapPoint;
import model.Region;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static IO.IOhelpers.convertToFileURL;

public class RegionParserHandler extends DefaultHandler
{
  private final List<Region> regionList;
  private Region tmpRegion;
  private List<MapPoint> tmpPerimeterSet;
  private boolean nameTag;

  public RegionParserHandler()
  {
    regionList = new LinkedList<>();
  }


  public List<Region> getRegionList()
  {
    return regionList;
  }


  @Override
  public void startDocument() throws SAXException
  {
//    super.startDocument();
    regionList.clear();
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
    switch (qName)
    {
      case "area":
        tmpRegion = new AtomicRegion();
        tmpPerimeterSet = new ArrayList<>();
        break;
    /*
     * sets flag to extract content of the same tag.
     */
      case "name":
        nameTag = true;
        break;

    /*
     * because the vertex tag only has atts and no content, we do not need
     * to set a flag as we did above.
     */
      case "vertex":
        // TODO add error checking around these two attributes.
        double lat = 0;
        double lon = 0;
        try
        {
          lat = Double.parseDouble(atts.getValue("lat"));
          lon = Double.parseDouble(atts.getValue("lon"));
        } catch (NumberFormatException e)
        {
          throw e;
        }
        MapPoint mapPoint = new MapPoint(lat, lon);
        tmpPerimeterSet.add(mapPoint);
        break;
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

}


