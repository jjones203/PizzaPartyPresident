package worldfoodgame.model;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import worldfoodgame.common.EnumCropType;
import worldfoodgame.common.EnumGrowMethod;

public class ContinentCropAllocator
{
  int year;
  Continent continent;
  private boolean occurCatastrophe = false;
  int[] tilesNeeded = new int[EnumCropType.SIZE];
  
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
  }
  
  private void calculateTilesNeeded()
  {
    for (EnumCropType crop:EnumCropType.values())
    {
      double cropLand = continent.getCropLand(year, crop);
      tilesNeeded[crop.ordinal()] = (int) cropLand/100;
    }
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
      int tilesToPlant = tilesNeeded[cropIndex];
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
      
    }
  }
  
}
