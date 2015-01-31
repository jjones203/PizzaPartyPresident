package gui.regionlooks;

/**
 * Created by winston on 1/31/15.
 */
public class RegionViewFactory
{
  private final static RegionView LONG = new RegionHappyView();
  private final static RegionView MEDUIM = new defaultLook();
  private final static RegionView CLOSE_UP = new RegionNameView(MEDUIM, 4000);


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
