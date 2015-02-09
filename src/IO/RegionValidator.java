package IO;

import gui.displayconverters.EquirectangularConverter;
import gui.displayconverters.MapConverter;
import org.xml.sax.SAXException;
import model.MapPoint;
import model.Region;

import java.awt.geom.Area;

/**
 * Created by winston on 1/25/15.
 * Phase_01 Utility class to validate regions at load time.
 * CS 351 spring 2015
 *
 */
public class RegionValidator
{
  private static final  MapConverter CONVERTER = new EquirectangularConverter();

  public boolean validate(Region region) throws SAXException
  {
    for (MapPoint mp : region.getPerimeter())
    {
      if (! isValidMapPoint(mp) ) throw new SAXException("Invalid Map Point.");
    }

    // check to make sure all region polygons are simple.
    boolean isSingular = new Area(CONVERTER.regionToPolygon(region)).isSingular();

    if (! isSingular) throw new SAXException("Invalid Region shape");

    return true;
  }


  private boolean isValidMapPoint(MapPoint mapPoint)
  {

    return Math.abs(mapPoint.getLat()) <= 90.00 &&
           Math.abs(mapPoint.getLon()) <= 180.00;

  }
}
