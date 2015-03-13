package worldfoodgame.gui.hud.cropPanel;

import worldfoodgame.gui.ColorsAndFonts;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Created by winston on 3/12/15.
 */
public class GraphLabel extends JPanel
{
  public static final Color ROLLOVER_C = Color.RED;
  private static Font labelTypeFace = ColorsAndFonts.GUI_FONT.deriveFont(14f);
  private Dimension dimension = new Dimension(200, labelTypeFace.getSize() * 4);
  private String formatter;

//  private String label;
  private double percent;
//  private int animationStep;
  private Color barColor;
  private JPanel bar;

  private JLabel valueLabel;

  public GraphLabel(String label, double percent, Color barColor, boolean controll, String formatter)
  {
//    this.label = label;
    this.percent = percent;
    this.barColor = barColor;
    this.formatter = formatter;

    //init
    JLabel jLabel = new JLabel(label + " " + percent);
    jLabel.setBorder(new EmptyBorder(5, 5, 5, 10));
    jLabel.setForeground(ColorsAndFonts.GUI_TEXT_COLOR);
    valueLabel = new JLabel(String.format(formatter, percent));
    valueLabel.setForeground(ColorsAndFonts.GUI_TEXT_COLOR);
    bar = getBar();
//    animationStep = 0;

    //config
    jLabel.setFont(labelTypeFace);
    setLayout(new BorderLayout());
    setBackground(ColorsAndFonts.GUI_BACKGROUND);
    setPreferredSize(dimension);


    //wire
    add(controll ? getControllPanel(label) : jLabel, BorderLayout.NORTH);
    add(bar, BorderLayout.CENTER);
  }

  private JPanel getControllPanel(String label)
  {
    JPanel tempPanel = new JPanel();
    tempPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
    tempPanel.setBackground(ColorsAndFonts.GUI_BACKGROUND);

    final JLabel sub = getControll("-", -0.01);
    tempPanel.add(sub);

    JLabel text = new JLabel(label);
    text.setBorder(new EmptyBorder(0, 5, 0, 5));
    text.setForeground(ColorsAndFonts.GUI_TEXT_COLOR);
    text.setFont(labelTypeFace);
    tempPanel.add(text);

    tempPanel.add(valueLabel);

    tempPanel.add(getControll("+", 0.01));

    return tempPanel;
  }

  private JLabel getControll(String sign, final double dx)
  {
    final JLabel sub = new JLabel(sign);
    sub.setForeground(ColorsAndFonts.GUI_TEXT_COLOR);
    sub.setFont(labelTypeFace);
    sub.addMouseListener(new MouseAdapter()
    {
      double epsilon = 0.001;
      Timer timer = new Timer(10, new AbstractAction()
      {
        @Override
        public void actionPerformed(ActionEvent e)
        {
          if (!(percent + (dx - epsilon) > 1.00  || percent + (dx + epsilon) < 0))
          {
            percent += dx;
            if (percent < 0) percent = 0;
            valueLabel.setText(String.format(formatter, percent));
          }
        }
      });

      @Override
      public void mouseEntered(MouseEvent e)
      {
        sub.setForeground(Color.red);
      }

      @Override
      public void mouseExited(MouseEvent e)
      {
        sub.setForeground(ColorsAndFonts.GUI_TEXT_COLOR);
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
    return sub;
  }

  private JPanel getBar()
  {
    return new JPanel()
    {
      @Override
      protected void paintComponent(Graphics g)
      {
        int barlen = (int) (percent * 100);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(barColor);
        g2d.fillRect(6, 0, barlen, 14);
      }
    };
  }


  public static void main(String[] args)
  {
    GraphLabel graphLabel = new GraphLabel("Corn", .20, Color.red, true, "%.2f");

    final JFrame jFrame = new JFrame();
    jFrame.add(graphLabel);
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
