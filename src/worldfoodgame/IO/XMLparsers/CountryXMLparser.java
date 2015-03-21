package worldfoodgame.IO.XMLparsers;

import org.xml.sax.Attributes;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;
import worldfoodgame.model.AtomicRegion;
import worldfoodgame.model.MapPoint;
import worldfoodgame.model.Region;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by winston on 3/21/15.
 */
public class CountryXMLparser extends DefaultHandler
{
  private List<Region> regionList;
  private Locator locator;

  private String countryName;

  private Region tmpRegion;
  private List<MapPoint> tmpPerimeterSet;
  private boolean
    country, name, area, vertex;

  @Override
  public void startDocument() throws SAXException
  {
    regionList = new ArrayList<>();
    tmpPerimeterSet = new LinkedList<>();
    countryName = null;
  }

  @Override
  public void startElement(String uri, String localName,
                           String qName, Attributes attributes) throws SAXException
  {
    qName = qName.toLowerCase();

    switch (qName)
    {
      case "country":
        //to nothing...
        break;

      case "area":
        tmpRegion = new AtomicRegion();
        if (countryName != null) tmpRegion.setName(countryName);
        else new NullPointerException("country name not set"); // todo remove when i think its working
        break;

      case "name":
        name = true;
        break;

      case "vertex":
        double lat = 0;
        double lon = 0;
        try
        {
          lat = Double.parseDouble(attributes.getValue("lat"));
          lon = Double.parseDouble(attributes.getValue("lon"));
        }
        catch (Exception e)
        {
          System.out.println(locator.getLineNumber());
          fatalError(new SAXParseException("Could not parse lat/lon.", locator));
        }
        tmpPerimeterSet.add(new MapPoint(lon, lat));
        break;

      default:
        String msg = qName + " is not a recognized tag.";
        fatalError(new SAXParseException(msg, locator));


    }
  }

  @Override
  public void characters(char[] ch, int start, int length) throws SAXException
  {
    if (name)
    {
      name = false;
      countryName = new String(ch, start, length);
    }
  }

  @Override
  public void endElement(String uri, String localName, String qName)
  throws SAXException
  {
    if (qName.equals("area"))
    {
      // save and reset....
      tmpRegion.setPerimeter(new ArrayList<>(tmpPerimeterSet));
      regionList.add(tmpRegion);
      tmpPerimeterSet.clear();
    }
  }


  public static void main(String[] args)
  {

  }
}
