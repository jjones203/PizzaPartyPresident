package IO;

import model.AtomicRegion;
import model.MapPoint;
import model.Region;
import model.RegionAttributes;

import static model.RegionAttributes.*;
import static model.RegionAttributes.PLANTING_ATTRIBUTES.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

/**
 Created by winston on 1/26/15.
 Phase_01
 CS 351 spring 2015
 <p>
 Class to generate random attribute sets.
 will only be used for testing...
 */
public class AttributeGenerator
{

  /* arbitrary */
  private static final double CROP_UNIT_LIMIT = 2000;

  private String[] crops = {
    "corn", "wheat", "grapeNuts",
    "coffee", "bread", "pudding",
    "scones with chocolate chips",
    "anti-rains", "apple sauce", "Soylent green",
    "Soylent blue",
  };

  /*
    Generates something resembling a normal distribution between 0 and limit
   */
  private static double genPosGaussian(Random rand, double limit)
  {
    double r;
    do
    {
      r = (rand.nextGaussian() + 1) / 2;
    } while (r < 0 || r > 1);

    return r * limit;
  }

  public static void stepAttributes(Random random, Collection<Region> world)
  {
    for (Region r : world) mutateAtts(r.getAttributes(), random);
  }

  private static void mutateAtts(RegionAttributes attributes, Random random)
  {
    double differential = 0.05;
    double attVal, change;

    for (PLANTING_ATTRIBUTES att : PLANTING_ATTRIBUTES.values())
    {
      attVal = attributes.getAttribute(att);
      change = random.nextGaussian() * differential;
      attVal = Math.max(0, Math.min(LIMITS.get(att), attVal + change));
      attributes.setAttribute(att, attVal);
    }

    for (String crop : attributes.getAllCrops())
    {
      attVal = attributes.getCropGrowth(crop);
      change = random.nextGaussian() * differential;
      attVal = Math.max(0, Math.min(CROP_UNIT_LIMIT, attVal * (1 + change)));
      attributes.setCrop(crop, attVal);
    }
  }

  public void setRegionAttributes(Region reg, Random rand)
  {
    RegionAttributes attributes = new RegionAttributes();

    for (PLANTING_ATTRIBUTES att : PLANTING_ATTRIBUTES.values())
    {
      switch (att)
      {
        case PLANTING_ZONE:
          attributes.setAttribute(att, genPlantZone(reg));
          break;
        case ANNUAL_RAINFALL:
          attributes.setAttribute(att, LIMITS.get(ANNUAL_RAINFALL) * rand.nextDouble());
          break;
        case AVE_MONTH_TEMP_HI:
          attributes.setAttribute(att, genHiTemp(reg, rand));
          break;
        case AVE_MONTH_TEMP_LO:
          attributes.setAttribute(att, genLowTemp(reg, rand));
          break;
        case COST_OF_CROPS:
        case ELEVATION:
        case HAPPINESS:
        case POPULATION:
        case PROFIT_FROM_CROPS:
        case SOIL_TYPE:
          attributes.setAttribute(att, genPosGaussian(rand, LIMITS.get(att)));
          break;
        default:
          attributes.setAttribute(att, rand.nextDouble());
      }
    }
    setCrops(reg, attributes, rand);
    reg.setAttributes(attributes);
  }

  /*
    Randomly generating crop growth for a region
   */
  private void setCrops(Region reg, RegionAttributes attrs, Random rand)
  {
    for (String crop : crops)
    {
      attrs.setCrop(crop, genPosGaussian(rand, CROP_UNIT_LIMIT));
    }
  }

  private double genHiTemp(Region reg, Random rand)
  {
    return genLowTemp(reg, rand) + 20 + rand.nextDouble() * 5;
  }

  private double genLowTemp(Region reg, Random rand)
  {
    double baseTemp = -65;
    double zoneTempDiff = 10;
    return genPlantZone(reg) * zoneTempDiff
      + rand.nextDouble() * zoneTempDiff - zoneTempDiff / 2 + baseTemp;
  }

  private double genPlantZone(Region reg)
  {
    int numZones = 13;
    double midLat = 0;

    for (MapPoint mp : reg.getPerimeter()) midLat += mp.getLat();

    midLat /= reg.getPerimeter().size();

    midLat += 90; /* shift to bring into natural number land */

    return Math.ceil(midLat / (180.0 / numZones));
  }

}
