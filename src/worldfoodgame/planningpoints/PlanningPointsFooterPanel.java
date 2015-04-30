package worldfoodgame.planningpoints;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import worldfoodgame.gui.ColorsAndFonts;

/**
 * 
 * @author Stephen on 4/29/15
 * 
 * The footer panel which simply holds
 * the invest button.
 *
 */
public class PlanningPointsFooterPanel extends JPanel
{
  private JButton investButton;
  private JFrame mainReference;
  
  PlanningPointsFooterPanel(JFrame investmentPanel)
  {
    mainReference=investmentPanel;
    investButton = new JButton("Invest");
    this.setBackground(ColorsAndFonts.GUI_BACKGROUND);
    investButton.addActionListener(new ActionListener() 
    {
      @ Override
      public void actionPerformed(ActionEvent evt)
      {
        PlanningPointsData.submitInvestment();
        PlanningPointsData.stopRunning();
        mainReference.dispose();
      }
    });
    investButton.setPreferredSize(new Dimension(100,50));
    add(investButton);
  }

}
