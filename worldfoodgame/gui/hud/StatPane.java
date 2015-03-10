package gui.hud;


import gui.ColorsAndFonts;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Created by winston on 1/31/15.
 * <p>
 * UI container element. Encapsulates a collection of BarPanel objects,
 * creates a panel that generates and plots bar graphs.
 */
public class StatPane extends JPanel
{
  private final static Color BORDER_COL = ColorsAndFonts.GUI_TEXT_COLOR.darker();
  private final static Font TITLE_FONT = ColorsAndFonts.HUD_TITLE;
  private final static Color GUI_BACKGROUND = ColorsAndFonts.GUI_BACKGROUND;
  private final static Color FOREGROUND_COL = ColorsAndFonts.GUI_TEXT_COLOR;
  private JPanel barGraphsPanel;
  private JLabel titleLable;

  /**
   * Constructor. The specified name is what is printed as the title of the
   * Stat Panel.
   * @param name
   */
  public StatPane(String name)
  {
    //init
    barGraphsPanel = new JPanel();
    titleLable = new JLabel(name);
    JPanel titlePane = new JPanel();

    //config
    titlePane.setBackground(GUI_BACKGROUND);
    titlePane.setLayout(new FlowLayout(FlowLayout.LEFT));
    titlePane.setBorder(
      BorderFactory.createMatteBorder(0, 0, 2, 0, BORDER_COL));

    titleLable.setFont(TITLE_FONT);
    titleLable.setForeground(FOREGROUND_COL);
    titleLable.setHorizontalAlignment(SwingConstants.LEFT);

    barGraphsPanel.setBackground(GUI_BACKGROUND);
    barGraphsPanel.setBorder(new EmptyBorder(5, 5, 5, 5));

    BoxLayout layout = new BoxLayout(barGraphsPanel, BoxLayout.Y_AXIS);
    barGraphsPanel.setLayout(layout);

    //wire
    titlePane.add(titleLable);
    setLayout(new BorderLayout());
    add(titlePane, BorderLayout.NORTH);
    add(barGraphsPanel, BorderLayout.CENTER);
  }


  /**
   * Adds another barPanel to the component.
   *
   * @param barPanel barPanel to be displayed.
   */
  public void addBar(BarPanel barPanel)
  {
    barPanel.setAlignmentY(TOP_ALIGNMENT);
    barGraphsPanel.add(barPanel);
  }

  /**
   * Removes all the currently registered bar plots.
   */
  public void clearBarPlots()
  {
    barGraphsPanel.removeAll();
  }

  /**
   * Removes the bar element at specified index.
   * order => order elements added.
   *
   * @param index position of element to remove
   */
  public void removeBar(int index)
  {
    barGraphsPanel.remove(index);
  }

  public BarPanel getBarPanel(int index)
  {
    try
    {
      return (BarPanel) barGraphsPanel.getComponent(index);
    }
    catch (Exception e)
    {
      System.err.println("could not cast object to bar panel");
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

}
