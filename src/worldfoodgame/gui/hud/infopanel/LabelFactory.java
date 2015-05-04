package worldfoodgame.gui.hud.infopanel;

import worldfoodgame.common.EnumCropType;
import worldfoodgame.common.EnumGrowMethod;
import worldfoodgame.gui.TradeWindow.TradeGraphLabel;
import worldfoodgame.model.Continent;
import worldfoodgame.model.Country;
import worldfoodgame.model.World;
import worldfoodgame.gui.TradeWindow.TradeBar;

import java.awt.Component;
import java.util.ArrayList;
import java.util.Collection;

import javax.imageio.ImageIO;
import javax.swing.JLabel;
import javax.swing.JPanel;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

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
  private BufferedImage image;

  public LabelFactory(CountryDataHandler dataHandler)
  {
    this.dataHandler = dataHandler;
    this.updates = new ArrayList<>();
  }
  
  public CountryDataHandler getDataHandler()
  {
    return dataHandler;
  }

  public void setContinent(Continent continent)
  {
    this.continent = continent;
  }

  public Continent getContinent()
  {
    return continent;
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

  public GraphLabel getGrowMethodLabel(final EnumGrowMethod grow)
  {
    double val;
    if (continent == null)
    {
      val = 0;
    }
    else
    {
      val = continent.getMethodPercentage(World.getWorld().getCurrentYear(), grow);
    }
    if (val < 0) val = 0;

    final GraphLabel methodControll = new GraphLabel(
        grow.toString(),
        val,
        1,
        "% 00.0",
        null);

    methodControll.setEffectRunnable(new Runnable()
    {
      @Override
      public void run()
      {
        if (continent == null)
        {
          //do nothing?
        }
        else
        {
          continent.updateMethodPercentage(World.getWorld().getCurrentYear(), grow, methodControll.getValue());
        }
        updateLabels();
      }
    });

    updates.add(new Runnable()
    {
      @Override
      public void run()
      {
        if (continent == null)
        {
          methodControll.setValue(0);
        }
        else
        {
          methodControll.setValue(continent.getMethodPercentage(World.getWorld().getCurrentYear(), grow));
        }
      }
    });

    return methodControll;
  }

  public GraphLabel getStaticGrowMethodLabel(final EnumGrowMethod grow)
  {
    double val;
    if (continent == null)
    {
      val = 0;
    }
    else
    {
      val = continent.getMethodPercentage(World.getWorld().getCurrentYear(), grow);
    }
    if (val < 0) val = 0;

    final GraphLabel methodControll = new GraphLabel(
        grow.toString(),
        val,
        1,
        "% 00.0");

    updates.add(new Runnable()
    {
      @Override
      public void run()
      {
        if (continent == null)
        {
          methodControll.setValue(0);
        }
        else
        {
          methodControll.setValue(continent.getMethodPercentage(World.getWorld().getCurrentYear(), grow));
        }
      }
    });

    return methodControll;
  }

  public GraphLabel getLandLabel(final EnumCropType type)
  {
    double val = 0;
    int year = World.getWorld().getCurrentYear();
    if (continent != null && continent.getArableLand(year)<= 0)
    {
      val = continent.getCropLand(year, type) / continent.getArableLand(year);
    }

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
        if (continent != null)
        {
          int year = World.getWorld().getCurrentYear();
          continent.updateCropLand(year, type, foodControll.getValue() * continent.getArableLand(year));
        }
        updateLabels();
      }
    });


    updates.add(new Runnable()
    {
      @Override
      public void run()
      {
        if (continent != null)
        {
          int year = World.getWorld().getCurrentYear();
          foodControll.setValue(continent.getCropLand(year, type) / continent.getArableLand(year));
        }
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

  public GraphLabel getStaticLandLabel(final EnumCropType type)
  {
    final GraphLabel foodControll = new GraphLabel(
        type.toString() + " land",
        dataHandler.getCropLand(type),
        dataHandler.getArable(),
        "#,###,### " + "sq");

    updates.add(new Runnable()
    {
      @Override
      public void run()
      {
        foodControll.setValue(dataHandler.getCropLand(type));
      }
    });

    return foodControll;
  }

  public GraphLabel getWaterLabel()
  {
    double val = 0;
    double limit = 0;
    if (continent != null)
    {
      limit = continent.getWaterAllowance() + continent.getRainfall();
      val = limit - continent.getWaterUsage(World.getWorld().getCurrentYear());
    }
    final GraphLabel waterControll = new GraphLabel(
            "Water Remaining",
            val,
            limit,
            "#,###,### gallons");

    updates.add(new Runnable()
    {
      @Override
      public void run()
      {
        if (continent == null)
        {
          waterControll.setValue(0);
        }
        else
        {
          waterControll.setValue(continent.getWaterAllowance() + continent.getRainfall()
                  - continent.getWaterUsage(World.getWorld().getCurrentYear()));
        }
      }
    });

    return waterControll;
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

  public GraphLabel getTradePlayLabel(final EnumCropType type, final TradeBar trade, double limit)
  {
    final GraphLabel foodPlayControl = new GraphLabel(
        type.toString(),
        0,
        limit,
        "#,###,### tons",
        null);

    foodPlayControl.setEffectRunnable(new Runnable()
    {
      @Override
      public void run()
      {
        trade.setCurrentTrade(foodPlayControl.getValue());
        updateLabels();
      }
    });

    updates.add(new Runnable()
    {
      @Override
      public void run()
      {
        foodPlayControl.setValue(trade.getCurrentTrade());
      }
    });

    return foodPlayControl;
  }

  public GraphLabel getTradeContLabel(final EnumCropType type, final TradeBar trade, double limit)
  {
    final GraphLabel foodControl = new GraphLabel(
        type.toString(),
        0,
        limit,
        "#,###,### tons");

    updates.add(new Runnable()
    {
      @Override
      public void run()
      {
        foodControl.setValue(trade.getCurrentTrade());
      }
    });

    return foodControl;
  }

  public TradeGraphLabel getTradeProdLabel(final EnumCropType type)
  {
    final TradeGraphLabel prodLabel = new TradeGraphLabel(
        type.toString(),
        continent.getCropProduction(World.getWorld().getCurrentYear()-1, type),
        continent.getTotalCropNeed(World.getWorld().getCurrentYear()-1, type),
        "#,###,### tons", type
        );

    updates.add(new Runnable()
    {
      @Override
      public void run()
      {
        prodLabel.setValue(continent.getCropProduction(World.getWorld().getCurrentYear()-1, type));
      }
    });

    return prodLabel;
  }

  public GraphLabel getApprovalBar()
  {
    final GraphLabel approvalLab;
    if (continent != null)
    {
      double rating = continent.getApprovalRating();
      approvalLab = new GraphLabel("Your Approval Rating",rating, 1,"% 00.0");
    }
    else
    {
      approvalLab = new GraphLabel("Your Approval Rating", .5, 1,"% 00.0");
    }

    updates.add(new Runnable()
    {
      @Override
      public void run()
      {
        approvalLab.setValue(continent.getApprovalRating());
      }
    });
    return approvalLab;
  }

  
  public GraphLabel getDiplomacyBar()
  {
    final GraphLabel diploLab;
    if (continent != null)
    {
      double rating = continent.getDiplomacyRating();
      diploLab = new GraphLabel("Your Diplomacy Rating",rating,1, "% 00.0" );
    }
    else
    {
      diploLab = new GraphLabel("Your Diplomacy Rating", .5, 1,"% 00.0");
    }
    updates.add(new Runnable()
    {
      @Override
      public void run()
      {
        diploLab.setValue(continent.getDiplomacyRating());
      }
    });
    return diploLab;
  }

  
  public BufferedImage getApprovalRating()
  {
    if(continent != null)
    {
      double rating = continent.getApprovalRating();
      String face = determineFace(rating);
      image = loadImage(face);
    }
    return image;
  }

  public BufferedImage getDiplomacyRating()
  {
    if(continent != null)
    {
      double rating = continent.getApprovalRating();
      String face = determineFace(rating);
      image = loadImage(face);
    }
    return image;
  }
  
  private String determineFace(double rating)
  {
    String expression = "okay.png";

    if (rating<.21)
    {
      expression = "distress.png";
    }
    else if(rating>.2 && rating<.41)
    {
      expression = "upset.png";
    }
    else if(rating>.4 && rating<.61)
    {
      expression = "okay.png";
    }
    else if(rating>.6 && rating<.81)
    {
      expression = "good.png";
    }
    else if(rating>.8)
    {
      expression = "excellent.png";
    }
    return expression;
  }
  
  
  private BufferedImage loadImage(String face)
  {
    try
    {
      String fileLocation = "resources/ratings/"+face;
      image = ImageIO.read(new File(fileLocation));
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
    return image;
  }
  
}
