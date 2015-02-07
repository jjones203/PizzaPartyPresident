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

 description: */
public class DatePanel extends JPanel
{
  private static final int INSET = 5;
  private static final String DATE_PATTERN = "EEE, MMM d, YYYY";
  private static final Font DATE_FONT = ColorsAndFonts.TOP_FONT;
  
  private SimpleDateFormat formatter;
  private Date date;

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

  @Override
  public void paintComponent(Graphics g)
  {
    super.paintComponent(g);
    Graphics2D g2 = (Graphics2D) g;

    g2.getRenderingHints()
      .put(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

    g2.setColor(ColorsAndFonts.GUI_BACKGROUND);
    g2.setFont(ColorsAndFonts.TOP_FONT);
    
    String s = getDateString();
    FontMetrics metrics = g2.getFontMetrics();
    
    int w = metrics.stringWidth(s);
    int h = (int) metrics.getLineMetrics(s, g2).getHeight();
  
  /* position given to Graphics context is lower left hand corner of text */
    int x = (getWidth() - w) / 2;
    int y = (getHeight() + h) / 2;

//    g2.fillRect(x - 5, y - h, w + 10, h + 10);
//    g2.setColor(ColorsAndFonts.GUI_TEXT_COLOR);
    
//    g2.setStroke(new BasicStroke(2));
//    g2.drawRect(x - 5, y - h, w + 10, h + 10);
    
    g2.setColor(ColorsAndFonts.GUI_TEXT_COLOR);
    g2.drawString(s, x, y);
  }

  private String getDateString(Date d)
  {
    StringBuffer s = new StringBuffer();
    formatter.format(d, s, new FieldPosition(DateFormat.FULL));
    return s.toString();
  }

  private String getDateString()
  {
    return getDateString(date);
  }
  
  public void setDate(Date d)
  {
    date = d;
    repaint();
  }

  public static void main(String[] args)
  {
    final JFrame frame = new JFrame();
    final DatePanel datePanel = new DatePanel();
    frame.setContentPane(datePanel);
    frame.pack();
    frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    frame.setVisible(true);

    new Timer(30, new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent e)
      {
        frame.repaint();
      }
    }).start();


  }


}
