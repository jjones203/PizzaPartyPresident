package worldfoodgame.gui.TradeWindow;
import worldfoodgame.gui.ColorsAndFonts;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Tim on 4/24/15.
 */
public class ButtonPanel extends JPanel implements ActionListener
{
  private final TradeAndImportFrame parent;
  private JButton reset = new JButton("Reset All Trades");
  private JButton cont = new JButton("Done Trading");
  private Dimension dim = new Dimension(120, 35);
  private boolean isTrade = true;
  //This isn't important right now.
  //private JRadioButton units = new JRadioButton();

  public ButtonPanel (Dimension dimension, TradeAndImportFrame parent, boolean isTrade)
  {
    this.parent = parent;
    this.isTrade = isTrade;
    if (!isTrade)
    {
      reset.setText("Reset Donations");
      cont.setText("Stop Donating");
    }
    setPreferredSize(dimension);
    setBackground(ColorsAndFonts.GUI_BACKGROUND);
    setLayout(new FlowLayout());
    add(reset);
    add(cont);
    reset.addActionListener(this);
    reset.setFont(ColorsAndFonts.BUTTON_FONT);
    reset.setPreferredSize(dim);
    reset.setForeground(ColorsAndFonts.GUI_TEXT_COLOR);
    reset.setBackground(ColorsAndFonts.REGION_NAME_FONT_C);
    reset.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
    cont.addActionListener(this);
    cont.setFont(ColorsAndFonts.BUTTON_FONT);
    cont.setPreferredSize(dim);
    cont.setForeground(ColorsAndFonts.GUI_TEXT_COLOR);
    cont.setBackground(ColorsAndFonts.REGION_NAME_FONT_C);
    cont.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
  }

  @Override
  public void actionPerformed(ActionEvent e)
  {
    if (e.getSource() == reset)
    {
      parent.reset();
    }
    else if (e.getSource() == cont)
    {
      parent.endTrading();
    }
  }
}
