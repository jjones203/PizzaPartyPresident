package gui.hud;

import gui.ColorsAndFonts;
import gui.WorldPresenter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
 private static final int INSET = 10;
 
 WorldPresenter presenter;
 SimpleDateFormat formatter;

 public DatePanel(WorldPresenter presenter)
 {
  this.presenter = presenter;
  formatter = new SimpleDateFormat("EEE, MMM d, YYYY");
  
  setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
  
  int h = getFontMetrics(ColorsAndFonts.TOP_FONT).getHeight();
  
  setPreferredSize(new Dimension(0,h+INSET));
  setMinimumSize(getPreferredSize());
  
 }
 
 @Override
 public void paintComponent(Graphics g)
 {
  Graphics2D g2 = (Graphics2D) g;
  
  g2.getRenderingHints()
    .put(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
  
  g2.setColor(ColorsAndFonts.GUI_BACKGROUND);
  g2.setFont(ColorsAndFonts.GUI_FONT.deriveFont(20.0f));
  String s = getDateString();
  FontMetrics metrics = g2.getFontMetrics();
  int w = metrics.stringWidth(s);
  int h = metrics.getHeight();
  
  
  /* position given to Graphics context is lower left hand corner of text */
  int x = (getWidth() - w)/2;
  int y = h + (getHeight() - h)/2;

  g2.fillRect(x-5, y-h, w+10, h+10);
  g2.setColor(ColorsAndFonts.ACTIVE_REGION);
  g2.setStroke(new BasicStroke(2));
  g2.drawRect(x - 5, y - h, w + 10, h + 10);
  g2.setColor(ColorsAndFonts.GUI_TEXT_COLOR);
  g2.drawString(s, x, y);

 }

 private String getDateString(long l)
 {
  Date d = new Date(l);
  StringBuffer s = new StringBuffer();
  formatter.format(d, s, new FieldPosition(DateFormat.FULL));
  return s.toString();
 }

 private String getDateString()
 {
  return getDateString(presenter.getWorldDate().getTime());
 }

 public static void main(String[] args)
 {
  final JFrame frame = new JFrame();
  final DatePanel datePanel = new DatePanel(null);
  frame.add(datePanel);
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
