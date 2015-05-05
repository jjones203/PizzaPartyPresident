package worldfoodgame.planningpoints;

import java.awt.Dimension;
import java.awt.Image;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import worldfoodgame.gui.ColorsAndFonts;
import worldfoodgame.model.Continent;
/**
 * 
 * @author Stephen Stromberg
 * 
 * Used in infoPanel to display planning points
 * of a country.
 *
 */
public class PlanningPointCategoryAndIconPanel extends JPanel
{
  private int IconSize = 50;
  public PlanningPointCategoryAndIconPanel(Continent continent, 
      PlanningPointCategory category)
  {
    PlanningPointsLevel currentLevel= 
        PlanningPointsLevel.pointsToLevel(
            continent.getPlanningPointsInCategory(category));
    
    this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    this.setBackground(ColorsAndFonts.GUI_BACKGROUND);
    this.setBorder(new EmptyBorder(10, 10, 10, 10) );
    
    JLabel tierGraphic = new JLabel();
    JLabel categoryDescription = new JLabel(category.toString());
    tierGraphic.setIcon(new ImageIcon(PlanningPointsLevel.levelToIcon(
      currentLevel).getScaledInstance(IconSize,IconSize,Image.SCALE_SMOOTH)));
    categoryDescription.setForeground(ColorsAndFonts.GUI_TEXT_COLOR);

    
    this.add(categoryDescription);
    
    JPanel spacer = new JPanel();
    spacer.setPreferredSize(new Dimension(IconSize/2,IconSize/2));
    spacer.setBackground(ColorsAndFonts.GUI_BACKGROUND);
    
    this.add(spacer);
    this.add(tierGraphic);
    categoryDescription.setHorizontalAlignment(SwingConstants.CENTER);
    tierGraphic.setHorizontalAlignment(SwingConstants.CENTER);
    tierGraphic.setBorder(BorderFactory.createEmptyBorder(
        0, IconSize/4, 0, IconSize/4));
    tierGraphic.setToolTipText(PlanningPointsData.getToolTipText(category));
    categoryDescription.setToolTipText(
        PlanningPointsData.getToolTipText(category));
  }
  

}
