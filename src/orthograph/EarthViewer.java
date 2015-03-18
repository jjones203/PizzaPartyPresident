package orthograph;

import IO.XMLparsers.KMLParser;
import model.AtomicRegion;
import model.MapPoint;
import model.Region;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 @author david
 created: 2015-02-11

 description: */
public class EarthViewer extends JPanel
  implements MouseListener, MouseMotionListener, MouseWheelListener
{

 private final Ellipse2D baseEarth;
 private List<Region> regions = new ArrayList<>();
 private OrthographicConverter conv;
 private Point dragStart;

 public EarthViewer()
 {
  setPreferredSize(new Dimension(700,700));
  setMinimumSize(getPreferredSize());
  addMouseListener(this);
  addMouseMotionListener(this);
  addMouseWheelListener(this);

  double circum = conv.EARTH_RAD * 2;
  double x = - conv.EARTH_RAD;
  double y = - conv.EARTH_RAD;
  baseEarth = new Ellipse2D.Double(x, y, circum, circum);
 }

 public void setConverter(OrthographicConverter c)
 {
  conv =c;
 }
 public void addRegions(Collection<Region> col)
 {
  regions.addAll(col);
 }


 @Override
 public void paintComponent(Graphics g)
 {
  super.paintComponent(g);


  Graphics2D g2 = (Graphics2D) g;
  g2.setTransform(conv.makeTransform());

  g2.setColor(new Color(144,144,144));
  g2.fill(baseEarth);
  
  g2.setColor(Color.white);
  for(Path2D p : conv.convertRegions(regions)) g2.draw(p);
 }

 public static void main(String[] args)
 {
  final EarthViewer cam = new EarthViewer();
  boolean showTiles = true;
  int tileSize = 100; /* km per side of tile */
  
  OrthographicConverter conv = new OrthographicConverter(cam);
  cam.setConverter(conv);

  cam.addRegions(KMLParser.getRegionsFromFile("resources/countries_world.xml"));
  if(showTiles)
  {
   cam.addRegions(makeEqAreaTiles(tileSize));
  }
  
  JFrame win = new JFrame();
  win.add(cam);
  win.pack();
  win.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  win.setVisible(true);
  
  new Timer(30, new ActionListener()
  {
   @Override
   public void actionPerformed(ActionEvent e)
   {
    cam.repaint();
   }
  }).start();
  
 }

 /*
  creates a grid of tiles mapped from the Lambert Cylindrical equal area 
  projection into spherical lon-lat space.
  */
 private static Collection<Region> makeEqAreaTiles(int tileSize)
 {
  /* can assume equator is length of circumference and prime meridian is 1/2 that */
  
  final double earth_circ = 40_000;/* in km */
  final double scale = earth_circ / 360; /* km per line of longitude */
  
  int cols = (int)(earth_circ/tileSize);
  int rows = (int)(earth_circ/(2*tileSize));
  System.out.println(rows);
  System.out.println(cols);
  
  List<Region> tiles = new ArrayList<>();

  Region reg;
  List<MapPoint> perim;
  
  /* x and y represent coord of upper left of tiles */
  for (int col = -cols/2; col < cols/2; col++)
  {

   for (int row = -rows/2; row < rows/2; row++)
   {
    perim = new ArrayList<>();
    int x = col * tileSize;
    int y = row * tileSize;
    if (col == 0) System.out.println(y/scale);

    perim.add(lambertToLatLon(x, y, scale));
    perim.add(lambertToLatLon(x + tileSize, y, scale));
    perim.add(lambertToLatLon(x + tileSize, y + tileSize, scale));
    perim.add(lambertToLatLon(x, y + tileSize, scale));

    reg = new AtomicRegion();
    reg.setPerimeter(perim);
    tiles.add(reg);
   }
  }
  return tiles;
 }
 
 private static MapPoint lambertToLatLon(double x, double y, double scale)
 {
  double cent_meridian = 0;
  
  double lon = x/scale + cent_meridian;
  
  /* this is a little hacky. lambert projection maps lat to y [-1,1], and assumes
     both lon and lat are in radians. This produces a map with aspect 3.14.
     The inverse must normalize the Y coordinate to the expected range, then
     remap it to whatever system (degrees
   */
  double lat = Math.toDegrees(Math.asin(y/(90 * scale)));
  
  return new MapPoint(lon, lat);
 }

 @Override
 public void mouseClicked(MouseEvent e)
 {
  
 }

 @Override
 public void mousePressed(MouseEvent e)
 {
  dragStart = e.getPoint();
 }

 @Override
 public void mouseReleased(MouseEvent e)
 {

 }

 @Override
 public void mouseEntered(MouseEvent e)
 {

 }

 @Override
 public void mouseExited(MouseEvent e)
 {

 }

 @Override
 public void mouseDragged(MouseEvent e)
 {
  Point loc = e.getPoint();
  
  double dx = -(loc.x-dragStart.x);
  double dy = (loc.y-dragStart.y);
  
  conv.rotate(dx, dy, 0, 0);
  
  dragStart = loc;
 }

 @Override
 public void mouseMoved(MouseEvent e)
 {
 }

 @Override
 public void mouseWheelMoved(MouseWheelEvent e)
 {
  double dHeight = e.getPreciseWheelRotation()/5;
  conv.rotate(0, 0, 0, dHeight);
 }

}
