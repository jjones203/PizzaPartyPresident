package worldfoodgame.gui.hud.infopanel;

import worldfoodgame.gui.ColorsAndFonts;
import worldfoodgame.gui.WorldPresenter;
import worldfoodgame.gui.regionlooks.RegionViewFactory;
import worldfoodgame.gui.regionlooks.RegionViewFactory.Overlay;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by winston on 3/29/15.
 * <p/>
 * presents the user with the controls to select different overlays for visualizing
 * the state of the world.
 *
 * this panel also contains a selector for displaying different unite converters.
 */
public class OverlayPanel extends JPanel implements ActionListener
{
  private WorldPresenter worldPresenter;

  private Overlay[] overlays = Overlay.values();

  /**
   * Constructor for the OverlayPanel. This Panel contains a set of radio
   * buttons, selecting different overlays and visualizations.
   * @param worldPresenter
   */
  public OverlayPanel(WorldPresenter worldPresenter)
  {
    this.worldPresenter = worldPresenter;
    this.setLayout(new FlowLayout(FlowLayout.LEFT));
    // styling
    setBackground(ColorsAndFonts.GUI_BACKGROUND);
    setForeground(ColorsAndFonts.GUI_TEXT_COLOR);


    add(getOverlayRadio());
    add(getConverterPanel());

  }

  /* returns the panel containing the Unite converters*/
  private JPanel getConverterPanel()
  {
    JPanel radioPanel = new JPanel();

    radioPanel.setBackground(ColorsAndFonts.GUI_BACKGROUND);
    radioPanel.setLayout(new GridLayout(0, 1));
    ButtonGroup group = new ButtonGroup();

    for (final CountryDataHandler.DISPLAY_UNIT unite : CountryDataHandler.DISPLAY_UNIT.values())
    {
      JRadioButton radioButton = new JRadioButton();
      radioButton.setBackground(ColorsAndFonts.GUI_BACKGROUND);
      radioButton.setForeground(ColorsAndFonts.GUI_TEXT_COLOR);
      radioButton.setIcon(new CustomIcon(radioButton));
      group.add(radioButton); // adds to logical group
      radioPanel.add(radioButton); // adds to panel
      radioButton.setAction(new changeUniteConverter(unite));

      if (unite == CountryDataHandler.DISPLAY_UNIT.METRIC)
      {
        radioButton.setSelected(true); // this is the default starting value.
      }
    }
    return radioPanel;
  }

  /**
   * private class to wrap up the logic of switching the display conversion in
   * the gui.
   */
  private class changeUniteConverter extends AbstractAction
  {
    private CountryDataHandler.DISPLAY_UNIT unite;
    public changeUniteConverter(CountryDataHandler.DISPLAY_UNIT unite)
    {
      super(unite.name());
      this.unite = unite;
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
      CountryDataHandler.activeConverter = this.unite;
      worldPresenter.registerChange();
      worldPresenter.notifyObservers();
    }
  }


  /* sets up container for all the different overlays*/
  private JPanel getOverlayRadio()
  {
    JPanel radioPanel = new JPanel();
    
    int rows = 10;
    int cols = 4;

    Overlay[][] all = new Overlay[][]{
      Overlay.getRasterOverlays(), 
      Overlay.getDemographicOverlays(), 
      Overlay.getCropDedicationOverlays()}; 
    
    radioPanel.setBackground(ColorsAndFonts.GUI_BACKGROUND);
    radioPanel.setLayout(new GridLayout(0, cols));
    ButtonGroup group = new ButtonGroup();

    for (int i = 0; i < cols; i++)
    {
      JPanel subPanel = new JPanel();
      subPanel.setBackground(ColorsAndFonts.GUI_BACKGROUND);
      subPanel.setLayout(new GridLayout(rows, 1));
      Overlay overlay;
      
      for (int j = 0; j < rows; j++)
      {
        JPanel noButton = new JPanel();
        noButton.setBackground(ColorsAndFonts.GUI_BACKGROUND);
        if(i == 0)
        {
          if(j != 0) { subPanel.add(noButton); continue; }
          else overlay = Overlay.NONE;
        }
        else
        {
          if(j < all[i-1].length) overlay = all[i-1][j];
          else { subPanel.add(noButton); continue; }
        }
        
        JRadioButton button = new JRadioButton(overlay.toString().replace("_", " "));
        
        button.setActionCommand(Integer.toString(overlay.ordinal()));
        button.setIcon(new CustomIcon(button));
        
        button.setBackground(ColorsAndFonts.GUI_BACKGROUND);
        button.setForeground(ColorsAndFonts.GUI_TEXT_COLOR);

        button.addActionListener(this);

        subPanel.add(button);
        group.add(button);
        
        if(i == 0 && j == 0) button.setSelected(true);
      }
      radioPanel.add(subPanel);
    }
    return radioPanel;
  }
  

  @Override
  public void actionPerformed(ActionEvent e)
  {
    Overlay overlay = Overlay.values()[Integer.parseInt(e.getActionCommand())];

    if (worldPresenter != null)
    {
      worldPresenter.setCurrentOverlay(overlay);
    }
    else
    {
      System.err.println("world Presenter is null!");
    }
  }

  /* customizes the look of the radio button*/
  private class CustomIcon implements Icon
  {
    private JRadioButton button;
    private int circle_size = 10;
    public CustomIcon(JRadioButton button)
    {
      this.button = button;
    }

    @Override
    public void paintIcon(Component c, Graphics g, int x, int y)
    {
      if (button.isSelected())
      {
        g.setColor(Color.red);
      }
      else
      {
        g.setColor(Color.LIGHT_GRAY);
      }

      ((Graphics2D)g).setRenderingHint(
        RenderingHints.KEY_ANTIALIASING,
        RenderingHints.VALUE_ANTIALIAS_ON
      );

      g.fillOval(x, y, circle_size, circle_size);
    }

    @Override
    public int getIconWidth()
    {
      return circle_size;
    }

    @Override
    public int getIconHeight()
    {
      return circle_size;
    }
  }
}
