package worldfoodgame.gui.hud.infopanel;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by winston on 3/17/15.
 *
 * creates a label factory that maintains th state of the associated data object.
 */
public class LabelFactory
{
  private Collection<GraphLabel> labels;
  private Collection<Runnable> updates;
  private CountryDataHandler dataHandler;

  public LabelFactory(CountryDataHandler dataHandler)
  {
    this.dataHandler = dataHandler;
    labels = new ArrayList<>();
    updates = new ArrayList<>();
  }

  public GraphLabel getPopulationLabel()
  {
    final GraphLabel label = new GraphLabel(
      "Population",
      dataHandler.population,
      dataHandler.population,
      "##");

    final Runnable update = new Runnable()
    {
      @Override
      public void run()
      {
        label.setValue(dataHandler.population);
      }
    };
    updates.add(update);
    return label;
  }

  public GraphLabel getPopulationControll()
  {
    final GraphLabel popControll = new GraphLabel(
      "pop controll test",
      dataHandler.population,
      dataHandler.population,
      "##",
      null);

    Runnable effect = new Runnable()
    {
      @Override
      public void run()
      {
        dataHandler.population = popControll.getValue();

        for (Runnable run : updates)
        {
          if (this != run) run.run();
        }
      }
    };

    popControll.setEffectRunnable(effect);
    updates.add(effect);

    return popControll;
  }


}
