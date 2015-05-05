package worldfoodgame.planningpoints;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;

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
  PlanningPointsAllocationPanel parent;
  
  PlanningPointsFooterPanel(JFrame investmentPanel, 
      final PlanningPointsAllocationPanel parent)
  {
    this.parent = parent;
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
        parent.closeInvesting();
      }
    });
    investButton.setPreferredSize(new Dimension(100,50));
    investButton.setFont(ColorsAndFonts.BUTTON_FONT);
    investButton.setForeground(ColorsAndFonts.GUI_TEXT_COLOR);
    investButton.setBackground(ColorsAndFonts.REGION_NAME_FONT_C);
    investButton.setBorder(
        BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
    add(investButton);
  }

}
