package worldfoodgame.planningpoints;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JPanel;

import worldfoodgame.gui.ColorsAndFonts;

public class PlanningPointsFooterPanel extends JPanel
{
  private JButton investButton;
  PlanningPointsFooterPanel()
  {
    investButton = new JButton("Invest");
    investButton.setBackground(Color.LIGHT_GRAY);
    this.setLayout(new GridLayout(1, 3));
    add(new JPanel());
    add(investButton);
    add(new JPanel());
  }

}
