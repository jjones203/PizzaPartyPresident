package gui;

import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
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
    g2.drawString("Testing", 100,100);

    AffineTransform transform = cam.getTransform();
    Point ptSrc = new Point(100,100);
    Point2D.Double ptDst = new Point2D.Double();
    transform.transform(ptSrc, ptDst);
    System.out.println(ptDst);
    g2.setTransform(transform);


    for(Polygon p : polys) g2.draw(p);
  }

  public Polygon genPoly(Random r)
  {
    int numVertices = 8;
    int sideLenOrder = 100;
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
    Timer t = new Timer(500, new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent e)
      {
        repaint();
        System.out.println(cam);
      }
    });
    t.start();
  }

}
