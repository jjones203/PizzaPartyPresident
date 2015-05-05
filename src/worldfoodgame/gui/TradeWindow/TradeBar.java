package worldfoodgame.gui.TradeWindow;

import worldfoodgame.common.EnumCropType;
import worldfoodgame.gui.ColorsAndFonts;
import worldfoodgame.gui.hud.infopanel.GraphLabel;
import worldfoodgame.gui.hud.infopanel.LabelFactory;
import worldfoodgame.model.Continent;
import worldfoodgame.model.World;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.BorderFactory;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Tim on 4/24/15. The JPanel responsible for exchanging crops.
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
  private JLabel contName = new JLabel();
  private TitledBorder border;
  private boolean isTrade = true;
  private Continent continent;

  /**
   * Constructor sets the dimensions, the outer panel and whether the trade bar
   * should function as trade or donating
   * @param dimension
   * @param parent
   * @param isTrade
   */
  public TradeBar (Dimension dimension, TradeAndImportFrame parent, boolean isTrade)
  {
    //setPreferredSize(dimension);
    this.isTrade = isTrade;
    setBackground(ColorsAndFonts.GUI_BACKGROUND);
    setLayout(new BorderLayout());
    if (isTrade)
    {
      border = BorderFactory.createTitledBorder(BorderFactory.createMatteBorder(2, 0, 0, 0, ColorsAndFonts.GUI_TEXT_COLOR.darker()), "Propose A Trade");
      makeTrade.setText("Trade These Amounts");
    }
    else
    {
      border = BorderFactory.createTitledBorder(BorderFactory.createMatteBorder(2, 0, 0, 0, ColorsAndFonts.GUI_TEXT_COLOR.darker()), "Prepare A Donation");
      makeTrade.setText("Make This Donation To");
    }
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

  /**
   * Sets the player label and the limit based on the limit of the current limits
   * @param playerLF  Appropriate label factory for the player's continent
   * @param crop      Crop to be traded
   */
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
    playerGL.setIncrease("Increase");
    playerGL.setDecrease("Decrease");
    currentTrade = 0;
    redraw();
  }

  /**
   * Sets continent label to trade with (trading only)
   * @param contLF  Appropriate label factory for chose continent
   * @param crop    Crop to be traded
   */
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
      playerGL.setIncrease("Increase");
      playerGL.setDecrease("Decrease");
    }
    contGL = contLF.getTradeContLabel(crop, this, currentLimit);
    currentTrade = 0;
    redraw();
  }

  /**
   * Sets the continent to donate to (if donating only)
   * @param continent Continent to donate to
   */
  public void setContinent(Continent continent)
  {
    this.continent = continent;
    contName.setText(continent.getContName());
    contName.setForeground(ColorsAndFonts.GUI_TEXT_COLOR);
    contName.setBorder(BorderFactory.createLineBorder(ColorsAndFonts.OCEANS));
    redraw();
  }

  /**
   * Redraws all the labels and buttons.
   */
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
    if (!isTrade && continent != null)
    {
      contPanel.add(contName);
    }
    add(tradeButtonPanel, BorderLayout.CENTER);
    tradeButtonPanel.add(makeTrade);
    contPanel.repaint();
    playerPanel.repaint();
    contPanel.revalidate();
    playerPanel.revalidate();
    validate();
  }

  /**
   * Set the current amount proposed for a trade/donation.
   * @param input current proposition.
   */
  public void setCurrentTrade(double input)
  {
    double temp = 0;
    if (input < 0)
    {
      currentTrade = 0;
    }
    else if (input > currentLimit)
    {
      currentTrade = currentLimit;
      temp = currentLimit;
    }
    else
    {
      currentTrade = input;
      temp = input;
    }
    if (isTrade && hasContinent)
    {
      contGL.setValue(temp);
    }
  }

  /**
   * Get the current amount proposed for a trade/donation.
   * @return  current proposition.
   */
  public double getCurrentTrade()
  {
    return currentTrade;
  }

  /**
   * Calls the trade or donate buttons and resets the labels and adjusts the limits
   * after trading and donating
   * @param e The button press event
   */
  @Override
  public void actionPerformed(ActionEvent e)
  {
    if (isTrade && hasPlayer && hasContinent && e.getSource() == makeTrade)
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
      playerGL.setIncrease("Increase");
      playerGL.setDecrease("Decrease");
      redraw();
    }
    else if (!isTrade && hasPlayer && continent != null && e.getSource() == makeTrade)
    {
      parent.donate(continent, playerCrop);
      currentTrade = 0;
      currentLimit = playerLF.getContinent().getCropProduction(World.getWorld().getCurrentYear() - 1, playerCrop);
      playerGL = playerLF.getTradePlayLabel(playerCrop, this, currentLimit);
      playerGL.setIncrease("Increase");
      playerGL.setDecrease("Decrease");
      redraw();
    }
  }
}
