package worldfoodgame.gui.TradeWindow;
import javax.swing.JPanel;

import worldfoodgame.gui.hud.infopanel.LabelFactory;
import worldfoodgame.gui.hud.infopanel.SingleCountryHandeler;
import worldfoodgame.model.Country;
/**
 * Created by Tim on 4/25/15.
 */
public class ContinentPanel extends JPanel
{
  private Country country;
  private SingleCountryHandeler handler;
  private LabelFactory labelFactory;

  public ContinentPanel (Country country, SingleCountryHandeler handler, LabelFactory labelFactory)
  {
    this.country= country;
    this.handler = handler;
    this.labelFactory = labelFactory;
  }

  public Country getCountry()
  {
    return country;
  }
}
