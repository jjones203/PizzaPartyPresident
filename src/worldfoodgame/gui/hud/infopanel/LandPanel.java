package worldfoodgame.gui.hud.infopanel;

import worldfoodgame.common.EnumCropType;
import worldfoodgame.gui.ColorsAndFonts;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by winston on 3/17/15.
 * <p/>
 * GUi info panel component. Intended to show an overview all land management
 * information to the user, also allow for user to change attributes.
 */
public class LandPanel extends JPanel implements Observer
{
  private CountryDataHandler dataHandler;

  // label set
  // overview panel
  private GraphLabel
    landTotal,
    arableLand,
    gmoPercentage,
    organicP,
    conventionalP,
    totalYeild,
    totalNeed;

  private HashMap<EnumCropType, GraphLabel> landByCrops;


  public LandPanel(CountryDataHandler dataHandler)
  {
    this.dataHandler = dataHandler;

    //config
    this.setLayout(new GridLayout(0, 3));
    this.setBackground(ColorsAndFonts.GUI_BACKGROUND);
    this.add(getOverViewPanel());


    // ONLY FOR TESTING
    this.add(getTestingPanel());

  }

  private JPanel getTestingPanel()
  {
    JPanel testPanel = new JPanel();
    testPanel.setBackground(ColorsAndFonts.GUI_BACKGROUND);
    testPanel.setLayout(new BoxLayout(testPanel, BoxLayout.Y_AXIS));

    LabelFactory labelFactory = new LabelFactory(dataHandler);

    testPanel.add(labelFactory.getPopulationLabel());
    testPanel.add(labelFactory.getPopulationControll());

    return testPanel;
  }

  private JPanel getOverViewPanel()
  {
    JPanel overView = new JPanel();
    overView.setBackground(ColorsAndFonts.GUI_BACKGROUND);
    overView.setLayout(new BoxLayout(overView, BoxLayout.Y_AXIS));

    landTotal = new GraphLabel("Land Total", dataHandler.landTotal,
      dataHandler.landTotal, "#,###,### km sq");
    overView.add(landTotal);

    arableLand = new GraphLabel("Arable Land", dataHandler.getOpenLand(),
      dataHandler.landTotal, "#,###,### km sq");
    overView.add(arableLand);

    return overView;
  }

  @Override
  public void update(Observable o, Object arg)
  {
    // todo add update value to here.
  }
}
