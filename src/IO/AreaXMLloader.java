package IO;

/**
 * Created by winston on 1/25/15.
 * Phase_01
 * CS 351 spring 2015
 */

import IO.XMLparsers.RegionParser;
import IO.XMLparsers.RegionParserHandler;
import gui.xmleditor.XMLeditor;
import model.Region;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import javax.swing.*;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static IO.IOhelpers.convertToFileURL;
import static IO.IOhelpers.getFilesInDir;

/**
 * Class to encapsulate the Processing of a folder of XML Files containing
 * area data.
 */
public class AreaXMLloader
{
  private RegionParser handler;
  private final static String AREA_DIR_PATH = "resources/areas";
  private XMLeditor editor;
  private XMLReader xmlReader;


  /**
   * Constructor for class.
   *
   * @throws ParserConfigurationException
   * @throws SAXException
   */
  public AreaXMLloader()
    throws ParserConfigurationException, SAXException
  {
    this(new RegionParserHandler());
  }

  /**
   * Constructor for class
   *
   * @param handler RegionParsing Handler for building and containing the data
   * @throws ParserConfigurationException
   * @throws SAXException
   */
  public AreaXMLloader(RegionParserHandler handler)
    throws ParserConfigurationException, SAXException
  {
    this.handler = handler;

    SAXParserFactory spf = SAXParserFactory.newInstance();

    spf.setNamespaceAware(true);

    SAXParser saxParser = spf.newSAXParser();
    xmlReader = saxParser.getXMLReader();
    xmlReader.setContentHandler(handler);
  }

  /**
   * Returns a collection of all the regions parsed from the files in the
   * specified DIR.
   *
   * @return Returns the set of regions expressed by the all the collection of
   * xml files
   */
  public Collection<Region> getRegions()
  {
    List<Region> regionList = new ArrayList<>();
    List<String> filesToRead = getFilesInDir(AREA_DIR_PATH);
    RegionValidator regionValidator = new RegionValidator();

    while (!filesToRead.isEmpty())
    {
      String currentFile = filesToRead.remove(0);
      try
      {
        Collection<Region> tmpRegions = parseFile(currentFile);

        for (Region r : tmpRegions) regionValidator.validate(r);

        regionList.addAll(tmpRegions);

      }
      catch (SAXException e)
      {
        if (editor == null) editor = new XMLeditor(); // to be lazy
        editor.loadFile(currentFile);

        Locator locator = handler.getLocator();

        if (locator.getLineNumber() != -1)
        {
          // we know the line that the error happened on
          editor.highlightLine(locator.getLineNumber()-1);
          editor.setCaretToline(locator.getLineNumber()-1);
        }

        editor.setTitle("editing: " + currentFile);
        editor.setErrorMessage(e.getMessage());
        editor.setVisible(true);

        if ( ! editor.getIgnoreFile())
        {
          filesToRead.add(0, currentFile);
        }
      }
      catch (IOException e)
      {
        e.printStackTrace();
      }
    }
    return regionList;
  }

  /**
   * Method used for parsing a given file. does no error handeling on its own.
   *
   * @param filePath file path to attemp to parse.
   * @return The collection of regions expressed in that file.
   * @throws IOException
   * @throws SAXException
   */
  public Collection<Region> parseFile(String filePath)
    throws IOException, SAXException
  {
    xmlReader.parse(convertToFileURL(filePath));
    return handler.getRegionList();
  }
}
