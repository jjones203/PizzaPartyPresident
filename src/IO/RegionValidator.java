package IO;

import gui.EquirectangularConverter;
import gui.MapConverter;
import org.xml.sax.SAXException;
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
  private static final  MapConverter CONVERTER = new EquirectangularConverter();

  public boolean validate(Region region) throws SAXException
  {
    for (MapPoint mp : region.getPerimeter())
    {
      if (! isValidMapPoint(mp) ) throw new SAXException("Invalid Map Point.");
    }

    //TODO make sure this is an adequate test...

    boolean isSigular = new Area(CONVERTER.regionToPolygon(region)).isSingular();

    if (! isSigular) throw new SAXException("Invalid Region shape");

    return true;
  }


  private boolean isValidMapPoint(MapPoint mapPoint)
  {
    boolean validCoords = Math.abs(mapPoint.getLat()) <= 90.00 &&
                          Math.abs(mapPoint.getLon()) <= 180.00;

    return validCoords;

  }
}
