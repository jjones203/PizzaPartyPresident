package worldfoodgame.model;

public enum EnumContinentNames
{
  N_AMERICA, S_AMERICA, EUROPE, AFRICA, OCEANIA, ASIA, MIDDLE_EAST;
  
  public String toString()
  {
    String name = name();
    String retString;
    
    switch (name)
    {
      case "N_AMERICA":
        retString = "North America";
        break;
      case "S_AMERICA":
        retString = "South America";
        break;
      case "MIDDLE_EAST":
        retString = "Middle East";
        break;
      default:
        String substring = name.substring(1).toLowerCase();
        retString = name.charAt(0)+substring;
        break;
    }
    return retString; 
  }
  
  public static EnumContinentNames findContinentName(String string)
  {
    for (EnumContinentNames continentName:EnumContinentNames.values())
    {
      if (continentName.toString().equalsIgnoreCase(string)) return continentName;
    }
    return null;
  }
  
}
