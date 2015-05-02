package worldfoodgame.gui.TradeWindow;
import javax.swing.*;

import worldfoodgame.common.EnumCropType;
import worldfoodgame.gui.ColorsAndFonts;
import worldfoodgame.gui.hud.infopanel.GroupCountryHandler;
import worldfoodgame.gui.hud.infopanel.LabelFactory;
import worldfoodgame.gui.hud.infopanel.SingleCountryHandeler;
import worldfoodgame.model.Country;
import worldfoodgame.model.Continent;
/**
 * Created by Tim on 4/25/15.
 */
public class ContinentPanel extends JPanel
{
  private Country country;
  private Continent continent;
  private SingleCountryHandeler handler; //do I need this?
  private LabelFactory labelFactory;
  public final TradeAndImportFrame parent;

  public ContinentPanel (Continent continent, LabelFactory labelFactory, TradeAndImportFrame parent)
  {
    this.continent = continent;
    this.labelFactory = labelFactory;
    this.parent = parent;
    redraw();
  }

  public Continent getContinent()
  {
    return continent;
  }

  public void chooseCrop(EnumCropType crop)
  {
    parent.newContinentCrop(labelFactory.getTradeContLabel(crop));
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
