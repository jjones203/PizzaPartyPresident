package gui;

import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
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


  public GameCanvas()
  {
    this(new Camera(0,0));
  }



  public GameCanvas(Camera cam)
  {
    this.cam = cam;
    controls = new CamController(cam);
    addKeyListener(controls);
    Dimension size = new Dimension(1000,800);
    setPreferredSize(size);
    setSize(size);
  }

  public KeyListener getControls() { return controls;}

  @Override
  protected void paintComponent(Graphics g)
  {
    Graphics2D g2 = (Graphics2D) g;

    g2.drawString(cam.toString(), 10, 510);
    Rectangle2D border = new Rectangle2D.Double(0,0,1000,500);
    g2.setStroke(new BasicStroke(2));
    g2.draw(border);
    g2.setColor(Color.lightGray);

    AffineTransform transform = cam.getTransform();

    g2.setTransform(transform);

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
    Timer t = new Timer(20, new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent e)
      {
        repaint();

        /* tick control listener too, same f as screen refresh */
        controls.actionPerformed(e);
      }
    });
    t.start();
  }

}
