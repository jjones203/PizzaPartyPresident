package worldfoodgame.gui.hud.infopanel;

import worldfoodgame.gui.ColorsAndFonts;

import javax.swing.*;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by winston on 3/17/15.
 * <p/>
 * Displays the demographic information for a given country.
 */
public class DemographicPanel extends JPanel
{
  private LabelFactory labelFactory;


  public DemographicPanel(LabelFactory labelFactory)
  {
    this.labelFactory = labelFactory;
    this.setBackground(ColorsAndFonts.GUI_BACKGROUND);
    this.setLayout(new GridLayout(0, 3));


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
    JPanel leftPanel = new JPanel();
    JPanel rightPanel = new JPanel();

    leftPanel.setBackground(ColorsAndFonts.GUI_BACKGROUND);
    leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));

    leftPanel.add(labelFactory.getPopulationLabel());
    leftPanel.add(labelFactory.getMedianAge());
    leftPanel.add(labelFactory.getBirthRate());
    leftPanel.add(labelFactory.getMortalityRate());
    leftPanel.add(labelFactory.getMalnurished());

//    BufferedImage smile = labelFactory.getApprovalRating();
//    if(smile != null)
//    {
//      rightPanel.setPreferredSize(new Dimension(smile.getWidth(null),
//          smile.getHeight(null)));
//    }
//    panel.setLayout(new GridLayout(1,4));
//    panel.add(leftPanel);
//    panel.add(new JPanel());
//    panel.add(new JPanel());
//    panel.add(rightPanel);

    return leftPanel;
  }
}
