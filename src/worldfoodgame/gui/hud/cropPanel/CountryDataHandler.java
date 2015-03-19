package worldfoodgame.gui.hud.cropPanel;

import worldfoodgame.common.EnumCropType;

import java.util.HashMap;

/**
 * Created by winston on 3/13/15.
 * <p/>
 * Stores and handles country data interactions for a given country at a given
 * year.
 */
public class CountryDataHandler
{
  String name;
  public double
    population, medianAge, birthRate,
    mortalityRate, migrationRate, undernourished,
    landArea, arableOpen;

  public HashMap<EnumCropType,Double> production = new HashMap<>();
         HashMap<EnumCropType,Double> imports = new HashMap<>();
         HashMap<EnumCropType,Double> exports = new HashMap<>();
         HashMap<EnumCropType,Double> land = new HashMap<>();

  private CountryDataHandler()
  {
    // to keep things secret/safe.
  }


  public double getCultivatedLand()
  {
    double sum = 0;

    for (EnumCropType type : EnumCropType.values())
    {
      sum += land.get(type);
    }
    return sum;
  }




    public static CountryDataHandler getTestData()
  {
    CountryDataHandler dataHandler = new CountryDataHandler();

    dataHandler.name = "Afghanistan";
    dataHandler.population = 31281000;
    dataHandler.medianAge = 16;
    dataHandler.birthRate = 35;
    dataHandler.mortalityRate = 8;
    dataHandler.migrationRate = -3;
    dataHandler.undernourished = 24.00; // percent

    dataHandler.production.put(EnumCropType.CORN, 300120.0);
    dataHandler.production.put(EnumCropType.WHEAT, 3388000.0);
    dataHandler.production.put(EnumCropType.RICE, 448224.0);
    dataHandler.production.put(EnumCropType.SOY, 0.0);
    dataHandler.production.put(EnumCropType.OTHER_CROPS, 2602689.0);

    dataHandler.imports.put(EnumCropType.CORN, 45.0);
    dataHandler.imports.put(EnumCropType.WHEAT, 0.0);
    dataHandler.imports.put(EnumCropType.RICE, 0.0);
    dataHandler.imports.put(EnumCropType.SOY, 0.0);
    dataHandler.imports.put(EnumCropType.OTHER_CROPS, 273980.0);

    dataHandler.exports.put(EnumCropType.CORN, 2885.0);
    dataHandler.exports.put(EnumCropType.WHEAT, 1758691.0);
    dataHandler.exports.put(EnumCropType.RICE, 73110.0);
    dataHandler.exports.put(EnumCropType.SOY, 0.0);
    dataHandler.exports.put(EnumCropType.OTHER_CROPS, 980216.0);

    dataHandler.land.put(EnumCropType.CORN, 1830000.0);
    dataHandler.land.put(EnumCropType.WHEAT, 22320000.0);
    dataHandler.land.put(EnumCropType.RICE, 2100000.0);
    dataHandler.land.put(EnumCropType.SOY, 0.0);
    dataHandler.land.put(EnumCropType.OTHER_CROPS, 72871130.0);

    dataHandler.landArea = 2.001123413E8;
    dataHandler.arableOpen = 1652018;


    return  dataHandler;
  }

  public void setland(EnumCropType type, double percent)
  {
    land.put(type, percent);
  }

  public double getOpenLand()
  {
    return landArea - getCultivatedLand();
  }


}
