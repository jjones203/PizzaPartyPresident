package worldfoodgame.model;

/**
 * Created by Tim on 4/25/15. Currently just holds the
 * player's continent, but just in case we wanted to add
 * more for the player...here's a class. Could hold all
 * of the upgrades "purchased" with planning points or
 * something. Just an idea.
 */
public class Player
{
  private Continent continent;
  private double importBudget; //double?
  private int planningPoints;

  public Player (Continent continent)
  {
    this.continent = continent;
    planningPoints = 0;
    importBudget = 4000000000.0D;
  }

  public double getImportBudget()
  {
    return importBudget;
  }

  public Continent getContinent()
  {
    return continent;
  }
}
