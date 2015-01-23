package gui.xmleditor;

import javax.swing.*;
import javax.swing.text.BadLocationException;

import org.fife.ui.rtextarea.*;
import org.fife.ui.rsyntaxtextarea.*;

import java.awt.*;

public class TextEditorDemo extends JFrame {

  public TextEditorDemo() {

    JPanel cp = new JPanel(new BorderLayout());

    RSyntaxTextArea textArea = new RSyntaxTextArea(20, 60);
    textArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_XML);
    RTextScrollPane sp = new RTextScrollPane(textArea);
    cp.add(sp);

    textArea.setText("here is a text string\n" +
        "antoher line here\n" +
        "and another one here");

    try
    {
      textArea.addLineHighlight(0, Color.red);
    } catch (BadLocationException e)
    {
      e.printStackTrace();
    }


    setContentPane(cp);
    setTitle("Text Editor Demo");
    setDefaultCloseOperation(EXIT_ON_CLOSE);
    pack();
    setLocationRelativeTo(null);

  }

  public static void main(String[] args) {
    // Start all Swing applications on the EDT.
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        new TextEditorDemo().setVisible(true);
      }
    });
  }

}
