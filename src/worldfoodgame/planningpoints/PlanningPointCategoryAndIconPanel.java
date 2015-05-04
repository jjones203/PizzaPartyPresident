package worldfoodgame.planningpoints;

import java.awt.Image;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import worldfoodgame.gui.ColorsAndFonts;
import worldfoodgame.model.Continent;

public class PlanningPointCategoryAndIconPanel extends JPanel
{
  private int IconSize = 75;
  public PlanningPointCategoryAndIconPanel(Continent continent, PlanningPointCategory category)
  {
    PlanningPointsLevel currentLevel= PlanningPointsLevel.pointsToLevel(continent.getPlanningPointsInCategory(category));
    JLabel tierGraphic = new JLabel();
    JLabel categoryDescription = new JLabel(category.toString());
    this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    this.setBackground(ColorsAndFonts.GUI_BACKGROUND);
    tierGraphic.setIcon(new ImageIcon(PlanningPointsLevel.levelToIcon(currentLevel).getScaledInstance(IconSize,IconSize,Image.SCALE_SMOOTH)));
    categoryDescription.setForeground(ColorsAndFonts.GUI_TEXT_COLOR);
    this.add(categoryDescription);
    categoryDescription.setHorizontalAlignment(SwingConstants.CENTER);
    this.add(tierGraphic);
    tierGraphic.setHorizontalAlignment(SwingConstants.CENTER);
    tierGraphic.setToolTipText(PlanningPointsData.getToolTipText(category));
    categoryDescription.setToolTipText(PlanningPointsData.getToolTipText(category));
  }
  

}
