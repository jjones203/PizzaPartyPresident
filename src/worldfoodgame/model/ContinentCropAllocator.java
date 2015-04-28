package worldfoodgame.model;

import worldfoodgame.common.EnumCropType;

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
  
  private void calculateTilesNeeded()
  {
    for (EnumCropType crop:EnumCropType.values())
    {
      double cropLand = continent.getCropLand(year, crop);
      tilesNeeded[crop.ordinal()] = (int) cropLand/100;
    }
  }
  
  private void plantCrop(EnumCropType crop, LandTile tile)
  {
    
  }
  
}
