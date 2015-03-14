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
  private GraphLabel openLand;
  public CropPanel(final CountryDataHandler dataHandler, final EnumCropType type)
  {
    this.type = type;


    setBackground(ColorsAndFonts.GUI_BACKGROUND);
    setLayout(new GridLayout(0, 3));

    JPanel overView = new JPanel();
    overView.setBackground(ColorsAndFonts.GUI_BACKGROUND);
    overView.setLayout(new BoxLayout(overView, BoxLayout.Y_AXIS));
    overView.add(
      new GraphLabel("Production", dataHandler.production.get(type),
        dataHandler.landArea, 1000, Color.red, false,"#,###,### tons")
    );

    overView.add(
      new GraphLabel("Exported", dataHandler.exports.get(type),
        dataHandler.exports.get(type), 10, Color.red, false, "#,###,### tons")
    );

    overView.add(
      new GraphLabel("Imported", dataHandler.imports.get(type),
        dataHandler.exports.get(type), 1, Color.red, false,"#,###,### tons")
    );

    JPanel landUse = new JPanel();
    landUse.setBackground(ColorsAndFonts.GUI_BACKGROUND);
    landUse.setLayout(new BoxLayout(landUse, BoxLayout.Y_AXIS));

    landcontroll = new GraphLabel("Land used", dataHandler.land.get(type),
      dataHandler.getCultivatedLand(), dataHandler.getCultivatedLand()/100, Color.red, true, "#,###,### kilos");

    landcontroll.setConsequent(new Runnable()
    {
      @Override
      public void run()
      {
        dataHandler.setland(type, landcontroll.getValue());
        System.out.println("get cultivated land: " + dataHandler.getCultivatedLand());
        openLand.setValue(dataHandler.getOpenLand());
        /**
         * todo
         * this is still very rough, basically what needs to happen is that
         * when ever a runnable like this is called it sets the appropriate filed in the data handler,
         * then calls a reset method on all the lables in  question so that there value is updated.
         */
      }
    });

    landUse.add(landcontroll);

    openLand = new GraphLabel("Arable open Land", dataHandler.getOpenLand(),
      dataHandler.getCultivatedLand(), dataHandler.getOpenLand()/100, Color.red, true, "#,###,### kilos");

    landUse.add(openLand);

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




