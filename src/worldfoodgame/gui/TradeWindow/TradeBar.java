package worldfoodgame.gui.TradeWindow;

import worldfoodgame.gui.ColorsAndFonts;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Tim on 4/24/15.
 */
public class TradeBar extends JPanel
{
  private JButton makeTrade = new JButton("Trade");
  public static final Color ROLLOVER_C = Color.WHITE;
  public static final Color SELECTED_C = Color.RED.darker();
  public static final Color TEXT_DEFAULT_COLOR = ColorsAndFonts.GUI_TEXT_COLOR;
  public static final Color BACKGROUND_COLOR = ColorsAndFonts.GUI_BACKGROUND;
  public static final Font TAB_FONT = ColorsAndFonts.GUI_FONT;

  public TradeBar (Dimension dimension)
  {
    setPreferredSize(dimension);
    setBackground(ColorsAndFonts.GUI_BACKGROUND);
  }
}
