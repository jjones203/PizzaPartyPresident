package worldfoodgame.planningpoints;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JLabel;

import worldfoodgame.gui.ColorsAndFonts;

public class InteractableLable extends JLabel implements MouseListener
{
  private DynamicTextInteractable interactable;
  private boolean isPositiveInteraction;
  
  InteractableLable(String text, DynamicTextInteractable interactable, boolean isPositiveInteraction)
  {
    super(text);
    this.setForeground(Color.WHITE);
    this.setText(text);
    this.interactable=interactable;
    this.isPositiveInteraction=isPositiveInteraction;
    addMouseListener(this);
  }

  @Override
  public void mouseClicked(MouseEvent arg0)
  {
    interactable.interact(isPositiveInteraction);
    // TODO Auto-generated method stub
    
  }

  @Override
  public void mouseEntered(MouseEvent arg0)
  {
    this.setForeground(Color.RED);
    // TODO Auto-generated method stub
    
  }

  @Override
  public void mouseExited(MouseEvent arg0)
  {
    this.setForeground(Color.WHITE);
    // TODO Auto-generated method stub
    
  }

  @Override
  public void mousePressed(MouseEvent arg0)
  {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void mouseReleased(MouseEvent arg0)
  {
    // TODO Auto-generated method stub
    
  }

}
