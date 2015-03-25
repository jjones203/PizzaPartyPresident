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
    ROLLOVER_COLOR = Color.red;


  private WorldPresenter worldPresenter;
  private SingleClickButton
    nextYear, run, pause;

  private numbericalLabel
    currentYear, yearRemainng, population, happiness;

  public ProgessControllPanel(WorldPresenter worldPresenter) throws HeadlessException
  {
    this.worldPresenter = worldPresenter;
    this.setLayout(new GridLayout(1, 6));

    JPanel controlls = new JPanel();
    controlls.setLayout(new GridLayout(1, 3));
    controlls.setBackground(ColorsAndFonts.GUI_BACKGROUND);


    nextYear = new SingleClickButton("next");
    controlls.add(nextYear);

    run = new SingleClickButton("run");
    controlls.add(run);

    pause = new SingleClickButton("pause");
    controlls.add(pause);

    // todo change this so that i get the world population form the world presenter.
    currentYear = new numbericalLabel("Current Year",
      Integer.toString(worldPresenter.getYear()));
    add(currentYear);

    yearRemainng = new numbericalLabel("Years Remaining",
      Integer.toString(worldPresenter.yearRemaining()));
    add(yearRemainng);

    population = new numbericalLabel("World Population",
      Double.toString(worldPresenter.getPoppulation()) + "Millon");
    add(population);

    happiness = new numbericalLabel("Happiness",
      "%" + Double.toString(worldPresenter.getHappinessP()));
    add(happiness);

    this.add(controlls, BorderLayout.WEST);
  }


  private class numbericalLabel extends JPanel
  {

    private JLabel titleLabel, numbericalValue;

    public numbericalLabel(String label, String valAsString)
    {
      //init
      this.titleLabel = new JLabel(label);
      this.numbericalValue = new JLabel(valAsString);

      //config
      this.setLayout(new BorderLayout());
      this.setBackground(ColorsAndFonts.GUI_BACKGROUND);

      titleLabel.setFont(ColorsAndFonts.GUI_FONT.deriveFont(12f));
      titleLabel.setForeground(ColorsAndFonts.GUI_TEXT_COLOR);

      numbericalValue.setFont(ColorsAndFonts.GUI_FONT.deriveFont(18f));
      numbericalValue.setForeground(ColorsAndFonts.GUI_TEXT_COLOR);

      //wire up
      this.add(titleLabel, BorderLayout.NORTH);
      this.add(numbericalValue, BorderLayout.CENTER);
    }

    public void setValString(String string)
    {
      numbericalValue.setText(string);
    }
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
          if (action != null)
          {
            action.run();
          }
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
