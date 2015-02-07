package gui.hud;

import model.RegionAttributes;

import static model.RegionAttributes.PLANTING_ATTRIBUTES;
import static model.RegionAttributes.PLANTING_ATTRIBUTES.*;

/**
 * Created by winston on 2/6/15.
 */
public abstract class DisplayUnitConverter
{
  public abstract String getCurrencySymbol();

  public abstract double convertInches(double inches);
  public abstract String getInchSymbol();

  public abstract double convertFeet(double feet);
  public abstract String getFeetSymbol();

  public abstract double convertFahrenheit(double temp);
  public abstract String getTmpSymbol();


  public RegionAttributes convertAttributes(RegionAttributes originalSet)
  {
    RegionAttributes copy = new RegionAttributes();

    // copies all the crop information.
    for (String cropName : originalSet.getAllCrops())
    {
      copy.setCrop(cropName, originalSet.getCropGrowth(cropName));
    }


   for (PLANTING_ATTRIBUTES att : PLANTING_ATTRIBUTES.values())
   {
     copy.setAttribute(att, originalSet.getAttribute(att));
   }

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
