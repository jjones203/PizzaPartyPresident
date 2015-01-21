package IO.XMLparsers;

/**
 * Created by winston on 1/20/15.
 */

import org.xml.sax.*;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.PrintStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Enumeration;
import java.util.Hashtable;


/**
 * at this point this is being blindly developed from :
 * http://docs.oracle.com/javase/tutorial/jaxp/sax/parsing.html
 */
public class XMLParser extends DefaultHandler
{
  private Hashtable tags;

  @Override
  public void startElement(String namespaceURI,
                           String localName,
                           String qName,
                           Attributes atts)
          throws SAXException
  {

    String key = localName;
    Object value = tags.get(key);

    if (value == null)
    {
      tags.put(key, new Integer(1));
    }
    else
    {
      int count = ((Integer) value).intValue();
      count++;
      tags.put(key, new Integer(count));
    }
  }

  @Override
  public void endDocument() throws SAXException
  {
    Enumeration e = tags.keys();
    while (e.hasMoreElements())
    {
      String tag = (String) e.nextElement();
      int count = ((Integer) tags.get(tag)).intValue();
      System.out.println("Local Name \"" + tag + "\" occurs "
              + count + " times");
    }
  }

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

  public static void main(String[] args)
          throws Exception
  {
    System.out.println("from XMLparser:");
    String testingFile = "assets/XML/regions/employTest.xml";

    XMLParser parser = new XMLParser();

    SAXParserFactory spf = SAXParserFactory.newInstance();
    spf.setNamespaceAware(true);
    SAXParser saxParser = spf.newSAXParser();
    XMLReader xmlReader = saxParser.getXMLReader();
    xmlReader.setContentHandler(new XMLParser());
    xmlReader.setErrorHandler(new MyErrorHandler(System.err));


    String url = convertToFileURL(testingFile);

    Path path = Paths.get(testingFile);

    System.out.println(Files.readAllLines(path, Charset.defaultCharset()));

//    String url = convertToFileURL(testingFile);

    xmlReader.parse(testingFile);


  }
}

