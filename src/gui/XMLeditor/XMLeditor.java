package gui.xmleditor;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rtextarea.RTextScrollPane;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import java.awt.*;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;




/**
 * Created by winston on 1/22/15.
 * Phase_01
 * CS 351 spring 2015
 */
public class XMLeditor extends JFrame
{
  private RSyntaxTextArea textArea = new RSyntaxTextArea();
  private final static Color HILIGHT_ERROR = new Color(255, 141, 45, 140);

  public XMLeditor()
  {

    textArea.setFont(new Font("Helvetica", Font.PLAIN, 16));
    textArea.setAntiAliasingEnabled(true);

    textArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_XML);
    RTextScrollPane scrollPane = new RTextScrollPane(textArea);

    setTitle("XML editor");
    setLayout(new BorderLayout());
    add(scrollPane, BorderLayout.CENTER);
    setSize(500, 500);
    setDefaultCloseOperation(EXIT_ON_CLOSE);
  }

  public void highlightLine(int lnum)
  {
    try
    {
      textArea.addLineHighlight(lnum, HILIGHT_ERROR);
    }
    catch (BadLocationException e)
    {
      e.printStackTrace();
    }
  }

  public void loadFile(String file)
  {
    try
    {
      FileReader reader = new FileReader(file);
      textArea.read(reader, null);
      reader.close();
    } catch (FileNotFoundException e)
    {
      e.printStackTrace();
    } catch (IOException e)
    {
      e.printStackTrace();
    }
  }

  public static void main(String[] args)
  {
    //to do, add thread safe stuff
    XMLeditor editor = new XMLeditor();
    editor.loadFile("resources/areas/newMexicoTest.xml");
    editor.highlightLine(13);
    editor.setVisible(true);
  }
}
