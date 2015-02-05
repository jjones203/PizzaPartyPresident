package gui.regionlooks;

import gui.Camera;
import model.Region;


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
  private final static RegionView PLANTING_VIEW = new PlantingZoneView();
  private final static RegionView HAPPINESS_VIEW = new RegionHappyView();

  private Overlay currentOverlay;


  public RegionViewFactory()
  {
    this.currentOverlay = Overlay.NONE;
  }

  public Overlay getCurrentOverlay()
  {
    return currentOverlay;
  }

  public void setCurrentOverlay(Overlay currentOverlay)
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
    switch (currentOverlay)
    {
      case PLANTING_ZONE:
        return PLANTING_VIEW;

      default:
        return DEFAULT_LOOK;
    }
  }

  public RegionView getViewFromDistance(Camera.CAM_DISTANCE distance)
  {
    switch (currentOverlay)
    {
      case PLANTING_ZONE:
        return PLANTING_VIEW;

      case HAPPINESS:
        return HAPPINESS_VIEW;

      default:
        if (distance == Camera.CAM_DISTANCE.LONG) return DEFAULT_LOOK;
        else return DEFAULT_WITH_NAME;
    }
  }

  public RegionView getLongView()
  {
    return DEFAULT_LOOK;
  }

  public enum Overlay
  {
    NONE,
    PLANTING_ZONE,
    HAPPINESS,
    MONTHLY_RAIL_FALL;
  }
}
