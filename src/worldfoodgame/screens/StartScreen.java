package worldfoodgame.screens;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.plaf.ColorUIResource;


public class StartScreen extends JFrame
{
  private Game gameManager;
  public boolean userNotReady = true;
  public int response;

  JButton start = new JButton();
  JButton  quit = new JButton();

  BufferedImage backgroundImage;

  JPanel directionsPanel = new JPanel();
  JPanel imagePanel = new backgroundPanel();
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
  
  private void init()
  {
    UIManager.put("Panel.background", Color.WHITE);
    UIManager.put("OptionPane.background",new ColorUIResource(100,155,61));
    setStory();

    imagePanel.setOpaque(true);
    midPanel.setOpaque(false);
    directionsPanel.setOpaque(false);
    menuPanel.setOpaque(false);
    holdingPanel.setOpaque(false);

    setMinimumSize(new Dimension(750,500));
    setMaximumSize(new Dimension(750,500));

    setLayout(new BorderLayout()); 
    
    imagePanel.add(midPanel, BorderLayout.CENTER);

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

    start.setBorder(null);
    start.setContentAreaFilled(false);
    
    quit.setBorder(null);
    quit.setContentAreaFilled(false);
    
    start.setIcon(new ImageIcon("resources/imgs/startButton.png"));
    quit.setIcon(new ImageIcon("resources/imgs/quitButton.png"));

    menuPanel.add(start);
    menuPanel.add(quit);

    add(imagePanel);
    addActionListeners();

    setTitle("Pizza Party President!");  

    setResizable(false);
    setVisible(true);
  }

  private class backgroundPanel extends JPanel{
    public backgroundPanel(){
      try {
        backgroundImage = ImageIO.read(new File("resources/imgs/start_background.png"));
      } catch (IOException ex) {
        System.err.println("Error loading:background image");
      }
    }

    public void paintComponent(Graphics g) {
      super.paintComponent(g);
      g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), null);
    }
  }


  private void addActionListeners()
  {
    start.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e)
      {
        setVisible(false);

        String[] options = new String[] {"North America", "South America", "Europe", "Africa", "Oceania", "Asia", "Middle East"};

        response = JOptionPane.showOptionDialog(null, " Congratulations! You've been elected Pizza Party President for your region.\n"+
            " What's your job? Throw a pizza party for every citizen, every day! This is no\n"+
            "easy task, but you’ve promised your citizens that you will be the best \n"+
            "Pizza Party President ever. In fact, not only will you feed your people, but the \n"+
            " entire world will be having pizza parties by the end of your term (year 2050).\n"+
            " \n All this while preserving our Earth.\n \n No hunger? A green scene?"+
            " Sounds like a reason to party!\n Are you up for the challenge?\n \n Choose the region you'd like to serve.",
            "Choose Your Player",
            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE,
            new ImageIcon("resources/imgs/balloons.png"), options, options[0]);

        gameManager = new Game(response);        
        gameManager.show();
        gameManager.start();
        
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
    //    firstLine.setText("Congratulations! You've been elected Pizza Party President for your region.");
    //    firstLine.setBounds(300, 100, 100, 100);
    //    secondLine.setText("Your job: throw a pizza party for everyone in the region, every day.This is ");
    //    secondLine.setBounds(300, 150, 100, 100);
    //    thirdLine.setText("no easy task, but you’ve promised your citizens that you will be the best ");
    //    thirdLine.setBounds(300, 200, 100, 100);
    //    fourthLine.setText(" Pizza Party President ever. You will feed the entire world by the end of your");
    //    fourthLine.setBounds(300, 250, 100, 100);
    //    fifthLine.setText("term (year 2050) while preserving our Earth. Are you up for the challenge?");
    //    fifthLine.setBounds(300, 300, 100, 100);
  }
  
  
  //*******
  // MAIN *
  //*******
  public static void main(String[] args)
  {
    StartScreen start = new StartScreen();
  }

}
