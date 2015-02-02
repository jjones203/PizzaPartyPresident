package gui.hud;

import gui.ColorsAndFonts;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * Created by winston on 1/31/15.
 * Draws a single bar of a bar chart, with a few labels.
 */
public class BarPanel extends JPanel
{
  private static final Font GUI_FONT = ColorsAndFonts.GUI_FONT;
  private static final Font OVERLAY_FONT = new Font("SansSerif", Font.PLAIN, 10);

  private final Color originalBarColor;
  private Color overLayColor;
  private Color barColor;
  private final JLabel label;
  private final double ratio;
  private final String overLayText;

  private int animationStep = 0;


  /**
   * Constructor for class.
   *
   * @param barColor  the barColor of the bar to be draw
   * @param ratio     a double between 0 and 1, 1 being 'full'.
   * @param labelText String that will be display labeling the bar
   */
  public BarPanel(Color barColor, double ratio, String labelText)
  {
    this(barColor, ratio, labelText, null);
  }

  /**
   * Constructor for class.
   *
   * @param barColor    the barColor of the bar to be draw
   * @param ratio       a double between 0 and 1, 1 being 'full'.
   * @param labelText   String that will be display labeling the bar
   * @param overLayText String that will be displayed on top of the bar.
   *                    (to show the ratio passed in for example
   */
  public BarPanel(Color barColor, double ratio, String labelText, String overLayText)
  {

    //init
    this.originalBarColor = barColor;
    this.barColor = barColor;
    this.overLayColor = Color.black;
    this.ratio = ratio;
    this.overLayText = overLayText;

    setLayout(new GridLayout(1, 2));
    label = new JLabel(labelText);
    Component barGraph = getBarPane();

    //config
    setBackground(ColorsAndFonts.GUI_BACKGROUND);

    label.setFont(GUI_FONT);
    label.setForeground(ColorsAndFonts.GUI_TEXT_COLOR);
    label.setHorizontalAlignment(SwingConstants.LEFT);
    label.setVerticalAlignment(SwingConstants.TOP);

    addMouseListener(getMouseListener());

    // tool tip setup
//    createToolTip();
//    setToolTipText(Double.toString(ratio));

    //wire
    add(label);
    add(barGraph);
  }

  public void setLabelText(String text)
  {
    label.setText(text);
  }

  private MouseListener getMouseListener()
  {
    return new MouseAdapter()
    {
      @Override
      public void mouseEntered(MouseEvent e)
      {
        overLayColor = Color.white;
        barColor = Color.gray;
        label.setForeground(Color.white);
      }

      @Override
      public void mouseExited(MouseEvent e)
      {
        overLayColor = Color.black;
        barColor = originalBarColor;
        label.setForeground(ColorsAndFonts.GUI_TEXT_COLOR);
      }
    };
  }

  /**
   * Generates an inner class to handel the custom drawing of the
   * bar.
   *
   * @return component that will be drawn in a special way.
   */
  private Component getBarPane()
  {
    return new JPanel()
    {
      @Override
      protected void paintComponent(Graphics g)
      {
        int length = (int) (ratio * 100); // this only needs to be computed once

        if (animationStep < length) animationStep += 3;

        g.setColor(barColor);
        g.fillRect(10, 2, animationStep, 12); //todo change 12 to font metric.

        // if over lay text has been specified.
        if (overLayText != null)
        {
          ((Graphics2D) g).setRenderingHint(
              RenderingHints.KEY_TEXT_ANTIALIASING,
              RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

          g.setColor(overLayColor);
          g.setFont(OVERLAY_FONT);
          g.drawString(overLayText, 12, 12);
        }
      }
    };
  }
}
