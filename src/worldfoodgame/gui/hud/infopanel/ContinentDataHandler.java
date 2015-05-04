package worldfoodgame.gui.hud.infopanel;

import worldfoodgame.common.EnumCropType;
import worldfoodgame.model.Continent;
import worldfoodgame.model.World;

/**
 * @author jessica
 * @version May 3, 2015
 */
public class ContinentDataHandler extends CountryDataHandler
{
  private Continent continent;
  private int year;
  
  public ContinentDataHandler(Continent continent, int year)
  {
    this.continent = continent;
    this.year = year;
  }

  @Override
  public String getName()
  {
    return continent.toString();
  }

  @Override
  public double getPopulation()
  {
    return continent.getPopulation(year);
  }

  @Deprecated
  @Override
  public double getMedianAge()
  {
    // TODO Auto-generated method stub
    return 0;
  }

  @Deprecated
  @Override
  public double getBirthRate()
  {
    // TODO Auto-generated method stub
    return 0;
  }

  @Deprecated
  @Override
  public double getMortalityRate()
  {
    // TODO Auto-generated method stub
    return 0;
  }

  @Deprecated
  @Override
  public double getMigrationRate()
  {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public double getUndernourished()
  {
    return continent.getUndernourished(year);
  }

  @Override
  public double getLandTotal()
  {
    return continent.getTotalLand();
  }

  @Override
  public double getArable()
  {
    return continent.getArableLand(year);
  }

  @Override
  public double getProduction(EnumCropType type)
  {
    return continent.getCropProduction(year, type);
  }

  @Override
  public double getImports(EnumCropType type)
  {
    return continent.getCropImport(year, type);
  }

  @Override
  public double getExports(EnumCropType type)
  {
    return continent.getCropExport(year, type);
  }

  @Override
  public double getCropLand(EnumCropType type)
  {
    return continent.getCropLand(year, type);
  }

  @Override
  public void setLand(EnumCropType type, double kilom)
  {
    double val = activeConverter.convert2ModelSpace(kilom);
    continent.updateCropLand(year, type, val);
  }

  @Override
  public double getNeed(EnumCropType type)
  {
    return continent.getTotalCropNeed(year, type);
  }

  @Override
  public double getOpenLand()
  {
    return continent.getArableLandUnused(year);
  }

}
