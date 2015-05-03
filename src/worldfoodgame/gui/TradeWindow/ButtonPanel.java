package worldfoodgame.gui.TradeWindow;
import worldfoodgame.gui.ColorsAndFonts;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Tim on 4/24/15.
 */
public class ButtonPanel extends JPanel
{
  private final TradeAndImportFrame parent;
  private JButton reset = new JButton("Reset All Trades");
  private JButton cont = new JButton("Done Trading");
  //This isn't important right now.
  //private JRadioButton units = new JRadioButton();
  public static final Color ROLLOVER_C = Color.WHITE;
  public static final Color SELECTED_C = Color.RED.darker();
  public static final Color TEXT_DEFAULT_COLOR = ColorsAndFonts.GUI_TEXT_COLOR;
  public static final Color BACKGROUND_COLOR = ColorsAndFonts.GUI_BACKGROUND;
  public static final Font TAB_FONT = ColorsAndFonts.GUI_FONT;

  public ButtonPanel (Dimension dimension, TradeAndImportFrame parent)
  {
    this.parent = parent;
    setPreferredSize(dimension);
    setBackground(ColorsAndFonts.GUI_BACKGROUND);
    setLayout(new FlowLayout());
    add(reset);
    add(cont);
  }
}
