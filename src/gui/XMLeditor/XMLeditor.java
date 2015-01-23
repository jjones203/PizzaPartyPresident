package gui.xmleditor;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rtextarea.RTextScrollPane;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import java.awt.*;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;




/**
 * Created by winston on 1/22/15.
 * Phase_01
 * CS 351 spring 2015
 */
public class XMLeditor extends JFrame
{
  private String currentFile;
  private RSyntaxTextArea textArea = new RSyntaxTextArea();
  private final static Color HILIGHT_ERROR = new Color(255, 141, 45, 140);
  private final static Font EDITOR_FONT = new Font("Helvetica", Font.PLAIN, 16);

  public XMLeditor()
  {

    textArea.setFont(EDITOR_FONT);
    textArea.setAntiAliasingEnabled(true);

    textArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_XML);
    RTextScrollPane scrollPane = new RTextScrollPane(textArea);

    setTitle("XML editor");
    setLayout(new BorderLayout());
    add(scrollPane, BorderLayout.CENTER);
    add(getControllPanel(), BorderLayout.SOUTH);

    setSize(500, 500);
    setDefaultCloseOperation(EXIT_ON_CLOSE);
  }

  private JPanel getControllPanel()
  {
    JPanel controlP = new JPanel();

    JButton save = new JButton("Save");
    JButton saveAs = new JButton("Save As");
    JButton exit = new JButton("Exit");

    controlP.add(save);
    controlP.add(saveAs);
    controlP.add(exit);

    return controlP;
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

  public void loadFile(String filename)
  {
    try
    {
      FileReader reader = new FileReader(filename);
      textArea.read(reader, null);
      currentFile = filename;
    }
    catch (FileNotFoundException e)
    {
      e.printStackTrace();
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
  }

  private void writeTo(String filename)
  {
    try
    {
      FileWriter writer = new FileWriter(filename);
      textArea.write(writer);
      writer.close();
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
