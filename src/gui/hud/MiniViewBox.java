package gui.hud;

import IO.AreaXMLloader;
import gui.ColorsAndFonts;
import org.xml.sax.SAXException;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.xml.parsers.ParserConfigurationException;
import java.awt.*;

/**
 * Encapsulates displaying the selected regions in the hud.
 *
 * TODO finish implementing this
 */
public class MiniViewBox extends JPanel
{
  private final static int WIDTH = 230;
//  private final static int HEIGHT =
  private final static Color BORDER_COL = ColorsAndFonts.GUI_TEXT_COLOR.darker();
  private final static Font TITLE_FONT = ColorsAndFonts.HUD_TITLE;
  private final static Color GUI_BACKGROUND = ColorsAndFonts.GUI_BACKGROUND;
  private final static Color FORGROUND_COL = ColorsAndFonts.GUI_TEXT_COLOR;

  private JLabel titleLabel;
  private JPanel regionViewer;
  private Polygon currentRegion;

  public MiniViewBox(String name)
  {
    // init
    this.titleLabel = new JLabel(name);
    this.regionViewer = getRegionView();

    // config
    this.setLayout(new BorderLayout());
    this.setBackground(ColorsAndFonts.GUI_BACKGROUND);

    titleLabel.setFont(ColorsAndFonts.HUD_TITLE);
    titleLabel.setHorizontalAlignment(SwingConstants.LEFT);
    titleLabel.setForeground(ColorsAndFonts.GUI_TEXT_COLOR);
    titleLabel.setBorder(new CompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 2, 0, BORDER_COL),
            new EmptyBorder(5, 5, 5, 5)));

    // wireup
    this.add(titleLabel, BorderLayout.NORTH);
    this.add(regionViewer, BorderLayout.CENTER);

  }

  public static void main(String[] args)
  {
    JFrame frame = new JFrame();

    MiniViewBox vb = new MiniViewBox("REGION NAME");

    try
    {
      AreaXMLloader loader = new AreaXMLloader();
      /// add a poly gon to test with
    }
    catch (ParserConfigurationException e)
    {
      e.printStackTrace();
    }
    catch (SAXException e)
    {
      e.printStackTrace();
    }



    frame.add(vb);
    frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    frame.pack();
    frame.setVisible(true);
  }

  public Polygon getCurrentRegion()
  {
    return currentRegion;
  }

  public void setCurrentRegion(Polygon currentRegion)
  {
    this.currentRegion = currentRegion;
  }

  private JPanel getRegionView()
  {
    return new JPanel()
    {
      @Override
      public void paint(Graphics g)
      {
        if (currentRegion != null)
        {
          g.fillPolygon(currentRegion);
        }
      }
    };
  }
}
