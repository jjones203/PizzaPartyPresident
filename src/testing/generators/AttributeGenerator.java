package testing.generators;

import model.RegionAttributes;
import model.RegionAttributes.PLANTING_ATTRIBUTES;

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
      "coffee", "bread", "pudding",
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

  public static void main(String[] args)
  {
    long seed = 45;
    AttributeGenerator attGen = new AttributeGenerator(new Random(seed));

    System.out.println(attGen.nextAttributeSet());
    System.out.println(attGen.nextAttributeSet());
  }
}
