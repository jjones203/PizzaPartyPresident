package worldfoodgame.gui.hud.infopanel;

import worldfoodgame.gui.ColorsAndFonts;
import worldfoodgame.gui.WorldPresenter;
import worldfoodgame.gui.regionlooks.RegionViewFactory.Overlay;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by winston on 3/29/15.
 * <p/>
 * presents the user with the controlls to select different overlays for visualizing
 * the state of the world.
 */
public class OverlayPanel extends JPanel implements ActionListener
{
  private WorldPresenter worldPresenter;

  private Overlay[] overlays = Overlay.values();


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

  private JPanel getConverterPanel()
  {
    JPanel radioPanel = new JPanel();

    radioPanel.setBackground(ColorsAndFonts.GUI_BACKGROUND);
    radioPanel.setLayout(new GridLayout(0, 1));
    ButtonGroup group = new ButtonGroup();

    for (final CountryDataHandler.DISPLAY_UNITE unite : CountryDataHandler.DISPLAY_UNITE.values())
    {
      JRadioButton radioButton = new JRadioButton();
      radioButton.setBackground(ColorsAndFonts.GUI_BACKGROUND);
      radioButton.setForeground(ColorsAndFonts.GUI_TEXT_COLOR);
      radioButton.setIcon(new CustomIcon(radioButton));
      group.add(radioButton); // adds to logical group
      radioPanel.add(radioButton); // adds to panel
      radioButton.setAction(new changeUniteConverter(unite));

      if (unite == CountryDataHandler.DISPLAY_UNITE.METRIC)
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
    private CountryDataHandler.DISPLAY_UNITE unite;
    public changeUniteConverter(CountryDataHandler.DISPLAY_UNITE unite)
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


  /* sets up and returns a get Overlay */
  private JPanel getOverlayRadio()
  {
    JPanel radioPanel = new JPanel();
    radioPanel.setBackground(ColorsAndFonts.GUI_BACKGROUND);
    radioPanel.setLayout(new GridLayout(0, 2));
    ButtonGroup group = new ButtonGroup();

    for (int i = 0; i < overlays.length; i++)
    {
      Overlay overlay = overlays[i];
      JRadioButton button = new JRadioButton(overlay.toString());
      button.setActionCommand(Integer.toString(overlay.ordinal()));
      button.setIcon(new CustomIcon(button));
      button.setBackground(ColorsAndFonts.GUI_BACKGROUND);

      button.setForeground(ColorsAndFonts.GUI_TEXT_COLOR);
      button.addActionListener(this);

      radioPanel.add(button);
      group.add(button);

      if (i == 0) button.setSelected(true);
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
