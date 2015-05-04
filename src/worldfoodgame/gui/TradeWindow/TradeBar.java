package worldfoodgame.gui.TradeWindow;

import worldfoodgame.common.EnumCropType;
import worldfoodgame.gui.ColorsAndFonts;
import worldfoodgame.gui.hud.infopanel.GraphLabel;
import worldfoodgame.gui.hud.infopanel.LabelFactory;
import worldfoodgame.model.World;

import javax.swing.*;
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
  private JButton makeTrade = new JButton("Make Trade");
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

  public TradeBar (Dimension dimension, TradeAndImportFrame parent)
  {
    //setPreferredSize(dimension);
    setBackground(ColorsAndFonts.GUI_BACKGROUND);
    setLayout(new BorderLayout());
    setBorder(BorderFactory.createTitledBorder(BorderFactory.createMatteBorder(2, 0, 0, 0, ColorsAndFonts.GUI_TEXT_COLOR.darker()), "Propose A Trade"));
    tradeButtonPanel.setPreferredSize(new Dimension(100, 50));
    playerPanel.setPreferredSize(new Dimension(180, 50));
    contPanel.setPreferredSize(new Dimension(180, 50));
    tradeButtonPanel.setBackground(ColorsAndFonts.GUI_BACKGROUND);
    playerPanel.setBackground(ColorsAndFonts.GUI_BACKGROUND);
    contPanel.setBackground(ColorsAndFonts.GUI_BACKGROUND);
    this.parent = parent;
    makeTrade.addActionListener(this);
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
    if (hasPlayer)
    {
      add(playerPanel, BorderLayout.SOUTH);
      playerPanel.add(playerGL);
    }
    if (hasContinent)
    {
      add(contPanel, BorderLayout.NORTH);
      contPanel.add(contGL);
    }
    add(tradeButtonPanel, BorderLayout.CENTER);
    tradeButtonPanel.add(makeTrade);
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
      playerGL = playerLF.getTradePlayLabel(playerCrop, this, currentLimit);
      contGL = contLF.getTradeContLabel(contCrop, this, currentLimit);
      currentTrade = 0;
      redraw();
    }
  }
}
