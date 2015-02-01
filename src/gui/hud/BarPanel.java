package gui.hud;

import gui.ColorSchemes;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * Created by winston on 1/31/15.
 * Draws a single bar of a bar chart.
 */
public class BarPanel extends JPanel
{
  public static final Font GUI_FONT = ColorSchemes.GUI_FONT;
  public static final Font OVERLAY_FONT = new Font("SansSerif", Font.PLAIN, 10);

  private final Color originalBarColor;
  private Color overLayColor;
  private Color barColor;
  private JLabel lable;
  private double value;
  private Component barGraph;
  private String labletxt;
  private String overLayText;

  private int animationStep = 0;



  /**
   * Contructor for class.
   * @param barColor the barColor of the bar to be draw
   * @param value a double between 0 and 1, 1 being 'full'.
   * @param labletxt String that will be display labeling the bar
   */
  public BarPanel(Color barColor, double value, String labletxt)
  {
    this(barColor, value, labletxt, null);
  }

  /**
   * Contructor for class.
   * @param barColor the barColor of the bar to be draw
   * @param value a double between 0 and 1, 1 being 'full'.
   * @param labletxt String that will be display labeling the bar
   * @param overLayText String that will be displayed on top of the bar.
   *                    (to show the value passed in for example
   */
  public BarPanel(Color barColor, double value, String labletxt, String overLayText)
  {

    //init
    this.originalBarColor = barColor;
    this.barColor = barColor;
    this.overLayColor = Color.black;
    this.value = value;
    this.labletxt = labletxt;
    this.overLayText = overLayText;

    setLayout(new GridLayout(1, 2));
    lable = new JLabel(labletxt);
    barGraph = getBarPane();

    //config
    setBackground(ColorSchemes.GUI_BACKGROUND);

    lable.setFont(GUI_FONT);
    lable.setForeground(ColorSchemes.GUI_TEXT_COLOR);
    lable.setHorizontalAlignment(SwingConstants.LEFT);
    lable.setVerticalAlignment(SwingConstants.TOP);

    addMouseListener(getMouseListener());

    // tool tip setup
//    createToolTip();
//    setToolTipText(Double.toString(value));

    //wire
    add(lable);
    add(barGraph);
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
        lable.setForeground(Color.white);
      }

      @Override
      public void mouseExited(MouseEvent e)
      {
        overLayColor = Color.black;
        barColor = originalBarColor;
        lable.setForeground(ColorSchemes.GUI_TEXT_COLOR);
      }
    };
  }

  private Component getBarPane()
  {
    return new JPanel()
    {
      @Override
      protected void paintComponent(Graphics g)
      {
        int length = (int) (value * 100);

        if (animationStep >= length) animationStep = length;
        else animationStep += 3; // animation step;

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
