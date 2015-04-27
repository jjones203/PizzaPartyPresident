package worldfoodgame.gui.TradeWindow;

import worldfoodgame.common.EnumCropType;
import worldfoodgame.gui.ColorsAndFonts;
import worldfoodgame.gui.hud.infopanel.LabelFactory;
import worldfoodgame.gui.hud.infopanel.SingleCountryHandeler;
import worldfoodgame.model.Player;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Tim on 4/24/15.
 */
public class PlayerPanel extends JPanel
{
  private Player player;
  private SingleCountryHandeler handler;
  private LabelFactory labelFactory;
  public static final Color ROLLOVER_C = Color.WHITE;
  public static final Color SELECTED_C = Color.RED.darker();
  public static final Color TEXT_DEFAULT_COLOR = ColorsAndFonts.GUI_TEXT_COLOR;
  public static final Color BACKGROUND_COLOR = ColorsAndFonts.GUI_BACKGROUND;
  public static final Font TAB_FONT = ColorsAndFonts.GUI_FONT;

  public PlayerPanel(Player player, Dimension dimension)
  {
    this.player = player;
    setPreferredSize(dimension);
    setBackground(ColorsAndFonts.GUI_BACKGROUND);
    setLayout(new GridLayout(0, 3));
  }

  public void setLabelFactory(LabelFactory labelFactory)
  {
    this.labelFactory = labelFactory;
    redraw();
  }

  public void redraw()
  {
    this.removeAll();
    this.add(getCropPanel());
    this.validate();
  }

  private JPanel getCropPanel()
  {
    JPanel cropPanel = new JPanel();
    cropPanel.setBackground(ColorsAndFonts.GUI_BACKGROUND);
    cropPanel.setLayout(new BoxLayout(cropPanel, BoxLayout.Y_AXIS));

    for (EnumCropType crop : EnumCropType.values())
    {
      cropPanel.add(labelFactory.getTradeProdLabel(crop));
    }

    return cropPanel;
  }
}