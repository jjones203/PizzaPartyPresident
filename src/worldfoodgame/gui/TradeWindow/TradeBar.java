package worldfoodgame.gui.TradeWindow;

import worldfoodgame.common.EnumCropType;
import worldfoodgame.gui.ColorsAndFonts;
import worldfoodgame.gui.hud.infopanel.GraphLabel;
import worldfoodgame.gui.hud.infopanel.LabelFactory;
import worldfoodgame.model.World;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Tim on 4/24/15.
 */
public class TradeBar extends JPanel implements ActionListener
{
  private JButton makeTrade = new JButton("Trade");
  private final TradeAndImportFrame parent;
  private GraphLabel playerGL;
  private GraphLabel contGL;
  private LabelFactory playerLF;
  private LabelFactory contLF;
  private EnumCropType playerCrop;
  private EnumCropType contCrop;
  private double currentTrade = 0;
  private boolean hasContinent = false;
  private boolean hasPlayer = false;
  public static final Color ROLLOVER_C = Color.WHITE;
  public static final Color SELECTED_C = Color.RED.darker();
  public static final Color TEXT_DEFAULT_COLOR = ColorsAndFonts.GUI_TEXT_COLOR;
  public static final Color BACKGROUND_COLOR = ColorsAndFonts.GUI_BACKGROUND;
  public static final Font TAB_FONT = ColorsAndFonts.GUI_FONT;

  public TradeBar (Dimension dimension, TradeAndImportFrame parent)
  {
    setPreferredSize(dimension);
    setBackground(ColorsAndFonts.GUI_BACKGROUND);
    setLayout(new BorderLayout());
    this.parent = parent;
    makeTrade.addActionListener(this);
    redraw();
  }

  public void setPlayerBar(LabelFactory playerLF, EnumCropType crop)
  {
    this.playerLF = playerLF;
    playerCrop = crop;
    hasPlayer = true;
    double temp = 0;
    if (hasContinent)
    {
      temp = contLF.getContinent().getCropProduction(World.getWorld().getCurrentYear() - 1, contCrop);
      temp = temp - contLF.getContinent().getTotalCropNeed(World.getWorld().getCurrentYear() - 1, contCrop);
      if (temp < 0)
      {
        temp = 0;
      }
      contGL = contLF.getTradeContLabel(contCrop, this, temp);
    }
    playerGL = playerLF.getTradePlayLabel(crop, this, temp);
    currentTrade = 0;
    redraw();
  }

  public void setContinentBar(LabelFactory contLF, EnumCropType crop)
  {
    this.contLF = contLF;
    contCrop = crop;
    hasContinent = true;
    double temp = contLF.getContinent().getCropProduction(World.getWorld().getCurrentYear() - 1, crop);
    temp = temp - contLF.getContinent().getTotalCropNeed(World.getWorld().getCurrentYear() - 1, crop);
    if (temp < 0)
    {
      temp = 0;
    }
    if (hasPlayer)
    {
      playerGL = playerLF.getTradePlayLabel(playerCrop, this, temp);
    }
    contGL = contLF.getTradeContLabel(crop, this, temp);
    currentTrade = 0;
    redraw();
  }

  public void redraw ()
  {
    removeAll();
    if (hasPlayer)
    {
      add(playerGL, BorderLayout.EAST);
    }
    if (hasContinent)
    {
      add(contGL, BorderLayout.WEST);
    }
    add(makeTrade, BorderLayout.CENTER);
    validate();
  }

  public void setCurrentTrade(double input)
  {
    if (input < 0)
    {
      currentTrade = 0;
    }
    else
    {
      currentTrade = input;
    }
  }

  public double getCurrentTrade()
  {
    return currentTrade;
  }

  @Override
  public void actionPerformed(ActionEvent e)
  {
    if (hasPlayer && hasContinent && e.getSource() == makeTrade)
    {
      parent.trade(contLF.getContinent(), contCrop, playerCrop);

      double temp = contLF.getContinent().getCropProduction(World.getWorld().getCurrentYear() - 1, contCrop);
      temp = temp - contLF.getContinent().getTotalCropNeed(World.getWorld().getCurrentYear() - 1, contCrop);
      if (temp < 0)
      {
        temp = 0;
      }
      playerGL = playerLF.getTradePlayLabel(playerCrop, this, temp);
      contGL = contLF.getTradeContLabel(contCrop, this, temp);
      currentTrade = 0;
      redraw();
    }
  }
}
