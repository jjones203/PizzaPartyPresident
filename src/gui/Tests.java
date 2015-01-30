package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Path2D;
import java.util.Random;

/**
 * @author david
 *         created: 2015-01-21
 *         <p/>
 *         description:
 *
 *         Main class for testing gui components....  not sure if necessary yet
 */
public class Tests extends JPanel
{

  private Random r = new Random();

  public Tests()
  {
    setSize(800,800);
  }

  @Override
  protected void paintComponent(Graphics g)
  {
    Path2D p = new Path2D.Double();
    Graphics2D g2d = (Graphics2D) g;
    for (int i = 0; i < 5; i++)
    {
      g2d.setStroke(new BasicStroke(2));
      p = randoPath();

      g2d.setColor(new Color(r.nextInt(), true));
      g2d.fill(p);
      
      g2d.setColor(new Color(r.nextInt(), true));
      g2d.draw(p);
    }
    
    p.reset();
    p.moveTo(100,100);
    p.lineTo(300,100);
    p.lineTo(300,300);
    p.lineTo(100,300);
    p.closePath();
    g2d.setColor(Color.BLUE);
    g2d.fill(p);
    g2d.setColor(Color.CYAN);
    g2d.draw(p);
    
    
  }

  private Path2D randoPath()
  {
    Path2D p = new Path2D.Double();
    p.moveTo(r.nextDouble()*300, r.nextDouble()*500);
    for (int i = 0; i < 6; i++)
    {
      p.lineTo(r.nextDouble() * 300, r.nextDouble() * 500);
    }
    p.closePath();
    return p;
  }

  public static void main(String[] args)
  {

    JFrame frame = new JFrame();
    Tests t = new Tests();
    frame.setSize(800,800);
    frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    frame.add(t);
    frame.pack();
    frame.setVisible(true);
  }


}
