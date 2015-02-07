package gui.hud;

import gui.ColorsAndFonts;
import gui.WorldPresenter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

/**
 @author david
 created: 2015-02-05

 description: */
public class TopPanel extends JPanel implements Observer
{

 private static final int PADDING = 8;


 private WorldPresenter presenter;
 private DatePanel datePanel;
 private Ticker ticker;
 private GroupLayout layout;
 
 public TopPanel(WorldPresenter presenter)
 {
  this.presenter = presenter;
  datePanel = new DatePanel();
  datePanel.setDate(presenter.getWorldDate());
  ticker = new Ticker();
  
  setBackground(ColorsAndFonts.GUI_BACKGROUND);
//  setBorder(BorderFactory.createCompoundBorder(
//    BorderFactory.createEmptyBorder(2,2,2,2),
//    BorderFactory.createLineBorder(ColorsAndFonts.GUI_TEXT_COLOR, 1)
//    ));
  
  initLayout();
  int prefH = datePanel.getPreferredSize().height + PADDING*2;
  int prefW = datePanel.getPreferredSize().width + ticker.getPreferredSize().width+PADDING*2;
  setMinimumSize(datePanel.getMinimumSize());
  setPreferredSize(new Dimension(prefW, prefH));
  
 }

 private void initLayout()
 {
  layout = new GroupLayout(this);
  setLayout(layout);

  layout.setAutoCreateGaps(true);

  layout.setHorizontalGroup(
    layout.createSequentialGroup()
      .addComponent(ticker, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
      .addComponent(datePanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
  );
  
  layout.setVerticalGroup(
    layout.createParallelGroup(GroupLayout.Alignment.BASELINE, false)
    .addComponent(ticker, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
    .addComponent(datePanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
  );
 }

 public static void main(String[] args)
 {
  final JFrame frame = new JFrame();
  frame.setContentPane(new TopPanel(null));
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


 @Override
 public void update(Observable o, Object arg)
 {
  datePanel.setDate(presenter.getWorldDate());
  
  /* update ticker text */
 }
}
