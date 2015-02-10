import IO.AreaXMLLoader;
import IO.XMLparsers.KMLParser;
import gui.*;
import gui.displayconverters.EquirectangularConverter;
import gui.displayconverters.MapConverter;
import gui.hud.InfoPanel;
import gui.hud.WorldFeedPanel;
import model.Region;
import model.World;
import IO.AttributeGenerator;

import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;

/**
 * @author david
 *         created: 2015-02-04
 *         <p/>
 *         description:
 */
public class Main
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
    modelMap.addAll(new AreaXMLLoader().getRegions()); // adds XML regions for area folder...
    for (Region r : modelMap)
    {
      randoAtts.setRegionAttributes(r, random);
    }

    List<Region> allRegions = new ArrayList<>(modelMap);
    allRegions.addAll(backgroundRegions);

    World world = new World(allRegions);


    MapConverter converter = new EquirectangularConverter();
    final WorldPresenter presenter = new WorldPresenter(converter, world);
    presenter.setBackgroundRegions(backgroundRegions);
    presenter.setModelRegions(modelMap);

    Camera cam = new Camera(converter);
    final MapPane mapPane = new MapPane(cam, presenter);


    final InfoPanel infoPanel = new InfoPanel();
    infoPanel.setPresenter(presenter);
    
    final WorldFeedPanel worldFeedPanel = new WorldFeedPanel(presenter);
    presenter.addObserver(worldFeedPanel);

    JFrame win = new JFrame();
    win.setLayout(new BorderLayout());
    win.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    win.add(worldFeedPanel, BorderLayout.NORTH);
    win.add(mapPane, BorderLayout.CENTER);
    win.add(infoPanel, BorderLayout.SOUTH);
    win.addKeyListener(mapPane);
    win.pack();
    win.setVisible(true);
    win.setResizable(false);
    
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


    Timer worldTime = new Timer(1000, new AbstractAction()
    {
      @Override
      public void actionPerformed(ActionEvent e)
      {
        presenter.setWorldForward(1);
        worldFeedPanel.setDate(presenter.getWorldDate());
      }
    });
    worldTime.start();

  }

}
