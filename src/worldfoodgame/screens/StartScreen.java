package worldfoodgame.screens;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;

import com.sun.glass.events.WindowEvent;

public class StartScreen extends JFrame
{
  public boolean userNotReady = true;
  JButton start = new JButton("START");
  JButton  quit = new JButton("QUIT");

  JPanel directionsPanel = new JPanel();
  JPanel imagePanel = new JPanel();
  JPanel menuPanel = new JPanel();
  JPanel midPanel = new JPanel();


  public StartScreen()
  {
    init();
    JLabel textLabel = new JLabel("I'm a label in the window",SwingConstants.CENTER); textLabel.setPreferredSize(new Dimension(300, 100)); 
  }

  private void init()
  {
    setLayout(new BorderLayout());
    add(imagePanel, BorderLayout.WEST);
    add(midPanel, BorderLayout.CENTER);
    add(imagePanel, BorderLayout.EAST);
    
    midPanel.setLayout(new GridLayout(2,1));
    
    midPanel.add(directionsPanel);
    midPanel.add(menuPanel);
    
    menuPanel.add(start);
    menuPanel.add(quit);
    
    addActionListeners();
    
    setTitle("Pizza Party!");  
    setSize(new Dimension(750,500));
    pack();
    setVisible(true);
  }


  private void addActionListeners()
  {
    start.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e)
      {
        userNotReady = false;
        dispose();
      }
    });      


    quit.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e)
      {
        System.exit(0);
      }
    });      
  }
}
