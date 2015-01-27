package gui;

import javax.swing.*;
import java.awt.*;

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

  public GameCanvas()
  {
  }

  public GameCanvas(Camera cam)
  {
    this.cam = cam;
    setSize(1000,800);
  }

}
