package worldfoodgame.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.TreeMap;

import worldfoodgame.common.CropZoneData.EnumCropZone;
import worldfoodgame.common.EnumCropType;
import worldfoodgame.common.AbstractScenario;

/**
 * CropOptimizer plants all crops for a given country in a given year in an
 * optimal way.
 * @author jessica
 */
public class CropOptimizer
{
  private static final int NUM_CROPS = EnumCropType.SIZE;
  private int year;
  private Country country;
  private ArrayList<CropBin> cropBins;
  private ArrayList<TileYield> tileYields;
  private double[] ctryYields;
  
  /**
   * @param year      year of planting
   * @param country   country to plant
   */
  public CropOptimizer(int year, Country country)
  {
    this.year = year;
    this.country = country;
    cropBins = new ArrayList<CropBin>();
    tileYields = new ArrayList<TileYield>();
    ctryYields = new double[EnumCropType.SIZE];
    for (EnumCropType crop:EnumCropType.values())
    {
      int index = crop.ordinal();
      double yield = country.getCropYield(AbstractScenario.START_YEAR, crop);
      ctryYields[index] = yield;
    }
  }
  
  /**
   * Plant all the crops
   */
  public void optimizeCrops()
  { 
    plantingSetup();
    // plant crops
    for (CropBin bin:cropBins)
    {
      plantCrop(bin);
    }
  }
  
  private void plantingSetup()
  {
    // figure out how many tiles needed for each crop
    for (EnumCropType crop:EnumCropType.values())
    {
      double cropLand = country.getCropLand(year, crop);
      CropBin bin = new CropBin(crop, (int) cropLand/100);
      cropBins.add(bin);
    }
    
    // calculate yield for each tile for each crop
    for (LandTile tile:country.getLandTiles())
    {
      TileYield tYield = new TileYield(tile,country);
      tileYields.add(tYield);
    }
    
    // sort crops by tiles needed, most to least
    Collections.sort(cropBins, Collections.reverseOrder());
  }
  
  /**
   * Plant a given crop
   * @param bin   CropBin with crop to plant and tiles to plant\
   */
  private void plantCrop(CropBin bin)
  { 
    EnumCropType crop = bin.crop;
    int tilesToPlant = bin.tilesNeeded;
    double production = 0;
    Comparator reverseComparator = Collections.reverseOrder(new TileYieldComparator(crop)); 
    Collections.sort(tileYields, reverseComparator);                     // sort tiles by descending yield 
    while (tilesToPlant > 0 && tileYields.isEmpty() == false)            // for top n tiles, where n = tilesNeeded for crop
    {
      TileYield tYield = tileYields.get(0);
      double yield = tYield.yields[crop.ordinal()];   // get the tile's yield for crop
      production += yield;                            // add tile's yield to total produced
      tYield.tile.setCurrCrop(crop);                  // set the tile's crop to this crop
      tileYields.remove(tYield);                      // remove tile's tYield object because tile now NA
      tilesToPlant--;
    }
    // after getting all the tiles we need, set total production for year
    if (year != AbstractScenario.START_YEAR)
    {
        country.setCropProduction(year, crop, production);
    }
  }
  
  /**
   * Class containing a LandTile and the amount that tile will yield of 
   * each crop for the current year (uses tile's currCrop for land use
   * penalty calculation).
   * @author jessica
   */
  private class TileYield
  {
    private LandTile tile;
    private double[] yields;
    
    private TileYield(LandTile tile, Country country)
    {
      this.tile = tile;
      yields = new double[NUM_CROPS];
      for (EnumCropType crop:EnumCropType.values())
      {
        EnumCropZone zone;
        double ctryYield = ctryYields[crop.ordinal()];
        if (crop.equals(EnumCropType.OTHER_CROPS)) zone = tile.rateTileForOtherCrops(country.getOtherCropsData());
        else zone = tile.rateTileForCrop(crop);
        double percentYield = 1;
        // for years after START, calculate percentage of yield depending on zone & prior crop
        if (year != AbstractScenario.START_YEAR)
        {
          percentYield = tile.getTileYieldPercent(crop, zone);
        }
        // for year 0, calculate percentage of yield based on zone only
        else
        {
          switch (zone)
          {
            case IDEAL:
              percentYield = 1;
            case ACCEPTABLE:
              percentYield = 0.6;
            case POOR:
              percentYield = 0.25;
            default:
          }
        }
        yields[crop.ordinal()] = percentYield * ctryYield;
      }
    }
    
  }
  
  /**
   * Comparator for TileYield class; compares based on their yield for the
   * crop given in the comparator's initializer.
   * @author jessica
   */
  private class TileYieldComparator implements Comparator<TileYield>
  {
    EnumCropType crop;
    
    private TileYieldComparator(EnumCropType crop)
    {
      this.crop = crop;
    }

    @Override
    public int compare(TileYield tile1, TileYield tile2)
    {
      double yield1 = tile1.yields[crop.ordinal()];
      double yield2 = tile2.yields[crop.ordinal()];
      double diff = yield1 - yield2;
      if (diff > 0) return 1;
      else if (diff < 0) return -1;
      else return 0;
    }
  }
  
  /**
   * Class that allows us to sort crops by number of tiles needed.
   * Note: this class has a natural ordering that is inconsistent with equals.
   * @author jessica
   */
  private class CropBin implements Comparable<CropBin>
  {
    private EnumCropType crop;
    private int tilesNeeded;
    
    CropBin(EnumCropType crop, int tilesNeeded)
    {
      this.crop = crop;
      this.tilesNeeded = tilesNeeded;
    }

    @Override
    public int compareTo(CropBin bin)
    {
      int diff = this.tilesNeeded - bin.tilesNeeded;
      return diff;
    }
  }
}
