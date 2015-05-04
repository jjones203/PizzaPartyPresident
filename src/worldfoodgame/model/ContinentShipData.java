package worldfoodgame.model;

/**
 * Created by Ken Kressin on 2/5/15.
 * Description:  Data class for storing continent shipping points and cities.
 * This class will be hooked to EnumContinentNames to lock this data to the
 * continent names.
 */
public interface ContinentShipData
{
  MapPoint N_AMERICA_SHIP_POINT = new MapPoint(29.25, -90.07);
  String   N_AMERICA_SHIP_CITY  = "New Orleans";

  MapPoint S_AMERICA_SHIP_POINT = new MapPoint(-34.604, -58.382);
  String   S_AMERICA_SHIP_CITY  = "Buenos Aires";

  MapPoint EUROPE_SHIP_POINT = new MapPoint(51.50, -0.14);
  String   EUROPE_SHIP_CITY  = "London";

  MapPoint AFRICA_SHIP_POINT = new MapPoint(30.04, 31.23);
  String   AFRICA_SHIP_CITY  = "Cairo";

  MapPoint OCEANIA_SHIP_POINT = new MapPoint(-33.86, 151.21);
  String   OCEANIA_SHIP_CITY  = "Sydney";

  MapPoint ASIA_SHIP_POINT = new MapPoint(39.94, 116.41);
  String   ASIA_SHIP_CITY  = "Beijing";

  MapPoint MIDDLE_EAST_SHIP_POINT = new MapPoint(35.69, 51.42);
  String MIDDLE_EAST_SHIP_CITY = "Tehran";
}
