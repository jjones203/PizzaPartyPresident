package worldfoodgame.planningpoints;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JLabel;

import worldfoodgame.gui.ColorsAndFonts;

public class InteractableLable extends JLabel implements MouseListener
{
  private DynamicTextInteractable interactable;
  private boolean isPositiveInteraction;
  private boolean mousePressed=false;
  private long dt;  //delta time
  private long lastMark = System.currentTimeMillis();
  private int mouseDelay;
  private Color neutralColor;
  private Color activeColor;

  
  InteractableLable(String text, DynamicTextInteractable interactable, boolean isPositiveInteraction, int mouseDelay, Color neutralColor, Color activeColor)
  {
    super(text);
    this.setForeground(neutralColor);
    this.setText(text);
    this.interactable=interactable;
    this.isPositiveInteraction=isPositiveInteraction;
    addMouseListener(this);
    this.mouseDelay=mouseDelay;
    new MouseHeldDownTask().start();
    this.neutralColor=neutralColor;
    this.activeColor=activeColor;
  }

  @Override
  public void mouseClicked(MouseEvent arg0)
  {
    //interactable.interact(isPositiveInteraction);
    // TODO Auto-generated method stub
    
  }

  @Override
  public void mouseEntered(MouseEvent arg0)
  {
    this.setForeground(activeColor);
    // TODO Auto-generated method stub
    
  }

  @Override
  public void mouseExited(MouseEvent arg0)
  {
    this.setForeground(neutralColor);
    // TODO Auto-generated method stub
    
  }

  @Override
  public void mousePressed(MouseEvent arg0)
  {
    mousePressed=true;
    
  }

  @Override
  public void mouseReleased(MouseEvent arg0)
  {
    mousePressed=false;
    // TODO Auto-generated method stub
    
  }
  
  private class MouseHeldDownTask extends Thread
  {

    @Override
    public void run()
    {
      while(true)
      {
        dt=System.currentTimeMillis()-lastMark;
        if(dt>mouseDelay && mousePressed)
        {
          interactable.interact(isPositiveInteraction);
          lastMark=System.currentTimeMillis();
        }
        try
        {
          Thread.sleep(10);
        } catch (InterruptedException e)
        {
          e.printStackTrace();
        }
      }
    }
    
  }
}




