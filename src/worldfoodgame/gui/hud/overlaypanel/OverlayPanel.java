package worldfoodgame.gui.hud.overlaypanel;

import worldfoodgame.gui.ColorsAndFonts;
import worldfoodgame.gui.WorldPresenter;
import worldfoodgame.gui.regionlooks.RegionViewFactory;

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

  private RegionViewFactory.Overlay[] overlays = {
    RegionViewFactory.Overlay.HAPPINESS,
    RegionViewFactory.Overlay.NONE
  };

  public OverlayPanel(WorldPresenter worldPresenter)
  {
    this.worldPresenter = worldPresenter;

    // styling
    setBackground(ColorsAndFonts.GUI_BACKGROUND);
    setForeground(ColorsAndFonts.GUI_TEXT_COLOR);


    add(getRadioPanel());

  }

  private JPanel getRadioPanel()
  {
    JPanel radioPanel = new JPanel();
    radioPanel.setBackground(ColorsAndFonts.GUI_BACKGROUND);
    radioPanel.setLayout(new GridLayout(0, 1));
    ButtonGroup group = new ButtonGroup();

    for (int i = 0; i < overlays.length; i++)
    {
      RegionViewFactory.Overlay overlay = overlays[i];
      JRadioButton button = new JRadioButton(overlay.toString());

      button.setIcon(new CustomIcon(button));

      button.setForeground(ColorsAndFonts.GUI_TEXT_COLOR);
      button.addActionListener(this);

      radioPanel.add(button);
      group.add(button);

      if (i == 0) button.setSelected(true);
    }

    return radioPanel;
  }


  private static void createAndShowGUI()
  {
    //Create and set up the window.
    JFrame frame = new JFrame("RadioButtonDemo");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    //Create and set up the content pane.
    JComponent newContentPane = new OverlayPanel(null);
    newContentPane.setOpaque(true); //content panes must be opaque
    frame.setContentPane(newContentPane);

    //Display the window.
    frame.pack();
    frame.setVisible(true);
  }


  public static void main(String[] args)
  {
    javax.swing.SwingUtilities.invokeLater(new Runnable()
    {
      public void run()
      {
        createAndShowGUI();
      }
    });
  }

  @Override
  public void actionPerformed(ActionEvent e)
  {
    System.out.println(e.getActionCommand());

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
