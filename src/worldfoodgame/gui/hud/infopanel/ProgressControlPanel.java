package worldfoodgame.gui.hud.infopanel;

import worldfoodgame.gui.ColorsAndFonts;
import worldfoodgame.gui.WorldPresenter;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DecimalFormat;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by winston on 3/23/15.
 * 
 * description:
 * ProgressControlPanel describes the panel through which a user can control the
 * game stepping functions.  By default, the game is paused. If the "run" button
 * is clicked, the game will step a year once every 30 seconds. 
 */
public class ProgressControlPanel extends JPanel implements Observer
{
  private final static int MS_PER_YEAR = 30_000;
 
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
    currentYear, yearRemaining, population, happiness;

  
  /* Game stepping and running objects defined here */
  private Timer gameStepper = new Timer(MS_PER_YEAR, new AbstractAction()
  {
    @Override
    public void actionPerformed(ActionEvent e)
    {
      nextAction.run();
    }
  });

  private Runnable nextAction = new Runnable()
  {
    @Override
    public void run()
    {
      running = false;
      updateLabels();
      
      ProgressControlPanel.this.getRootPane().setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
      
      worldPresenter.stepWorld();

      ProgressControlPanel.this.getRootPane().setCursor(null);
    }
  };
  private boolean running  = false;
  
  private Runnable runGameAction = new Runnable()
  {
    @Override
    public void run()
    {
      if(!running)
      {
        gameStepper.start();
        running = true;
      }
      updateLabels();
    }
  };
  
  private Runnable pauseGameAction = new Runnable()
  {
    @Override
    public void run()
    {
      gameStepper.stop();
      running = false;
      updateLabels();
    }
  };

  private void updateLabels()
  {
    if(running)
    {
      run.setText("running");
      run.setForeground(ColorsAndFonts.GUI_TEXT_HIGHLIGHT);
      pause.setText("pause");
      pause.setForeground(ColorsAndFonts.GUI_TEXT_COLOR);
    }
    else
    {
      run.setText("run");
      run.setForeground(ColorsAndFonts.GUI_TEXT_COLOR);
      pause.setText("paused");
      pause.setForeground(ColorsAndFonts.GUI_TEXT_HIGHLIGHT);
    }
  }

  /**
   Construct a new ProgressControlPanel with a given WorldPresenter
   
   @param worldPresenter  WorldPresenter to construct this panel with.
   @throws HeadlessException
   */
  public ProgressControlPanel(final WorldPresenter worldPresenter) throws HeadlessException
  {
    this.worldPresenter = worldPresenter;
    this.setLayout(new GridLayout(1, 6));
    worldPresenter.addObserver(this);
    JPanel controls = new JPanel();
    controls.setLayout(new GridLayout(1, 3));
    controls.setBackground(ColorsAndFonts.GUI_BACKGROUND);

    setBackground(ColorsAndFonts.GUI_BACKGROUND);
    setBorder(new CompoundBorder(ColorsAndFonts.HEADING_UNDERLINE, new EmptyBorder(3,3,3,3)));


    // Controls
    nextYear = new SingleClickButton("next");
    nextYear.setAction(nextAction);
    controls.add(nextYear);

    run = new SingleClickButton("run");
    run.setAction(runGameAction);
    controls.add(run);

    pause = new SingleClickButton("pause");
    pause.setAction(pauseGameAction);
    controls.add(pause);

    updateLabels();

    // Labels
    currentYear = new NumericalLabel("Current Year", null);
    add(currentYear);

    yearRemaining = new NumericalLabel("Years Remaining", null);
    add(yearRemaining);

    population = new NumericalLabel("World Population", null);
    add(population);

    happiness = new NumericalLabel("Happiness", null);
    add(happiness);

    this.add(controls, BorderLayout.WEST);
    this.update(null, null);
  }

  /**
   Called by the WorldPresent upon update of data/display
   @param o   Obeservable calling the method
   @param arg Optional data carrying Object
   */
  @Override
  public void update(Observable o, Object arg)
  {
    currentYear.setValString(Integer.toString(worldPresenter.getYear()));
    yearRemaining.setValString(Integer.toString(worldPresenter.yearRemaining()));
    population.setValString(popuLationFormatter.format(worldPresenter.getWorldPopulationMil()) + " Mill");
    happiness.setValString("% " + happinessP.format(worldPresenter.getHappinessP() * 100));
  }


  private class NumericalLabel extends JPanel
  {

    private JLabel titleLabel, numericalValue;

    public NumericalLabel(String label, String valAsString)
    {
      //init
      this.titleLabel = new JLabel(label);
      this.numericalValue = new JLabel(valAsString);

      //config
      this.setLayout(new BorderLayout());
      this.setBackground(ColorsAndFonts.GUI_BACKGROUND);

      titleLabel.setFont(ColorsAndFonts.GUI_FONT.deriveFont(11f));
      titleLabel.setForeground(ColorsAndFonts.GUI_TEXT_COLOR);

      numericalValue.setFont(ColorsAndFonts.GUI_FONT.deriveFont(16f));
      numericalValue.setForeground(ColorsAndFonts.GUI_TEXT_COLOR);

      //wire up
      this.add(titleLabel, BorderLayout.NORTH);
      this.add(numericalValue, BorderLayout.CENTER);
    }

    public void setValString(String string)
    {
      numericalValue.setText(string);
    }
  }


  private class SingleClickButton extends JLabel
  {
    private Runnable action;
    
    
    public SingleClickButton(String text)
    {
      super(text);
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
          SingleClickButton.this.setForeground(ColorsAndFonts.GUI_TEXT_COLOR);
          updateLabels();
        }
      });
    }

    void setAction(Runnable action)
    {
      this.action = action;
    }
  }
}
