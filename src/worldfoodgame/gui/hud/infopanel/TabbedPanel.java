package worldfoodgame.gui.hud.infopanel;

import worldfoodgame.common.EnumCropType;
import worldfoodgame.gui.ColorsAndFonts;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;

/**
 * Created by winston on 3/10/15.
 */

/*
 todo clean up padding logic...?
 todo add consturtor support for animation handeling ?
 todo cleanup selection logic and handelers.
 todo add handler for border color and most importantly width.
 */
public class TabbedPanel extends JPanel
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
  private HashMap<Tab, Component> tabmap;
  private float alpha;
  private Tab currentTab;

  public TabbedPanel()
  {
    //init
    alpha = 0;
    tabmap = new HashMap<>();
    tabpabel = new JPanel();
    contentArea = getContentPanel();

    //config
    fontSize = 14;        // default values
    vPadding = 3;
    hPadding = 7;

    setLayout(new BorderLayout());
    setBackground(ColorsAndFonts.GUI_BACKGROUND);

    tabpabel.setLayout(new FlowLayout(FlowLayout.LEFT));
    tabpabel.setBackground(ColorsAndFonts.GUI_BACKGROUND);
    tabpabel.setBorder(ColorsAndFonts.HEADING_UNDERLINE);

    contentArea.setLayout(new BorderLayout());
    contentArea.setBackground(ColorsAndFonts.GUI_BACKGROUND);

    //wireup
    add(tabpabel, BorderLayout.NORTH);
    add(contentArea, BorderLayout.CENTER);
  }

  // for testin only
  public static void main(String[] args)
  {
    TabbedPanel tabbedPanel = new TabbedPanel();

    JPanel greenPanel = new JPanel();
    greenPanel.setBackground(Color.green);
    JPanel bluePanel = new JPanel();
    bluePanel.setBackground(Color.blue);

    CountryDataHandler dataHandler = CountryDataHandler.getTestData();

    LabelFactory labelFactory = new LabelFactory(dataHandler);

    tabbedPanel.addTab("demographic", new DemographicPanel(labelFactory));
    tabbedPanel.addTab("land", new LandPanel(labelFactory));



    TabbedPanel tabbedPanel2 = new TabbedPanel();
    tabbedPanel2.fontSize = 12;


    tabbedPanel2.addTab("corn",
      new CropPanel(labelFactory, EnumCropType.CORN));

    tabbedPanel2.addTab("wheat",
      new CropPanel(labelFactory, EnumCropType.WHEAT));

    tabbedPanel2.addTab("rice",
      new CropPanel(labelFactory, EnumCropType.RICE));

    tabbedPanel2.addTab("soy",
      new CropPanel(labelFactory, EnumCropType.SOY));

    tabbedPanel2.addTab("other",
      new CropPanel(labelFactory, EnumCropType.OTHER_CROPS));


    tabbedPanel.addTab("crops", tabbedPanel2);

    final JFrame frame = new JFrame();
    frame.add(tabbedPanel);
    frame.setSize(670, 250);
    frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    frame.setVisible(true);


    System.out.println("testing repainting at 10 ms");
    new Timer(10, new AbstractAction()
    {
      @Override
      public void actionPerformed(ActionEvent e)
      {
        frame.repaint();
      }
    }).start();
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
          g2d.setComposite(
            AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
          alpha += ALPHA_STEP;
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
   * @param component component that is displayed when tab is selected.
   */
  public void addTab(String label, Component component)
  {
    Tab tab = new Tab(label);
    tabmap.put(tab, component);
    tabpabel.add(tab);
  }

  @Override
  protected void paintComponent(Graphics g)
  {
    super.paintComponent(g);
  }

  private class Tab extends JLabel
  {
    public Tab(String text)
    {
      super(text);
      this.setForeground(TEXT_DEFAULT_COLOR);
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

          setForeground(SELECTED_C);

          contentArea.removeAll();
          contentArea.add(tabmap.get(Tab.this));

          getRootPane().revalidate();
          getRootPane().repaint();
        }

        @Override
        public void mouseEntered(MouseEvent e)
        {
          if (Tab.this != currentTab)
          {
            super.mouseEntered(e);
            Tab.this.setForeground(ROLLOVER_C);
          }
        }

        @Override
        public void mouseExited(MouseEvent e)
        {
          if (Tab.this != currentTab)
          {
            super.mouseEntered(e);
            Tab.this.setForeground(TEXT_DEFAULT_COLOR);
          }
        }
      });
    }

    /* changes there graphical representation */
    private void deselect()
    {
      Tab.this.setForeground(TEXT_DEFAULT_COLOR);
    }
  }
}
