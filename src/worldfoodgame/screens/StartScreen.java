package worldfoodgame.screens;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;

import com.sun.glass.events.WindowEvent;

public class StartScreen extends JFrame
{
  public boolean userNotReady = true;
  public int response;

  JButton start = new JButton("START");
  JButton  quit = new JButton("QUIT");

  JPanel directionsPanel = new JPanel();
  JPanel imagePanel = new JPanel();
  JPanel menuPanel = new JPanel();
  JPanel midPanel = new JPanel();
  JPanel holdingPanel = new JPanel();
  
  JLabel firstLine = new JLabel();
  JLabel secondLine = new JLabel();
  JLabel thirdLine = new JLabel();
  JLabel fourthLine = new JLabel();
  JLabel fifthLine = new JLabel();

  public StartScreen()
  {    
    init();
  }

//    @Override
//    protected void paintComponent(Graphics g) {
//        super.paintComponent(g); // paint the background image and scale it to fill the entire space
//        g.drawImage(/*....*/);
//    }
    
  private void init()
  {
    setStory();
    
    setMinimumSize(new Dimension(750,500));
    setMaximumSize(new Dimension(750,500));
    
    setLayout(new BorderLayout());
    add(imagePanel, BorderLayout.WEST);
    add(midPanel, BorderLayout.CENTER);
    add(imagePanel, BorderLayout.EAST);

    midPanel.setLayout(new GridLayout(3,1));
    
    midPanel.add(holdingPanel);
    midPanel.add(directionsPanel);
    midPanel.add(menuPanel);
    
    
    holdingPanel.setMinimumSize(new Dimension(300,100));
       
    directionsPanel.add(firstLine);
    directionsPanel.add(secondLine);
    directionsPanel.add(thirdLine);
    directionsPanel.add(fourthLine);
    directionsPanel.add(fifthLine);
    
    menuPanel.add(start);
    menuPanel.add(quit);

    addActionListeners();
   
    setTitle("Pizza Party President!");  

    setResizable(false);
    setVisible(true);
  }


  private void addActionListeners()
  {
    start.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e)
      {
        String[] options = new String[] {"North America", "South America", "Europe", "Africa", "Oceania", "Asia", "Middle East"};

        response = JOptionPane.showOptionDialog(null, " Congratulations! You've been elected Pizza Party President for your region.\n What's your job? Throw a pizza party for every citizen, every day! This is no\n easy task, but you’ve promised your citizens that you will be the best \n Pizza Party President ever. In fact, not only will you feed your people, but the \n entire world will be having pizza parties by the end of your term (year 2050).\n All this while preserving our Earth.\n No hunger? A green scene? Sounds like a reason to party! Are you up for the challenge?\n\n Choose the region you'd like to serve.", "Choose Your Player",
            JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,
            new ImageIcon("resources/imgs/balloons.png"), options, options[0]);

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
  
  
  private void setStory()
  {
    firstLine.setText("Congratulations! You've been elected Pizza Party President for your region.");
    firstLine.setBounds(300, 100, 100, 100);
    secondLine.setText("Your job: throw a pizza party for everyone in the region, every day.This is ");
    secondLine.setBounds(300, 150, 100, 100);
    thirdLine.setText("no easy task, but you’ve promised your citizens that you will be the best ");
    thirdLine.setBounds(300, 200, 100, 100);
    fourthLine.setText(" Pizza Party President ever. You will feed the entire world by the end of your");
    fourthLine.setBounds(300, 250, 100, 100);
    fifthLine.setText("term (year 2050) while preserving our Earth. Are you up for the challenge?");
    fifthLine.setBounds(300, 300, 100, 100);
  }
}
