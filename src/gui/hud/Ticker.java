package gui.hud;

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
 
 private WorldPresenter presenter;

 private final static String[] testStrings = 
 {
   "Cost of Soylent Green Is Rising As Birthrates Fall In China",
   "Chocolate Chips Outlawed As Scone Ingredient; Scone Farmers Rejoice", 
   "Lorem Ipsum Dolor Sit Amet"
 };
 
 private List<String> marquis;

 private Timer timer;

 public Ticker(WorldPresenter presenter)
 {
  this.presenter = presenter;
  marquis = new ArrayList<>();
  marquis.addAll(Arrays.asList(testStrings));
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
