package worldfoodgame.gui.TradeWindow;
import javax.swing.*;

import worldfoodgame.common.EnumCropType;
import worldfoodgame.gui.ColorsAndFonts;
import worldfoodgame.gui.hud.infopanel.LabelFactory;
import worldfoodgame.gui.hud.infopanel.SingleCountryHandeler;
import worldfoodgame.model.Country;
/**
 * Created by Tim on 4/25/15.
 */
public class ContinentPanel extends JPanel
{
  private Country country;
  private SingleCountryHandeler handler; //do I need this?
  private LabelFactory labelFactory;

  public ContinentPanel (Country country, SingleCountryHandeler handler, LabelFactory labelFactory)
  {
    this.country= country;
    this.handler = handler;
    this.labelFactory = labelFactory;
    redraw();
  }

  public Country getCountry()
  {
    return country;
  }

  public void chooseCrop(EnumCropType crop)
  {

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
      TradeGraphLabel temp = labelFactory.getTradeProdLabel(crop);
      temp.setExternalPanel(this);
      cropPanel.add(temp);
    }

    return cropPanel;
  }
}
