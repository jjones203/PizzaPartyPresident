package gui.regionlooks;

enum OVER_LAYS
{
  DEFAULT,
  PLANTING_ZONE,
  HAPPINESS,
  MONTHLY_RAIL_FALL;
}

/**
 * Created by winston on 1/31/15.
 * <p/>
 * Class used to Generate various regions views. Basically a way to hide all the
 * specifics of this class from the calling context.
 */
public class RegionViewFactory
{
  /* view currently correspond to camera angles */
  private final static RegionView DEFAULT_LOOK = new defaultLook();
  private final static RegionView DEFAULT_WITH_NAME = new RegionNameView(DEFAULT_LOOK, 800);



  private OVER_LAYS currentOverlay;

  public RegionViewFactory()
  {
    this.currentOverlay = OVER_LAYS.DEFAULT;
  }

  public OVER_LAYS getCurrentOverlay()
  {
    return currentOverlay;
  }

  public void setCurrentOverlay(OVER_LAYS currentOverlay)
  {
    this.currentOverlay = currentOverlay;
  }

  public RegionView getBackgroundMapView()
  {
    return DEFAULT_LOOK;
  }

  public RegionView getCloseUpView()
  {
    return DEFAULT_WITH_NAME;
  }

  public RegionView getMediumView()
  {
    return DEFAULT_LOOK;
  }

  public RegionView getLongView()
  {
    return DEFAULT_LOOK;
  }
}
