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
 *
 * This is not fully implemented, but should key off the game year or player end
 * conditions.  When either the year 2050 is hit, or when the player loses
 * (which is a future implementation), this set of screens should display.
 *
 * GameDone is the overall JFrame.  Background is the bufferedImage for the
 * background pane.  InfoPanel is the JPanel holding most of the ending game
 * information.
 *
 */
public class GameDone extends JFrame
{

  BufferedImage backgroundImage;

  JPanel  pizzaPanel    = new Background();


  private String popText    = "Final World Population: ";
  private String hungerText = "Hungry people today: ";
  private String greenText  = "Your final 'green' rating: ";

  private String[] rating = {
                              "Outstanding!",
                              "Good Job",
                              "OK...",
                              "Nice try, but...",
                              "Hmmm... Not so good",
                              "Oops!  Not good."
  };

  private String[] endMessage = {
                                  "You fed everyone!  The world is a better place!",
                                  "Most of the world is fed and happy, and a nice"
                                  + " place to live.",
                                  "You managed to keep the world satified, and fed."
                                  + "  The world survived.",
                                  "there is still work to do...",
                                  "Unfortunately, the world didn't survive."
  };
  private String   nextTime   = "Better luck next time...";

  public GameDone()
  {
    setup();
  }

  private void setup()
  {
    try
    {
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    }
    catch (ClassNotFoundException | InstantiationException |
             UnsupportedLookAndFeelException | IllegalAccessException e)
    {
      e.printStackTrace();
    }

    pizzaPanel.setOpaque(true);

    setMinimumSize(new Dimension(750, 500));
    setMaximumSize(new Dimension(750, 500));

    BorderLayout layout = new BorderLayout();
    //setLayout(new BorderLayout());
    this.add(pizzaPanel, layout.CENTER);



  }

  private class Background extends JPanel
  {
    JPanel gameInfo = new InfoPanel();

    JButton ok        = new JButton("OK");
    JButton playAgain = new JButton("Play Again");
    JLabel  finished  = new JLabel("Game Over");


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

      BorderLayout layout = new BorderLayout();
      add(finished, layout.PAGE_START);
      add(gameInfo, layout.CENTER);
      add(playAgain, layout.PAGE_END);
      add(ok, layout.PAGE_END);


    }


    public void paintComponent(Graphics g)
    {
      super.paintComponent(g);
      g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), null);
    }

  }

  private class InfoPanel extends JPanel
  {
    JLabel endYear       = new JLabel();
    JLabel blank         = new JLabel();
    JLabel population    = new JLabel();
    JLabel hunger        = new JLabel();
    JLabel green         = new JLabel();
    JLabel ratingLabel   = new JLabel();
    JLabel messageLabel1 = new JLabel();
    JLabel messageLabel2 = new JLabel();


    InfoPanel()
    {
      GridLayout infoLayout = new GridLayout(8, 1);
      add(endYear);
      add(blank);
      add(population);
      add(hunger);
      add(green);
      add(ratingLabel);
      add(messageLabel1);
      add(messageLabel2);
    }
  }

}
