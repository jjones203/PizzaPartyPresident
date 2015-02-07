package gui.hud;

import gui.ColorsAndFonts;
import gui.GUIRegion;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.geom.Area;
import java.util.*;
import java.util.List;

/**
 Encapsulates displaying the selected regions in the hud.
 */
public class MiniViewBox extends JPanel
{

  private final static Color BORDER_COL = ColorsAndFonts.GUI_TEXT_COLOR.darker();
  private final static Font TITLE_FONT = ColorsAndFonts.HUD_TITLE;
  private final static Color GUI_BACKGROUND = ColorsAndFonts.GUI_BACKGROUND;
  private final static Color FORGROUND_COL = ColorsAndFonts.GUI_TEXT_COLOR;
  private final static RenderingHints rh = new RenderingHints(
    RenderingHints.KEY_ANTIALIASING,
    RenderingHints.VALUE_ANTIALIAS_ON
  );

  public static final EmptyBorder PADDING_BORDER = new EmptyBorder(5, 5, 5, 5);
  private JLabel titleLabel;
  private JPanel regionViewer;
  private Area drawableArea;
  private float alpha;
  private List<GUIRegion> regions = new ArrayList<>();

  public MiniViewBox(String name)
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
    titleLabel.setForeground(FORGROUND_COL);
    titleLabel.setBorder(new CompoundBorder(
      BorderFactory.createMatteBorder(0, 0, 2, 0, BORDER_COL),
      PADDING_BORDER));

    regionViewer.setBorder(PADDING_BORDER);

    // wire-up
    this.add(titleLabel, BorderLayout.NORTH);
    this.add(regionViewer, BorderLayout.CENTER);

  }

  public Area getDrawableArea()
  {
    return drawableArea;
  }

  public void setDrawableArea(Area drawableArea)
  {
    this.drawableArea = drawableArea;
    alpha = 0; // resets alpha for animation.
  }

  public void setTitle(String name)
  {
    titleLabel.setText(name);
  }

  public String getTitle()
  {
    return titleLabel.getText();
  }


  private JPanel getRegionView()
  {
    return new JPanel()
    {
      @Override
      public void paint(Graphics g)
      {
        if (!regions.isEmpty())
        {
          Graphics2D g2d = (Graphics2D) g;
          g2d.setRenderingHints(rh);

          if (alpha < 1)
          {
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
            alpha += 0.10f;
          }

          double scaleValue;

          int inset = 5;
          double boxW = getWidth() - 2 * inset;
          double boxH = getHeight() - 2 * inset;
          double boxAspect = boxW / boxH;

          double polyW, polyH;
          double minX = Double.MAX_VALUE, minY = Double.MAX_VALUE;
          double maxX = Double.MIN_VALUE, maxY = Double.MIN_VALUE;
          for(GUIRegion gr : regions)
          {
            Rectangle bounds = gr.getPoly().getBounds();
            minX = Math.min(minX, bounds.getX());
            minY = Math.min(minY, bounds.getY());
            maxX = Math.max(maxX, bounds.getMaxX());
            maxY = Math.max(maxY, bounds.getMaxY());
          }
          polyW = maxX - minX;
          polyH = maxY - minY;

          

          double polyAspect = polyW / polyH;

          double xshift, yshift;

          if (boxAspect > polyAspect)
          {
            scaleValue = (boxH) / polyH;
            xshift = (boxW - scaleValue * polyW) / 2 + inset;
            yshift = inset;

          }
          else
          {
            scaleValue = (boxW) / polyW;
            yshift = (boxH - scaleValue * polyH) / 2 + inset;
            xshift = inset;
          }

          double xTranslate = xshift - (scaleValue * minX);
          double yTranslate = yshift - (scaleValue * minY);

          g2d.translate(xTranslate, yTranslate);
          g2d.scale(scaleValue, scaleValue);

          g2d.setColor(ColorsAndFonts.MINI_BOX_REGION);
          for(GUIRegion gr : regions) g2d.fill(gr.getPoly());
        }
        /* doing some tests */
        else if (true) return;
        
        if (drawableArea != null)
        {
          Graphics2D g2d = (Graphics2D) g;
          g2d.setRenderingHints(rh);

          if (alpha < 1)
          {
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
            alpha += 0.10f;
          }

          double scaleValue;

          int inset = 5;
          double boxW = getWidth() - 2 * inset;
          double boxH = getHeight() - 2 * inset;
          double boxAspect = boxW / boxH;

          double polyW = drawableArea.getBounds().getWidth();
          double polyH = drawableArea.getBounds().getHeight();

          double polyx = drawableArea.getBounds().x;
          double polyy = drawableArea.getBounds().y;

          double polyAspect = polyW / polyH;

          double xshift, yshift;

          if (boxAspect > polyAspect)
          {
            scaleValue = (boxH) / polyH;
            xshift = (boxW - scaleValue * polyW) / 2 + inset;
            yshift = inset;

          }
          else
          {
            scaleValue = (boxW) / polyW;
            yshift = (boxH - scaleValue * polyH) / 2 + inset;
            xshift = inset;
          }

          double xTranslate = xshift - (scaleValue * polyx);
          double yTranslate = yshift - (scaleValue * polyy);

          g2d.translate(xTranslate, yTranslate);
          g2d.scale(scaleValue, scaleValue);

          g2d.setColor(ColorsAndFonts.MINI_BOX_REGION);
          g2d.fill(drawableArea);
        }
      }
    };
  }

  public void setDrawableRegions(List<GUIRegion> regions)
  {
    this.regions = regions;
  }
}
