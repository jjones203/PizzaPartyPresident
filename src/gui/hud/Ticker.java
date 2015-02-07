package gui.hud;

import gui.ColorsAndFonts;
import gui.WorldPresenter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 @author david
 created: 2015-02-05

 description: */
public class Ticker extends JPanel
{

  private static final Font MARQUIS_FONT = ColorsAndFonts.TOP_FONT;
  private static final int INSET = 5;

  private final static String[] testStrings =
    {
      "Cost of Soylent Green Is Rising As Birthrates Fall In China",
      "Chocolate Chips Outlawed As Scone Ingredient; Scone Farmers Rejoice",
      "Lorem Ipsum Dolor Sit Amet"
    };

  private List<String> marquis;

  private Timer timer;

  public Ticker()
  {
    marquis = new ArrayList<>();
    marquis.addAll(Arrays.asList(testStrings));

    int h = getFontMetrics(MARQUIS_FONT).getHeight() + INSET;
    int w = 600;
    setMinimumSize(new Dimension(0, h));
    setPreferredSize(new Dimension(w, h));
    setMaximumSize(getPreferredSize());
    
    setBackground(ColorsAndFonts.GUI_BACKGROUND);

    timer = new Timer(25, new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent e)
      {
        repaint();
      }
    });
  }

  @Override
  protected void paintComponent(Graphics g)
  {
    super.paintComponent(g);
    Graphics2D g2 = (Graphics2D) g;
    
  }
  
  public void clearMarquisText()
  {
    marquis.clear();
  }
  
  public void setMarquisText(List<String> newsList )
  {
    marquis = newsList;
  }
  
  public void addMarquisText(String news)
  {
    marquis.add(news);
  }
  
  public void addMarquisText(List<String> newsList)
  {
    marquis.addAll(newsList);
  }

  public void start()
  {
    timer.start();
  }

  public void pause()
  {
    timer.stop();
  }
}
