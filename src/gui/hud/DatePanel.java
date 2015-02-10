package gui.hud;

import gui.ColorsAndFonts;
import gui.WorldPresenter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 @author david
 created: 2015-02-06

 description: DatePanel is a Swing component that can be instructed to display
 a Date in a simple format.  It is meant to be driven by a parent container
 (WorldFeedPanel in this case)
 */
public class DatePanel extends JPanel
{
  private static final int INSET = 5;
  private static final String DATE_PATTERN = "EEE, MMM d, YYYY";
  private static final Font DATE_FONT = ColorsAndFonts.TOP_FONT;
  private static final Color guiBackground = ColorsAndFonts.GUI_BACKGROUND;

  private SimpleDateFormat formatter;
  private Date date;


  /**
   Instantiates a DatePanel whose Dimension is dependent on FontMetrics and a
   default GUI font (see gui.ColorsAndFonts)
   */
  public DatePanel()
  {
    formatter = new SimpleDateFormat(DATE_PATTERN);

    setOpaque(true);
    setBackground(ColorsAndFonts.GUI_BACKGROUND);

    FontMetrics metrics = getFontMetrics(DATE_FONT);
    int h = metrics.getHeight() + INSET*2;
    int w = metrics.stringWidth(DATE_PATTERN) + INSET * 2;
    
    setPreferredSize(new Dimension(w, h));
    setMinimumSize(getPreferredSize());
    setMaximumSize(getPreferredSize());
  }

  /**
   Overridden paintComponent draws the Date with pleasant insets, locating
   itself according to FontMetrics
   @param g Graphics context to draw to
   */
  @Override
  public void paintComponent(Graphics g)
  {
    super.paintComponent(g);
    Graphics2D g2 = (Graphics2D) g;

    g2.setRenderingHint(
      RenderingHints.KEY_TEXT_ANTIALIASING,
      RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

    g2.setColor(guiBackground);
    g2.setFont(ColorsAndFonts.TOP_FONT);
    
    String s = getDateString().toUpperCase();
    FontMetrics metrics = g2.getFontMetrics();
    
    int w = metrics.stringWidth(s);
    int h = (int) metrics.getLineMetrics(s, g2).getHeight();
  
  /* position given to Graphics context is lower left hand corner of text */
    int x = (getWidth() - w) / 2;
    int y = (getHeight() + h) / 2;

    g2.setColor(ColorsAndFonts.GUI_TEXT_COLOR);
    g2.drawString(s, x, y);
  }

  /**
   Set the date to display
   @param d   Date to display in panel
   */
  public void setDate(Date d)
  {
    date = d;
    repaint();
  }

  /* wraps getDateString base with the member variable date as an arg*/
  private String getDateString()
  {
    return getDateString(date);
  }

  /* construct a string using the SimpleDateFormat for the passed Date object */
  private String getDateString(Date d)
  {
    StringBuffer s = new StringBuffer();
    formatter.format(d, s, new FieldPosition(DateFormat.FULL));
    return s.toString();
  }

}
