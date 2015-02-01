package gui.hud;

import gui.ColorSchemes;
import model.RegionAttributes;
import testing.generators.AttributeGenerator;

import javax.swing.*;
import java.awt.*;
import java.util.Random;

/**
 * Created by winston on 1/31/15.
 * Draws a single bar of a bar chart.
 */
public class BarPanel extends JPanel
{
  private Color color;
  private JLabel lable;
  private double value;
  private Component barGraph;
  private String labletxt;
  private String overLayText;

  private int animationStep = 0;



  /**
   * Contructor for class.
   * @param color the color of the bar to be draw
   * @param value a double between 0 and 1, 1 being 'full'.
   * @param labletxt String that will be display labeling the bar
   */
  public BarPanel(Color color, double value, String labletxt)
  {
    this(color, value, labletxt, null);
  }

  /**
   * Contructor for class.
   * @param color the color of the bar to be draw
   * @param value a double between 0 and 1, 1 being 'full'.
   * @param labletxt String that will be display labeling the bar
   * @param overLayText String that will be displayed on top of the bar.
   *                    (to show the value passed in for example
   */
  public BarPanel(Color color, double value, String labletxt, String overLayText)
  {

    //init
    this.color = color;
    this.value = value;
    this.labletxt = labletxt;
    this.overLayText = overLayText;

    setLayout(new GridLayout(1, 2));
    lable = new JLabel(labletxt);
    barGraph = getBarPane();

    //config
    setBackground(ColorSchemes.GUI_BACKGROUND);

    lable.setFont(ColorSchemes.GUI_FONT);
    lable.setForeground(ColorSchemes.GUI_TEXT_COLOR);
    lable.setHorizontalAlignment(SwingConstants.LEFT);
    lable.setVerticalAlignment(SwingConstants.TOP);

    //wire
    add(lable);
    add(barGraph);
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

        g.setColor(color);
        g.fillRect(10, 2, animationStep, 12); //todo change 12 to font metric.

        if (overLayText != null)
        {
          ((Graphics2D) g).setRenderingHint(
              RenderingHints.KEY_TEXT_ANTIALIASING,
              RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

          g.setColor(Color.black);
          g.setFont(new Font("SansSerif", Font.PLAIN, 10));
          g.drawString(overLayText, 12, 12);
        }
      }
    };
  }
}
