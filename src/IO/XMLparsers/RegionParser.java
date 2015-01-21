package IO.XMLparsers;

/**
 * Created by winston on 1/20/15.
 */

import model.Region;
import org.xml.sax.*;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.awt.*;
import java.io.File;
import java.io.PrintStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;


/**
 * at this point this is being blindly developed from :
 * http://docs.oracle.com/javase/tutorial/jaxp/sax/parsing.html
 */
public class RegionParser extends DefaultHandler
{
  private List<Region> regionList;
  private Region tmpRegion;
  private List<Point> tmpPreemeterSet;

  private boolean nameTag,
          vertexTag;

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
      System.out.println("starting area tag");
    }
    else if (qName.equals("name"))
    {
      System.out.println("encoutering nameTage");
      nameTag = true;
    }
    else if (qName.equals("vertex"))
    {
      System.out.println("encoutering Vertextag");
      vertexTag = true;
    }
  }


  @Override
  public void characters(char[] ch, int start, int length) throws SAXException
  {
    if (nameTag || vertexTag)
    {
      System.out.println(new String(ch, start, length));
    }

  }


  //todo implemnt main and start testing....
  private static String convertToFileURL(String filename)
  {
    String path = new File(filename).getAbsolutePath();
    if (File.separatorChar != '/')
    {
      path = path.replace(File.separatorChar, '/');
    }

    if (!path.startsWith("/"))
    {
      path = "/" + path;
    }
    return "file:" + path;
  }


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

