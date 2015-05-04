package worldfoodgame.gui.TradeWindow;

import worldfoodgame.gui.ColorsAndFonts;
import worldfoodgame.gui.hud.infopanel.CountryDataHandler;
import worldfoodgame.model.Country;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Collection;
import java.util.HashMap;
import java.util.ArrayList;

/**
 * Created by Tim on 4/24/15.
 */
public class ContinentTabPanel extends JPanel
{
  public static final Color ROLLOVER_C = Color.WHITE;
  public static final Color SELECTED_C = Color.RED.darker();
  public static final Color TEXT_DEFAULT_COLOR = ColorsAndFonts.GUI_TEXT_COLOR;
  public static final Color BACKGROUND_COLOR = ColorsAndFonts.GUI_BACKGROUND;
  public static final Font TAB_FONT = ColorsAndFonts.GUI_FONT;
  private static final float ALPHA_STEP = 0.1f;

  public float fontSize;
  public int vPadding;  // padding must be set before adding tabs.
  public int hPadding;  // padding must be set before adding tabs.
  private JPanel tabpabel;
  private JPanel contentArea;
  private HashMap<Tab, ContinentPanel> tabmap;
  private float alpha;
  private Tab currentTab;

  public ContinentTabPanel(Collection<Country> countries, ArrayList<CountryDataHandler> handlers, Dimension dimension)
  {
    //init
    alpha = 0;
    tabmap = new HashMap<>();
    tabpabel = new JPanel();
    contentArea = getContentPanel();

    //config
    fontSize = 14;        // default values
    vPadding = 0;
    hPadding = 7;

    setPreferredSize(dimension);
    setLayout(new BorderLayout());
    setBackground(ColorsAndFonts.GUI_BACKGROUND);

    tabpabel.setLayout(new FlowLayout(FlowLayout.CENTER));
    tabpabel.setBackground(ColorsAndFonts.GUI_BACKGROUND);
    tabpabel.setBorder(ColorsAndFonts.HEADING_UNDERLINE);

    contentArea.setLayout(new BorderLayout());
    contentArea.setBackground(ColorsAndFonts.GUI_BACKGROUND);

    //wireup
    add(tabpabel, BorderLayout.NORTH);
    add(contentArea, BorderLayout.CENTER);
  }


  /* Extending JPanel to animation */
  private JPanel getContentPanel()
  {
    return new JPanel()
    {
      @Override
      public void paint(Graphics g)
      {
        if (alpha < 1)
        {
          Graphics2D g2d = (Graphics2D) g;
          //g2d.setComposite(
           //       AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
          alpha += ALPHA_STEP;
          g2d.setColor(Color.CYAN);
          g2d.fillRect(0,0,50,50);
          super.paint(g2d);
        }
        else
        {
          super.paint(g);
        }
      }
    };
  }

  /**
   * Adds a tab and a corresponding JComponent (generally a Jpanel) to the tab
   * panel.
   * @param label string to be displayed in the Tab Panel
   * @param contPanel component that is displayed when tab is selected.
   */
  public void addTab(String label, ContinentPanel contPanel)
  {
    Tab tab = new Tab(label);
    tabmap.put(tab, contPanel);
    tabpabel.add(tab);
  }

  @Override
  protected void paintComponent(Graphics g)
  {
    super.paintComponent(g);
  }

  /* encapsulates the logic of a 'tab' for the tab panel*/
  private class Tab extends JLabel
  {
    public Tab(String text)
    {
      super(text);
      //this.setForeground(TEXT_DEFAULT_COLOR);
      this.setForeground(ColorsAndFonts.OCEANS);
      this.setBackground(BACKGROUND_COLOR);
      this.setFont(TAB_FONT.deriveFont(fontSize));
      this.setBorder(new EmptyBorder(vPadding, hPadding, vPadding, hPadding));

      this.addMouseListener(new MouseAdapter()
      {
        @Override
        public void mouseClicked(MouseEvent e)
        {
          super.mouseClicked(e);
          if (Tab.this == currentTab) return;

          Tab.this.doLayout();

          for (Tab t : tabmap.keySet()) t.deselect();

          alpha = 0;
          currentTab = Tab.this;

          //setForeground(SELECTED_C);
          setForeground(ColorsAndFonts.GUI_TEXT_COLOR);

          contentArea.removeAll();
          contentArea.add(tabmap.get(Tab.this));

          getRootPane().revalidate();
          getRootPane().repaint();
          tabmap.get(Tab.this).redraw();
        }

        @Override
        public void mouseEntered(MouseEvent e)
        {
          if (Tab.this != currentTab)
          {
            super.mouseEntered(e);
            Tab.this.setForeground(ColorsAndFonts.OCEANS.brighter());
          }
        }

        @Override
        public void mouseExited(MouseEvent e)
        {
          if (Tab.this != currentTab)
          {
            super.mouseEntered(e);
            Tab.this.setForeground(ColorsAndFonts.OCEANS);
          }
        }
      });
    }

    /* changes there graphical representation */
    private void deselect()
    {
      Tab.this.setForeground(ColorsAndFonts.OCEANS);
    }
  }
}
