package gui.displayconverters;

import model.RegionAttributes;

/**
 * Created by winston on 2/6/15.
 */
public class AmericanUniteConverter extends DisplayUnitConverter
{
  @Override
  public String getCurrencySymbol()
  {
    return "$";
  }

  @Override
  public double convertInches(double inches)
  {
    return inches;
  }

  @Override
  public String getInchSymbol()
  {
    return "In.";
  }

  @Override
  public double convertFeet(double feet)
  {
    return feet;
  }

  @Override
  public String getFeetSymbol()
  {
    return "ft.";
  }

  @Override
  public double convertFahrenheit(double temp)
  {
    return temp;
  }

  @Override
  public String getTmpSymbol()
  {
    return "F";
  }


  @Override
  public RegionAttributes convertAttributes(RegionAttributes originalSet)
  {
    return originalSet;
  }
}
