package IO.XMLparsers;

/**
 * Created by winston on 1/20/15.
 */

import model.MapPoint;
import model.Region;
import org.xml.sax.*;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;


/**
 * at this point this is being blindly developed from :
 * http://docs.oracle.com/javase/tutorial/jaxp/sax/parsing.html
 */
public class RegionParserHandeler extends DefaultHandler
{
  private List<Region> regionList = new ArrayList<>();

  private Region tmpRegion;
  private List<MapPoint> tmpPreemeterSet;

  private boolean nameTag,
          vertexTag;

  public List<Region> getRegionList()
  {
    return regionList;
  }

  @Override
  public void startElement(String namespaceURI,
                           String localName,
                           String qName,
                           Attributes atts)
          throws SAXException
  {

    if (qName.equals("area"))
    {
      tmpRegion = new Region();
      tmpPreemeterSet = new ArrayList<>();

      System.out.println("Starting area tag");
    }
    else if (qName.equals("name"))
    {
      System.out.println("encoutering nameTage");
      nameTag = true;
    }
    else if (qName.equals("vertex"))
    {
      double lat = Double.parseDouble(atts.getValue("lat"));
      double lon = Double.parseDouble(atts.getValue("lon"));
      MapPoint mapPoint = new MapPoint(lat, lon);
      tmpPreemeterSet.add(mapPoint);
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
  public void endElement(String uri, String localName, String qName) throws SAXException
  {
    if (qName.equals("area"))
    {
      System.out.println("add region to array and clean up");
      tmpRegion.setPermineter(tmpPreemeterSet);
      regionList.add(tmpRegion);
    }
  }

  //******//
  // MAIN //
  //******//
  public static void main(String[] args)
  {
    String fileName = "assets/XML/regions/newMexicoTest.xml";
    SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
    RegionParserHandeler handler = new RegionParserHandeler();
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
      for (MapPoint mp : region.getPermineter())
      {
        System.out.println(mp);
      }
    }
  }
//  private static String convertToFileURL(String filename)
//  {
//    String path = new File(filename).getAbsolutePath();
//    if (File.separatorChar != '/')
//    {
//      path = path.replace(File.separatorChar, '/');
//    }
//
//    if (!path.startsWith("/"))
//    {
//      path = "/" + path;
//    }
//    return "file:" + path;
//  }


  private static class MyErrorHandler implements ErrorHandler
  {
    private PrintStream out;

    public MyErrorHandler(PrintStream out)
    {
      this.out = out;
    }

    private String getParseExceptionInfo(SAXParseException spe)
    {
      String systemId = spe.getSystemId();

      if (systemId == null)
      {
        systemId = "null";
      }

      String info = "URI=" + systemId + " Line="
              + spe.getLineNumber() + ": " + spe.getMessage();

      return info;
    }

    public void warning(SAXParseException spe) throws SAXException
    {
      out.println("Warning: " + getParseExceptionInfo(spe));
    }

    public void error(SAXParseException spe) throws SAXException
    {
      String message = "Error: " + getParseExceptionInfo(spe);
      throw new SAXException(message);
    }

    public void fatalError(SAXParseException spe) throws SAXException
    {
      String message = "Fatal Error: " + getParseExceptionInfo(spe);
      throw new SAXException(message);
    }
  }


}

