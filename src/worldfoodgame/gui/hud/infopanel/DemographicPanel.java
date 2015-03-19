package worldfoodgame.gui.hud.infopanel;

import worldfoodgame.gui.ColorsAndFonts;

import javax.swing.*;
import java.awt.*;

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

    this.add(getDemoPanel());
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

    return panel;
  }
}
