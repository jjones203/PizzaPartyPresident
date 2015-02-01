package gui.hud;

import gui.ColorSchemes;
import javafx.application.Application;
import model.RegionAttributes;
import testing.generators.AttributeGenerator;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Random;

/**
 * Created by winston on 1/31/15.
 */
public class StatPane extends JPanel
{
  private JPanel bargraphs;
  private JLabel title;

  public StatPane(String name)
  {
    //init
    bargraphs = new JPanel();
    title = new JLabel(name);
    JPanel titlePane = new JPanel();

    //config
    titlePane.setBackground(ColorSchemes.GUI_BACKGROUND);
    titlePane.setLayout(new FlowLayout(FlowLayout.LEFT));
    titlePane.setBorder(
        BorderFactory.createMatteBorder(0, 0, 2, 0, ColorSchemes.GUI_TEXT_COLOR.darker()));
    titlePane.add(title);

    title.setFont(new Font("SansSerif", Font.PLAIN, 14));
    title.setForeground(ColorSchemes.GUI_TEXT_COLOR);
    title.setHorizontalAlignment(SwingConstants.LEFT);

    bargraphs.setBackground(ColorSchemes.GUI_BACKGROUND);
    bargraphs.setBorder(new EmptyBorder(5, 5, 5, 5));
    bargraphs.setLayout(new BoxLayout(bargraphs, BoxLayout.Y_AXIS));

    //wire
    setLayout(new BorderLayout());
    add(titlePane, BorderLayout.NORTH);
    add(bargraphs, BorderLayout.CENTER);
  }

  public void addBar(BarPanel barPanel)
  {
    bargraphs.add(barPanel);
  }

  public static void main(String[] args)
  {
    final JFrame frame = new JFrame();
    StatPane stats = new StatPane("CROPS:");

    Random random = new Random();
    RegionAttributes atts = new AttributeGenerator(random).nextAttributeSet();

    for (String s : atts.getAllCrops())
    {
      double pval = random.nextDouble();
      BarPanel bp = new BarPanel(
          random.nextBoolean()? Color.cyan : Color.red,
          pval,
          s.toUpperCase()
      );
      stats.addBar(bp);
    }

//    for (RegionAttributes.PLANTING_ATTRIBUTES at : RegionAttributes.PLANTING_ATTRIBUTES.values())
//    {
//      stats.addBar(at.toString().toUpperCase(), random.nextDouble(), random.nextBoolean()? Color.cyan : Color.red);
//    }

    frame.add(stats);
    frame.pack();
    frame.setVisible(true);
    frame.setBackground(ColorSchemes.GUI_BACKGROUND);
    frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

    Timer timer = new Timer(10, new AbstractAction()
    {
      @Override
      public void actionPerformed(ActionEvent e)
      {
        frame.repaint();
      }
    });
    timer.start();

  }

}
