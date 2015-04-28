package worldfoodgame.gui.hud;

import worldfoodgame.common.EnumCropType;
import worldfoodgame.gui.ColorsAndFonts;
import worldfoodgame.gui.GUIRegion;
import worldfoodgame.model.Continent;
import worldfoodgame.model.Country;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.List;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.util.*;

/**
 * Created by Tim on 4/24/15.
 */
public class PieChart extends JPanel
{
  private final static Color BORDER_COL = ColorsAndFonts.GUI_TEXT_COLOR.darker();
  private final static Font TITLE_FONT = ColorsAndFonts.HUD_TITLE;
  private final static Color GUI_BACKGROUND = ColorsAndFonts.GUI_BACKGROUND;
  private final static Color FOREGROUND_COL = ColorsAndFonts.GUI_TEXT_COLOR;
  private final static RenderingHints rh = new RenderingHints(
          RenderingHints.KEY_ANTIALIASING,
          RenderingHints.VALUE_ANTIALIAS_ON
  );

  private boolean isHovered;
  private int year = 2014;

  public static final EmptyBorder PADDING_BORDER = new EmptyBorder(5, 5, 5, 5);
  private JLabel titleLabel;
  private JPanel regionViewer;
  private float alpha;
  private java.util.List<GUIRegion> regions = new ArrayList<>();

  public PieChart(String name)
  {
    // init
    this.alpha = 0.0f;
    this.titleLabel = new JLabel(name);
    this.regionViewer = getRegionView();

    // config
    this.setLayout(new BorderLayout());
    this.setBackground(GUI_BACKGROUND);

    titleLabel.setFont(TITLE_FONT);
    titleLabel.setHorizontalAlignment(SwingConstants.LEFT);
    titleLabel.setForeground(FOREGROUND_COL);
    titleLabel.setBorder(new CompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 2, 0, BORDER_COL),
            PADDING_BORDER));

    regionViewer.setBorder(PADDING_BORDER);
    regionViewer.addMouseListener(new MouseAdapter()
    {
      @Override
      public void mouseEntered(MouseEvent e)
      {
        isHovered = true;
      }

      @Override
      public void mouseExited(MouseEvent e)
      {
        isHovered = false;
      }
    });


    // wire-up
    this.add(titleLabel, BorderLayout.NORTH);
    this.add(regionViewer, BorderLayout.CENTER);

  }

  public void setTitle(String name)
  {
    titleLabel.setText(name);
  }

  public String getTitle()
  {
    return titleLabel.getText();
  }

  public void incrementYear()
  {
    year++;
  }

  /* constructs a JPanel whose paintComponent method is overridden with
     a definition for displaying a nicely scaled and located aggregation of
     polygons representing region borders
   */
  private JPanel getRegionView()
  {
    return new JPanel()
    {
      @Override
      public void paint(Graphics g)
      {
        /* logical short circuiting not reliable here? */
        if (regions == null) return;
        if (regions.isEmpty()) return;


        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHints(rh);

        /* set alpha blending var for a nice fade in animation */
        if (alpha < 1)
        {
          g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
          alpha += 0.10f;
        }

        int inset = 5;

        double boxW = getWidth() - 2 * inset;
        double boxH = getHeight() - 2 * inset;

        double [] needTotals = new double [5];
        double [] actualTotals = new double [5];

        ArrayList<CropSlice> slices = new ArrayList<>();
        ArrayList<Continent> continents = new ArrayList<>();
/*
        for (GUIRegion gr : regions)
        {
          needTotals[0] = needTotals[0] + gr.getCountry().getTotalCropNeed(year, EnumCropType.CORN);
          actualTotals[0] = actualTotals[0] + gr.getCountry().getNetCropAvailable(year, EnumCropType.CORN);
          needTotals[1] = needTotals[1] + gr.getCountry().getTotalCropNeed(year, EnumCropType.WHEAT);
          actualTotals[1] = actualTotals[1] + gr.getCountry().getNetCropAvailable(year, EnumCropType.WHEAT);
          needTotals[2] = needTotals[2] + gr.getCountry().getTotalCropNeed(year, EnumCropType.SOY);
          actualTotals[2] = actualTotals[2] + gr.getCountry().getNetCropAvailable(year, EnumCropType.SOY);
          needTotals[3] = needTotals[3] + gr.getCountry().getTotalCropNeed(year, EnumCropType.RICE);
          actualTotals[3] = actualTotals[3] + gr.getCountry().getNetCropAvailable(year, EnumCropType.RICE);
          needTotals[4] = needTotals[4] + gr.getCountry().getTotalCropNeed(year, EnumCropType.OTHER_CROPS);
          actualTotals[4] = actualTotals[4] + gr.getCountry().getNetCropAvailable(year, EnumCropType.OTHER_CROPS);
        }
        slices.add(new CropSlice(needTotals[0], actualTotals[0], EnumCropType.CORN, Color.BLUE));
        slices.add(new CropSlice(needTotals[1], actualTotals[1], EnumCropType.WHEAT, Color.GREEN));
        slices.add(new CropSlice(needTotals[2], actualTotals[2], EnumCropType.SOY, Color.YELLOW));
        slices.add(new CropSlice(needTotals[3], actualTotals[3], EnumCropType.RICE, Color.RED));
        slices.add(new CropSlice(needTotals[4], actualTotals[4], EnumCropType.OTHER_CROPS, Color.WHITE));
*/
        for (GUIRegion gr : regions)
        {
          if (!continents.contains(gr.getCountry().getContinent()))
          {
            continents.add(gr.getCountry().getContinent());
          }
        }
        for (Continent cont : continents)
        {
          needTotals[0] = needTotals[0] + cont.getTotalCropNeed(year, EnumCropType.CORN);
          actualTotals[0] = actualTotals[0] + cont.getProduction(year, EnumCropType.CORN);
          needTotals[1] = needTotals[1] + cont.getTotalCropNeed(year, EnumCropType.WHEAT);
          actualTotals[1] = actualTotals[1] + cont.getProduction(year, EnumCropType.WHEAT);
          needTotals[2] = needTotals[2] + cont.getTotalCropNeed(year, EnumCropType.SOY);
          actualTotals[2] = actualTotals[2] + cont.getProduction(year, EnumCropType.SOY);
          needTotals[3] = needTotals[3] + cont.getTotalCropNeed(year, EnumCropType.RICE);
          actualTotals[3] = actualTotals[3] + cont.getProduction(year, EnumCropType.RICE);
          needTotals[4] = needTotals[4] + cont.getTotalCropNeed(year, EnumCropType.OTHER_CROPS);
          actualTotals[4] = actualTotals[4] + cont.getProduction(year, EnumCropType.OTHER_CROPS);
        }
        slices.add(new CropSlice(needTotals[0], actualTotals[0], EnumCropType.CORN, Color.BLUE));
        slices.add(new CropSlice(needTotals[1], actualTotals[1], EnumCropType.WHEAT, Color.GREEN));
        slices.add(new CropSlice(needTotals[2], actualTotals[2], EnumCropType.SOY, Color.YELLOW));
        slices.add(new CropSlice(needTotals[3], actualTotals[3], EnumCropType.RICE, Color.RED));
        slices.add(new CropSlice(needTotals[4], actualTotals[4], EnumCropType.OTHER_CROPS, Color.WHITE));
        double needTotal = 0;
        double actualTotal = 0;
        for (int i = 0; i < 5; i++)
        {
          needTotal = needTotal + needTotals[i];
        }
        for (int i = 0; i < 5; i++)
        {
          actualTotal = actualTotal + actualTotals[i];
        }

        // currently there is no roll over behavior. but maybe someday...
        if (isHovered) g2d.setColor(ColorsAndFonts.MINI_BOX_REGION);
        else g2d.setColor(ColorsAndFonts.MINI_BOX_REGION);

        double currentValue = 0.0D;
        int startAngle = 0;
        int arcAngle = 0;
        for (CropSlice cs : slices)
        {
          if (cs.actual >= cs.needed)
          {
            startAngle = (int) (currentValue * 360 / needTotal);
            arcAngle = (int) (cs.needed * 360 / needTotal);
            g.setColor(cs.color);
            g.fillArc(5, 5, (int) boxW, (int) boxH, startAngle, arcAngle);
            currentValue += cs.needed;
          }
          else
          {
            g.setColor(cs.color);
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.4f));
            startAngle = (int) (currentValue * 360 / needTotal);
            arcAngle = (int) (cs.needed * 360 / needTotal);
            g.fillArc(5, 5, (int) boxW, (int) boxH, startAngle, arcAngle);
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
            startAngle = (int) (currentValue * 360 / needTotal);
            arcAngle = (int) (cs.actual * 360 / needTotal);
            g.fillArc(5, 5, (int) boxW, (int) boxH, startAngle, arcAngle);
            currentValue += cs.needed;
          }
        }
      }
    };
  }

  /**
   * Set level of transparency for the displayed regions. Can be used to
   * controls animation starting opacity.
   * @param x float between 0 and 1. 0 => completely translucent, 1 => Opaque.
   */
  public void setAlph(float x)
  {
    alpha = x;
  }

  /**
   * Collection of Gui regions to draw in the mini Display box.
   * @param regions
   */
  public void setRegions(java.util.List<GUIRegion> regions)
  {
    this.regions = regions;
  }

  private class CropSlice
  {
    double needed;
    double actual;
    EnumCropType name;
    Color color;

    public CropSlice(double needed, double actual, EnumCropType name, Color color)
    {
      this.needed = needed;
      this.actual = actual;
      this.name = name;
      this.color = color;
    }
  }
}
