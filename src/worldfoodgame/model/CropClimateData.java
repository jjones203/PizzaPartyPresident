package worldfoodgame.model;


/**
 * Place for storing data on crops' growing conditions, etc.
 * @author jessica
 *Edited by Ken Kressin - adding crop water use data.
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
  double RICE_WATER_USE = 264276959; //amount of water (gals) per km2
  
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
  
  double ANNUAL_TONS_PER_PERSON = 25;  // same for each ingredient

  double CONVENTIONAL_YIELD_PERCENT = 1;
  double ORGANIC_YIELD_PERCENT = 0.8;             // organic cultivation produces 80% as much as conventional
  double GMO_YIELD_PERCENT = 1.25;                // gmo cultivation produces 125% as much as conventional
  double ANNUAL_YIELD_DECLINE = 0.002;            // annual decline in yield to account for climate change

}
