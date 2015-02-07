package IO.XMLparsers;

import model.Region;
import org.xml.sax.Locator;

import java.util.List;

/**
 * Created by winston on 1/27/15.
 * Phase_01
 * CS 351 spring 2015
 */
@Deprecated
public interface RegionParser
{
  List<Region> getRegionList();

  Locator getLocator();
}
