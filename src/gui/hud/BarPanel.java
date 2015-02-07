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

  private static final Color BAR_TEXT_C = Color.black;
  public static final Color TEXT_ROLLOVER_C = new Color(255, 165, 148);
  public static final Color BAR_ROLLOVER_C = Color.gray;

  private final Color originalBarColor;
  private Color overLayTextColor;
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
   * @param labelText String that will be displayGUIRegion labeling the bar
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
   * @param labelText   String that will be displayGUIRegion labeling the bar
   * @param overLayText String that will be displayed on top of the bar.
   *                    (to show the ratio passed in for example
   */
  public BarPanel(Color barColor, double ratio, String labelText, String overLayText)
  {

    //init
    this.originalBarColor = barColor;
    this.barColor = barColor;
    this.overLayTextColor = BAR_TEXT_C;
    this.ratio = ratio;
    this.overLayText = overLayText;

    // 6000 is just to make things too big! fighting with swing.
    Dimension size = new Dimension(6000, 16);
    setMaximumSize(size);


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
        overLayTextColor = TEXT_ROLLOVER_C;
        barColor = BAR_ROLLOVER_C;
        label.setForeground(TEXT_ROLLOVER_C);
      }

      @Override
      public void mouseExited(MouseEvent e)
      {
        overLayTextColor = BAR_TEXT_C;
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

        // position info, animation, and seize of bar, should correlate to font
        // size.
        g.fillRect(10, 2, animationStep, 12);

        // if over lay text has been specified.
        if (overLayText != null)
        {
          ((Graphics2D) g).setRenderingHint(
            RenderingHints.KEY_TEXT_ANTIALIASING,
            RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

          g.setColor(overLayTextColor);
          g.setFont(OVERLAY_FONT);
          g.drawString(overLayText, 12, 12);
        }
      }
    };
  }
}
