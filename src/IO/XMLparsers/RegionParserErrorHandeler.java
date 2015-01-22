package IO.XMLparsers;

import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * Created by winston on 1/22/15.
 * Phase_01
 * CS 351 spring 2015
 */
public class RegionParserErrorHandeler implements ErrorHandler
{
  @Override
  public void warning(SAXParseException exception) throws SAXException
  {
    System.out.println("warning generated: ");
  }

  @Override
  public void error(SAXParseException exception) throws SAXException
  {
    System.out.println("error generated");
  }

  @Override
  public void fatalError(SAXParseException exception) throws SAXException
  {
    System.out.println("fatal error generated");
    String fileName = exception.getSystemId();
    int lineNumber = exception.getLineNumber();
    String msg = exception.getLocalizedMessage();

    System.out.println(msg);
  }
}
