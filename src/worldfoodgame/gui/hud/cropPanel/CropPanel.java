package worldfoodgame.gui.hud.cropPanel;

import worldfoodgame.common.EnumCropType;
import worldfoodgame.gui.ColorsAndFonts;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * Created by winston on 3/12/15.
 */
public class CropPanel extends JPanel
{
  private EnumCropType type;
  private GraphLabel landcontroll;
  private GraphLabel inverse;
  public CropPanel(final CountryDataHandler dataHandler, EnumCropType type)
  {
    this.type = type;


    setBackground(ColorsAndFonts.GUI_BACKGROUND);
    setLayout(new GridLayout(0, 3));

    JPanel overView = new JPanel();
    overView.setBackground(ColorsAndFonts.GUI_BACKGROUND);
    overView.setLayout(new BoxLayout(overView, BoxLayout.Y_AXIS));
    overView.add(
      new GraphLabel("Production", dataHandler.getProduction(type),
        dataHandler.landArea, 1000, Color.red, false,"#,###,### tons")
    );

    overView.add(
      new GraphLabel("Exported", dataHandler.getExport(type),
        dataHandler.getExport(type), 10, Color.red, false, "#,###,### tons")
    );

    overView.add(
      new GraphLabel("Imported", dataHandler.getImports(type),
        dataHandler.getExport(type), 1, Color.red, false,"#,###,### tons")
    );

    JPanel landUse = new JPanel();
    landUse.setBackground(ColorsAndFonts.GUI_BACKGROUND);
    landUse.setLayout(new BoxLayout(landUse, BoxLayout.Y_AXIS));

    landcontroll = new GraphLabel("Land used", dataHandler.getLand(type),
      dataHandler.getCultivatedLand(), dataHandler.getCultivatedLand()/100, Color.red, true, "#,###,### kilos");

    landcontroll.setConsequent(new Runnable()
    {
      @Override
      public void run()
      {
        inverse.setPercent(inverse.getPercent() - dataHandler.getCultivatedLand()/100);
      }
    });

    landUse.add(landcontroll);

    inverse = new GraphLabel("inverse", dataHandler.getLand(type),
      dataHandler.getCultivatedLand(), dataHandler.getCultivatedLand()/100, Color.red, true, "#,###,### kilos");

    landUse.add(inverse);

    add(overView);
    add(landUse);
  }

  public static void main(String[] args)
  {

    final JFrame jFrame = new JFrame();

    jFrame.add(new CropPanel(CountryDataHandler.getTestData(), EnumCropType.CORN));

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
}




