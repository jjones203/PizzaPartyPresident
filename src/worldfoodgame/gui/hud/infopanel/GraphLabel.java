package worldfoodgame.gui.hud.infopanel;

import worldfoodgame.gui.ColorsAndFonts;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DecimalFormat;

/**
 * Created by winston on 3/12/15.
 */
public class GraphLabel extends JPanel
{
  private static final int WIDTH = 1000;
  private static final int HEIGHT = 40;
  private static final Color ROLLOVER_C = Color.RED;
  private static final int BAR_MAX_LEN = 130;
  private static final Font labelTypeFace = ColorsAndFonts.GUI_FONT.deriveFont(13.5f);
  private final double LIMIT;
  private final double STEP;
  private final boolean isController;

  public void setEffectRunnable(Runnable effectRunnable)
  {
    this.effectRunnable = effectRunnable;
  }

  private Runnable effectRunnable;
  private double value;
  private final Color barColor;
  private DecimalFormat formatter;
  private JLabel valueLabel;

  /**
   * Create a new Graph Label object. Used to display country data and control
   * interface for user.
   *
   * @param label         string to be printed above the bar graph.
   * @param value         value to be draw as a bar.
   * @param limit         represents what a full bar would be. (used to scale the bar)
   * @param formatPattern defines how to display the numerical information.
   */
  public GraphLabel(String label, double value, double limit, String formatPattern)
  {
    this(label, value, limit, limit / 10.0, Color.red, false, new DecimalFormat(formatPattern));
  }


  /**
   * Create a new Graph Label object. Used to display country data and control
   * interface for user.
   *
   * @param label         string to be printed above the bar graph.
   * @param value         value to be draw as a bar.
   * @param limit         represents what a full bar would be. (used to scale the bar)
   * @param decimalFormat Formatter that determines how the label is printed.
   */
  public GraphLabel(String label, double value, double limit, DecimalFormat decimalFormat)
  {
    this(label, value, limit, limit / 10.0, Color.red, false, decimalFormat);
  }


  /**
   * Create a new Graph Label object. Used to display country data and control
   * interface for user.
   *
   * @param label         string to be printed above the bar graph.
   * @param value         value to be draw as a bar.
   * @param limit         represents what a full bar would be. (used to scale the bar)
   * @param formatPattern defines how to display the numerical information.
   * @param runnable      this runnable is executed whenever the controls are
   *                      engaged.
   */
  public GraphLabel(String label, double value,
                    double limit, String formatPattern, Runnable runnable)
  {
    this(label, value, limit, limit / 100.0, Color.red, true, new DecimalFormat(formatPattern));
    this.effectRunnable = runnable;
  }


  private GraphLabel(String label, double value,
                     double limit, double step,
                     Color barColor, boolean isController, DecimalFormat formatter)
  {
    this.value = value;
    this.barColor = barColor;
    this.formatter = formatter;
    this.valueLabel = new JLabel();
    this.LIMIT = limit;
    this.STEP = step;
    this.isController = isController;


    //init
    valueLabel.setText(formatter.format(value));
    valueLabel.setForeground(ColorsAndFonts.GUI_TEXT_COLOR);
    valueLabel.setFont(labelTypeFace);

    //config
    setLayout(new BorderLayout());
    setBackground(ColorsAndFonts.GUI_BACKGROUND);
    setMaximumSize(new Dimension(WIDTH, HEIGHT));

    //wire
    add(getControlPanel(label), BorderLayout.NORTH);
    add(getBar(), BorderLayout.CENTER);
  }

//  // FOR TESTING ONLY
  // todo remove:
//  public static void main(String[] args)
//  {
//    GraphLabel graphLabel = new GraphLabel("Population", 10.0, 100, .2, Color.red, true, "##,#000 tons");
//
//    final JFrame jFrame = new JFrame();
//    jFrame.setLayout(new BoxLayout(jFrame.getContentPane(), BoxLayout.Y_AXIS));
//
//    double dtest = 20;
//
//
//    jFrame.add(graphLabel);
//    jFrame.add(new GraphLabel("corn", dtest, 1, 0.01, Color.red, false, "00"));
//    jFrame.add(new GraphLabel("cornz", .20, 1, 0.01, Color.red, false, "00"));
//    jFrame.add(new GraphLabel("cornz", dtest, 1, 0.01, Color.red, true, "00"));
//
//    jFrame.pack();
//    jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
//    jFrame.setVisible(true);
//
//    new Timer(10, new AbstractAction()
//    {
//      @Override
//      public void actionPerformed(ActionEvent e)
//      {
//        jFrame.repaint();
//      }
//    }).start();
//  }

  public double getValue()
  {
    return value;
  }

  public void setValue(double value)
  {
    this.value = value;
    valueLabel.setText(formatter.format(value));
  }

  private JPanel getControlPanel(String label)
  {
    JPanel tempPanel = new JPanel();
    tempPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
    tempPanel.setBackground(ColorsAndFonts.GUI_BACKGROUND);

    JLabel text = new JLabel(label + ":");
    text.setForeground(ColorsAndFonts.GUI_TEXT_COLOR);
    text.setFont(labelTypeFace);

    if (isController) tempPanel.add(makeControl("-", -STEP));
    tempPanel.add(text);
    tempPanel.add(valueLabel);
    if (isController) tempPanel.add(makeControl("+", STEP));

    return tempPanel;
  }

  private JLabel makeControl(String sign, final double dx)
  {
    final JLabel control = new JLabel(sign);
    control.setForeground(ColorsAndFonts.GUI_TEXT_COLOR);
    control.setFont(labelTypeFace);
    control.addMouseListener(new MouseAdapter()
    {
      final double epsilon = 0.001;
      final Timer timer = new Timer(10, new AbstractAction()
      {
        @Override
        public void actionPerformed(ActionEvent e)
        {
          if (!(value + (dx - epsilon) > LIMIT || value + (dx + epsilon) < 0))
          {
            value += dx;
            if (value < 0) value = 0.0;
            valueLabel.setText(formatter.format(value));
            if (effectRunnable != null)
            {
              effectRunnable.run();
            }
          }
        }
      });

      @Override
      public void mouseEntered(MouseEvent e)
      {
        control.setForeground(Color.red);
      }

      @Override
      public void mouseExited(MouseEvent e)
      {
        control.setForeground(ColorsAndFonts.GUI_TEXT_COLOR);
      }

      @Override
      public void mousePressed(MouseEvent e)
      {
        timer.start();
      }

      @Override
      public void mouseReleased(MouseEvent e)
      {
        timer.stop();
      }
    });
    return control;
  }

  private JPanel getBar()
  {
    return new JPanel()
    {
      @Override
      protected void paintComponent(Graphics g)
      {
        int barLen = (int) (value / LIMIT * BAR_MAX_LEN);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(barColor);
        g2d.fillRect(6, -4, barLen, 14);
      }
    };
  }
}
