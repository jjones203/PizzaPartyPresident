package testing.generators;

import model.RegionAttributes;
import model.RegionAttributes.PLANTING_ATTRIBUTES;

import java.util.Random;

/**
 * Created by winston on 1/26/15.
 * Phase_01
 * CS 351 spring 2015
 *
 * Class to generate random attribute sets.
 */
public class AttributeGenerator
{
  private String[] crops = {"corn", "wheat", "grapeNuts", "coffee"};
  private Random rand;

  public AttributeGenerator(Random rand)
  {
    this.rand = rand;
  }

  public RegionAttributes nextAttribute()
  {
    RegionAttributes atts = new RegionAttributes();

    for (PLANTING_ATTRIBUTES att : PLANTING_ATTRIBUTES.values())
    {
      atts.setAttribute(att, rand.nextDouble() * 20);
    }

    for (String crop: crops)
    {
      atts.addCrop(crop, 1.0 / crops.length);
    }
    return atts;
  }
}
