package IO.validators;

import gui.MapConverter;
import model.MapPoint;
import model.Region;

import java.awt.geom.Area;
import java.awt.geom.Line2D;
import java.util.List;

/**
 * Created by winston on 1/25/15.
 * Phase_01 Utility class to validate regions at load time.
 * CS 351 spring 2015
 */
public class RegionValidator
{
  private MapConverter converter; // TODO init this somewhere

  public boolean isValid(Region region)
  {
    for (MapPoint mp : region.getPerimeter())
    {
      if (! isValidMapPoint(mp) ) return false;
    }

    //TODO make sure this is an adequate test...
    /*
     */
    return  new Area(converter.regionToPolygon(region)).isSingular() ;
  }


  private boolean isValidMapPoint(MapPoint mapPoint)
  {
    boolean validCoords = Math.abs(mapPoint.getLat()) <= 90.00 &&
                          Math.abs(mapPoint.getLon()) <= 180.00;

    return validCoords;

  }
}
