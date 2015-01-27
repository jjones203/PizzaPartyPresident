package IO;

import IO.XMLparsers.RegionParserErrorHandler;
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
 * Created by winston on 1/25/15.
 * Phase_01
 * CS 351 spring 2015
 */
public class AreaXMLloader
{
  private RegionParserHandler handler;
  private String dirPath;
  private XMLeditor editor;

  private XMLReader xmlReader;


  public AreaXMLloader(String areaFolder)
  throws ParserConfigurationException, SAXException
  {
    this(
        new RegionParserHandler(),
        areaFolder
    );
  }

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

      } catch (SAXException e)
      {
        Locator locator = handler.getLocator();
        if (locator == null) //todo this should still call the editor just with out a line number. and nust use the above current line
        {
          e.printStackTrace();
        }

        if (editor == null)
        {
          editor = new XMLeditor();
        }

        editor.setTitle("(!) " + e.getMessage());

        editor.loadFile(currentFile);
        editor.highlightLine(locator.getLineNumber() - 1);
        editor.setVisible(true);
        //TODO make editor track an ignore setting or something of the like.... so that the user can ignore a file is they choose.

        filesToRead.add(0, currentFile);

      } catch (IOException e)
      {
        // could not process file will ignore
//        filesToIgnore.add(currentFile);
        e.printStackTrace();
      }
    }


    return regionList;
  }


  public Collection<Region> parseFile(String filePath)
  throws IOException, SAXException
  {
    xmlReader.parse(convertToFileURL(filePath));
    return handler.getRegionList();
  }
}

/* TODO rethink the error handeling implementation....
    we also need to handel logical errors, regions with log at out of bounds...
    and complex polygons, these error need to be able to triggler editing/saving
    offending XML file, and then reparsing it.
 */