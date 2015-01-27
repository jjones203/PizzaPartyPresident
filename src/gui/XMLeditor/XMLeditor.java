package gui.xmleditor;

/**
 * Created by winston on 1/22/15.
 * Phase_01
 * CS 351 spring 2015
 */

import gui.ColorSchemes;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rtextarea.RTextScrollPane;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;


/**
 * User Interface for Editing XML files. This is a modal dialogue box
 */
public class XMLeditor extends JDialog
{
  private final static Color HILIGHT_ERROR = ColorSchemes.XML_ERROR;
  private final static Font EDITOR_FONT = new Font("Helvetica", Font.PLAIN, 16);
  private boolean isEdited;
  private String currentFile;
  private RSyntaxTextArea textArea = new RSyntaxTextArea();

  // this is probably overkill...
  private DocumentListener docListener = new DocumentListener()
  {
    @Override
    public void insertUpdate(DocumentEvent e)
    {

    }

    @Override
    public void removeUpdate(DocumentEvent e)
    {

    }

    @Override
    public void changedUpdate(DocumentEvent e)
    {
      isEdited = true;
      textArea.getDocument().removeDocumentListener(docListener);
    }
  };

  public XMLeditor()
  {
    setModal(true);
    textArea.setFont(EDITOR_FONT);
    textArea.setAntiAliasingEnabled(true);

    textArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_XML);
    RTextScrollPane scrollPane = new RTextScrollPane(textArea);

    setLayout(new BorderLayout());
    add(scrollPane, BorderLayout.CENTER);
    add(getControlPanel(), BorderLayout.SOUTH);

    setSize(700, 500);
    setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    setMinimumSize(new Dimension(400, 300));
  }


  private JPanel getControlPanel()
  {
    JPanel controlP = new JPanel();

    JButton save = new JButton("Save");
    save.addActionListener(new AbstractAction()
    {
      @Override
      public void actionPerformed(ActionEvent e)
      {
        writeTo(currentFile);
      }
    });

    controlP.add(save);

    // TODO implement this so that the user can make a copy!
//    JButton saveAs = new JButton("Save As");
//    controlP.add(saveAs);

    JButton exit = new JButton("Exit");
    exit.addActionListener(new AbstractAction()
    {
      @Override
      public void actionPerformed(ActionEvent e)
      {
        dispose();
      }
    });
    controlP.add(exit);

    /*TODO
      and a mark as ignore button to specify files that will simply be excluded
     */

    return controlP;
  }

  /**
   * Will hilight a line as an error.
   * @param lnum line number
   */
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

  /**
   * Will load a file into the editor
   * @param filename  path of file.
   */
  public void loadFile(String filename)
  {
    try
    {
      FileReader reader = new FileReader(filename);
      textArea.read(reader, null);
      currentFile = filename;
      isEdited = false;
      textArea.getDocument().addDocumentListener(docListener);
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
  }

  /**
   * saves the content of the editing frame to a file.
   * @param filename file to overwrite or create.
   */
  private void writeTo(String filename)
  {
    try
    {
      FileWriter writer = new FileWriter(filename);
      textArea.write(writer);
      writer.close();
      currentFile = filename;
      isEdited = false;
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
  }
}
