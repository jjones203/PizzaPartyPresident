package worldfoodgame.gui.hud.infopanel;

import worldfoodgame.common.EnumCropType;
import worldfoodgame.gui.ColorsAndFonts;

import javax.swing.*;
import java.awt.*;
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
  private LabelFactory labelFactory;


  public LandPanel(CountryDataHandler dataHandler, LabelFactory labelFactory)
  {
    this.dataHandler = dataHandler;
    this.labelFactory = labelFactory;

    //config
    this.setLayout(new GridLayout(0, 3));
    this.setBackground(ColorsAndFonts.GUI_BACKGROUND);
    this.add(getOverViewPanel());
    this.add(getCropView());

  }

  private JPanel getCropView()
  {
    JPanel cropView = new JPanel();
    cropView.setBackground(ColorsAndFonts.GUI_BACKGROUND);
    cropView.setLayout(new BoxLayout(cropView, BoxLayout.Y_AXIS));

    for (EnumCropType crop : EnumCropType.values())
    {
      cropView.add(labelFactory.getLandLabel(crop));
    }

    return cropView;
  }


  private JPanel getOverViewPanel()
  {
    JPanel overView = new JPanel();
    overView.setBackground(ColorsAndFonts.GUI_BACKGROUND);
    overView.setLayout(new BoxLayout(overView, BoxLayout.Y_AXIS));

    overView.add(labelFactory.getTotalLand());
    overView.add(labelFactory.getArableLand());
    overView.add(labelFactory.getOpenLandLabel());
    return overView;
  }

  @Override
  public void update(Observable o, Object arg)
  {
    // todo add update value to here.
  }
}
