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

 description:
 Ticker is mostly a placeholder class at the moment.  The idea is that it will
 scroll relevant game information across the screen as the model changes through
 time, alerting the player to situations that need attention (maybe with some
 kind of priority queue?)
 */
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

  /* timer controls rate of scroll/repaint */
  private Timer timer;

  public Ticker()
  {
    marquisStr = new String();
    setMarquisText(Arrays.asList(testStrings));

    int h = getFontMetrics(MARQUIS_FONT).getHeight() + INSET;
    int w = 600;
    
    /* config */
    setMinimumSize(new Dimension(0, h));
    setPreferredSize(new Dimension(w, h));
    setMaximumSize(getPreferredSize());
    setBackground(ColorsAndFonts.GUI_BACKGROUND);
  }

  @Override
  protected void paintComponent(Graphics g)
  {

    
  }

  /**
   clear the marquis
   */
  public void clearMarquisText()
  {
    marquisStr = "";
  }

  /**
   set the marquis source of text to the contents of a list
   @param newsList  list to source Strings from
   */
  public void setMarquisText(List<String> newsList)
  {
    clearMarquisText();
    addMarquisText(newsList);
  }

  /**
   Set the marquis text to a single String
   @param news    String to set marquis to
   */
  public void setMarquisText(String news)
  {
    clearMarquisText();
    addMarquisText(news);
  }

  /**
   add a String to the marquis
   @param news  String to add
   */
  public void addMarquisText(String news)
  {
    marquisStr += news + SPACING;
  }


  /**
   Add a list of Strings to the marquis
   @param newsList  list to source Strings from
   */
  public void addMarquisText(List<String> newsList)
  {
    for(String s : newsList) addMarquisText(s);
  }

  /**
   start marquis scrolling
   */
  public void start()
  {
    timer.start();
  }

  /**
   pause marquis scrolling
   */
  public void pause()
  {
    timer.stop();
  }
}
