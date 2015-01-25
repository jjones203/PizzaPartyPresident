package IO.XMLparsers;

import gui.xmleditor.XMLeditor;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * Created by winston on 1/22/15.
 * Phase_01
 * CS 351 spring 2015
 */
public class RegionParserErrorHandler implements ErrorHandler
{
  private XMLeditor editor;
//  private RegionParserHandler handler;
//
//  public RegionParserErrorHandler(RegionParserHandler handler)
//  {
//    this.handler = handler;
//  }

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
    if (editor == null) editor = new XMLeditor();

    System.out.println("fatal error generated");
    String fileName = exception.getSystemId();
    int lineNumber = exception.getLineNumber();
    String msg = exception.getLocalizedMessage();

    editor.loadFile(fileName.substring(5));
    editor.highlightLine(lineNumber);
    editor.setVisible(true);

    System.out.println(msg);
  }
}
