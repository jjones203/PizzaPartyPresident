package gui.hud;

import IO.XMLparsers.KMLParser;
import gui.EquirectangularConverter;
import gui.GUIRegion;
import model.Region;
import testing.generators.AttributeGenerator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Collections;
import java.util.Random;

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

  public void display(GUIRegion region)
  {
    miniViewBox.setTitle(region.getName());
    miniViewBox.setRegionPolygon(region.getPoly());
    attributeStats.displayRegionAttributes(region.getRegion().getAttributes());
    cropStatPane.displayRegionCrops(region.getRegion().getAttributes());
  }

  public static void main(String[] args)
  {
    long seed = 442;
    Random random = new Random();
    AttributeGenerator randoAtts = new AttributeGenerator(random);

    java.util.List<Region> testlist = (java.util.List<Region>) KMLParser.getRegionsFromFile("resources/ne_50m_admin_1_states_provinces_lakes.kml");
    Collections.shuffle(testlist, random);

    Region firstRegion = testlist.get(0);
    firstRegion.setAttributes(randoAtts.nextAttributeSet());
    System.out.println("regoins name: " + firstRegion);
    GUIRegion testRegion = new GUIRegion(firstRegion, new EquirectangularConverter(), null);

    final JFrame frame = new JFrame();
    LowerPanel lowerPanel = new LowerPanel();
    lowerPanel.display(testRegion);

    frame.add(lowerPanel);
    frame.pack();
    frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
//    frame.setResizable(false);
    frame.setVisible(true);

    Timer animator = new Timer(20, new AbstractAction()
    {
      @Override
      public void actionPerformed(ActionEvent e)
      {
        frame.repaint();
      }
    });

    animator.start();

  }
}
