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
  public static final EmptyBorder PADDING_BORDER = new EmptyBorder(5, 5, 5, 5);

  private JLabel titleLabel;
  private JPanel regionViewer;
  private Polygon regionPolygon;

  public MiniViewBox(String name)
  {
    // init
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
    JFrame frame = new JFrame();

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
    frame.setVisible(true);
  }

  public Polygon getRegionPolygon()
  {
    return regionPolygon;
  }

  public void setRegionPolygon(Polygon regionPolygon)
  {
    this.regionPolygon = regionPolygon;
  }


  private static Polygon movePolyToOrigin(Polygon regionPolygon)
  {
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
          double padding = .90;
          Graphics2D g2d = (Graphics2D) g;

          Polygon shifted = movePolyToOrigin(regionPolygon);
//          shifted = regionPolygon
          double scaleValue;


          // shift for width
          if (shifted.getBounds().width > shifted.getBounds().height)
          {
            scaleValue = ((double) R_PANEL_WIDTH/ shifted.getBounds().width);
            scaleValue *= padding;
          }
          // shift for height
          else
          {
            scaleValue = ((double) R_PANEL_HEIGHT / shifted.getBounds().height);
            scaleValue *= padding;
          }


          System.out.println("scaleValue" + scaleValue);


          double scaledWidth = shifted.getBounds().width * scaleValue;
          double scaledHeight = shifted.getBounds().height * scaleValue;


          System.out.println("scaledWidth" + scaledWidth);
          System.out.println("scaledHeight" + scaledHeight);

          double xshift = Math.abs(scaledWidth - R_PANEL_WIDTH) / 2;
          double yshift = Math.abs(scaledHeight - R_PANEL_HEIGHT) / 2;

          g2d.translate(xshift, yshift);

          g2d.scale(scaleValue, scaleValue);


          System.out.println("shifted bounds: " + shifted.getBounds());

          g2d.setColor(ColorsAndFonts.ACTIVE_REGION);
          g2d.fillPolygon(shifted);
          // scalling
//          if (regionPolygon.getBounds().width > regionPolygon.getBounds().height)
//          {
//            System.out.println("scaling by width: " + regionPolygon.getBounds().width);
//            double scaleValue = (double) R_PANEL_WIDTH / regionPolygon.getBounds().width;
//            g2d.scale(scaleValue, scaleValue);
//            System.out.println("scale value: " + scaleValue);
//          }
//          else
//          {
//            System.out.println("scaling by height: " + regionPolygon.getBounds().height);
//            double scaleValue = (double) R_PANEL_HEIGHT / regionPolygon.getBounds().height;
//            g2d.scale(scaleValue, scaleValue);
//            System.out.println("scale value: " + scaleValue);
//          }


          //shifting
//          g2d.translate(-regionPolygon.getBounds().x, -regionPolygon.getBounds().y);
          //todo find a clean way to add padding. so that the region is centerned nicely.

          //drawing
//          g2d.setColor(ColorsAndFonts.ACTIVE_REGION);
//          g2d.fillPolygon(regionPolygon);

          //todo for testing only...
//          g2d.fillRect(0, 0, 100, 100);
        }
      }
    };
  }
}
