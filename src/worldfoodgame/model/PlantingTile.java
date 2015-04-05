package worldfoodgame.model;

import worldfoodgame.common.EnumCropType;

/**
 * Created by winston on 3/9/15.
 */
@Deprecated
public class PlantingTile
{
  public double annualHighTemp;
  public double annualLowTemp;

  public double aveDayTemp;
  public double aveNightTemp;

  public boolean isCoastal;
  public double annualPercipitaion;

  public EnumCropType crop;
}
