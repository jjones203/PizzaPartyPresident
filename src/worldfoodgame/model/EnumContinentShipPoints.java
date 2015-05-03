package worldfoodgame.model;

/**
 * Created by Ken Kressin on 2/5/15.
 * Description:  Holds the continent names and ties their shipping cities and
 * shipping latitude amnd longitude to the continents.
 * Used for calculating trade efficiency between continents controlled by the
 * computer.
 */
public enum EnumContinentShipPoints implements ContinentShipData
{
  N_AMERICA("North America", N_AMERICA_SHIP_POINT, N_AMERICA_SHIP_CITY),
  S_AMERICA("South America", S_AMERICA_SHIP_POINT, S_AMERICA_SHIP_CITY),
  EUROPE("Europe", EUROPE_SHIP_POINT, EUROPE_SHIP_CITY),
  AFRICA("Africa", AFRICA_SHIP_POINT, AFRICA_SHIP_CITY),
  OCEANIA("Oceania", OCEANIA_SHIP_POINT, OCEANIA_SHIP_CITY),
  ASIA("Asia", ASIA_SHIP_POINT, ASIA_SHIP_CITY),
  MIDDLE_EAST("Middle East", MIDDLE_EAST_SHIP_POINT, MIDDLE_EAST_SHIP_CITY);

  public final String name;
  public final MapPoint shipPoint;
  public final String shipCity;

  /**
   * Constructor which connects the continent with it's shipping data.  All the
   * data to be loaded is contained in the data class ContinentShipData.  If you
   * want to change shipping points, change the data in the data class.
   * @param name The String representation of the continent name
   * @param shipPoint This is a MapPoint object holding the latitude and longitude
   *                  of the designated shipping city for the continent.
   * @param shipCity  The String representation of the continent's designated
   *                  shipping city.
   */
  EnumContinentShipPoints(String name, MapPoint shipPoint, String shipCity)
  {
    this.name = name;
    this.shipPoint = shipPoint;
    this.shipCity = shipCity;
  }

  public String toString()
  {
    return name;
  }

  public static final int SIZE = values().length;
}
