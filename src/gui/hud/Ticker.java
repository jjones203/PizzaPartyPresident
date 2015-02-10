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

  private static final Font MARQUIS_FONT = ColorsAndFonts.GUI_FONT;
  private static final int INSET = 5;

  private final static String[] testStrings =
    {
      "Cost of Soylent Green Is Rising As Birthrates Fall In China",
      "Chocolate Chips Outlawed As Scone Ingredient; Scone Farmers Rejoice",
      "Lorem Ipsum Dolor Sit Amet"
    };
  private static final String SPACING = "   ";

  private String marquisStr;

  private Timer timer;

  public Ticker()
  {
    marquisStr = new String();
    setMarquisText(Arrays.asList(testStrings));

    int h = getFontMetrics(MARQUIS_FONT).getHeight() + INSET;
    int w = 600;
    setMinimumSize(new Dimension(0, h));
    setPreferredSize(new Dimension(w, h));
    setMaximumSize(getPreferredSize());
    
    setBackground(ColorsAndFonts.GUI_BACKGROUND);
  }

  @Override
  protected void paintComponent(Graphics g)
  {

    
  }
  
  public void clearMarquisText()
  {
    marquisStr = "";
  }
  
  public void setMarquisText(List<String> newsList)
  {
    clearMarquisText();
    addMarquisText(newsList);
  }
  
  public void setMarquisText(String news)
  {
    clearMarquisText();
    addMarquisText(news);
  }
  
  public void addMarquisText(String news)
  {
    marquisStr += news + SPACING;
  }
  
  public void addMarquisText(List<String> newsList)
  {
    for(String s : newsList) addMarquisText(s);
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
