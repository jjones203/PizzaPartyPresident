package worldfoodgame.planningpoints;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

import worldfoodgame.gui.ColorsAndFonts;

/**
 * 
 * @author Stephen Stromberg on 4/29/15
 * 
 * Represents the top panel on the planning points
 * allocation panel which shows a bar and a count
 * that express how many planning points the player
 * has free to invest
 *
 */
public class PlanningPointBarPanel extends JPanel implements ActionListener
{
  private final int BAR_HEIGHT=20;
  private final int BAR_LENGTH=200;
  private final Color BACKGROUND_BAR_COLOR = new Color(211,236,237);
  private final Color FOREGROUND_BAR_COLOR = new Color(23,203,227);
  private JLabel pointsRemaining;
  private Timer timer;
  
  PlanningPointBarPanel(JLabel pointsRemaining)
  {
    this.setBackground(ColorsAndFonts.GUI_BACKGROUND);
    this.pointsRemaining=pointsRemaining;
    timer = new Timer(10,this);
    timer.start();
    pointsRemaining.setForeground(ColorsAndFonts.GUI_TEXT_COLOR);
  }
 
  /**
   * Called by updateBar(). Paints a percentage
   * of the bar to represent how many planning points
   * remain
   * 
   */
  @Override
  public void paintComponent(Graphics g)
  {
    super.paintComponent(g);
    
    double pixPerPoint = 
        (double)BAR_LENGTH/(double)PlanningPointConstants.MAX_POINTS_PER_YEAR;
    
    int startingHeight=this.getBounds().height/2-BAR_HEIGHT/2;
    
    int barPercent =(int)
        (PlanningPointsData.getPlanningPointsAvalable()*pixPerPoint);
    
    g.setColor(BACKGROUND_BAR_COLOR);
    g.fillRect(0,startingHeight, BAR_LENGTH, BAR_HEIGHT);
    g.setColor(FOREGROUND_BAR_COLOR);
    g.fillRect(0,startingHeight,barPercent, BAR_HEIGHT);
  }
  
  /**
   * Called from timer to update
   * JLabel and bar graphic. Checks
   * the global static boolean to
   * see if the timer should end. 
   * This boolean is changed on the submit button
   */
  public void updateBar()
  {
    pointsRemaining.setText("Planning Points Remaining: "
        +PlanningPointsData.getPlanningPointsAvalable()
        +"/"+PlanningPointConstants.MAX_POINTS_PER_YEAR+" ");
    this.repaint();
    if(!PlanningPointsData.getRunning()) timer.stop();
  }

  @Override
  public void actionPerformed(ActionEvent arg0)
  {
    if(arg0.getSource()==timer) updateBar();
  }
  
  
  
}
