package worldfoodgame.planningpoints;

import java.awt.Color;
import java.awt.GridLayout;
import java.util.Collection;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import worldfoodgame.gui.ColorsAndFonts;

public class PlanningPointContinentSelector extends JPanel implements DynamicTextInteractable
{
  int currentCountryIndex=0;
  InteractableLable next;
  InteractableLable previous;
  JLabel currentCountry;
  List <PlanningPointsInteractableRegion> regions;
  PlanningPointContinentSelector(List <PlanningPointsInteractableRegion> regions)
  {
    this.regions=regions;
    
    this.setLayout(new GridLayout(1, 3));
    this.setBackground(ColorsAndFonts.GUI_BACKGROUND);
    
    previous=new InteractableLable("Previous",this,false);
    previous.setHorizontalAlignment(SwingConstants.RIGHT);
    this.add(previous);
    
    currentCountry=new JLabel(regions.get(currentCountryIndex).getContName());
    currentCountry.setHorizontalAlignment(SwingConstants.CENTER);
    currentCountry.setForeground(Color.WHITE);
    this.add(currentCountry);
    
    next=new InteractableLable("Next",this,true);
    next.setHorizontalAlignment(SwingConstants.LEFT);
    this.add(next);
  }
  

  public void interact(boolean increment)
  {
    currentCountryIndex+= (increment) ? 1 : -1;
    if(currentCountryIndex<0) currentCountryIndex=regions.size()-1;
    else if (!(currentCountryIndex<regions.size())) currentCountryIndex=0;
    updateLabel();
  } 
  
  private void updateLabel()
  {
    currentCountry.setText(regions.get(currentCountryIndex).getContName());
  }
  
}
