package worldfoodgame.gui.hud.infopanel;

import worldfoodgame.common.EnumCropType;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by winston on 3/17/15.
 * <p/>
 * creates a label factory that maintains the state of the associated data object.
 *
 * to get a label, call the corresponding getter method.
 * <p/>
 */
public class LabelFactory
{
  private Collection<Runnable> updates;
  private CountryDataHandler dataHandler;

  private void updateLabels()
  {
    for (Runnable update : updates) update.run();
  }

  public LabelFactory(CountryDataHandler dataHandler)
  {
    this.dataHandler = dataHandler;
    this.updates = new ArrayList<>();
  }

  public GraphLabel getPopulationLabel()
  {
    final GraphLabel population = new GraphLabel(
      "Population",
      dataHandler.getPopulation() / 1_000_000.0,
      dataHandler.getPopulation() / 1_000_000.0,
      "#,###.00 million");

    updates.add(new Runnable()
    {
      @Override
      public void run()
      {
        population.setValue(dataHandler.getPopulation() / 1_000_000.0);
      }
    });
    return population;
  }


  public GraphLabel getMedianAge()
  {
    final GraphLabel medianAge = new GraphLabel(
      "Median Age",
      dataHandler.getMedianAge(),
      120,
      "# yrs");

    updates.add(new Runnable()
    {
      @Override
      public void run()
      {
        medianAge.setValue(dataHandler.getMedianAge());
      }
    });

    return medianAge;
  }

  public GraphLabel getBirthRate()
  {
    final GraphLabel birthRate = new GraphLabel(
      "Birth Rate",
      dataHandler.getBirthRate(),
      100,
      "# per thousand"
    );

    updates.add(new Runnable()
    {
      @Override
      public void run()
      {
        birthRate.setValue(dataHandler.getBirthRate());
      }
    });

    return birthRate;
  }

  public GraphLabel getMortalityRate()
  {
    final GraphLabel mortalityRate = new GraphLabel(
      "Mortality Rate",
      dataHandler.getMortalityRate(),
      100,
      "# per thousand"
    );

    updates.add(new Runnable()
    {
      @Override
      public void run()
      {
        mortalityRate.setValue(dataHandler.getBirthRate());
      }
    });

    return mortalityRate;
  }


  public GraphLabel getTotalLand()
  {
    return new GraphLabel(
      "Total Land",
      dataHandler.getLandTotal(),
      dataHandler.getLandTotal(),
      "#,###,### " + dataHandler.landUnite() + " sq");
  }

  public GraphLabel getArableLand()
  {
    return new GraphLabel(
      "Arable Land",
      dataHandler.getArableOpen(),
      dataHandler.getLandTotal(),
      "#,###,### " + dataHandler.landUnite() + " sq");
  }

  public GraphLabel getProductionLabel(final EnumCropType type)
  {
    final GraphLabel productionLabel = new GraphLabel(
      "Production",
      dataHandler.getProduction(type),
      dataHandler.getProduction(type), //todo choose the right graphical limit
      "# metric Tons"
    );

    updates.add(new Runnable()
    {
      @Override
      public void run()
      {
        productionLabel.setValue(dataHandler.getProduction(type));
      }
    });

    return productionLabel;
  }

  public GraphLabel getLandLabel(final EnumCropType type)
  {
    final GraphLabel foodControll = new GraphLabel(
      type.toString() + " land",
      dataHandler.getLand(type) / dataHandler.getArableOpen(), // this is a percent value that is returned.
      1,
      "% #.#",
      null);

    foodControll.setEffectRunnable(new Runnable()
    {
      @Override
      public void run()
      {
        dataHandler.setLand(type, foodControll.getValue() * dataHandler.getArableOpen());
        updateLabels();
      }
    });


    updates.add(new Runnable()
    {
      @Override
      public void run()
      {
        foodControll.setValue(dataHandler.getLand(type) / dataHandler.getArableOpen());
      }
    });

    return foodControll;
  }

  public GraphLabel getOpenLandLabel()
  {
    final GraphLabel openLandLabel = new GraphLabel(
      "Open Land",
      dataHandler.getOpenLand(),
      dataHandler.getArableOpen(),
      "#,###,### " + dataHandler.landUnite() + " sq");

    updates.add(new Runnable()
    {
      @Override
      public void run()
      {
        openLandLabel.setValue(dataHandler.getOpenLand());
      }
    });

    return openLandLabel;
  }

  public GraphLabel getExportedLabel(final EnumCropType type)
  {
    final GraphLabel exports = new GraphLabel(
      "Exported",
      dataHandler.getExports(type),
      dataHandler.getProduction(type),
      "#,###,### tons");

    updates.add(new Runnable()
    {
      @Override
      public void run()
      {
        exports.setValue(dataHandler.getExports(type));
      }
    });

    return exports;
  }

  public GraphLabel getImportedLabel(final EnumCropType type)
  {
    final GraphLabel imported = new GraphLabel(
      "Imported",
      dataHandler.getImports(type),
      dataHandler.getProduction(type),
      "#,###,### tons");

    updates.add(new Runnable()
    {
      @Override
      public void run()
      {
        imported.setValue(dataHandler.getImports(type));
      }
    });

    return  imported;
  }
}
