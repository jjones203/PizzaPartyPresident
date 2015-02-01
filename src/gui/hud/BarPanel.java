package gui.hud;

import gui.ColorSchemes;

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
  private static final Font GUI_FONT = ColorSchemes.GUI_FONT;
  private static final Font OVERLAY_FONT = new Font("SansSerif", Font.PLAIN, 10);

  private final Color originalBarColor;
  private Color overLayColor;
  private Color barColor;
  private final JLabel label;
  private final double value;
  private final String overLayText;

  private int animationStep = 0;


  /**
   * Constructor for class.
   *
   * @param barColor  the barColor of the bar to be draw
   * @param value     a double between 0 and 1, 1 being 'full'.
   * @param labelText String that will be display labeling the bar
   */
  public BarPanel(Color barColor, double value, String labelText)
  {
    this(barColor, value, labelText, null);
  }

  /**
   * Constructor for class.
   *
   * @param barColor    the barColor of the bar to be draw
   * @param value       a double between 0 and 1, 1 being 'full'.
   * @param labelText   String that will be display labeling the bar
   * @param overLayText String that will be displayed on top of the bar.
   *                    (to show the value passed in for example
   */
  public BarPanel(Color barColor, double value, String labelText, String overLayText)
  {

    //init
    this.originalBarColor = barColor;
    this.barColor = barColor;
    this.overLayColor = Color.black;
    this.value = value;
    this.overLayText = overLayText;

    setLayout(new GridLayout(1, 2));
    label = new JLabel(labelText);
    Component barGraph = getBarPane();

    //config
    setBackground(ColorSchemes.GUI_BACKGROUND);

    label.setFont(GUI_FONT);
    label.setForeground(ColorSchemes.GUI_TEXT_COLOR);
    label.setHorizontalAlignment(SwingConstants.LEFT);
    label.setVerticalAlignment(SwingConstants.TOP);

    addMouseListener(getMouseListener());

    // tool tip setup
//    createToolTip();
//    setToolTipText(Double.toString(value));

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
        label.setForeground(ColorSchemes.GUI_TEXT_COLOR);
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
        int length = (int) (value * 100);

        if (animationStep >= length)
        {
          animationStep = length;
        }
        else
        {
          animationStep += 3; // animation step;
        }

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
