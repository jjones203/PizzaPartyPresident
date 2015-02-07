package gui.hud;

import gui.WorldPresenter;

import javax.swing.*;

/**
 @author david
 created: 2015-02-05

 description: */
public class TopPanel extends JPanel
{
 
 WorldPresenter presenter;
 private DatePanel datePanel;
 
 
 public TopPanel(WorldPresenter presenter)
 {
  this.presenter = presenter;
  datePanel = new DatePanel(presenter);

  
 }
 
 
 
}
