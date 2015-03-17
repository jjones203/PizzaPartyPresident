package worldfoodgame.gui.hud.infopanel;

import worldfoodgame.common.EnumCropType;
import worldfoodgame.gui.ColorsAndFonts;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by winston on 3/12/15.
 */
public class CropPanel extends JPanel
{
  private final static Collection<CropPanel> relatedPanels = new ArrayList<>();
  private final EnumCropType type;
  private CountryDataHandler dataHandler;
  private GraphLabel
    landcontroll,
    openLand,
    production,
    exported,
    imported;

  private Runnable landAdjustment = new Runnable()
  {
    @Override
    public void run()
    {
      dataHandler.setland(type, landcontroll.getValue());
      for (CropPanel panel : relatedPanels)
      {
        panel.resetValues();
      }
    }
  };

  public CropPanel(CountryDataHandler dataHandler, EnumCropType type)
  {
    // init.
    this.dataHandler = dataHandler;
    this.type = type;
    initLabelsAndControlls(dataHandler, type);

    //conf.
    setBackground(ColorsAndFonts.GUI_BACKGROUND);
    setLayout(new GridLayout(0, 3));
    add(getIconPanel());
    add(getOverViewPanel());
    add(getControllPanel());

    CropPanel.relatedPanels.add(this);
  }

  private JPanel getIconPanel()
  {
    JLabel img = null;
    BufferedImage icon = null;
    try
    {
      icon = ImageIO.read(new File("resources/imgs/wheatLogo.png"));
      img = new JLabel(new ImageIcon(icon));
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
    JPanel iconPanel = new JPanel();
    iconPanel.setBackground(ColorsAndFonts.GUI_BACKGROUND);
    iconPanel.add(img);
    iconPanel.setMaximumSize(new Dimension(icon.getWidth(), icon.getHeight()));
    return iconPanel;
  }

  // creates the control sub-panel for adjusting the amount of land for crop.
  private JPanel getControllPanel()
  {
    JPanel landUse = new JPanel();
    landUse.setBackground(ColorsAndFonts.GUI_BACKGROUND);
    landUse.setLayout(new BoxLayout(landUse, BoxLayout.Y_AXIS));
    landUse.add(landcontroll);
    landUse.add(openLand);
    return landUse;
  }

  // creates the sub-panel for production, exports, and imports info.
  private JPanel getOverViewPanel()
  {
    JPanel overView = new JPanel();
    overView.setBackground(ColorsAndFonts.GUI_BACKGROUND);
    overView.setLayout(new BoxLayout(overView, BoxLayout.Y_AXIS));
    overView.add(production);
    overView.add(exported);
    overView.add(imported);
    return overView;
  }

  private void initLabelsAndControlls(CountryDataHandler dataHandler, EnumCropType type)
  {
    production = new GraphLabel("Production", dataHandler.production.get(type),
      dataHandler.landArea, "#,###,### tons");

    exported = new GraphLabel("Exported", dataHandler.exports.get(type),
      dataHandler.exports.get(type), "#,###,### tons");

    imported = new GraphLabel("Imported", dataHandler.imports.get(type),
      dataHandler.exports.get(type), "#,###,### tons");

    landcontroll = new GraphLabel("Land used", dataHandler.land.get(type),
      dataHandler.getCultivatedLand(), "#.## km sq", landAdjustment);

    openLand = new GraphLabel("Arable open Land", dataHandler.getOpenLand(),
      dataHandler.getCultivatedLand(), "#,###,### km sq");
  }

  // FOR TESTING ONLY
  public static void main(String[] args)
  {

    final JFrame jFrame = new JFrame();
    final CropPanel cropPanel = new CropPanel(CountryDataHandler.getTestData(), EnumCropType.OTHER_CROPS);
    jFrame.add(cropPanel);

    jFrame.pack();
    jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    jFrame.setVisible(true);

    new Timer(10, new AbstractAction()
    {
      @Override
      public void actionPerformed(ActionEvent e)
      {
        jFrame.repaint();
      }
    }).start();

  }


  public void setData(CountryDataHandler dataHandler)
  {
    this.dataHandler = dataHandler;
    resetValues();
  }

  private void resetValues()
  {
    landcontroll.setValue(dataHandler.land.get(type));
    openLand.setValue(dataHandler.getOpenLand());
    production.setValue(dataHandler.production.get(type));
    exported.setValue(dataHandler.exports.get(type));
    imported.setValue(dataHandler.imports.get(type));
  }
}




