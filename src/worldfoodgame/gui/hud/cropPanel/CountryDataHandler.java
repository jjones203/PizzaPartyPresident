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

  private HashMap<EnumCropType,Double> production = new HashMap<>();
  private HashMap<EnumCropType,Double> imports = new HashMap<>();
  private HashMap<EnumCropType,Double> exports = new HashMap<>();
  private HashMap<EnumCropType,Double> land = new HashMap<>();

  private CountryDataHandler()
  {
    // to keep things secret/safe.
  }


  public double getCultivatedLand()
  {
    double sum = 0;

    for (EnumCropType type : EnumCropType.values())
    {
      sum += getLand(type);
    }
    return sum;
  }


  public double getProduction(EnumCropType type)
  {
    return production.get(type);
  }

  public double getImports(EnumCropType type)
  {
    return imports.get(type);
  }

  public double getExport(EnumCropType type)
  {
    return exports.get(type);
  }

  public double getLand(EnumCropType type)
  {
    return land.get(type);
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

    dataHandler.landArea = 652860;
    dataHandler.arableOpen = 1652018;


    return  dataHandler;
  }
//

}
