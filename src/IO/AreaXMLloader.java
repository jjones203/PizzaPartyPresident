package IO;

/**
 * Created by winston on 1/25/15.
 * Phase_01
 * CS 351 spring 2015
 */


import IO.XMLparsers.RegionParserHandler;
import gui.XMLEditor;
import model.Region;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static IO.IOHelpers.convertToFileURL;
import static IO.IOHelpers.getFilesInDir;

/**
 * Class to encapsulate the Processing of a folder of XML Files containing
 * area data.
 */
public class AreaXMLLoader
{
  private RegionParserHandler handler;
  private final static String AREA_DIR_PATH = "resources/areas";
  private XMLEditor editor;
  private XMLReader xmlReader;


  /**
   * Constructor for class.
   *
   */
  public AreaXMLLoader()
  {
    this(new RegionParserHandler());
  }

  /**
   * Constructor for class
   *
   * @param handler RegionParsing Handler for building and containing the data
   */
  public AreaXMLLoader(RegionParserHandler handler)
  {
    this.handler = handler;

    SAXParserFactory spf = SAXParserFactory.newInstance();

    spf.setNamespaceAware(true);

    SAXParser saxParser = null;
    try
    {
      saxParser = spf.newSAXParser();
      xmlReader = saxParser.getXMLReader();
    }
    catch (ParserConfigurationException | SAXException e)
    {
      e.printStackTrace();
    }
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
      catch (SAXException e) //routine for loading the editor.
      {
        String errorMessage = "";
        if (editor == null) editor = new XMLEditor(); // to be lazy
        editor.loadFile(currentFile);

        Locator locator = handler.getLocator();

        if (locator.getLineNumber() != -1)
        {
          // we know the line that the error happened on
          editor.highlightLine(locator.getLineNumber() - 1);
          editor.setCaretToLine(locator.getLineNumber() - 1);
          errorMessage = "line " + locator.getLineNumber() + ": ";
        }

        editor.setTitle("editing: " + currentFile);
        editor.setErrorMessage(errorMessage + e.getMessage());
        editor.setVisible(true);

        if (!editor.getIgnoreFile())
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
   * Method used for parsing a given file. does no error handling on its own.
   *
   * @param filePath file path to attempt to parse.
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
