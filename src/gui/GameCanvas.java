package gui;

import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.List;
import java.util.*;

/**
 * @author david
 *         created: 2015-01-21
 *         <p/>
 *         description:
 *
 */
public class GameCanvas extends JPanel
{
  private Camera cam;
  private CamController controls;
  private List<Polygon> polys = new ArrayList<>();


  public GameCanvas()
  {
    this(new Camera(0,0));
  }

  private void makePolys(int numPolys)
  {
    Random r = new Random();
    for (int i = 0; i < numPolys; i++)
    {
      polys.add(genPoly(r));
    }
  }

  public GameCanvas(Camera cam)
  {
    this.cam = cam;
    controls = new CamController(cam);
    addKeyListener(controls);
    makePolys(100);
    Dimension size = new Dimension(1000,800);
    setPreferredSize(size);
    setSize(size);
  }

  public KeyListener getControls() { return controls;}

  @Override
  protected void paintComponent(Graphics g)
  {
    Graphics2D g2 = (Graphics2D) g;
    Rectangle2D r = new Rectangle2D.Double(10,10,100,100);
    Rectangle2D border = new Rectangle2D.Double(0,0,1000,500);
    g2.setStroke(new BasicStroke(10));
    g2.draw(r);
    g2.draw(border);


    AffineTransform transform = cam.getTransform();

//    testTransform(transform);

    g2.setTransform(transform);
    g2.draw(r);
    for(Polygon p : polys) g2.draw(p);
  }



  private void testTransform(AffineTransform transform)
  {
    Point p0 = new Point(0,0);
    Point p1 = new Point(20,20);
    Point p2 = new Point(-10,-30);
    Point p0n = new Point();
    Point p1n = new Point();
    Point p2n = new Point();

    transform.transform(p0, p0n);
    transform.transform(p1, p1n);
    transform.transform(p2, p2n);

    System.out.println("p0="+p0);
    System.out.println("p0n="+p0n);
    System.out.println("p1="+p1);
    System.out.println("p1n="+p1n);
    System.out.println("p2="+p2);
    System.out.println("p2n="+p2n);
  }

  public Polygon genPoly(Random r)
  {
    int numVertices = 8;
    int sideLenOrder = 10000;
    int xRange = (int) 3.6e8;
    int yRange = (int) 1.8e8;

    Polygon p = new Polygon();
    int baseX = r.nextInt(xRange);
    int baseY = r.nextInt(yRange);
    for (int i = 0; i <= numVertices; i++)
    {
      int x = r.nextInt(sideLenOrder) + baseX;
      int y = r.nextInt(sideLenOrder) + baseY;
      p.addPoint(x, y);
    }
    return p;
  }

  public static void main(String[] args)
  {
    JFrame win = new JFrame();
    GameCanvas canvas = new GameCanvas();
    win.setContentPane(canvas);
    win.addKeyListener(canvas.getControls());
    win.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    win.pack();
    win.setVisible(true);
    canvas.start();
  }

  private void start()
  {
    Timer t = new Timer(30, new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent e)
      {
        repaint();
        controls.actionPerformed(e);
      }
    });
    t.start();
//    Timer controllerPoll = new Timer(30,controls);
//    controllerPoll.start();
  }

}
