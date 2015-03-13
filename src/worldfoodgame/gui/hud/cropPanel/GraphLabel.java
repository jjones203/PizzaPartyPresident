package worldfoodgame.gui.hud.cropPanel;

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
  private static final Color ROLLOVER_C = Color.RED;
  private static Font labelTypeFace = ColorsAndFonts.GUI_FONT.deriveFont(13.5f);
  private static final int BAR_MAX_LEN = 130;

  private String label;
  private double percent;
  private Color barColor;
  private JPanel bar;
  private DecimalFormat formatter;
  private JLabel valueLabel;
  private final double LIMIT;
  private final double STEP;
  private final boolean isController;

  public GraphLabel(String label, double percent,
                    double limit, double step,
                    Color barColor, boolean isController, String formatPattern)
  {
    this.label = label;
    this.percent = percent;
    this.barColor = barColor;
    this.formatter = new DecimalFormat(formatPattern);
    this.valueLabel = new JLabel();
    this.LIMIT = limit;
    this.STEP = step;
    this.isController = isController;


    //init
    valueLabel.setText(formatter.format(percent));
    valueLabel.setForeground(ColorsAndFonts.GUI_TEXT_COLOR);
    valueLabel.setFont(labelTypeFace);

    //config
    setLayout(new BorderLayout());
    setBackground(ColorsAndFonts.GUI_BACKGROUND);

    //wire
    add(getControllPanel(label), BorderLayout.NORTH);
    add(getBar(), BorderLayout.CENTER);
  }

  private JPanel getControllPanel(String label)
  {
    JPanel tempPanel = new JPanel();
    tempPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
    tempPanel.setBackground(ColorsAndFonts.GUI_BACKGROUND);

    JLabel text = new JLabel(label+":");
    text.setForeground(ColorsAndFonts.GUI_TEXT_COLOR);
    text.setFont(labelTypeFace);

    if (isController) tempPanel.add(makeControll("-", -STEP));
    tempPanel.add(text);
    tempPanel.add(valueLabel);
    if (isController) tempPanel.add(makeControll("+", STEP));

    return tempPanel;
  }

  private JLabel makeControll(String sign, final double dx)
  {
    final JLabel controll = new JLabel(sign);
    controll.setForeground(ColorsAndFonts.GUI_TEXT_COLOR);
    controll.setFont(labelTypeFace);
    controll.addMouseListener(new MouseAdapter()
    {
      double epsilon = 0.001;
      Timer timer = new Timer(10, new AbstractAction()
      {
        @Override
        public void actionPerformed(ActionEvent e)
        {
          if (!(percent + (dx - epsilon) > LIMIT || percent + (dx + epsilon) < 0))
          {
            percent += dx;
            if (percent < 0) percent = 0.0;
            valueLabel.setText(formatter.format(percent));
          }
        }
      });

      @Override
      public void mouseEntered(MouseEvent e)
      {
        controll.setForeground(Color.red);
      }

      @Override
      public void mouseExited(MouseEvent e)
      {
        controll.setForeground(ColorsAndFonts.GUI_TEXT_COLOR);
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
    return controll;
  }

  private JPanel getBar()
  {
    return new JPanel()
    {
      @Override
      protected void paintComponent(Graphics g)
      {
        int barlen = (int) (percent / LIMIT * BAR_MAX_LEN);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(barColor);
        g2d.fillRect(6, -4, barlen, 14);
      }
    };
  }


  public static void main(String[] args)
  {
    GraphLabel graphLabel = new GraphLabel("Population", 10.0, 100, .2, Color.red, true, "##,#000 tons");

    final JFrame jFrame = new JFrame();
    jFrame.setLayout(new BoxLayout(jFrame.getContentPane(), BoxLayout.Y_AXIS));

    Double dtest = new Double(.20);


    jFrame.add(graphLabel);
    jFrame.add(new GraphLabel("corn", dtest, 1, 0.01, Color.red, false, "00"));
    jFrame.add(new GraphLabel("cornz", .20, 1, 0.01, Color.red, false, "00"));
    jFrame.add(new GraphLabel("cornz", dtest, 1, 0.01, Color.red, true, "00"));

    jFrame.pack();
    jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    jFrame.setVisible(true);

    new Timer(10, new AbstractAction()
    {
      @Override
      public void actionPerformed(ActionEvent e)
      {
        jFrame.repaint();
      }
    }).start();
  }
}
