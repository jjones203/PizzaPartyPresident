package worldfoodgame.model;


/**
 * Place for storing data on crops' growing conditions.
 * @author jessica
 *
 * Added data on water usage per crop (number of gallons per square km)
 */
public interface CropClimateData
{
  int RICE_MAX_TEMP = 45;
  int RICE_MIN_TEMP = 10;
  int RICE_DAY_TEMP = 35;
  int RICE_NIGHT_TEMP = 20;
  int RICE_MAX_RAIN = 400;
  int RICE_MIN_RAIN = 70;
  double RICE_WATER_USE = 264276959;
  
  int WHEAT_MAX_TEMP = 45; 
  int WHEAT_MIN_TEMP = -20;
  int WHEAT_DAY_TEMP = 20;
  int WHEAT_NIGHT_TEMP = 10;
  int WHEAT_MAX_RAIN = 78;
  int WHEAT_MIN_RAIN = 25;
  double WHEAT_WATER_USE = 140847000;
  
  int CORN_MAX_TEMP = 45; 
  int CORN_MIN_TEMP = 0;
  int CORN_DAY_TEMP = 29;
  int CORN_NIGHT_TEMP = 20;
  int CORN_MAX_RAIN = 100;
  int CORN_MIN_RAIN = 60;
  double CORN_WATER_USE = 86485000;
  
  int SOY_MAX_TEMP = 45; 
  int SOY_MIN_TEMP = 7;
  int SOY_DAY_TEMP = 35;
  int SOY_NIGHT_TEMP = 30;
  int SOY_MAX_RAIN = 70;
  int SOY_MIN_RAIN = 45;
  double SOY_WATER_USE = 140965608;
  
}
