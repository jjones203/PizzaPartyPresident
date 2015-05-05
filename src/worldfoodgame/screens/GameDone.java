package worldfoodgame.screens;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by Ken Kressin on 4/5/15.
 * Description: A basic end game screen, which will show how the player did.
 */
public class GameDone extends JFrame
{

  BufferedImage backgroundImage;

  JButton OK            = new JButton("OK");
  JLabel finished = new JLabel("Game Over");
  JLabel  population    = new JLabel();
  JLabel  hunger        = new JLabel();
  JLabel  green         = new JLabel();
  JLabel  ratingLabel   = new JLabel();
  JLabel  messageLabel1 = new JLabel();
  JLabel  messageLabel2 = new JLabel();


  private String popText = "Final World Population: ";
  private String hungerText = "Hungry people today: ";
  private String greenText = "Your final 'green' rating: ";

  private String[] rating = {
                              "Outstanding!",
                              "Good Job",
                              "OK...",
                              "Nice try, but...",
                              "Hmmm... Not so good",
                              "Oops!  Not good."};

  private String[] endMessage = {"You fed everyone!  The world is a better place!",
                                 "Most of the world is fed and happy, and a nice"
                                 + " place to live.",
                                 "You managed to keep the world satified, and fed."
                                 + "  The world survived.",
                                 "there is still work to do...",
                                 "Unfortunately, the world didn't survive."};
  private String   nextTime   = "Better luck next time...";

  public GameDone()
  {
    setup();
  }

  private void setup()
  {

  }
  public void paintComponent(Graphics g)
  {

  }

  private class Background extends JPanel
  {

    Background()
    {
      try
      {
        backgroundImage = ImageIO.read(new File("resources/imgs/start_background.png"));
      }
      catch (IOException e)
      {
        System.err.println("Error: Couldn't load background image start_background.png");
      }
    }

    public void paintComponent(Graphics g)
    {
      super.paintComponent(g);
      g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), null);
    }

  }

}
