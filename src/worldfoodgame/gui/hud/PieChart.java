package worldfoodgame.gui.hud;

import worldfoodgame.common.EnumCropType;
import worldfoodgame.gui.ColorsAndFonts;
import worldfoodgame.gui.GUIRegion;
import worldfoodgame.model.Continent;
import worldfoodgame.model.Country;

import javax.swing.*;
import javax.swing.Timer;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;

import java.awt.*;
import java.awt.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
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
    return new PiePanel();
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
  
  private class PiePanel extends JPanel implements MouseListener, ActionListener
  {
    private int inset;

    private double boxW;
    private double boxH;
    
    private double boxWCenter;
    private double boxHCenter;

    private double [] needTotals;
    private double [] actualTotals;

    private ArrayList<CropSlice> slices;
    private ArrayList<Continent> continents;
    
    private double needTotal;
    private double actualTotal;
    
    private Timer timer;

    PiePanel()
    {
      initiatePiePanel();
    }
    
    private void initiatePiePanel()
    {
      addMouseListener(this);
      timer = new Timer(10,this);
    }
    
    @Override
    public void paint(Graphics g)
    {
      
      /* logical short circuiting not reliable here? */
      if (regions == null) return;
      if (regions.isEmpty()) return;
      
      inset = 5;
      boxW = getWidth() - 2 * inset;
      boxH = getHeight() - 2 * inset;
      boxWCenter=boxW/2;
      boxHCenter=boxH/2;
      needTotals = new double [5];
      actualTotals = new double [5];
      slices = new ArrayList<>();
      continents = new ArrayList<>();
      needTotal=0;
      actualTotal=0;

      for (GUIRegion gr : regions)
      {
        if (!continents.contains(gr.getCountry().getContinent()))
        {
          continents.add(gr.getCountry().getContinent());
        }
      }
      
      for (Continent cont : continents)
      {
        needTotals[0] = cont.getTotalCropNeed(year, EnumCropType.CORN);
        actualTotals[0] = cont.getCropProduction(year, EnumCropType.CORN);
        needTotals[1] =  cont.getTotalCropNeed(year, EnumCropType.WHEAT);
        actualTotals[1] = cont.getCropProduction(year, EnumCropType.WHEAT);
        needTotals[2] = cont.getTotalCropNeed(year, EnumCropType.SOY);
        actualTotals[2] = cont.getCropProduction(year, EnumCropType.SOY);
        needTotals[3] = cont.getTotalCropNeed(year, EnumCropType.RICE);
        actualTotals[3] = cont.getCropProduction(year, EnumCropType.RICE);
        needTotals[4] =  cont.getTotalCropNeed(year, EnumCropType.OTHER_CROPS);
        actualTotals[4] = cont.getCropProduction(year, EnumCropType.OTHER_CROPS);
      }
      slices.add(new CropSlice(needTotals[0], actualTotals[0], EnumCropType.CORN, ColorsAndFonts.A_PIE_PEPPERS_COLOR,ColorsAndFonts.N_PIE_PEPPERS_COLOR));
      slices.add(new CropSlice(needTotals[1], actualTotals[1], EnumCropType.WHEAT, ColorsAndFonts.A_PIE_TOMATOES_COLOR,ColorsAndFonts.N_PIE_TOMATOES_COLOR));
      slices.add(new CropSlice(needTotals[2], actualTotals[2], EnumCropType.SOY, ColorsAndFonts.A_PIE_MUSHROOMS_COLOR,ColorsAndFonts.N_PIE_MUSHROOMS_COLOR));
      slices.add(new CropSlice(needTotals[3], actualTotals[3], EnumCropType.RICE, ColorsAndFonts.A_PIE_PINEAPPLE_COLOR,ColorsAndFonts.N_PIE_PINEAPPLE_COLOR));
      slices.add(new CropSlice(needTotals[4], actualTotals[4], EnumCropType.OTHER_CROPS, ColorsAndFonts.A_PIE_PEPPERONI_COLOR,ColorsAndFonts.N_PIE_PEPPERONI_COLOR));

      for (int i = 0; i < 5; i++)
      {
        needTotal = needTotal + needTotals[i];
      }
      for (int i = 0; i < 5; i++)
      {
        actualTotal = actualTotal + actualTotals[i];
      }
      
      
      Graphics2D g2d = (Graphics2D) g;
      g2d.setRenderingHints(rh);

      double currentValue = 0.0D;
      int startAngle = 0;
      int arcAngle = 0;
      double percent=0;
      for (CropSlice cs : slices)
      {
        percent = cs.actual/cs.needed;
        percent = Math.min(1,percent);

        startAngle = (int) (currentValue * 360 / needTotal);
        arcAngle = (int) (cs.needed * 360 / needTotal);
        g2d.setColor(cs.needColor);
          

        g2d.fillArc(inset+(int)(boxWCenter-boxW/2), inset+(int)(boxHCenter-boxH/2), (int) boxW, (int) boxH, startAngle, arcAngle);
       // g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
        g2d.setColor(cs.actualColor);
        g2d.fillArc(inset+(int)(boxWCenter-percent*boxW/2), inset+(int)(boxHCenter-percent*boxH/2), (int) (boxW*percent), (int) (boxH*percent), startAngle, arcAngle);
        currentValue += cs.needed;
      }
    }

    @Override
    public void mouseClicked(MouseEvent arg0)
    {
      
    }

    @Override
    public void mouseEntered(MouseEvent arg0)
    {
      timer.start();
   }

    @Override
    public void mouseExited(MouseEvent arg0)
    {
      // TODO Auto-generated method stub
      timer.stop();
    }

    @Override
    public void mousePressed(MouseEvent arg0)
    {
      // TODO Auto-generated method stub
      
    }

    @Override
    public void mouseReleased(MouseEvent arg0)
    {
      // TODO Auto-generated method stub
      
    }

    @Override
    public void actionPerformed(ActionEvent arg0)
    {
      if(arg0.getSource()==timer) setMouseTooltip();
    }

    private void setMouseTooltip()
    {
      
      Point coord;
      Robot robot = null;
      try 
      {
         robot = new Robot();
       
      } catch (AWTException e) 
      {
         e.printStackTrace();
      }
      Color color = null;

      coord = MouseInfo.getPointerInfo().getLocation();
      color = robot.getPixelColor(coord.x, coord.y);
      
      String cropType="";
      double needAmt=0;
      double actualAmt=0;
      double excess=0;
      
      if (color.equals(ColorsAndFonts.N_PIE_TOMATOES_COLOR)||color.equals(ColorsAndFonts.A_PIE_TOMATOES_COLOR))
      {
        cropType="Tomatoes";
        needAmt=needTotals[1];
        actualAmt=actualTotals[1];
      }
      else if (color.equals(ColorsAndFonts.N_PIE_PINEAPPLE_COLOR)||color.equals(ColorsAndFonts.A_PIE_PINEAPPLE_COLOR))
      {
        needAmt=needTotals[3];
        actualAmt=actualTotals[3];
        cropType="Pineapple";
      }
      else if (color.equals(ColorsAndFonts.N_PIE_PEPPERS_COLOR)||color.equals(ColorsAndFonts.A_PIE_PEPPERS_COLOR))
      {
        cropType="Peppers";
        needAmt=needTotals[0];
        actualAmt=actualTotals[0];
      }
      else if (color.equals(ColorsAndFonts.N_PIE_PEPPERONI_COLOR)||color.equals(ColorsAndFonts.A_PIE_PEPPERONI_COLOR))
      {
        cropType="Pepperoni";
        needAmt=needTotals[4];
        actualAmt=actualTotals[4];
      }
      else if (color.equals(ColorsAndFonts.N_PIE_MUSHROOMS_COLOR)||color.equals(ColorsAndFonts.A_PIE_MUSHROOMS_COLOR))
      {
        cropType="Mushrooms";
        needAmt=needTotals[2];
        actualAmt=actualTotals[2];
      }
      
      excess=actualAmt-needAmt;
      
      String tooltip;
      if(excess>=0)
      {
      tooltip=String.format("<html>%s<br>" +
          "Need: %8.2f Tons<br>" +
          "Actual: %8.2f Tons <br>"+
          "Surplus!: %8.2f Tons <br>"+
          "</html>",
          cropType,needAmt,actualAmt,excess);
      }
      else
      {
        tooltip=String.format("<html>%s<br>" +
            "need: %8.2f Tons<br>" +
            "actual: %8.2f Tons <br>"+
            "Defecit: %8.2f Tons <br>"+
            "</html>",
            cropType,needAmt,actualAmt,excess);
      }
      this.setToolTipText(tooltip);
    }
  }

  private class CropSlice
  {
    double needed;
    double actual;
    EnumCropType name;
    Color actualColor;
    Color needColor;

    public CropSlice(double needed, double actual, EnumCropType name, Color actualColor,Color needColor)
    {
      this.needed = needed;
      this.actual = actual;
      this.name = name;
      this.actualColor=actualColor;
      this.needColor=needColor;
    }
  }
}

