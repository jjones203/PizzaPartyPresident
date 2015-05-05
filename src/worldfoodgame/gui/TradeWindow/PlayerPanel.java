package worldfoodgame.gui.TradeWindow;

import worldfoodgame.common.EnumCropType;
import worldfoodgame.gui.ColorsAndFonts;
import worldfoodgame.gui.hud.infopanel.LabelFactory;
import worldfoodgame.gui.hud.infopanel.SingleCountryHandeler;
import worldfoodgame.model.Player;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Tim on 4/24/15. The panel for displaying the crops for a player's continent.
 */
public class PlayerPanel extends JPanel
{
  private Player player;
  private SingleCountryHandeler handler;
  private LabelFactory labelFactory;
  private final TradeAndImportFrame parent;
  private TitledBorder border;

  /**
   * Constructor sets up the panel and displays crop bars.
   * @param player
   * @param dimension
   * @param parent
   */
  public PlayerPanel(Player player, Dimension dimension, TradeAndImportFrame parent)
  {
    this.player = player;
    this.parent = parent;
    setPreferredSize(dimension);
    setBackground(ColorsAndFonts.GUI_BACKGROUND);
    border = BorderFactory.createTitledBorder(BorderFactory.createMatteBorder(2, 0, 0, 0, ColorsAndFonts.GUI_TEXT_COLOR.darker()),
            player.getContinent().getContName());
    border.setTitleJustification(TitledBorder.CENTER);
    border.setTitleFont(ColorsAndFonts.HUD_TITLE);
    border.setTitleColor(ColorsAndFonts.OCEANS);
    setBorder(border);
    setLayout(new FlowLayout());
  }

  /**
   * Sets the label factory for the panel.
   * @param labelFactory  Appropriate label factory
   */
  public void setLabelFactory(LabelFactory labelFactory)
  {
    this.labelFactory = labelFactory;
    labelFactory.setContinent(player.getContinent());
    redraw();
  }

  /**
   * Called by a crop label to give the trade bar a label.
   * @param crop  Crop to trade
   */
  public void chooseCrop(EnumCropType crop)
  {
    parent.newPlayerCrop(labelFactory, crop);
  }

  /**
   * Redraw the panel.
   */
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
      TradeGraphLabel temp = labelFactory.getTradeProdLabel(crop);
      temp.setExternalPanel(this);
      cropPanel.add(temp);
    }

    return cropPanel;
  }
}