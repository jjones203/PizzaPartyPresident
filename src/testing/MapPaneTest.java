package testing;

import IO.XMLparsers.KMLParser;
import gui.*;
import gui.hud.AmericanUniteConverter;
import gui.hud.InfoPanel;
import gui.hud.MetricDisplayConverter;
import gui.regionlooks.RegionViewFactory;
import model.Region;
import model.World;
import testing.generators.AttributeGenerator;

import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

/**
 * @author david
 *         created: 2015-02-04
 *         <p/>
 *         description:
 */
public class MapPaneTest
{

  public static void main(String[] args)
  {

    Random random = new Random(234);
    AttributeGenerator randoAtts = new AttributeGenerator();

    Collection<Region> backgroundRegions = KMLParser.getRegionsFromFile("resources/countries_world.xml");

    for (Region r : backgroundRegions)
    {
      randoAtts.setRegionAttributes(r, random);
    }

    Collection<Region> modelMap = KMLParser.getRegionsFromFile("resources/ne_10m_admin_1_states_provinces.kml");

    for (Region r : modelMap)
    {
      randoAtts.setRegionAttributes(r, random);
    }

    Collection<Region> allregions = new ArrayList<>(backgroundRegions);
    allregions.addAll(modelMap);

    World world = new World(allregions);


    MapConverter converter = new EquirectangularConverter();
    final WorldPresenter presenter = new WorldPresenter(converter, world);
    presenter.setBackgroundRegions(backgroundRegions);
    presenter.setModelRegions(modelMap);

    Camera cam = new Camera(converter);
    final MapPane mapPane = new MapPane(cam, presenter);


    final InfoPanel infoPanel = new InfoPanel();
    infoPanel.setPresenter(presenter);

    JFrame win = new JFrame();
    win.setLayout(new BorderLayout());
    win.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    win.add(mapPane, BorderLayout.CENTER);
    win.add(infoPanel, BorderLayout.SOUTH);
    win.addKeyListener(mapPane);
    win.pack();
    win.setVisible(true);
    
    
    new Timer(20, new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent e)
      {
        mapPane.repaint();
        mapPane.update();
        infoPanel.repaint(); // todo this is weird. we should look into why this is needed
      }
    }).start();

  }

}
