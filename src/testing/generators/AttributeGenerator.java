package testing.generators;

import model.Region;
import model.RegionAttributes;
import model.RegionAttributes.PLANTING_ATTRIBUTES;

import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.util.ArrayList;
import java.util.Collection;
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
  
  private static List<Path2D> plantingLines = generatePlantingZones();
  

  private static List<Path2D> generatePlantingZones()
  {
    Random r = new Random();
    List<Path2D> divisions = new ArrayList<>();
    
    
    int numZones = 13;
    double latRange = 180;
    double latShift = 90;
    double lonRange = 360;
    double lonShift = 180;
    double lonStep = 2;
    
    double varRange = 20;
    double varShift = 10;
    
    double startLat;
    double lat, lon;
    for (int zoneNum = 1; zoneNum < numZones; zoneNum++)
    {
      Path2D zone = new Path2D.Double();
      lat = zoneNum * lonRange + r.nextDouble() * varRange - varShift;
      
      
      
      for(lon = 0; lon < lonRange; lon += r.nextDouble()*lonStep)
      {
        if(lon <= 0) zone.moveTo(0, lat);
        else
        {
          /* line to next random lat lon, within a set variance */
        }
        
      }
      
    }
    
    
    
    return null;
  }

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
  
  public void setRegionAttributes(Region r)
  {
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
    final double mutator = 1.10;
    double tmpVal;

    tmpVal = attributes.getAttribute(PLANTING_ATTRIBUTES.HAPPINESS);
    attributes.setAttribute(
      PLANTING_ATTRIBUTES.HAPPINESS,
      tmpVal *= random.nextBoolean() ? -mutator: mutator);


    tmpVal = attributes.getAttribute(PLANTING_ATTRIBUTES.POPULATION);
    attributes.setAttribute(
      PLANTING_ATTRIBUTES.POPULATION,
      tmpVal *= random.nextBoolean() ? -mutator : mutator);
  }


}
