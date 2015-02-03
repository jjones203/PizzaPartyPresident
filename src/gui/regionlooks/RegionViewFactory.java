package gui.regionlooks;

/**
 * Created by winston on 1/31/15.
 * <p/>
 * Class used to Generate various regions views. Basically a way to hide all the
 * specifics of this class from the calling context.
 */
public class RegionViewFactory
{
  /* view currently correspond to camera angles */
  private final static RegionView LONG = new defaultLook();
  private final static RegionView MEDUIM = new RegionNameView(LONG, 8000);
  private final static RegionView CLOSE_UP = new RegionNameView(LONG, 800);

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
