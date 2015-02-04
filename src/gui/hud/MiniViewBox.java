package gui.hud;

import IO.XMLparsers.KMLParser;
import gui.ColorsAndFonts;
import gui.EquirectangularConverter;
import gui.GUIRegion;
import model.Region;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Collections;
import java.util.Random;

/**
 * Encapsulates displaying the selected regions in the hud.
 */
public class MiniViewBox extends JPanel
{
  private final static int R_PANEL_WIDTH = 230;
  private final static int R_PANEL_HEIGHT = 300;
  private final static Color BORDER_COL = ColorsAndFonts.GUI_TEXT_COLOR.darker();
  private final static Font TITLE_FONT = ColorsAndFonts.HUD_TITLE;
  private final static Color GUI_BACKGROUND = ColorsAndFonts.GUI_BACKGROUND;
  private final static Color FORGROUND_COL = ColorsAndFonts.GUI_TEXT_COLOR;
  private static final double PADDING = .90;
  private final static RenderingHints rh = new RenderingHints(
      RenderingHints.KEY_ANTIALIASING,
      RenderingHints.VALUE_ANTIALIAS_ON
  );

  public static final EmptyBorder PADDING_BORDER = new EmptyBorder(5, 5, 5, 5);
  private JLabel titleLabel;
  private JPanel regionViewer;
  private Polygon regionPolygon;
  private float alpha;

  public MiniViewBox(String name)
  {
    // init
    this.alpha = 0.0f;
    this.titleLabel = new JLabel(name);
    this.regionViewer = getRegionView();

    // config
    Dimension prefSize = new Dimension(R_PANEL_WIDTH, R_PANEL_HEIGHT);
    regionViewer.setPreferredSize(prefSize);
    regionViewer.setMinimumSize(prefSize);
    this.setLayout(new BorderLayout());
    this.setBackground(GUI_BACKGROUND);

    titleLabel.setFont(TITLE_FONT);
    titleLabel.setHorizontalAlignment(SwingConstants.LEFT);
    titleLabel.setForeground(FORGROUND_COL);
    titleLabel.setBorder(new CompoundBorder(
        BorderFactory.createMatteBorder(0, 0, 2, 0, BORDER_COL),
        PADDING_BORDER));

    regionViewer.setBorder(PADDING_BORDER);

    // wireup
    this.add(titleLabel, BorderLayout.NORTH);
    this.add(regionViewer, BorderLayout.CENTER);

  }

  public static void main(String[] args)
  {
    final JFrame frame = new JFrame();

    MiniViewBox vb = new MiniViewBox("REGION NAME");

    long seed = 44;
    java.util.List<Region> testlist = (java.util.List<Region>) KMLParser.getRegionsFromFile("resources/ne_50m_admin_1_states_provinces_lakes.kml");
    Collections.shuffle(testlist, new Random());

    Region firstRegion = testlist.get(0);
    System.out.println("regoins name: " + firstRegion);
    Polygon testPoly = new GUIRegion(firstRegion, new EquirectangularConverter(), null).getPoly();
    System.out.println(testPoly.getBounds());
    vb.setRegionPolygon(testPoly);


    frame.add(vb);
    frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    frame.pack();
//    frame.setResizable(false);
    frame.setVisible(true);

    Timer animator = new Timer(20, new AbstractAction()
    {
      @Override
      public void actionPerformed(ActionEvent e)
      {
        frame.repaint();;
      }
    });
    animator.start();
  }

  public Polygon getRegionPolygon()
  {
    return regionPolygon;
  }

  public void setRegionPolygon(Polygon regionPolygon)
  {
    this.regionPolygon = regionPolygon;
  }

  public void setTitle(String name)
  {
    titleLabel.setText(name);
  }

  public String getTitle()
  {
    return titleLabel.getText();
  }

  /**
   * shifts a given polygon back to the the origin (0, 0). Is used to make the
   * mini display work.
   *
   * (!) note, this method does not mutaite its argument.
   * @param regionPolygon region to shift back
   * @return
   */
  private static Polygon movePolyToOrigin(Polygon regionPolygon)
  {
    //TODO if we see a performace problem here we could memoize the results.
    Polygon shifted = new Polygon(
        regionPolygon.xpoints, regionPolygon.ypoints, regionPolygon.npoints
    );

    int x = regionPolygon.getBounds().x;
    int y = regionPolygon.getBounds().y;

    shifted.translate( -x, -y);

    return shifted;
  }

  private JPanel getRegionView()
  {
    return new JPanel()
    {
      @Override
      public void paint(Graphics g)
      {
        if (regionPolygon != null)
        {
          Graphics2D g2d = (Graphics2D) g;
          g2d.setRenderingHints(rh);


          if (alpha < 1){
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
            alpha += 0.10f;
          }

          Polygon shifted = movePolyToOrigin(regionPolygon);
          double scaleValue;

          double boxW = getWidth();
          double boxH = getHeight();
          double boxAspect = boxW/boxH;

          double polyW = shifted.getBounds().getWidth();
          double polyH = shifted.getBounds().getHeight();
          double polyAspect = polyW / polyH;
          
          double dx, dy;
          
          if (boxAspect > polyAspect)
          {
            scaleValue = boxH / polyH;
            dx = (boxW - polyW * scaleValue) / 2;
            dy = 0;
          }
          else
          {
            scaleValue = boxW/polyW;
            dy = (boxH - polyH * scaleValue) / 2;
            dx = 0;
          }

          g2d.translate(dx, dy);
          g2d.scale(scaleValue, scaleValue);

          g2d.setColor(ColorsAndFonts.ACTIVE_REGION);
          g2d.fillPolygon(shifted);
        }
      }
    };
  }
}
