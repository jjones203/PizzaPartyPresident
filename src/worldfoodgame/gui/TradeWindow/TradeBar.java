package worldfoodgame.gui.TradeWindow;

import worldfoodgame.common.EnumCropType;
import worldfoodgame.gui.ColorsAndFonts;
import worldfoodgame.gui.hud.infopanel.GraphLabel;
import worldfoodgame.gui.hud.infopanel.LabelFactory;
import worldfoodgame.model.World;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.BorderFactory;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Tim on 4/24/15.
 */
public class TradeBar extends JPanel implements ActionListener
{
  private JButton makeTrade = new JButton("Trade These Amounts");
  private final TradeAndImportFrame parent;
  private GraphLabel playerGL;
  private GraphLabel contGL;
  private LabelFactory playerLF;
  private LabelFactory contLF;
  private EnumCropType playerCrop;
  private EnumCropType contCrop;
  private double currentTrade = 0;
  private double currentLimit = 0;
  private boolean hasContinent = false;
  private boolean hasPlayer = false;
  private JPanel tradeButtonPanel = new JPanel();
  private JPanel playerPanel = new JPanel();
  private JPanel contPanel = new JPanel();
  private TitledBorder border;

  public TradeBar (Dimension dimension, TradeAndImportFrame parent)
  {
    //setPreferredSize(dimension);
    setBackground(ColorsAndFonts.GUI_BACKGROUND);
    setLayout(new BorderLayout());
    border = BorderFactory.createTitledBorder(BorderFactory.createMatteBorder(2, 0, 0, 0, ColorsAndFonts.GUI_TEXT_COLOR.darker()), "Propose A Trade");
    border.setTitleJustification(TitledBorder.CENTER);
    border.setTitleFont(ColorsAndFonts.HUD_TITLE);
    border.setTitleColor(ColorsAndFonts.OCEANS);
    setBorder(border);
    tradeButtonPanel.setPreferredSize(new Dimension(300, 50));
    playerPanel.setPreferredSize(new Dimension(180, 50));
    contPanel.setPreferredSize(new Dimension(300, 50));
    tradeButtonPanel.setBackground(ColorsAndFonts.GUI_BACKGROUND);
    playerPanel.setBackground(ColorsAndFonts.GUI_BACKGROUND);
    contPanel.setBackground(ColorsAndFonts.GUI_BACKGROUND);
    this.parent = parent;
    makeTrade.addActionListener(this);
    makeTrade.setFont(ColorsAndFonts.BUTTON_FONT);
    makeTrade.setPreferredSize(new Dimension(180, 25));
    makeTrade.setForeground(ColorsAndFonts.GUI_TEXT_COLOR);
    makeTrade.setBackground(ColorsAndFonts.REGION_NAME_FONT_C);
    makeTrade.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
    redraw();
  }

  public void setPlayerBar(LabelFactory playerLF, EnumCropType crop)
  {
    this.playerLF = playerLF;
    playerCrop = crop;
    hasPlayer = true;
    double temp = playerLF.getContinent().getCropProduction(World.getWorld().getCurrentYear() - 1, playerCrop);
    if (temp < currentLimit)
    {
      currentLimit = temp;
    }
    if (hasContinent)
    {
      contGL = contLF.getTradeContLabel(contCrop, this, currentLimit);
    }
    playerGL = playerLF.getTradePlayLabel(crop, this, currentLimit);
    currentTrade = 0;
    redraw();
  }

  public void setContinentBar(LabelFactory contLF, EnumCropType crop)
  {
    this.contLF = contLF;
    contCrop = crop;
    hasContinent = true;
    currentLimit = contLF.getContinent().getCropProduction(World.getWorld().getCurrentYear() - 1, crop);
    currentLimit = currentLimit - contLF.getContinent().getTotalCropNeed(World.getWorld().getCurrentYear() - 1, crop);
    if (currentLimit < 0)
    {
      currentLimit = 0;
    }
    else if (hasPlayer)
    {
      double temp = playerLF.getContinent().getCropProduction(World.getWorld().getCurrentYear() - 1, playerCrop);
      if (temp < currentLimit)
      {
        currentLimit = temp;
      }
    }
    if (hasPlayer)
    {
      playerGL = playerLF.getTradePlayLabel(playerCrop, this, currentLimit);
    }
    contGL = contLF.getTradeContLabel(crop, this, currentLimit);
    currentTrade = 0;
    redraw();
  }

  public void redraw ()
  {
    playerPanel.removeAll();
    contPanel.removeAll();
    tradeButtonPanel.removeAll();
    removeAll();
    add(contPanel, BorderLayout.SOUTH);
    add(playerPanel, BorderLayout.NORTH);
    if (hasContinent)
    {
      contPanel.add(contGL);
    }
    if (hasPlayer)
    {
      playerPanel.add(playerGL);
    }
    add(tradeButtonPanel, BorderLayout.CENTER);
    tradeButtonPanel.add(makeTrade);
    contPanel.repaint();
    playerPanel.repaint();
    contPanel.revalidate();
    playerPanel.revalidate();
    validate();
  }

  public void setCurrentTrade(double input)
  {
    if (input < 0)
    {
      currentTrade = 0;
      contGL.setValue(0);
    }
    else if (input > currentLimit)
    {
      currentTrade = currentLimit;
      contGL.setValue(currentLimit);
    }
    else
    {
      currentTrade = input;
      contGL.setValue(input);
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

      currentLimit = contLF.getContinent().getCropProduction(World.getWorld().getCurrentYear() - 1, contCrop);
      currentLimit = currentLimit - contLF.getContinent().getTotalCropNeed(World.getWorld().getCurrentYear() - 1, contCrop);
      if (currentLimit < 0)
      {
        currentLimit = 0;
      }
      else
      {
        double temp = playerLF.getContinent().getCropProduction(World.getWorld().getCurrentYear() - 1, playerCrop);
        if (temp < currentLimit)
        {
          currentLimit = temp;
        }
      }
      currentTrade = 0;
      playerGL = playerLF.getTradePlayLabel(playerCrop, this, currentLimit);
      contGL = contLF.getTradeContLabel(contCrop, this, currentLimit);
      redraw();
    }
  }
}
