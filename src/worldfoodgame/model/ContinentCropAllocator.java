package worldfoodgame.model;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import worldfoodgame.common.AbstractScenario;
import worldfoodgame.common.EnumCropType;
import worldfoodgame.common.EnumGrowMethod;

public class ContinentCropAllocator
{
  private int year;
  private Continent continent;
  private boolean occurCatastrophe = false;
  private double[] areaToPlant = new double[EnumCropType.SIZE];
  private int[] tilesNeeded = new int[EnumCropType.SIZE];
  
  ContinentCropAllocator(int year, Continent continent)
  {
    this.year = year;
    this.continent = continent;
  }
  
  public void allocateCrops()
  {
    calculateTilesNeeded();
    plantCrops();
    updateProduction();
    updateDeforestation();
  }
  
  public void setOccurCatastrope(boolean occurCatastrophe)
  {
    this.occurCatastrophe = occurCatastrophe;
  }
  
  private void calculateTilesNeeded()
  {
    for (EnumCropType crop:EnumCropType.values())
    {
      double cropLand = continent.getCropLand(year, crop);
      areaToPlant[crop.ordinal()] = cropLand;
      tilesNeeded[crop.ordinal()] = (int) cropLand/100;
    }
  }
  
  private int getTilesNeeded(EnumCropType crop)
  {
    return tilesNeeded[crop.ordinal()];
  }
  
  private void plantCrops()
  {
    Collection<LandTile> continentTiles = continent.getLandTiles();
    Iterator tileItr = continentTiles.iterator();
    int cropIndex = 0;
    // go through crops
    while (tileItr.hasNext() && cropIndex < EnumCropType.SIZE)
    {
      EnumCropType crop = EnumCropType.values()[cropIndex];
      int tilesToPlant = getTilesNeeded(crop);
      // plant as many tiles as needed for crop
      while (tilesToPlant > 0 && tileItr.hasNext())
      {
        LandTile tile = (LandTile) tileItr.next();
        tile.setCurrCrop(crop);
        tilesToPlant--;
      }
      cropIndex++;
    }
    // if tiles left after planting, they should be set to null
    while (tileItr.hasNext())
    {
      LandTile tile = (LandTile) tileItr.next();
      tile.setCurrCrop(null);
    }
  }
  
  private void updateProduction()
  {
    double convPercent = continent.getMethodPercentage(year, EnumGrowMethod.CONVENTIONAL);
    double gmoPercent = continent.getMethodPercentage(year, EnumGrowMethod.GMO);
    double orgPercent = continent.getMethodPercentage(year, EnumGrowMethod.ORGANIC);
    
    for (EnumCropType crop:EnumCropType.values())
    {
      double convYield = continent.getCropYield(year, crop, EnumGrowMethod.CONVENTIONAL);
      double gmoYield = continent.getCropYield(year, crop, EnumGrowMethod.GMO);
      double orgYield = continent.getCropYield(year, crop, EnumGrowMethod.ORGANIC);
      // calculate yield per tile; *100 because tile is 100 sq km
      double yieldPerTile = (convPercent * convYield + gmoPercent * gmoYield + orgPercent * orgYield) * 100;
      
      double production = yieldPerTile * getTilesNeeded(crop);
      continent.setCropProduction(year, crop, production);
    }
  }
  
  /* Sets deforestation to whichever is larger: difference between this year's area planted
   * and year 0 area planted, or last year's deforestation. (Even if you plant less area
   * this year than last year, land can't be re-forested.)
   */
  private void updateDeforestation()
  {
    if (year == AbstractScenario.START_YEAR)
    {
      continent.setDeforestation(year, 0);
      return;
    }
    double startAreaPlanted = continent.getStartAreaPlanted();
    double lastYearDeforested = continent.getDeforestation(year-1);
    double currAreaPlanted = 0;
    for (Double area:areaToPlant)
    {
      currAreaPlanted += area;
    }
    double areaDeforested = currAreaPlanted - startAreaPlanted;
    if (areaDeforested > lastYearDeforested) continent.setDeforestation(year, areaDeforested);
    else continent.setDeforestation(year, lastYearDeforested);
  }
  
}
