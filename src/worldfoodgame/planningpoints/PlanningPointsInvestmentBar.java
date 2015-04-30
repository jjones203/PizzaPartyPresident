package worldfoodgame.planningpoints;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

import worldfoodgame.gui.ColorsAndFonts;

/**
 * 
 * @author Stephen Stromberg on 4/29/15
 * 
 * The interactable bar that shows how much
 * has been invested into a category of the
 * active region. This bar keeps track of original
 * points to keep the user from back tracking
 * prior to what has already been invested.
 *
 */
public class PlanningPointsInvestmentBar  extends JPanel 
                                implements ActionListener
{
  private PlanningPointCategory category;
  private PlanningPointsInteractableRegion activeRegion;
  
  protected final int BAR_HEIGHT;
  protected final int BAR_LENGTH;
  protected final Color BACKGROUND_BAR_COLOR = new Color(211,236,237);
  protected final Color FOREGROUND_BAR_COLOR = new Color(38,221,235);
  protected final Color OLD_BAR_COLOR = new Color(18,96,102);
  private JLabel pointsRemaining;
  private JLabel startTier;
  private JLabel endTier;
  private Timer timer;
  
  /**
   * 
   * @param category to invest into
   * @param pointsLabel for updating the text
   * @param startTier for drawing icon
   * @param endTier for drawing icon
   */
  PlanningPointsInvestmentBar(PlanningPointCategory category, 
      JLabel pointsLabel,JLabel startTier,JLabel endTier)
  {
    this.category=category;
    this.startTier=startTier;
    this.endTier=endTier;
    this.setBackground(ColorsAndFonts.GUI_BACKGROUND);
    pointsRemaining=pointsLabel;
    BAR_HEIGHT=20;
    BAR_LENGTH=150;
    timer = new Timer(10,this);
    timer.start();
  }
  
  /**
   * Paints a bar showing how much
   * has been invested into a certain category.
   * This is split into cases based on 
   * 1. a full category
   * 2. a category that has original investments
   * 3. a category that is not in initial investments
   */
  @Override
  public void paintComponent(Graphics g)
  {
    super.paintComponent(g);
    
    double pixPerPoint = (double)BAR_LENGTH/
        (double)PlanningPointConstants.POINTS_PER_TIER;
    int totalPoints = 
        PlanningPointsData.getOriginalIvestment(activeRegion, category)
        +PlanningPointsData.getTempIvestment(activeRegion, category);
    
    if(totalPoints==PlanningPointConstants.MAX_POINTS)
    {
      g.setColor(ColorsAndFonts.GUI_BACKGROUND);
      g.fillRect(0,this.getPreferredSize().height/2-BAR_HEIGHT/2,
          BAR_LENGTH, BAR_HEIGHT);
    }
    else if(totalPoints/PlanningPointConstants.POINTS_PER_TIER == 
        PlanningPointsData.getOriginalIvestment(activeRegion, category)/
        PlanningPointConstants.POINTS_PER_TIER)
    {
      int originalOffset = (int)
          ((PlanningPointsData.getOriginalIvestment(activeRegion, category)
              %PlanningPointConstants.POINTS_PER_TIER)*pixPerPoint);
      
      g.setColor(BACKGROUND_BAR_COLOR);
      g.fillRect(0,this.getPreferredSize().height/2-BAR_HEIGHT/2,
          BAR_LENGTH, BAR_HEIGHT);
      
      g.setColor(OLD_BAR_COLOR);
      g.fillRect(0,this.getPreferredSize().height/2-BAR_HEIGHT/2,
          originalOffset, BAR_HEIGHT);
      
      g.setColor(FOREGROUND_BAR_COLOR);
      g.fillRect(originalOffset,this.getPreferredSize().height/2-BAR_HEIGHT/2,
          (int) ((PlanningPointsData.getTempIvestment(activeRegion, category)
              %PlanningPointConstants.POINTS_PER_TIER)*pixPerPoint), 
              BAR_HEIGHT);
    }
    else
    {
      g.setColor(BACKGROUND_BAR_COLOR);
      g.fillRect(0,this.getPreferredSize().height/2-BAR_HEIGHT/2,
          BAR_LENGTH, BAR_HEIGHT);
      g.setColor(FOREGROUND_BAR_COLOR);
      g.fillRect(0,this.getPreferredSize().height/2-BAR_HEIGHT/2,
          (int) 
          ((totalPoints%PlanningPointConstants.POINTS_PER_TIER)*pixPerPoint),
          BAR_HEIGHT);
    }
  }
  
  /**
   * Called by timer. This not only
   * updates the bar but all of the text and image
   * icons
   */
  public void updateBar()
  {
    activeRegion=PlanningPointsData.getActiveRegion();
    int totalPoints = 
        PlanningPointsData.getOriginalIvestment(activeRegion, category)
        +PlanningPointsData.getTempIvestment(activeRegion, category);
    int pointsInLevel = 
        totalPoints%PlanningPointConstants.POINTS_PER_TIER;
    PlanningPointsLevel currentLevel=
        PlanningPointsLevel.pointsToLevel(totalPoints);
    PlanningPointsLevel nextLevel=PlanningPointsLevel.pointsToLevel(
        Math.min(PlanningPointConstants.MAX_POINTS, 
            totalPoints+PlanningPointConstants.POINTS_PER_TIER));
    pointsRemaining.setText(pointsInLevel+"/"
            +PlanningPointConstants.POINTS_PER_TIER);
    startTier.setIcon(
        new ImageIcon(
            PlanningPointsLevel.levelToIcon(
                currentLevel).getScaledInstance(
                    PlanningPointConstants.ICON_DIMENSION,
                    PlanningPointConstants.ICON_DIMENSION,
                    Image.SCALE_SMOOTH)));
    endTier.setIcon(
        new ImageIcon(PlanningPointsLevel.levelToIcon(nextLevel)
            .getScaledInstance(PlanningPointConstants.ICON_DIMENSION,
                PlanningPointConstants.ICON_DIMENSION, Image.SCALE_SMOOTH)));
    this.repaint();
    if(!PlanningPointsData.getRunning()) timer.stop();
  }

  @Override
  public void actionPerformed(ActionEvent arg0)
  {
    if(arg0.getSource()==timer) updateBar();
  }
  
}
