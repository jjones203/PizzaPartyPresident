package gui.displayconverters;

import model.RegionAttributes;

import static model.RegionAttributes.PLANTING_ATTRIBUTES;
import static model.RegionAttributes.PLANTING_ATTRIBUTES.*;

/**
 * Created by winston on 2/6/15.
 * <p/>
 * Class is responsible for converting and properly displaying unites of
 * measurements in different systems of measurements. All conventions happen in
 * terms of the model unit system, which uses the customary United States units.
 * <p/>
 * for more info see: http://en.wikipedia.org/wiki/United_States_customary_units
 */
public abstract class DisplayUnitConverter
{
  /**
   * Returns the currency symbol. used in display contexts to show the
   * appropriate currency annotation.
   *
   * @return string representing the currency of the converter.
   */
  public abstract String getCurrencySymbol();

  /**
   * Converts the given measurement in inches into the Converters equivalent.
   *
   * @param inches value in inches
   * @return the converters equivalent inch, returned as a double.
   */
  public abstract double convertInches(double inches);

  /**
   * Returns the appropriate symbol corresponding the the converters 'version'
   * of inch
   *
   * @return string representing the converters unite for inches (eg. mm or in)
   */
  public abstract String getInchSymbol();

  /**
   * Converts the given measurement in feet into the Converters equivalent.
   *
   * @param feet value measured in feet
   * @return the converters equivalent of feet, returned as a double.
   */
  public abstract double convertFeet(double feet);

  /**
   * Returns the appropriate symbol corresponding the the converters 'version'
   * of feet
   *
   * @return string representing the converters unite for feet (eg. m or ft)
   */
  public abstract String getFeetSymbol();

  /**
   * Converts the given measurement in Fahrenheit into the Converters equivalent.
   *
   * @param temp value measured in Fahrenheit
   * @return the converters equivalent of Fahrenheit, returned as a double.
   */
  public abstract double convertFahrenheit(double temp);

  /**
   * Returns the appropriate symbol corresponding the the converters 'version'
   * of Fahrenheit
   *
   * @return string representing the converters unite for Fahrenheit
   * (eg. C° or F°)
   */
  public abstract String getTmpSymbol();


  /**
   * Given a set of attributes this method generates a new set according the
   * the conversion rules defined in the implementing class.
   * <p/>
   * Note, this class creates a new object, it does not mutate the model data!
   * <p/>
   * Note, this is where extensions to the conversion should be added.
   * ie, if currency conversion was to be added it would be done in this method.
   *
   * @param originalSet model unite RegionAttributes object
   * @return converted new RegionAttributes object.
   */
  public RegionAttributes convertAttributes(RegionAttributes originalSet)
  {
    RegionAttributes copy = new RegionAttributes();

    // copies all the crop information.
    for (String cropName : originalSet.getAllCrops())
    {
      copy.setCrop(cropName, originalSet.getCropGrowth(cropName));
    }

    // makes a copy of all remaining data.
    for (PLANTING_ATTRIBUTES att : PLANTING_ATTRIBUTES.values())
    {
      copy.setAttribute(att, originalSet.getAttribute(att));
    }

    // updates only the needed fields with the conversion rules.
    copy.setAttribute(
      ANNUAL_RAINFALL,
      convertInches(originalSet.getAttribute(ANNUAL_RAINFALL))
    );

    copy.setAttribute(
      MONTHLY_RAINFALL,
      convertInches(originalSet.getAttribute(MONTHLY_RAINFALL))
    );

    copy.setAttribute(
      AVE_MONTH_TEMP_HI,
      convertFahrenheit(originalSet.getAttribute(AVE_MONTH_TEMP_HI))
    );

    copy.setAttribute(
      AVE_MONTH_TEMP_LO,
      convertFahrenheit(originalSet.getAttribute(AVE_MONTH_TEMP_LO))
    );

    copy.setAttribute(
      ELEVATION,
      convertFeet(originalSet.getAttribute(ELEVATION))
    );

    return copy;
  }
}
