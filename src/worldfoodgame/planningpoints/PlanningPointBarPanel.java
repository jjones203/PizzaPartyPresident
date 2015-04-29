package worldfoodgame.planningpoints;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

public class PlanningPointBarPanel extends JPanel implements ActionListener
{
  protected final int BAR_HEIGHT=20;
  protected final int BAR_LENGTH=200;
  protected final Color BACKGROUND_BAR_COLOR = new Color(211,236,237);
  protected final Color FOREGROUND_BAR_COLOR = new Color(237,237,71);
  private JLabel pointsRemaining;
  private Timer timer;
  
  PlanningPointBarPanel(JLabel pointsRemaining)
  {
    this.setBackground(Color.LIGHT_GRAY);
    this.pointsRemaining=pointsRemaining;
    timer = new Timer(10,this);
    timer.start();
  }
 
  /**
   */
  @Override
  public void paintComponent(Graphics g)
  {
    super.paintComponent(g);
    
    double pixPerPoint = (double)BAR_LENGTH/(double)PlanningPointConstants.MAX_POINTS_PER_YEAR;
    
    
    g.setColor(BACKGROUND_BAR_COLOR);
    g.fillRect(0,this.getBounds().height/2-BAR_HEIGHT/2, BAR_LENGTH, BAR_HEIGHT);
    g.setColor(FOREGROUND_BAR_COLOR);
    g.fillRect(0,this.getBounds().height/2-BAR_HEIGHT/2, (int) (PlanningPointsData.getPlanningPointsAvalable()*pixPerPoint), BAR_HEIGHT);
    //g.fillRect(0,this.getBounds().height/2-BAR_HEIGHT/2, BAR_LENGTH-(int) (((double)PlanningPointsData.getPlanningPointsAvalable()/PlanningPointConstants.MAX_POINTS)*BAR_LENGTH), BAR_HEIGHT);
  }
  
  public void updateBar()
  {
    pointsRemaining.setText("Planning Points Remaining: "+PlanningPointsData.getPlanningPointsAvalable()+" ");
    this.repaint();
  }

  @Override
  public void actionPerformed(ActionEvent arg0)
  {
    if(arg0.getSource()==timer) updateBar();
    
  }
  
  
  
}
