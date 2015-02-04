package gui.hud;

import IO.XMLparsers.KMLParser;
import gui.EquirectangularConverter;
import gui.GUIRegion;
import model.Region;
import model.RegionAttributes;
import testing.generators.AttributeGenerator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Collections;
import java.util.Random;
import static model.RegionAttributes.PLANTING_ATTRIBUTES;

/**
 * Created by winston on 2/3/15.
 */
public class LowerPanel extends JPanel
{
  private MiniViewBox miniViewBox;
  private StatPane attributeStats;
  private StatPane cropStatPane;

  public LowerPanel()
  {
    // init
    miniViewBox = new MiniViewBox("REGION NAME");
    attributeStats = new StatPane("ATTRIBUTES:");
    cropStatPane = new StatPane("CROPS:");

    //config
    this.setLayout(new GridLayout(1,3));

    //wire
    this.add(miniViewBox);
    this.add(attributeStats);
    this.add(cropStatPane);
  }

  public void displayGUIRegion(GUIRegion region)
  {
    System.out.println("region name: " + region.getName());
    miniViewBox.setTitle(region.getName());
    miniViewBox.setRegionPolygon(region.getPoly());

    attributeStats.clearBarPlots();
    displayAttributes(region, attributeStats);
    attributeStats.revalidate();

    cropStatPane.clearBarPlots();
    diplayCropState(region, cropStatPane);
    cropStatPane.revalidate();
  }

  /**
   * Controls the presentation logic of building up the crop percentages section
   * of the GUI info pane.
   * @param region  data that will be extracted and displayed.
   * @param statPane GUI element to 'write' to.
   */
  private void diplayCropState(GUIRegion region, StatPane statPane)
  {
    RegionAttributes atts = region.getRegion().getAttributes();
    for (String cropName : atts.getAllCrops())
    {
      BarPanel bp = new BarPanel(
          Color.cyan,
          atts.getCropP(cropName),
          cropName,
          "%" + String.format("%.2f", atts.getCropP(cropName) * 100)
      );
      statPane.addBar(bp);
    }
  }


  /**
   * Controls the presentation logic for displaying the the soil attributes
   * in the info panel for the specified region.
   * @param region to be displayed
   * @param statPane GUI element to 'write' to.
   */
  private void displayAttributes(GUIRegion region, StatPane statPane)
  {
    RegionAttributes atts = region.getRegion().getAttributes();
    for (PLANTING_ATTRIBUTES att : PLANTING_ATTRIBUTES.values())
    {
      BarPanel bp = new BarPanel(
          Color.cyan,
          atts.getAttribute(att) / 20,
          att.toString(),
          String.format("%.2f", atts.getAttribute(att))
      );
      statPane.addBar(bp);
    }
  }

  public static void main(String[] args)
  {
    long seed = 442;
    final Random random = new Random();
    final AttributeGenerator randoAtts = new AttributeGenerator(random);

    final java.util.List<Region> testlist = (java.util.List<Region>) KMLParser.getRegionsFromFile("resources/ne_50m_admin_1_states_provinces_lakes.kml");
    Collections.shuffle(testlist, random);

    Region firstRegion = testlist.get(0);
    firstRegion.setAttributes(randoAtts.nextAttributeSet());
    System.out.println("regoins name: " + firstRegion);
    GUIRegion testRegion = new GUIRegion(firstRegion, new EquirectangularConverter(), null);

    final JFrame frame = new JFrame();
    final LowerPanel lowerPanel = new LowerPanel();
    lowerPanel.displayGUIRegion(testRegion);

    frame.add(lowerPanel);
    frame.pack();
    frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
//    frame.setResizable(false);
    frame.setVisible(true);

    // code this ugly is only for testing....
    new Timer(20, new AbstractAction()
    {
      @Override
      public void actionPerformed(ActionEvent e)
      {
        frame.repaint();
      }
    }).start();


    new Timer(1000 * 5, new AbstractAction()
    {
      @Override
      public void actionPerformed(ActionEvent e)
      {
        Region region = testlist.remove(0);
        region.setAttributes(randoAtts.nextAttributeSet());
        GUIRegion guiRegion = new GUIRegion(region, new EquirectangularConverter(), null);
        lowerPanel.displayGUIRegion(guiRegion);
      }
    }).start();
  }
}
