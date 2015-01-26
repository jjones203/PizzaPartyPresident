package IO.XMLparsers;

import gui.xmleditor.XMLeditor;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import javax.swing.*;

/**
 * Created by winston on 1/22/15.
 * Phase_01
 * CS 351 spring 2015
 */
public class RegionParserErrorHandler implements ErrorHandler
{
  private XMLeditor editor;

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
    System.out.println("(!) FATAL ERROR IN: " + getClass().getCanonicalName());
    if (editor == null)
    {
      editor = new XMLeditor();
      editor.setSize(500, 700);
    }

    String fileName = exception.getSystemId();
    int lineNumber = exception.getLineNumber() - 1;
    String msg = exception.getLocalizedMessage();

    JOptionPane.showMessageDialog(null, msg);

    editor.loadFile(fileName.substring(5));
    editor.highlightLine(lineNumber);
    editor.setVisible(true);


    /*
    TODO try only setting member variables in the error parser when there is a problem,
    then get those val out of the class in the context in which it is used.
     */
  }
}
