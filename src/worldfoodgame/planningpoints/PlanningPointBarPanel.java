package worldfoodgame.planningpoints;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;

public class PlanningPointBarPanel extends JPanel
{
  private final int BAR_HEIGHT=20;
  private final int BAR_LENGTH=100;
  private final Color BACKGROUND_BAR_COLOR = new Color(211,236,237);
  private final Color FOREGROUND_BAR_COLOR = new Color(237,237,71);
  private int numPoints=0;
  
  PlanningPointBarPanel()
  {
    this.setBackground(Color.LIGHT_GRAY);
  }
  /**
   * Paints the cell array onto this Jpanel. This
   * is based on the viewing window's position.
   */
  @Override
  public void paintComponent(Graphics g)
  {
    super.paintComponent(g);
    g.setColor(BACKGROUND_BAR_COLOR);
    g.fillRect(0,this.getBounds().height/2-BAR_HEIGHT/2, BAR_LENGTH, BAR_HEIGHT);
    g.setColor(FOREGROUND_BAR_COLOR);
    g.fillRect(0,this.getBounds().height/2-BAR_HEIGHT/2, numPoints, BAR_HEIGHT);
  }
  
  public void updateBar(int points)
  {
    this.repaint();
    numPoints=points;
  }
}
