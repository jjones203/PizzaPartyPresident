package worldfoodgame.gui.hud.infopanel;

import worldfoodgame.common.EnumCropType;
import worldfoodgame.gui.ColorsAndFonts;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by winston on 3/12/15.
 */
public class CropPanel extends JPanel
{
  private final EnumCropType type;
  private LabelFactory labelFactory;

  public CropPanel(LabelFactory labelFactory, EnumCropType type)
  {
    // init.
    this.type = type;
    this.labelFactory = labelFactory;

    //conf.
    setBackground(ColorsAndFonts.GUI_BACKGROUND);
    setLayout(new GridLayout(0, 3));
    redraw();
  }

  public LabelFactory getLabelFactory()
  {
    return labelFactory;
  }

  public void setLabelFactory(LabelFactory labelFactory)
  {
    this.labelFactory = labelFactory;
  }

  public void redraw()
  {
    removeAll();
    add(getIconPanel());
    add(getOverViewPanel());
    add(getControllPanel());
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
    landUse.add(labelFactory.getLandLabel(type));
    landUse.add(labelFactory.getOpenLandLabel());
    return landUse;
  }

  // creates the sub-panel for production, exports, and imports info.
  private JPanel getOverViewPanel()
  {
    JPanel overView = new JPanel();
    overView.setBackground(ColorsAndFonts.GUI_BACKGROUND);
    overView.setLayout(new BoxLayout(overView, BoxLayout.Y_AXIS));
    overView.add(labelFactory.getProductionLabel(type));
    overView.add(labelFactory.getExportedLabel(type));
    overView.add(labelFactory.getImportedLabel(type));
    return overView;
  }



  // FOR TESTING ONLY
//  public static void main(String[] args)
//  {
//
//    final JFrame jFrame = new JFrame();
//    final CropPanel cropPanel = new CropPanel(CountryDataHandler.getTestData(), EnumCropType.OTHER_CROPS);
//    jFrame.add(cropPanel);
//
//    jFrame.pack();
//    jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
//    jFrame.setVisible(true);
//
//    new Timer(10, new AbstractAction()
//    {
//      @Override
//      public void actionPerformed(ActionEvent e)
//      {
//        jFrame.repaint();
//      }
//    }).start();
//
//  }

}




