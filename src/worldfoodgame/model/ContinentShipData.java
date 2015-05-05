package worldfoodgame.model;

/**
 * Created by Ken Kressin on 2/5/15.
 * Description:  Data class for storing continent shipping points and cities.
 * This class will be hooked to EnumContinentNames to lock this data to the
 * continent names.  These shipping points will be used to show trading routes
 * for computer trading.
 */
public interface ContinentShipData
{
  MapPoint N_AMERICA_SHIP_POINT = new MapPoint(-90.07, 29.25);
  String   N_AMERICA_SHIP_CITY  = "New Orleans";

  MapPoint S_AMERICA_SHIP_POINT = new MapPoint(-58.382, -34.604);
  String   S_AMERICA_SHIP_CITY  = "Buenos Aires";

  MapPoint EUROPE_SHIP_POINT = new MapPoint(-0.14, 51.50);
  String   EUROPE_SHIP_CITY  = "London";

  MapPoint AFRICA_SHIP_POINT = new MapPoint(31.23, 30.04);
  String   AFRICA_SHIP_CITY  = "Cairo";

  MapPoint OCEANIA_SHIP_POINT = new MapPoint(151.21, -33.86);
  String   OCEANIA_SHIP_CITY  = "Sydney";

  MapPoint ASIA_SHIP_POINT = new MapPoint(116.41, 39.94);
  String   ASIA_SHIP_CITY  = "Beijing";

  MapPoint MIDDLE_EAST_SHIP_POINT = new MapPoint(51.42, 35.69);
  String MIDDLE_EAST_SHIP_CITY = "Tehran";
}
