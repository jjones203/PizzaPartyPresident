package worldfoodgame.gui.TradeWindow;
import javax.swing.*;

import worldfoodgame.common.EnumCropType;
import worldfoodgame.gui.ColorsAndFonts;
import worldfoodgame.gui.hud.infopanel.GroupCountryHandler;
import worldfoodgame.gui.hud.infopanel.GraphLabel;
import worldfoodgame.gui.hud.infopanel.LabelFactory;
import worldfoodgame.gui.hud.infopanel.SingleCountryHandeler;
import worldfoodgame.model.Country;
import worldfoodgame.model.Continent;
/**
 * Created by Tim on 4/25/15. Panel for a continent to choose crops
 * to trade.
 */
public class ContinentPanel extends JPanel
{
  private Country country;
  private Continent continent;
  private SingleCountryHandeler handler; //do I need this?
  private LabelFactory labelFactory;
  private final TradeAndImportFrame parent;
  private boolean isTrade = true;

  /**
   * Constructor sets variables
   * @param continent     Continent for this panel
   * @param labelFactory  Label factory for the continent
   * @param parent        Outer frame
   * @param isTrade       Whether trading or donating
   */
  public ContinentPanel (Continent continent, LabelFactory labelFactory, TradeAndImportFrame parent, boolean isTrade)
  {
    this.continent = continent;
    this.labelFactory = labelFactory;
    this.parent = parent;
    this.isTrade = isTrade;
    labelFactory.setContinent(continent);
    setBackground(ColorsAndFonts.GUI_BACKGROUND);
    redraw();
  }

  /**
   * @return  The continent of this panel
   */
  public Continent getContinent()
  {
    return continent;
  }

  /**
   * Called by one of the crop labels to give the trade bar
   * a new crop label
   * @param crop  Crop to be passed along
   */
  public void chooseCrop(EnumCropType crop)
  {
    parent.newContinentCrop(labelFactory, crop);
  }

  /**
   * Redraws the panel
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
      temp.setControl(isTrade);
      cropPanel.add(temp);
    }

    return cropPanel;
  }
}
