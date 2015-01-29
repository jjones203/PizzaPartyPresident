package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * @author david
 *         created: 2015-01-21
 *         <p/>
 *         description:
 *
 *         Main class for testing gui components....  not sure if necessary yet
 */
public class Tests extends JFrame
{

  public Tests()
  {
    setSize(300, 300);
    setLayout(new FlowLayout(FlowLayout.LEFT));
    setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    addMouseListener(new MouseAdapter()
    {
      @Override
      public void mouseClicked(MouseEvent e)
      {
        System.out.println("JFRAME");
        System.out.println(e.getLocationOnScreen());
        System.out.println(e.getPoint());
      }
    });

    JPanel p1 = new JPanel();
    p1.setBorder(BorderFactory.createLineBorder(Color.BLACK));
    p1.setSize(100,100);
    add(p1);
    p1.addMouseListener(new MouseAdapter()
    {
      @Override
      public void mouseClicked(MouseEvent e)
      {
        System.out.println("P1");
        System.out.println(e.getLocationOnScreen());
        System.out.println(e.getPoint());
      }
    });

    JPanel p2 = new JPanel();
    p2.setSize(100,100);
    p2.setBorder(BorderFactory.createLineBorder(Color.RED));
    add(p2);
    p2.addMouseListener(new MouseAdapter()
    {
      @Override
      public void mouseClicked(MouseEvent e)
      {
        System.out.println("P2");
        System.out.println(e.getLocationOnScreen());
        System.out.println(e.getPoint());
      }
    });


  }
  public static void main(String[] args)
  {

    Tests t = new Tests();
    t.pack();
    t.setVisible(true);
  }


}
