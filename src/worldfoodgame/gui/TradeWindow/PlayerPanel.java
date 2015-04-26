package worldfoodgame.gui.TradeWindow;

import worldfoodgame.model.Player;
import javax.swing.JPanel;
import java.awt.*;

/**
 * Created by Tim on 4/24/15.
 */
public class PlayerPanel extends JPanel
{
  private Player player;

  public PlayerPanel(Player player, Dimension dimension)
  {
    this.player = player;
    setPreferredSize(dimension);
  }
}
