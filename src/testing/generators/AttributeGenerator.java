package testing.generators;

import model.MapPoint;
import model.Region;
import model.RegionAttributes;
import model.RegionAttributes.PLANTING_ATTRIBUTES;
import static model.RegionAttributes.PLANTING_ATTRIBUTES.*;

import java.util.Collection;
import java.util.Random;

/**
 * Created by winston on 1/26/15.
 * Phase_01
 * CS 351 spring 2015
 * <p>
 * Class to generate random attribute sets.
 * will only be used for testing...
 */
public class AttributeGenerator
{
  

  
  private String[] crops = {
      "corn", "wheat", "grapeNuts",
//      "coffee", "bread", "pudding",
//      "scones with chocolate chips",
//      "anti-rasins", "apple sauce", "Soylent green",
      "Soylent blue",
  };
  private Random rand;

  public AttributeGenerator()
  {
    this(new Random());
  }

  public AttributeGenerator(Random rand)
  {
    this.rand = rand;
  }

  public RegionAttributes nextAttributeSet()
  {
    RegionAttributes atts = new RegionAttributes();

    for (PLANTING_ATTRIBUTES att : PLANTING_ATTRIBUTES.values())
    {
      atts.setAttribute(att, rand.nextDouble() * 20);
    }

    for (String crop : crops)
    {
      atts.addCrop(crop, 1.0 / crops.length);
    }
    return atts;
  }
  
  public void setRegionAttributes(Region reg, Random rand)
  {
    RegionAttributes attribs = reg.getAttributes();

    for(PLANTING_ATTRIBUTES att : PLANTING_ATTRIBUTES.values())
    {
      switch(att)
      {
        case PLANTING_ZONE:
          setPlantingZoneAttribute(reg, attribs, rand);
          break;
        case ANNUAL_RAINFALL:
          break;
        case AVE_MONTH_TEMP_HI:
          break;
        case AVE_MONTH_TEMP_LO:
          break;
        case COST_OF_CROPS:
          break;
        case ELEVATION:
          break;
        case HAPPINESS:
          break;
        case POPULATION:
          break;
        case PROFIT_FROM_CROPS:
          break;
        case SOIL_TYPE:
          break;
        default:
          attribs.setAttribute(att, rand.nextDouble());
      }
    }
  }

  private void setPlantingZoneAttribute(Region reg, RegionAttributes attribs, Random rand)
  {
    int numZones = 13;
    double midLat = 0;
    
    for(MapPoint mp : reg.getPerimeter()) midLat += mp.getLat();
    
    midLat /= reg.getPerimeter().size();
    
    midLat += 90; /* shift to bring into natural number land */
    
    double zone =  midLat / (180.0/numZones) + rand.nextDouble() * 1.2 - .6;
    
    attribs.setAttribute(PLANTING_ZONE, zone);
  }


  public static void main(String[] args)
  {
    long seed = 45;
    AttributeGenerator attGen = new AttributeGenerator(new Random(seed));

    System.out.println(attGen.nextAttributeSet());
    System.out.println(attGen.nextAttributeSet());
  }

  public static void stepAttributes(Random random, Collection<Region> world)
  {
    for (Region r : world) mutateAtts(r.getAttributes(), random);
  }

  private static void mutateAtts(RegionAttributes attributes, Random random)
  {
    final double mutator = 0.10;
    double tmpVal;

    tmpVal = attributes.getAttribute(HAPPINESS);
    attributes.setAttribute(
      HAPPINESS,
      tmpVal +=  tmpVal * (random.nextBoolean() ? -mutator: mutator));


    tmpVal = attributes.getAttribute(POPULATION);
    attributes.setAttribute(
      POPULATION,
      tmpVal +=  tmpVal * (random.nextBoolean() ? -mutator: mutator));
  }


}
