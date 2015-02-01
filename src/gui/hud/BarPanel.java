package gui.hud;

import gui.ColorSchemes;
import model.RegionAttributes;
import testing.generators.AttributeGenerator;

import javax.swing.*;
import java.awt.*;
import java.util.Random;

/**
 * Created by winston on 1/31/15.
 */
public class BarPanel extends JPanel
{
  private Color color;
  private JLabel lable;
  private double value;
  private Component barGraph;
  private String labletxt;


  public BarPanel(Color color, double value, String labletxt)
  {
    this.color = color;
    this.value = value;
    this.labletxt = labletxt;

    //init
    setLayout(new GridLayout(1, 2));
    lable = new JLabel(labletxt);
    barGraph = getBarPane();

    //config
    setBackground(ColorSchemes.GUI_BACKGROUND);

    lable.setFont(ColorSchemes.GUI_FONT);
    lable.setForeground(ColorSchemes.GUI_TEXT_COLOR);
    lable.setHorizontalAlignment(SwingConstants.LEFT);
    lable.setVerticalAlignment(SwingConstants.TOP);
    lable.setPreferredSize(getLableDim());
    lable.setMinimumSize(getLableDim());

    barGraph.setMinimumSize(new Dimension(115, 12));
    barGraph.setPreferredSize(new Dimension(115, 12));

    //wire
    add(lable);
    add(barGraph);
  }

  private Dimension getLableDim()
  {
    FontMetrics fontMetrics = lable.getFontMetrics(lable.getFont());
    int length = fontMetrics.charsWidth(labletxt.toCharArray(), 0, labletxt.length());
    int height = fontMetrics.getHeight();
    System.out.println("len and height: " + length + ", " + height);
    return new Dimension(length, height);
  }

  private Component getBarPane()
  {
    return new JPanel()
    {

      @Override
      protected void paintComponent(Graphics g)
      {
//        super.paintComponent(g);
        int length = (int) (value * 100);

        g.setColor(color);
        g.fillRect(10, 2, length, 12); //todo change 12 to font metric.

      }
    };
  }


  public static void main(String[] args)
  {

    JPanel mainPanel = new JPanel();
    mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
    mainPanel.setBackground(Color.gray);

    Random random = new Random();
    AttributeGenerator attGen = new AttributeGenerator(random);

    RegionAttributes atts = attGen.nextAttributeSet();

    for (String s : atts.getAllCropsPercentage().keySet())
    {
      BarPanel pb = new BarPanel(random.nextBoolean() ? Color.cyan : Color.red, random.nextDouble(), s.toUpperCase() + ":");
      mainPanel.add(pb);
    }



    JFrame frame = new JFrame();
    frame.setContentPane(mainPanel);
//    frame.setSize(width, 300);
    frame.pack();
    frame.setVisible(true);
    frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);


  }

}
