package gui.hud;

import IO.XMLparsers.KMLParser;
import gui.EquirectangularConverter;
import gui.GUIRegion;
import gui.regionlooks.PlantingZoneView;
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
public class InfoPanel extends JPanel
{
  private MiniViewBox miniViewBox;
  private StatPane attributeStats;
  private StatPane cropStatPane;
  private Dimension size = new Dimension(1000, 220);

  public InfoPanel()
  {
    // init
    miniViewBox = new MiniViewBox("REGION NAME");
    attributeStats = new StatPane("ATTRIBUTES:");
    cropStatPane = new StatPane("CROPS:");

    //config
    this.setLayout(new GridLayout(1,3));
    this.setMinimumSize(size);
    this.setPreferredSize(size);

    //wire
    this.add(miniViewBox);
    this.add(attributeStats);
    this.add(cropStatPane);
  }

  public void displayGUIRegion(GUIRegion region)
  {
    miniViewBox.setTitle(region.getName());
    miniViewBox.setDrawableArea(region.getArea());

    RegionAttributes attributes = region.getRegion().getAttributes();

    attributeStats.clearBarPlots();
    displayAttributes(attributes, attributeStats);
    attributeStats.revalidate();

    cropStatPane.clearBarPlots();
    diplayCropState(attributes, cropStatPane);
    cropStatPane.revalidate();
  }



  /**
   * Controls the presentation logic of building up the crop percentages section
   * of the GUI info pane.
   * @param atts  data that will be extracted and displayed.
   * @param statPane GUI element to 'write' to.
   */
  private void diplayCropState(RegionAttributes atts, StatPane statPane)
  {
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
   * @param atts Attribute set to be displayed.
   * @param statPane GUI element to 'write' to.
   */
  private void displayAttributes(RegionAttributes atts, StatPane statPane)
  {
    if (atts == null)
    {
      System.err.println("atts for region are null.");
      return;
    }

    for (PLANTING_ATTRIBUTES att : PLANTING_ATTRIBUTES.values())
    {
      BarPanel bp = getBarPanel(atts, att);
      statPane.addBar(bp);
    }
  }

  private BarPanel getBarPanel(RegionAttributes attributesSet, PLANTING_ATTRIBUTES att)
  {

    String Primarylable = att.toString();
    Color barColor = Color.cyan;
    double ratio = attributesSet.getAttribute(att) / 20; // magic number from ramdom number generation
    String secondaryLable = String.format("%.2f", ratio);

    switch (att)
    {
      case PLANTING_ZONE:
        barColor = PlantingZoneView.getPlantingColor(attributesSet.getAttribute(att));
        ratio = 1;
        secondaryLable = "ZONE: " + (int) (double) attributesSet.getAttribute(att);
        break;

      case COST_OF_CROPS:
        barColor = Color.red;
        secondaryLable = "$" + secondaryLable;
        break;

      case HAPPINESS:
        boolean unhappy = ratio < 0.5;
        barColor = unhappy ? Color.red : Color.cyan;
        secondaryLable = unhappy ? "unhappy" : "happy";

      default:
        // no nothing fall back on the above default values.


    }

    return new BarPanel(
            barColor,
            ratio,
            Primarylable,
            secondaryLable
        );
  }

  public void clearDisplay()
  {
    miniViewBox.setTitle(" ");
    miniViewBox.setDrawableArea(null);
    cropStatPane.clearBarPlots();
    attributeStats.clearBarPlots();
  }

  /**
   * FOR TESTING ONLY
   * todo remove when done!
   * @param args
   */
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
    final InfoPanel infoPanel = new InfoPanel();
    infoPanel.displayGUIRegion(testRegion);

    frame.add(infoPanel);
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
        infoPanel.displayGUIRegion(guiRegion);
      }
    }).start();
  }


}
