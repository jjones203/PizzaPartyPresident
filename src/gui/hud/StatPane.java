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
 * <p>
 * UI container element. Encapsulates a collection of BarPanel objects,
 * creates a panel that generates and plots bar graphs.
 */
public class StatPane extends JPanel
{
  private final static Color BORDER_COL = ColorSchemes.GUI_TEXT_COLOR.darker();
  private final static Font TITLE_FONT = new Font("SansSerif", Font.PLAIN, 14);
  private final static Color GUI_BACKGROUND = ColorSchemes.GUI_BACKGROUND;
  private final static Color FORGROUND_COL = ColorSchemes.GUI_TEXT_COLOR;
  private JPanel bargraphs;
  private JLabel titleLable;

  public StatPane(String name)
  {
    //init
    bargraphs = new JPanel();
    titleLable = new JLabel(name);
    JPanel titlePane = new JPanel();

    //config
    titlePane.setBackground(GUI_BACKGROUND);
    titlePane.setLayout(new FlowLayout(FlowLayout.LEFT));
    titlePane.setBorder(
        BorderFactory.createMatteBorder(0, 0, 2, 0, BORDER_COL));

    titleLable.setFont(TITLE_FONT);
    titleLable.setForeground(FORGROUND_COL);
    titleLable.setHorizontalAlignment(SwingConstants.LEFT);

    bargraphs.setBackground(GUI_BACKGROUND);
    bargraphs.setBorder(new EmptyBorder(5, 5, 5, 5));
    bargraphs.setLayout(new BoxLayout(bargraphs, BoxLayout.Y_AXIS));

    //wire
    titlePane.add(titleLable);
    setLayout(new BorderLayout());
    add(titlePane, BorderLayout.NORTH);
    add(bargraphs, BorderLayout.CENTER);
  }

  /**
   * Adds another barPanel to the component.
   *
   * @param barPanel barPanel to be displayed.
   */
  public void addBar(BarPanel barPanel)
  {
    bargraphs.add(barPanel);
  }

  /**
   * Removes all the currently registered bar plots.
   */
  public void cleanBarPlots()
  {
    //todo test this method
    bargraphs.removeAll();
  }

  /**
   * Removes the bar element at specified index.
   * order => order elements added.
   *
   * @param index position of element to remove
   */
  public void removeBar(int index)
  {
    bargraphs.remove(index);
  }

  public BarPanel getBarPanel(int index)
  {
    try
    {
      return (BarPanel) bargraphs.getComponent(index);
    }
    catch (Exception e)
    {
      System.err.println("could not cast object to barpanbel");
      return null;
    }
  }


  /**
   * Sets the title of the StatePane to the specified string.
   *
   * @param title string representing the title of the panel.
   */
  public void setTitle(String title)
  {
    titleLable.setText(title);
  }

  // only for testing.
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
          random.nextBoolean() ? Color.cyan : Color.red,
          pval,
          s.toUpperCase(),
          String.format("%.2f", pval)
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


    Timer setTitle = new Timer(1000 * 5, new AbstractAction()
    {
      @Override
      public void actionPerformed(ActionEvent e)
      {
        System.out.println("SETTING LABLE");
        stats.setTitle("QUALITATIVE DATA:");
        stats.cleanBarPlots();
        stats.addBar(new BarPanel(Color.magenta, 1, "Planting Zone", "ZONE 1"));
        stats.addBar(new BarPanel(Color.green, 1, "Soil Type", "TYPE 4"));
        stats.addBar(new BarPanel(Color.yellow, 1, "HAPPYNESS INDEX:", "VERY"));
      }
    });
    setTitle.setRepeats(false);
    setTitle.start();

    Timer addOneMoreBar = new Timer(1000 * 8, new AbstractAction()
    {
      @Override
      public void actionPerformed(ActionEvent e)
      {
        System.out.println("adding two new bars, taking one away");
        stats.addBar(new BarPanel(Color.green, .5, "Bar added mid way through"));
        stats.addBar(new BarPanel(Color.PINK, 1.25, "second bar added mid way through"));
        stats.removeBar(1);
        stats.getBarPanel(0).setLabelText("new Text!");

        stats.revalidate(); // this is needed to repait the newly added components.
      }
    });
    addOneMoreBar.setRepeats(false);
    addOneMoreBar.start();
  }

}
