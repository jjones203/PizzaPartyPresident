package IO.XMLparsers;

/**
 * Created by winston on 1/20/15.
 * phase 1
 * CS 351 spring 2015
 */

import model.AtomicRegion;
import model.MapPoint;
import model.Region;
import org.xml.sax.Attributes;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class RegionParserHandler extends DefaultHandler
{
  //TODO clean up object creation locations...
  private Locator locator;
  private final List<Region> regionList;
  private Region tmpRegion;
  private List<MapPoint> tmpPerimeterSet;
  private boolean nameTag;

  public RegionParserHandler()
  {
    regionList = new LinkedList<>();
    tmpPerimeterSet = new LinkedList<>();
    tmpRegion = null;
  }


  public List<Region> getRegionList()
  {
    return regionList;
  }


  @Override
  public void setDocumentLocator(Locator locator)
  {
    this.locator = locator;
  }

  @Override
  public void startDocument() throws SAXException
  {
    regionList.clear();
    tmpPerimeterSet.clear();
    tmpRegion = null;
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
        }
        catch (Exception e)
        {
          System.out.println("ERROR inside vertex branch of" + this.getClass().getCanonicalName());
          /* TODO
              push error handeling HERE!
              1) null pointers
              2) Doulbe PArsing errors down here,
              move this out of the area XML loder.
           */
//          registerParsingProblem();
        }

        tmpPerimeterSet.add(new MapPoint(lat, lon));
        break;
    }

  }

  // todo refactor this method.
  private void registerParsingProblem() throws SAXException
  {
    SAXParseException exp;
    if (locator != null)
    {
      System.out.println(locator.getLineNumber() + "locator was set!!!");
      exp = new SAXParseException("(!) problem at line: " + locator.getLineNumber(), locator);
    }
    else
    {
      exp = new SAXParseException("(!) problem with vertex attribute.", null);
    }
    fatalError(exp);
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
      tmpRegion.setPerimeter(new ArrayList<MapPoint>(tmpPerimeterSet));
      regionList.add(tmpRegion);
      tmpPerimeterSet.clear();
    }
  }
}


