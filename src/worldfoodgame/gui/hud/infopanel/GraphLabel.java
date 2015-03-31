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
  private static final int BAR_MAX_LEN = 175;
  private static final Font labelTypeFace = ColorsAndFonts.GUI_FONT.deriveFont(13.5f);
  private final double LIMIT;
  private final double STEP;
  private final boolean isController;
  private final Color barColor;
  private Runnable effectRunnable;
  private double value;
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


  /* private constructor that exposes coloring information and step values*/
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

  /**
   * The specivied runnable with be envoked when a control label has been modified.
   * this is used to commuicate the the instaciating context what the effects
   * of the controlls sould be.
   * @param effectRunnable Runnable that is envoked when controlls are activated.
   */
  public void setEffectRunnable(Runnable effectRunnable)
  {
    this.effectRunnable = effectRunnable;
  }

  /**
   * Returns the value stored in the label.
   * Caller must keep track of the kind / maginitude of  value being represented.
   *
   * @return value being stored in label.
   */
  public double getValue()
  {
    return value;
  }

  /**
   * sets the stored value. This value is used to Draw the bar graph, limiting
   * values are value used at construction of object.
   * @param value to be visualized.
   */
  public void setValue(double value)
  {
    this.value = value;
    valueLabel.setText(formatter.format(value));
  }

  /* sets up and returns the label and possible controlls */
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

  /* encapsulated the control interface logic */
  private JLabel makeControl(String sign, final double dx)
  {
    final JLabel control = new JLabel(sign);
    control.setForeground(ColorsAndFonts.GUI_TEXT_COLOR);
    control.setFont(labelTypeFace);

    /* this timer is turned on and off by pressing the controll buttons */
    control.addMouseListener(new MouseAdapter()
    {
      final Timer timer = new Timer(10, new AbstractAction()
      {
        @Override
        public void actionPerformed(ActionEvent e)
        {
          value += dx;
          valueLabel.setText(formatter.format(value));
          if (effectRunnable != null)
          {
            effectRunnable.run();
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

  /* bar that actually is painted to represent the value*/
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
