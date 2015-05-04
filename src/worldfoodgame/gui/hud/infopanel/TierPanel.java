package worldfoodgame.gui.hud.infopanel;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import worldfoodgame.common.EnumCropType;
import worldfoodgame.common.EnumGrowMethod;
import worldfoodgame.gui.ColorsAndFonts;
import worldfoodgame.gui.WorldPresenter;
import worldfoodgame.planningpoints.PlanningPointCategory;
import worldfoodgame.planningpoints.PlanningPointCategoryAndIconPanel;
import worldfoodgame.planningpoints.PlanningPointsInvestmentPanel;

public class TierPanel extends JPanel
{
  private LabelFactory labelFactory;
  private WorldPresenter worldPresenter;

  public TierPanel(LabelFactory labelFactory, WorldPresenter worldPrsenter)
  {
    this.worldPresenter=worldPrsenter;
    this.labelFactory = labelFactory;
    this.setBackground(ColorsAndFonts.GUI_BACKGROUND);


    redraw();
  }

  public LabelFactory getLabelFactory()
  {
    return labelFactory;
  }

  public void setLabelFactory(LabelFactory labelFactory)
  {
    this.labelFactory = labelFactory;
  }

  // this is meant to be called to update the panel, after resetting the label
  // factory to the correct data.
  public void redraw()
  {
    this.removeAll();
    this.add(getDemoPanel());
    this.validate();
  }

  private JPanel getDemoPanel()
  {
    JPanel panel = new JPanel();
    panel.setBackground(ColorsAndFonts.GUI_BACKGROUND);
    panel.setLayout(new GridLayout(0,PlanningPointCategory.values().length));
    

    if (worldPresenter.getActiveCont().size()==0) return panel;
    for(PlanningPointCategory category: PlanningPointCategory.values())
    {
      this.add(new PlanningPointCategoryAndIconPanel(worldPresenter.getActiveCont().get(0),category)); 
    }
    return panel;
  }
}
