package worldfoodgame.gui.hud.infopanel;

import worldfoodgame.gui.ColorsAndFonts;
import worldfoodgame.model.Continent;

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
    this.add(getRatingPanel());
    this.validate();
  }

  private JPanel getRatingPanel()
  {
    JPanel panel = new JPanel();

    panel.setBackground(ColorsAndFonts.GUI_BACKGROUND);

    BufferedImage smile = labelFactory.getApprovalRating();
    
    if(smile != null)
    {
      JLabel pic = new JLabel(new ImageIcon(smile));
      panel.add(pic);
    }   
    else
    {
      System.out.println("Approval image is NULL");
    }

    JLabel label = new JLabel("The Pizza President's Approval Rating");
    label.setFont(ColorsAndFonts.GUI_FONT);
    panel.add(labelFactory.getApprovalBar());
    
    return panel;
  }  
  
  private JPanel getDemoPanel()
  {
    JPanel panel = new JPanel();

    panel.setBackground(ColorsAndFonts.GUI_BACKGROUND);
    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

    panel.add(labelFactory.getPopulationLabel());
    panel.add(labelFactory.getMedianAge());
    panel.add(labelFactory.getBirthRate());
    panel.add(labelFactory.getMortalityRate());
    panel.add(labelFactory.getMalnurished());

    return panel;
  }
}
