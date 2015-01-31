package gui.regionlooks;

import model.Region;

/**
 * Created by winston on 1/31/15.
 */
public class RegionViewFactory
{
  private final static RegionView LONG = new defaultLook();
  private final static RegionView MEDUIM = new defaultLook();
  private final static RegionView CLOSE_UP = new RegionNameView(MEDUIM, 4000);

  public RegionView getBackgroundMapView()
  {
    return MEDUIM;
  }

  public RegionView getCloseUpView()
  {
    return CLOSE_UP;
  }

  public RegionView getMediumView()
  {
    return MEDUIM;
  }

  public RegionView getLongView()
  {
    return LONG;
  }
}
