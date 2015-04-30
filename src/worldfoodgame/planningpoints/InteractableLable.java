package worldfoodgame.planningpoints;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JLabel;
import javax.swing.Timer;

/**
 * 
 * @author Stephen Stromberg on 4/29/15
 * 
 * A JLabel that changes color on a mouse
 * over, and performs a DynamicTextInteractable
 * while being held down or clicked.
 *
 */
public class InteractableLable extends JLabel 
                              implements MouseListener, ActionListener
{
  //interactable
  private DynamicTextInteractable interactable;
  private boolean isPositiveInteraction;
  
  //Swing timer
  private Timer timer;
  
  //text color
  private Color neutralColor;
  private Color activeColor;

  /**
   * 
   * @param text of label
   * @param interactable to perform
   * @param isPositiveInteraction decides to increment or decrement
   * @param mouseDelay determines the threads refresh rate scanning
   * for mouse input
   * @param neutralColor of text
   * @param activeColor of text
   */
  InteractableLable(String text, DynamicTextInteractable interactable,
      boolean isPositiveInteraction, int mouseDelay, 
      Color neutralColor, Color activeColor)
  {
    super(text);
    this.setForeground(neutralColor);
    this.setText(text);
    this.interactable=interactable;
    this.isPositiveInteraction=isPositiveInteraction;
    addMouseListener(this);
    this.neutralColor=neutralColor;
    this.activeColor=activeColor;
    timer = new Timer(mouseDelay,this);
  }

  @Override
  /**
   * timer event is not instantaneous
   */
  public void mouseClicked(MouseEvent arg0)
  {
    interactable.interact(isPositiveInteraction);
  }

  @Override
  /**
   * change color
   */
  public void mouseEntered(MouseEvent arg0)
  {
    this.setForeground(activeColor);
  }

  @Override
  /**
   * change color
   */
  public void mouseExited(MouseEvent arg0)
  {
    this.setForeground(neutralColor);
  }

  @Override
  public void mousePressed(MouseEvent arg0)
  {
    timer.start();
  }

  @Override
  public void mouseReleased(MouseEvent arg0)
  {
    timer.stop();
  }

  @Override
  public void actionPerformed(ActionEvent arg0)
  {
    if(arg0.getSource()==timer) interactable.interact(isPositiveInteraction);
  }
  
}




