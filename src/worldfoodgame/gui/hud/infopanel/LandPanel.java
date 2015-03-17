package worldfoodgame.gui.hud.infopanel;

import javax.swing.*;
import java.awt.*;

/**
 * Created by winston on 3/17/15.
 *
 * GUi info panel component. Intended to show an overview all land management
 * information to the user, also allow for user to change attributes.
 */
public class LandPanel extends JPanel
{
  private CountryDataHandler dataHandler;

  // label set
    // overview panel
  private GraphLabel totalLand;
  private GraphLabel arableLand;


  public LandPanel(CountryDataHandler dataHandler)
  {
    this.dataHandler = dataHandler;

    //config
    this.setLayout(new GridLayout(0,3));
    this.add(getOverViewPanel());

  }

  private JPanel getOverViewPanel()
  {
    return null;
  }
}
