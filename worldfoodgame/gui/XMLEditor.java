package gui;

/**
 * Created by winston on 1/22/15.
 * Phase_01
 * CS 351 spring 2015
 *
 * XML Editor.
 */

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rtextarea.RTextScrollPane;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;


/**
 * GUI XML editor. Gui interface for editing and saving XML files in the
 * context of the game's initialization process.
 */
public class XMLEditor extends JDialog
{
  private final static Color HIGHLIGHT_ERROR = ColorsAndFonts.XML_ERROR;
  private final static Font EDITOR_FONT = new Font("Monospaced", Font.PLAIN, 14);
  private String currentFile;
  private RSyntaxTextArea textArea = new RSyntaxTextArea();
  private JLabel errorMsg;
  private boolean ignoreFile;
  private RTextScrollPane scrollPane;

  /**
   * Constructor for XMLEditor.
   */
  public XMLEditor()
  {
    setModal(true);
    textArea.setFont(EDITOR_FONT);
    textArea.setAntiAliasingEnabled(true);
    textArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_XML);
    scrollPane = new RTextScrollPane(textArea);

    setLayout(new BorderLayout());
    add(scrollPane, BorderLayout.CENTER);
    add(getControlPanel(), BorderLayout.SOUTH);

    errorMsg = new JLabel();
    errorMsg.setHorizontalAlignment(SwingConstants.CENTER);
    errorMsg.setForeground(Color.red.darker());
    add(errorMsg, BorderLayout.NORTH);

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

    final JButton exit = new JButton("Exit");
    exit.addActionListener(new AbstractAction()
    {
      @Override
      public void actionPerformed(ActionEvent e)
      {
        dispose();
      }
    });
    controlP.add(exit);

    JButton ignoreBtn = new JButton("Ignore");
    ignoreBtn.createToolTip();
    ignoreBtn.setToolTipText("mark file as DO NOT LOAD");
    ignoreBtn.addActionListener(new AbstractAction()
    {
      @Override
      public void actionPerformed(ActionEvent e)
      {
        ignoreFile = true;
        exit.doClick();
      }
    });
    controlP.add(ignoreBtn);

    return controlP;
  }

  /**
   * Will highlight a line as an error.
   * @param lineNum line number
   */
  public void highlightLine(int lineNum)
  {
    try
    {
      textArea.addLineHighlight(lineNum, HIGHLIGHT_ERROR);
    }
    catch (BadLocationException e)
    {
      e.printStackTrace();
    }
  }


  /**
   * Moves the Caret to the given line number
   * @param lineNum line number to move to.
   */
  public void setCaretToLine(int lineNum)
  {
    int moveTo = textArea.getDocument()
                    .getDefaultRootElement()
                    .getElement(lineNum)
                    .getStartOffset();

    textArea.setCaretPosition(moveTo);

    int caretPos = textArea.getCaretLineNumber();
    int lineHeight = textArea.getLineHeight();
    Rectangle viewBox = new Rectangle(1, caretPos * lineHeight, 1, 1);
    textArea.scrollRectToVisible(viewBox);
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
      ignoreFile = false;
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
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
  }


  /**
   * Set the user feed back error message. Used to convey what kind of Error has
   * occurred.
   * @param message to be displayed in the GUI.
   */
  public void setErrorMessage(String message)
  {
    errorMsg.setText(message);
  }

  /**
   * Used to retrieve a flag as to whether the current file should be ignored
   * or not.
   * @return boolean value for if the file is still of interest.
   */
  public boolean getIgnoreFile()
  {
    return ignoreFile;
  }
}
