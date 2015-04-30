package worldfoodgame.planningpoints;

import java.awt.Color;
import java.awt.GridLayout;
import java.util.Collection;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import worldfoodgame.gui.ColorsAndFonts;

/**
 * 
 * @author Stephen Stromberg on 4/29/15
 * 
 * This panel is the control that allows the user
 * to choose which continent to be investing into.
 *
 */
public class PlanningPointContinentSelector extends JPanel 
                                          implements DynamicTextInteractable
{
  int currentCountryIndex=0; 
  
  InteractableLable next;
  InteractableLable previous;
  
  JLabel currentCountry;
  List <PlanningPointsInteractableRegion> regions;
  PlanningPointContinentSelector(
      List <PlanningPointsInteractableRegion> regions)
  {
    this.regions=regions;
    
    this.setLayout(new GridLayout(1, 3));
    this.setBackground(Color.LIGHT_GRAY);
    
    previous=new InteractableLable(
        "Previous",this,false,200,Color.WHITE,Color.BLUE);
    previous.setHorizontalAlignment(SwingConstants.RIGHT);
    this.add(previous);
    
    currentCountry=new JLabel(
        regions.get(currentCountryIndex).getContName());
    currentCountry.setHorizontalAlignment(SwingConstants.CENTER);
    currentCountry.setForeground(new Color(103,171,245,220));
    currentCountry.setFont (currentCountry.getFont ().deriveFont (24.0f));
    this.add(currentCountry);
    
    next=new InteractableLable("Next",this,true,200,Color.WHITE,Color.BLUE);
    next.setHorizontalAlignment(SwingConstants.LEFT);
    this.add(next);
  }
  

  /**
   * Defined in implemented interface. This
   * method iterates through the countries
   * list.
   * 
   */
  public void interact(boolean increment)
  {
    currentCountryIndex+= (increment) ? 1 : -1;
    if(currentCountryIndex<0) currentCountryIndex=regions.size()-1;
    else if (!(currentCountryIndex<regions.size())) currentCountryIndex=0;
    updateCurrentCountry();
  } 
  
  /**
   * Calls static method in PlanningPointsData
   * that all GUI components use for getting
   * the active region
   */
  private void updateCurrentCountry()
  {
    PlanningPointsData.setActiveRegion(regions.get(currentCountryIndex));
    currentCountry.setText(PlanningPointsData.getActiveRegion().getContName());
  }
  
}
