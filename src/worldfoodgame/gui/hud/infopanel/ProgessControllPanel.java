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
public class ProgessControllPanel extends JPanel
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
    this.setLayout(new GridLayout(1, 6));

    JPanel controlls = new JPanel();
    controlls.setLayout(new GridLayout(1,3));
    controlls.setBackground(ColorsAndFonts.GUI_BACKGROUND);


    nextYear = new SingleClickButton("next");
    controlls.add(nextYear);

    run = new SingleClickButton("run");
    controlls.add(run);

    pause = new SingleClickButton("pause");
    controlls.add(pause);

    // todo change this so that i get the world population form the world presenter.
    add(getNumbericalLabel("Current Year", "2015"));
    add(getNumbericalLabel("Years Remaining", "20"));
    yearLabel = getNumbericalLabel("World Population", "8 billion");
    add(yearLabel);
    add(getNumbericalLabel("Happiness", "% 45.2"));

    this.add(controlls, BorderLayout.WEST);
  }


  private JPanel getNumbericalLabel(String label, String formatedNum)
  {
    JPanel jPanel = new JPanel();
    jPanel.setLayout(new BorderLayout());
    jPanel.setBackground(ColorsAndFonts.GUI_BACKGROUND);


    JLabel titleLabel = new JLabel(label);
    titleLabel.setFont(ColorsAndFonts.GUI_FONT.deriveFont(12f));
    titleLabel.setForeground(ColorsAndFonts.GUI_TEXT_COLOR);
//    titleLabel.setBackground(ColorsAndFonts.GUI_BACKGROUND);

    jPanel.add(titleLabel, BorderLayout.NORTH);

    JLabel numbericalValue = new JLabel(formatedNum);
    numbericalValue.setFont(ColorsAndFonts.GUI_FONT.deriveFont(18f));
    numbericalValue.setForeground(ColorsAndFonts.GUI_TEXT_COLOR);
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
      this.setForeground(ColorsAndFonts.GUI_TEXT_COLOR);

      addMouseListener(new MouseAdapter()
      {
        @Override
        public void mouseClicked(MouseEvent e)
        {
          if (action != null) action.run();
          else
          {
            System.err.println("action not set on Single click button");
          }
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
    jFrame.setSize(900, 60);
    jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    jFrame.setVisible(true);
  }
}
