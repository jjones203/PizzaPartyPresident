package worldfoodgame.IO.CSVhelpers;

import worldfoodgame.common.EnumCropType;

/**
 * Place for constants needed to generate values when country data missing from csv.
 * @author jessica
 * @version 21-Mar-2015
 */
public interface CountryCSVDefaultData
{
  // median values for countries with data
  static final int WORLD_AVG_AGE = 26;
  static final int WORLD_BIRTH_RATE = 20;
  static final int WORLD_MORTALITY = 8;
  static final int WORLD_MIGRATION = 0;
  static final int WORLD_UNDERNOURISH = 5;
  static final double WORLD_PERCENT_ARABLE = 0.89;

  // need also to check given data = 100%
  static final double WORLD_ORGANIC = 0.00349895;
  static final double WORLD_CONVENTIONAL = 0.99650105;
  static final double WORLD_GMO = 0.0;

  // median tons per person
  static final double WORLD_CORN_PER_CAPITA = 0.1128367783;
  static final double WORLD_WHEAT_PER_CAPITA = 0.1280831007;
  static final double WORLD_RICE_PER_CAPITA = 0.0198223017;
  static final double WORLD_SOY_PER_CAPITA = 0.0018427136;
  static final double WORLD_OTHER_PER_CAPITA = 1.0078726372;
  // corresponding arrays for lookup
  static final EnumCropType[] cropTypes = {EnumCropType.CORN, EnumCropType.WHEAT, EnumCropType.RICE, EnumCropType.SOY, EnumCropType.OTHER_CROPS};
  static final double[] cropTonPerCapita = {WORLD_CORN_PER_CAPITA, WORLD_WHEAT_PER_CAPITA, WORLD_RICE_PER_CAPITA, WORLD_SOY_PER_CAPITA,
                                            WORLD_OTHER_PER_CAPITA};
  
}
