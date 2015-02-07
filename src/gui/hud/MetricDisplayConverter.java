package gui.hud;

/**
 * Created by winston on 2/6/15.
 */
public class MetricDisplayConverter extends DisplayUnitConverter
{
  @Override
  public String getCurrencySymbol()
  {
    return "$";
  }

  @Override
  public double convertInches(double inches)
  {
    return inches/0.039370;
  }

  @Override
  public String getInchSymbol()
  {
    return "mm";
  }

  @Override
  public double convertFeet(double feet)
  {
    return feet/3.2808;
  }

  @Override
  public String getFeetSymbol()
  {
    return "m.";
  }

  @Override
  public double convertFahrenheit(double temp)
  {
    return (temp - 32.0) / 1.800;
  }

  @Override
  public String getTmpSymbol()
  {
    return "C";
  }
}
