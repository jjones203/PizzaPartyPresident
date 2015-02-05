package testing.generators;

import model.Region;
import model.RegionAttributes;
import model.RegionAttributes.PLANTING_ATTRIBUTES;

import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.util.ArrayList;
import java.util.List;
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
    RegionAttributes attribs = new RegionAttributes();

    for(PLANTING_ATTRIBUTES att : PLANTING_ATTRIBUTES.values())
    {
      switch(att)
      {
        case PLANTING_ZONE:
          setPlantingZoneAttribute(reg, attribs, rand);
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

  }


  public static void main(String[] args)
  {
    long seed = 45;
    AttributeGenerator attGen = new AttributeGenerator(new Random(seed));

    System.out.println(attGen.nextAttributeSet());
    System.out.println(attGen.nextAttributeSet());
  }
}
