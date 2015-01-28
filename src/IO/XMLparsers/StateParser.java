package IO.XMLparsers;

import model.AtomicRegion;
import model.Map;
import model.MapPoint;
import model.Region;
import org.xml.sax.Attributes;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by winston on 1/27/15.
 * Phase_01
 * CS 351 spring 2015
 */
public class StateParser extends RegionParserHandler implements RegionParser
{
  private List<Region> regionList;
  private List<MapPoint> tmpPerimeter;
  private Region tmpRegion;
  private Locator locator;

  public StateParser()
  {
    regionList = new ArrayList<>();
    tmpPerimeter = new LinkedList<>();
  }


  @Override
  public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException
  {
    switch (qName)
    {
      case "state":
        tmpRegion = new AtomicRegion();
        tmpRegion.setName(atts.getValue("name"));
        tmpPerimeter.clear();
        break;

      case "point":
        MapPoint mp = new MapPoint(
            Double.parseDouble(atts.getValue("lat")),
            Double.parseDouble(atts.getValue("lng")));
        tmpPerimeter.add(mp);
    }
  }

  @Override
  public void endElement(String uri, String localName, String qName) throws SAXException
  {
    switch (qName)
    {
      case "state":
        tmpRegion.setPerimeter(new ArrayList<MapPoint>(tmpPerimeter));
        regionList.add(tmpRegion);
    }
  }

  @Override
  public List<Region> getRegionList()
  {
    return regionList;
  }

  @Override
  public Locator getLocator()
  {
    return locator;
  }
}
