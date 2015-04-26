package worldfoodgame.gui.TradeWindow;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Tim on 4/24/15.
 */
public class TradeBar extends JPanel
{
  private JButton makeTrade = new JButton("Trade");

  public TradeBar (Dimension dimension)
  {
    setPreferredSize(dimension);
  }
}
