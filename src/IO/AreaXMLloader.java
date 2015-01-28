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
  private String dirPath;
  private XMLeditor editor;
  private XMLReader xmlReader;


  /**
   * Constructor for class.
   *
   * @param areaFolder String represeting the folder of XML files to be read in
   * @throws ParserConfigurationException
   * @throws SAXException
   */
  public AreaXMLloader(String areaFolder)
      throws ParserConfigurationException, SAXException
  {
    this(new RegionParserHandler(), areaFolder);
  }

  /**
   * Constructor for class
   *
   * @param handler RegionParsing Handler for building and containing the data
   * @param dirPath path to folder where xml files are located.
   * @throws ParserConfigurationException
   * @throws SAXException
   */
  public AreaXMLloader(RegionParserHandler handler, String dirPath)
      throws ParserConfigurationException, SAXException
  {
    this.handler = handler;
    this.dirPath = dirPath;

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
   * @return
   */
  public Collection<Region> getRegions()
  {
    List<Region> regionList = new ArrayList<>();
    List<String> filesToRead = getFilesInDir(dirPath);


    while (!filesToRead.isEmpty())
    {
      String currentFile = filesToRead.remove(0);
      try
      {
        Collection<Region> tmpRegions = parseFile(currentFile);
        regionList.addAll(tmpRegions);
      }
      catch (SAXException e)
      {
        Locator locator = handler.getLocator();
        if (locator == null)
        {
          //todo this should still call the editor just with out a line number. and nust use the above current line
          System.out.println("NO LOCATOR! (LN 96) on " + this.getClass().getName());
          e.printStackTrace();
        }

        if (editor == null)
        {
          editor = new XMLeditor();
        }

        // TODO change this to either a proper Dialogue box,
        // or some info pane inside the editor.
        editor.setTitle("(!) " + e.getMessage());

        editor.loadFile(currentFile);
        editor.highlightLine(locator.getLineNumber() - 1);
        editor.setCaretToline(locator.getLineNumber() - 1);
        editor.setVisible(true);
        //TODO make editor track an ignore setting or something of the like....
        // so that the user can ignore a file is they so choose.

        filesToRead.add(0, currentFile);

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
   * @param filePath
   * @return
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
