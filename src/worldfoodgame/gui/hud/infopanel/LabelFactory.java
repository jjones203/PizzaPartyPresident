package worldfoodgame.gui.hud.infopanel;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by winston on 3/17/15.
 * <p/>
 * creates a label factory that maintains th state of the associated data object.
 *
 * todo add the idea of a unite converter.
 */
public class LabelFactory
{
//  private Collection<GraphLabel> labels;
  private Collection<Runnable> updates;
  private CountryDataHandler dataHandler;

  private void updateLabels()
  {
    for (Runnable update: updates) update.run();;
  }
  public LabelFactory(CountryDataHandler dataHandler)
  {
    this.dataHandler = dataHandler;
//    labels = new ArrayList<>();
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
        updateLabels();
      }
    };
    popControll.setEffectRunnable(effect);

    updates.add(new Runnable()
    {
      @Override
      public void run()
      {
        popControll.setValue(dataHandler.population);
      }
    });

    return popControll;
  }

  public GraphLabel getMedianAge()
  {
    final GraphLabel medianAge = new GraphLabel(
      "Median Age",
      dataHandler.medianAge,
      120,
      "# yrs");

    updates.add(new Runnable()
    {
      @Override
      public void run()
      {
        medianAge.setValue(dataHandler.medianAge);
      }
    });

    return medianAge;
  }

  public GraphLabel getBirthRate()
  {
    final GraphLabel birthRate = new GraphLabel(
      "Birth Rate",
      dataHandler.birthRate,
      100,
      "# per thousand"
    );

    updates.add(new Runnable()
    {
      @Override
      public void run()
      {
        birthRate.setValue(dataHandler.birthRate);
      }
    });

    return birthRate;
  }

  public GraphLabel getMortalityRate()
  {
    final GraphLabel mortalityRate = new GraphLabel(
      "Mortality Rate",
      dataHandler.mortalityRate,
      100,
      "# per thousand"
    );

    updates.add(new Runnable()
    {
      @Override
      public void run()
      {
        mortalityRate.setValue(dataHandler.birthRate);
      }
    });

    return mortalityRate;
  }


  public GraphLabel getTotalLand()
  {
    return new GraphLabel(
      "Total Land",
      dataHandler.landTotal,
      dataHandler.landTotal,
      "# sq km");
  }

  public GraphLabel getArableLand()
  {
    return new GraphLabel(
      "Total Land",
      dataHandler.arableOpen,
      dataHandler.arableOpen,
      "# sq km");
  }
}
