package worldfoodgame.gui.hud.infopanel;

import worldfoodgame.common.EnumCropType;
import worldfoodgame.gui.TradeWindow.TradeGraphLabel;
import worldfoodgame.model.Continent;
import worldfoodgame.model.World;

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
  private Continent continent;

  public LabelFactory(CountryDataHandler dataHandler)
  {
    this.dataHandler = dataHandler;
    this.updates = new ArrayList<>();
  }

  public void setContinent(Continent continent)
  {
    this.continent = continent;
  }

  private void updateLabels()
  {
    for (Runnable update : updates) update.run();
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
      dataHandler.getArable(),
      dataHandler.getLandTotal(),
      "#,###,### " + dataHandler.landUnite() + " sq");
  }

  public GraphLabel getProductionLabel(final EnumCropType type)
  {
    final GraphLabel productionLabel = new GraphLabel(
      "Prod",
      dataHandler.getProduction(type),
      dataHandler.getNeed(type),
      "#,###,### tons"
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

  public GraphLabel getNeedLabel(final EnumCropType type)
  {
    final GraphLabel needLabel = new GraphLabel(
      "Need",
      dataHandler.getNeed(type),
      dataHandler.getNeed(type),
      "#,###,### tons"
    );

    updates.add(new Runnable()
    {
      @Override
      public void run()
      {
        needLabel.setValue(dataHandler.getNeed(type));
      }
    });

    return needLabel;
  }

  public GraphLabel getLandLabel(final EnumCropType type)
  {
    double val;
    if (dataHandler.getArable()<= 0) val = 0;
    else val = dataHandler.getCropLand(type) / dataHandler.getArable();

    final GraphLabel foodControll = new GraphLabel(
      type.toString() + " land",
      val,
      1,
      "% 00.0",
      null);

    foodControll.setEffectRunnable(new Runnable()
    {
      @Override
      public void run()
      {
        dataHandler.setLand(type, foodControll.getValue() * dataHandler.getArable());
        updateLabels();
      }
    });


    updates.add(new Runnable()
    {
      @Override
      public void run()
      {
        foodControll.setValue(dataHandler.getCropLand(type) / dataHandler.getArable());
      }
    });

    return foodControll;
  }

  public GraphLabel getOpenLandLabel()
  {
    final GraphLabel openLandLabel = new GraphLabel(
      "Open Land",
      dataHandler.getOpenLand(),
      dataHandler.getArable(),
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
      dataHandler.getNeed(type),
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
      dataHandler.getNeed(type),
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

  public GraphLabel getMalnurished()
  {
    final GraphLabel malnurishedLab = new GraphLabel(
      "Malnourished",
      dataHandler.getUndernourished(),
      1,
      "% 00.0"
    );

    updates.add(new Runnable()
    {
      @Override
      public void run()
      {
        malnurishedLab.setValue(dataHandler.getUndernourished());
      }
    });
    return malnurishedLab;
  }

  public GraphLabel getTradePlayLabel(final EnumCropType type)
  {
    double val;
    if (dataHandler.getProduction(type) <= 0) val = 0;
    else val = dataHandler.getProduction(type);

    final GraphLabel foodControl = new GraphLabel(
            type.toString(),
            0,
            val,
            "#,###,### tons",
            null);

    foodControl.setEffectRunnable(new Runnable()
    {
      @Override
      public void run()
      {
        //dataHandler.setLand(type, foodControl.getValue() * dataHandler.getArable());
        updateLabels();
      }
    });

    updates.add(new Runnable()
    {
      @Override
      public void run()
      {
        System.out.println("Set TradePlayLabel to 0");
        foodControl.setValue(0);
      }
    });

    return foodControl;
  }

  public GraphLabel getTradeContLabel(final EnumCropType type)
  {
    double val;
    if ((dataHandler.getProduction(type) - dataHandler.getNeed(type)) <= 0) val = 0;
    else val = dataHandler.getProduction(type) - dataHandler.getNeed(type);

    final GraphLabel foodControl = new GraphLabel(
            type.toString(),
            0,
            val,
            "#,###,### tons");

    updates.add(new Runnable()
    {
      @Override
      public void run()
      {
        System.out.println("Set TradeContLabel to 0");
        foodControl.setValue(0);
      }
    });

    return foodControl;
  }

  public TradeGraphLabel getTradeProdLabel(final EnumCropType type)
  {
    final TradeGraphLabel prodLabel = new TradeGraphLabel(
            type.toString(),
            continent.getCropProduction(World.getWorld().getCurrentYear(), type),
            continent.getTotalCropNeed(World.getWorld().getCurrentYear(), type),
            "#,###,### tons", type
    );

    updates.add(new Runnable()
    {
      @Override
      public void run()
      {
        prodLabel.setValue(continent.getCropProduction(World.getWorld().getCurrentYear(), type));
      }
    });

    return prodLabel;
  }
}
