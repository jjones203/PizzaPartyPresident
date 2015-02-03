package gui.hud;

import IO.AreaXMLloader;
import IO.XMLparsers.KMLParser;
import IO.XMLparsers.StateParser;
import gui.ColorsAndFonts;
import gui.EquirectangularConverter;
import gui.GUIRegion;
import model.Region;
import org.xml.sax.SAXException;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.xml.parsers.ParserConfigurationException;

import java.awt.*;
import java.util.Collections;
import java.util.Random;

/**
 * Encapsulates displaying the selected regions in the hud.
 */
public class MiniViewBox extends JPanel
{
  private final static int PANEL_WIDTH = 230;
  private final static int PANEL_HEIGHT = 400;
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
    Dimension prefSize = new Dimension(PANEL_WIDTH, PANEL_HEIGHT);
    setPreferredSize(prefSize);
    setMinimumSize(prefSize);
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


  private Polygon zeroPospolyGo(Polygon regionPolygon)
  {
    Polygon shifted = new Polygon(
        regionPolygon.xpoints, regionPolygon.ypoints, regionPolygon.npoints);

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

          // scalling
          if (regionPolygon.getBounds().width > regionPolygon.getBounds().height)
          {
            System.out.println("scaling by width: " + regionPolygon.getBounds().width);
            double scaleValue = (double) PANEL_WIDTH / regionPolygon.getBounds().width;
            g2d.scale(scaleValue, scaleValue);
            System.out.println("scale value: " + scaleValue);
          }
          else
          {
            System.out.println("scaling by height: " + regionPolygon.getBounds().height);
            double scaleValue = (double) PANEL_HEIGHT / regionPolygon.getBounds().height;
            g2d.scale(scaleValue, scaleValue);
            System.out.println("scale value: " + scaleValue);
          }


          //shifting
          g2d.translate(-regionPolygon.getBounds().x, -regionPolygon.getBounds().y);
          //todo find a clean way to add padding. so that the region is centerned nicely.

          //drawing
          g2d.setColor(ColorsAndFonts.ACTIVE_REGION);
          g2d.fillPolygon(regionPolygon);

          //todo for testing only...
//          g2d.fillRect(0, 0, 100, 100);
        }
      }
    };
  }
}
