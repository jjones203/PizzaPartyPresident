package worldfoodgame.IO.XMLparsers;

import org.xml.sax.*;
import org.xml.sax.helpers.DefaultHandler;
import worldfoodgame.model.AtomicRegion;
import worldfoodgame.model.Country;
import worldfoodgame.model.MapPoint;
import worldfoodgame.model.Region;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.util.*;

import static worldfoodgame.IO.IOHelpers.convertToFileURL;
import static worldfoodgame.IO.IOHelpers.getFilesInDir;

/**
 * Created by winston on 3/21/15.
 * Reads in the country XML data and produces a set of regions.
 *
 * also provides supporting method to link regions to countries and generate
 * a set of liked countries form regions.
 */
public class CountryXMLparser extends DefaultHandler
{
  private static String COUNTRY_DIR = "resources/countries";

  private Collection<Region> regionList;
  private Locator locator;
  private String countryName;
  private AtomicRegion tmpRegion;
  private List<MapPoint> tmpPerimeterSet;
  private boolean name;

  /**
   * generates a set of Countries from a list of regions. liked properly together.
   * @param regions list to derive countries from
   * @return collection of countries created form the given regions.
   */
  public static Collection<Country> RegionsToCountries(Collection<Region> regions)
  {
    HashMap<String, Country> nameToCountry = new HashMap<>();

    for (Region region : regions)
    {
      if ( ! nameToCountry.containsKey(region.getName()))
      {
        Country country = new Country(region.getName());
        nameToCountry.put(region.getName(), country);
      }
      region.setCountry( nameToCountry.get(region.getName()));
    }

    return nameToCountry.values();
  }


  public Locator getLocator()
  {
    return locator;
  }

  @Override
  public void setDocumentLocator(Locator locator)
  {
    this.locator = locator;
  }

  @Override
  public void startDocument() throws SAXException
  {
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
        //do nothing...
        break;

      case "area":
        tmpRegion = new AtomicRegion();
        tmpRegion.setName(countryName);
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

  public Collection<Region> getRegionList()
  {
    if (regionList == null)
    {
      try
      {
        generateRegions();
      }
      catch (ParserConfigurationException e)
      {
        e.printStackTrace();
      }
      catch (SAXException e)
      {
        e.printStackTrace();
      }
    }
    return regionList;
  }

  public Collection<Country> getCountries()
  {
    return RegionsToCountries(getRegionList());
  }

  /* private method to generate the set of regions*/
  private void generateRegions()
  throws ParserConfigurationException, SAXException
  {
    regionList = new ArrayList<>();
    XMLReader xmlReader = SAXParserFactory.newInstance().newSAXParser().getXMLReader();
    xmlReader.setContentHandler(this);
    for (String file : getFilesInDir(COUNTRY_DIR))
    {
      try
      {
        xmlReader.parse(convertToFileURL(file));
      }
      catch (IOException e)
      {
        e.printStackTrace();
        // todo add Editor support here!
      }
    }
  }


  public Collection<Country> getCountries()
  {
    return RegionsToCountries(getRegionList());
  }


  public static void main(String[] args)
  {
    System.out.println(new CountryXMLparser().getCountries().size());
  }
}
