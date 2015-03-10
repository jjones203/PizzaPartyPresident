package orthograph;

import IO.XMLparsers.KMLParser;
import model.Region;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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
  
  OrthographicConverter conv = new OrthographicConverter(cam);
  cam.setConverter(conv);
  
  cam.addRegions(KMLParser.getRegionsFromFile("resources/countries_world.xml"));
  
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
