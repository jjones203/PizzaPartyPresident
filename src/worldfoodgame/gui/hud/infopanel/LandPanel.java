package worldfoodgame.gui.hud.infopanel;

import worldfoodgame.common.EnumCropType;
import worldfoodgame.gui.ColorsAndFonts;

import javax.swing.*;
import java.awt.*;

/**
 * Created by winston on 3/17/15.
 * <p/>
 * GUi info panel component. Intended to show an overview all land management
 * information to the user, also allow for user to change attributes.
 */
public class LandPanel extends JPanel
{
  private CountryDataHandler dataHandler;

  public LabelFactory getLabelFactory()
  {
    return labelFactory;
  }

  public void setLabelFactory(LabelFactory labelFactory)
  {
    this.labelFactory = labelFactory;
  }

  private LabelFactory labelFactory;


  public LandPanel(LabelFactory labelFactory)
  {
    this.labelFactory = labelFactory;

    //config
    this.setLayout(new GridLayout(0, 3));
    this.setBackground(ColorsAndFonts.GUI_BACKGROUND);
    redraw();
  }

  public void redraw()
  {
    this.removeAll();
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

}
