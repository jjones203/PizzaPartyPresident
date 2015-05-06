package worldfoodgame.screens;

import java.awt.BorderLayout;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.plaf.ColorUIResource;


/***************
 * Opening screen where player 
 * chooses continent and games begins
 * 
 * @author Valarie
 ******/
public class StartScreen extends JFrame
{
  private Game gameManager;
  
  private JButton start = new JButton();
  private JButton  quit = new JButton();

  private BufferedImage backgroundImage;

  private JPanel messagePanel = new JPanel(); // for holding start message
  private JPanel imagePanel = new backgroundPanel(); // for holding background image
  private JPanel menuPanel = new JPanel(); // for holding buttons
  private JPanel midPanel = new JPanel(); // for holding menu and holding panel
  private JPanel holdingPanel = new JPanel(); // placeholder panel, takes up space to allow menu panel position

  // For adding start message
  //  JLabel firstLine = new JLabel(); 
  //  JLabel secondLine = new JLabel();
  //  JLabel thirdLine = new JLabel();
  //  JLabel fourthLine = new JLabel();
  //  JLabel fifthLine = new JLabel();

  public StartScreen()
  {    
    init();
  }

  private void init()
  {
    // Set-up option for 'Choose Player' option pane
    UIManager.put("Panel.background", Color.WHITE);
    UIManager.put("OptionPane.background",new ColorUIResource(100,155,61));
    setMessage();

    // Sets up panel so background is visible
    imagePanel.setOpaque(true);
    midPanel.setOpaque(false);
    messagePanel.setOpaque(false);
    menuPanel.setOpaque(false);
    holdingPanel.setOpaque(false);

    // Sets window size
    setMinimumSize(new Dimension(750,500));
    setMaximumSize(new Dimension(750,500));

    setLayout(new BorderLayout()); 

    imagePanel.add(midPanel, BorderLayout.CENTER);

    midPanel.setLayout(new GridLayout(3,1));

    midPanel.add(holdingPanel);
    midPanel.add(messagePanel);
    midPanel.add(menuPanel);

    // Place-holding panel, allows menu panel to be placed slightly high center
    holdingPanel.setMinimumSize(new Dimension(300,100));

    // No longer implemented but starting message if needed would be added here
    //    directionsPanel.add(firstLine);
    //    directionsPanel.add(secondLine);
    //    directionsPanel.add(thirdLine);
    //    directionsPanel.add(fourthLine);
    //    directionsPanel.add(fifthLine);

    // Set-up start button
    start.setBorder(null);
    start.setContentAreaFilled(false);
    start.setBorderPainted(false);  
    start.setMargin(new Insets(0, 0, 0, 0));
    start.setIcon(new ImageIcon("resources/imgs/startButton.png"));

    // Set-up quit button
    quit.setBorder(null);
    quit.setContentAreaFilled(false);    
    quit.setBorderPainted(false);  
    quit.setMargin(new Insets(0, 0, 0, 0));  
    quit.setIcon(new ImageIcon("resources/imgs/quitButton.png"));

    menuPanel.add(start);
    menuPanel.add(quit);

    add(imagePanel);
    addActionListeners();

    setTitle("Pizza Party President!");  

    setLocationRelativeTo(null); // centering screen
    setResizable(false);
    setVisible(true);
  }


  // Panel for background image
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


  // Adds action listeners to menu buttons, game started here
  private void addActionListeners()
  {
    start.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e)
      {
        setVisible(false);

        String[] options = new String[] {"North America", "South America", "Europe", "Africa", "Oceania", "Asia", "Middle East"};

       int response = JOptionPane.showOptionDialog(null, " Congratulations! You've been elected Pizza Party President for your region.\n"+
            " What's your job? Throw a pizza party for every citizen, every day! This is no\n"+
            "easy task, but you’ve promised your citizens that you will be the best \n"+
            "Pizza Party President ever. In fact, not only will you feed your people, but the \n"+
            " entire world will be having pizza parties by the end of your term (year 2050).\n"+
            " \n All this while preserving our Earth.\n \n No hunger? A green scene?"+
            " Sounds like a reason to party!\n Are you up for the challenge?\n \n Choose the region you'd like to serve.",
            "Choose Your Player",
            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE,
            new ImageIcon("resources/imgs/balloons.png"), options, options[0]);

        gameManager = new Game(response);       // Begin game  
        gameManager.show();
        gameManager.start();

        dispose(); // Close start menu
      }
    });      


    quit.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e)
      {
        System.exit(0);
      }
    });      
  }

  
  //Set-up start message
  private void setMessage()
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
