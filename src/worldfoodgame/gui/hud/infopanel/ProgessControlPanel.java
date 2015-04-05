package worldfoodgame.gui.hud.infopanel;

import worldfoodgame.gui.ColorsAndFonts;
import worldfoodgame.gui.WorldPresenter;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DecimalFormat;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by winston on 3/23/15.
 */
public class ProgessControlPanel extends JPanel implements Observer
{

  private static DecimalFormat
    happinessP = new DecimalFormat("#.00"),
    popuLationFormatter = new DecimalFormat("0.0");
  private static Color
    DEFAULT_FONT_COL = ColorsAndFonts.GUI_TEXT_COLOR,
    ROLLOVER_COLOR = Color.red;


  private WorldPresenter worldPresenter;
  private SingleClickButton
    nextYear, run, pause;

  private NumericalLabel
    currentYear, yearRemainng, population, happiness;

  private Runnable worldStepper = new Runnable()
  {
    @Override
    public void run()
    {
      ProgessControlPanel.this.getRootPane().setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

      worldPresenter.stepWorld();

      ProgessControlPanel.this.getRootPane().setCursor(null);
    }
  };

  public ProgessControlPanel(final WorldPresenter worldPresenter) throws HeadlessException
  {
    this.worldPresenter = worldPresenter;
    this.setLayout(new GridLayout(1, 6));
    worldPresenter.addObserver(this);
    JPanel controlls = new JPanel();
    controlls.setLayout(new GridLayout(1, 3));
    controlls.setBackground(ColorsAndFonts.GUI_BACKGROUND);

    setBackground(ColorsAndFonts.GUI_BACKGROUND);
    setBorder(new CompoundBorder(ColorsAndFonts.HEADING_UNDERLINE, new EmptyBorder(3,3,3,3)));


    // Controlls
    nextYear = new SingleClickButton("next");
    nextYear.setAction(worldStepper);
    controlls.add(nextYear);

    run = new SingleClickButton("run");
    controlls.add(run);

    pause = new SingleClickButton("pause");
    controlls.add(pause);


    // Labels
    currentYear = new NumericalLabel("Current Year", null);
    add(currentYear);

    yearRemainng = new NumericalLabel("Years Remaining", null);
    add(yearRemainng);

    population = new NumericalLabel("World Population", null);
    add(population);

    happiness = new NumericalLabel("Happiness", null);
    add(happiness);

    this.add(controlls, BorderLayout.WEST);
    this.update(null, null);
  }

  @Override
  public void update(Observable o, Object arg)
  {
    currentYear.setValString(Integer.toString(worldPresenter.getYear()));
    yearRemainng.setValString(Integer.toString(worldPresenter.yearRemaining()));
    population.setValString(popuLationFormatter.format(worldPresenter.getWorldPopulationMil()) + " Mill");
    happiness.setValString("% " + happinessP.format(worldPresenter.getHappinessP() * 100));
  }


  private class NumericalLabel extends JPanel
  {

    private JLabel titleLabel, numbericalValue;

    public NumericalLabel(String label, String valAsString)
    {
      //init
      this.titleLabel = new JLabel(label);
      this.numbericalValue = new JLabel(valAsString);

      //config
      this.setLayout(new BorderLayout());
      this.setBackground(ColorsAndFonts.GUI_BACKGROUND);

      titleLabel.setFont(ColorsAndFonts.GUI_FONT.deriveFont(11f));
      titleLabel.setForeground(ColorsAndFonts.GUI_TEXT_COLOR);

      numbericalValue.setFont(ColorsAndFonts.GUI_FONT.deriveFont(16f));
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

    void setAction(Runnable action)
    {
      this.action = action;
    }
  }

}
