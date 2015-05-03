package worldfoodgame.gui.TradeWindow;

import worldfoodgame.gui.ColorsAndFonts;
import worldfoodgame.gui.hud.infopanel.GraphLabel;

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
  private boolean continentCrop = false;
  private boolean playerCrop = false;
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

  public void setPlayerBar(GraphLabel gl)
  {
    playerCrop = true;
    playerGL = gl;
    if (continentCrop)
    {
      contGL.setValue(0);
    }
    redraw();
  }

  public void setContinentBar(GraphLabel gl)
  {
    continentCrop = true;
    contGL = gl;
    if (playerCrop)
    {
      playerGL.setValue(0);
      playerGL.setLimit(contGL.getLimit());
    }
    redraw();
  }

  public void redraw ()
  {
    removeAll();
    if (playerCrop)
    {
      add(playerGL, BorderLayout.EAST);
    }
    if (continentCrop)
    {
      add(contGL, BorderLayout.WEST);
    }
    add(makeTrade, BorderLayout.CENTER);
    validate();
  }

  @Override
  public void actionPerformed(ActionEvent e)
  {
    if (playerCrop && continentCrop)
    {
      parent.trade();
    }
  }
}
