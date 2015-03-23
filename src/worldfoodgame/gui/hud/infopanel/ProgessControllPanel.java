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

  private JPanel yearLabel;

  public ProgessControllPanel(WorldPresenter worldPresenter) throws HeadlessException
  {
    this.worldPresenter = worldPresenter;
    this.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));

    JPanel controlls = new JPanel();
    controlls.setLayout(new GridLayout(1,3));
    this.add(controlls, BorderLayout.WEST);

    nextYear = new SingleClickButton("next");
    controlls.add(nextYear);

    run = new SingleClickButton("run");
    controlls.add(nextYear);

    pause = new SingleClickButton("pause");
    controlls.add(pause);

    // todo change this so that i get the world population form the world presenter.
    yearLabel = getNumbericalLabel("World Population", "15");
    add(yearLabel);
  }


  private JPanel getNumbericalLabel(String label, String formatedNum)
  {
    JPanel jPanel = new JPanel();
    jPanel.setLayout(new BorderLayout());
    jPanel.setBackground(ColorsAndFonts.GUI_BACKGROUND);

    JLabel titleLabel = new JLabel(label);
    titleLabel.setFont(ColorsAndFonts.GUI_FONT.deriveFont(12f));
//    titleLabel.setBackground(ColorsAndFonts.GUI_BACKGROUND);

    jPanel.add(titleLabel, BorderLayout.NORTH);

    JLabel numbericalValue = new JLabel(formatedNum);
    jPanel.add(numbericalValue, BorderLayout.CENTER);

    return jPanel;
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

  public static void main(String[] args)
  {
    ProgessControllPanel pcp = new ProgessControllPanel(null);

    JFrame jFrame = new JFrame();

    jFrame.add(pcp);
    jFrame.pack();
    jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    jFrame.setVisible(true);
  }
}
