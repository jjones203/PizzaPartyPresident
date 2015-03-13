package worldfoodgame.gui.hud.cropPanel;

import worldfoodgame.common.EnumCropType;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * Created by winston on 3/12/15.
 */
public class CropPanel extends JPanel
{
  private EnumCropType type;
  public CropPanel(CountryDataHandler dataHandler, EnumCropType type)
  {
    this.type = type;



    setLayout(new GridLayout(0, 3));

    JPanel overView = new JPanel();
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
    landUse.setLayout(new BoxLayout(landUse, BoxLayout.Y_AXIS));
    landUse.add(
      new GraphLabel("Land used", dataHandler.getLand(type),
        dataHandler.getCultivatedLand(), 10, Color.red, false, "#,###,### kilos")
    );

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




