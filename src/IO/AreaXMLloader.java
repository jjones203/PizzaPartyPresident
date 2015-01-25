package IO;

import IO.XMLparsers.RegionParserErrorHandler;
import IO.XMLparsers.RegionParserHandler;
import gui.xmleditor.XMLeditor;
import model.Region;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import javax.swing.*;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.util.Collection;

import static IO.IOhelpers.convertToFileURL;

/**
 * Created by winston on 1/25/15.
 * Phase_01
 * CS 351 spring 2015
 */
public class AreaXMLloader
{
  private RegionParserHandler handler;
  private RegionParserErrorHandler errorHandler;
  private String dirPath;

  private XMLReader xmlReader;


  public AreaXMLloader(String areaFolder)
  throws ParserConfigurationException, SAXException
  {
    this(
        new RegionParserHandler(),
        new RegionParserErrorHandler(),
        areaFolder
    );
  }

  public AreaXMLloader(RegionParserHandler handler,
                       RegionParserErrorHandler errorHandler, String dirPath)
  throws ParserConfigurationException, SAXException
  {
    this.handler = handler;
    this.errorHandler = errorHandler;
    this.dirPath = dirPath;


    SAXParserFactory spf = SAXParserFactory.newInstance();
    spf.setNamespaceAware(true);

    SAXParser saxParser = spf.newSAXParser();
    xmlReader = saxParser.getXMLReader();
    xmlReader.setContentHandler(handler);
    xmlReader.setErrorHandler(errorHandler);
  }

  public Collection<Region> getRegions()
  {

    return null;
  }

  public Collection<Region> parseFile(String filePath)
  {
    try
    {
      xmlReader.parse(convertToFileURL(filePath));
    } catch (IOException e)
    {
      e.printStackTrace();
      System.exit(1);
    } catch (SAXException e)
    {
//      e.printStackTrace();
      System.out.println("Parsing Exception:");
//      JOptionPane.showMessageDialog(null, "message");
//      System.exit(1);
    }
    return handler.getRegionList();
  }
}
