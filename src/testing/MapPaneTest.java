package testing;

import IO.XMLparsers.KMLParser;
import gui.*;
import gui.hud.InfoPanel;
import gui.regionlooks.RegionViewFactory;
import model.Region;
import model.World;
import testing.generators.AttributeGenerator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;
import java.util.Observable;
import java.util.Observer;
import java.util.Random;

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
    AttributeGenerator randoAtts = new AttributeGenerator(random);

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

    World world = new World(modelMap);

    MapConverter converter = new EquirectangularConverter();
    final WorldPresenter presenter = new WorldPresenter(converter);
    presenter.setBackgroundRegions(backgroundRegions);
    presenter.setModelRegions(modelMap);

    Camera cam = new Camera(converter);
    final MapPane mapPane = new MapPane(cam, presenter);


    final InfoPanel infoPanel = new InfoPanel();

    JFrame win = new JFrame();
    win.setLayout(new BorderLayout());
    win.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    win.add(mapPane, BorderLayout.CENTER);
    win.add(infoPanel, BorderLayout.SOUTH);
    win.addKeyListener(mapPane);
    win.pack();
    win.setVisible(true);


    Observer observer = new Observer()
    {
      @Override
      public void update(Observable o, Object arg)
      {
        java.util.List<GUIRegion> regions = presenter.getActiveRegions();
        if (regions == null || regions.size() > 1)
        {
          infoPanel.clearDisplay();
          return;
        }

        if (regions.size() == 1)
        {
          infoPanel.displayGUIRegion(regions.get(0));
        }
        else
        {
          infoPanel.displayAllGUIRegions(regions);
        }



      }
    };
    presenter.addObserver(observer);

    
    
    new Timer(20, new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent e)
      {
        mapPane.repaint();
        mapPane.update();
        infoPanel.repaint();
      }
    }).start();

  }

}
