package worldfoodgame.gui.hud.infopanel;

import worldfoodgame.common.EnumCropType;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by winston on 3/17/15.
 * <p/>
 * creates a label factory that maintains the state of the associated data object.
 * <p/>
 * todo add the idea of a unite converter.
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
      dataHandler.population,
      dataHandler.population,
      "##");

    updates.add(new Runnable()
    {
      @Override
      public void run()
      {
        population.setValue(dataHandler.population);
      }
    });
    return population;
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

  public GraphLabel getProductionLabel(final EnumCropType type)
  {
    final GraphLabel productionLabel = new GraphLabel(
      "Production",
      dataHandler.production.get(type),
      dataHandler.production.get(type), //todo choose the right graphical limit
      "# metric Tons"
    );

    updates.add(new Runnable()
    {
      @Override
      public void run()
      {
        productionLabel.setValue(dataHandler.production.get(type));
      }
    });

    return productionLabel;
  }

  public GraphLabel getLandLabel(final EnumCropType type)
  {
    final GraphLabel foodControll = new GraphLabel(
      type.toString() + " land",
      dataHandler.land.get(type),
      dataHandler.getCultivatedLand(),
      "#.## km sq",
      null);

    foodControll.setEffectRunnable(new Runnable()
    {
      @Override
      public void run()
      {
        dataHandler.land.put(type, foodControll.getValue());
        updateLabels();
      }
    });


    updates.add(new Runnable()
    {
      @Override
      public void run()
      {
        foodControll.setValue(dataHandler.land.get(type));
      }
    });

    return foodControll;
  }

  public GraphLabel getOpenLandLabel()
  {
    final GraphLabel openLandLabel = new GraphLabel(
      "Arable Land",
      dataHandler.getOpenLand(),
      dataHandler.getCultivatedLand(),
      "#,###,### km sq");

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
      dataHandler.exports.get(type),
      dataHandler.production.get(type),
      "#,###,### tons");

    updates.add(new Runnable()
    {
      @Override
      public void run()
      {
        exports.setValue(dataHandler.exports.get(type));
      }
    });

    return exports;
  }

  public GraphLabel getImportedLabel(final EnumCropType type)
  {
    final GraphLabel imported = new GraphLabel(
      "Imported",
      dataHandler.imports.get(type),
      dataHandler.production.get(type),
      "#,###,### tons");

    updates.add(new Runnable()
    {
      @Override
      public void run()
      {
        imported.setValue(dataHandler.imports.get(type));
      }
    });

    return  imported;
  }
}
