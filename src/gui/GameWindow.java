package gui;

import javax.swing.*;
import java.awt.*;

/**
 @author david
 created: 2015-02-08

 description: */
public class GameWindow extends JFrame
{

  public static final double ASPECT = 1.6;

  public GameWindow()
  {
    setSize();
    setResizable(false);
    setDefaultCloseOperation(EXIT_ON_CLOSE);
    


  }

  /* sets the size of the game window based on the screen it's displayed in */
  private void setSize()
  {
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    double scrAspect = screenSize.width / screenSize.height;
    int w, h;

    if (scrAspect > ASPECT)
    {
      h = screenSize.height;
      w = (int) (h * ASPECT);
    }
    else
    {
      w = screenSize.width;
      h = (int) (w / ASPECT);
    }
    setSize(w, h);
  }
}
