package worldfoodgame.gui.TradeWindow;
import worldfoodgame.gui.ColorsAndFonts;

import javax.swing.*;
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
  //This isn't important right now.
  //private JRadioButton units = new JRadioButton();

  public ButtonPanel (Dimension dimension, TradeAndImportFrame parent)
  {
    this.parent = parent;
    setPreferredSize(dimension);
    setBackground(ColorsAndFonts.GUI_BACKGROUND);
    setLayout(new FlowLayout());
    add(reset);
    add(cont);
    reset.addActionListener(this);
    cont.addActionListener(this);
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
