package worldfoodgame.gui.hud.infopanel;

import worldfoodgame.gui.ColorsAndFonts;
import worldfoodgame.gui.WorldPresenter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Created by winston on 3/23/15.
 */
public class ProgessControllPanel extends JFrame
{
  private static Color
    DEFAULT_FONT_COL = ColorsAndFonts.GUI_TEXT_COLOR,
    ROLLOVER_COLOR = Color.red,
    SELECTED_COL = Color.yellow;

  private WorldPresenter worldPresenter;
  private SingleClickButton
    nextYear, run, pause;

  public ProgessControllPanel(WorldPresenter worldPresenter) throws HeadlessException
  {
    this.worldPresenter = worldPresenter;
    this.setLayout(new BorderLayout());

    JPanel controlls = new JPanel();
    this.add(controlls, BorderLayout.EAST);

    nextYear = new SingleClickButton("next year");
    controlls.add(nextYear);

    run = new SingleClickButton("run");
    controlls.add(nextYear);

    pause = new SingleClickButton("pause");
    controlls.add(pause);


  }

  private class SingleClickButton extends JLabel
  {
    private String label;
    private Runnable action;

    public SingleClickButton(String text)
    {
      super(text);
      this.label = text;
      addMouseListener(new MouseAdapter()
      {
        @Override
        public void mouseClicked(MouseEvent e)
        {
          action.run();
        }

        @Override
        public void mouseEntered(MouseEvent e)
        {
          SingleClickButton.this.setForeground(ROLLOVER_COLOR);
        }

        @Override
        public void mouseExited(MouseEvent e)
        {
          SingleClickButton.this.setForeground(DEFAULT_FONT_COL);
        }
      });
    }



  }
}
