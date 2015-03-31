package worldfoodgame.gui.regionlooks;

import worldfoodgame.gui.GUIRegion;
import worldfoodgame.model.World;

import java.awt.*;

/**
 * Created by winston on 3/31/15.
 */
public class PopulationView implements RegionView
{
  private static PopulationView populationView;

  public static PopulationView getPopulationView()
  {
    if (populationView == null) populationView = new PopulationView();
    return populationView;
  }

  private PopulationView()
  {
  }


  @Override
  public void draw(Graphics g, GUIRegion gRegion)
  {
    int population = gRegion.getRegion()
      .getCountry().getPopulation(World.getWorld().getCurrentYear());

    float popRatio = population / 10_000_000;

    if (popRatio > 1)
    {
      System.err.println("popration is no good");
    }
    else
    {
      g.setColor(new Color(1f, 1f-popRatio, 1f-popRatio));
      g.fillPolygon(gRegion.getPoly());
    }
  }
}
