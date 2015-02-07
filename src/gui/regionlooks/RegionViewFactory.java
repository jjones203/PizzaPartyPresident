package gui.regionlooks;

import gui.Camera;


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
  private final static RegionView PLANTING_VIEW = new PlantingZoneView();
  private final static RegionView HAPPINESS_VIEW = new RegionHappyView();
  private final static RegionView HAPPINESS_WITH_NAME = new RegionNameView(HAPPINESS_VIEW);
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


  public RegionView getViewFromDistance(Camera.CAM_DISTANCE distance)
  {
//    if (distance == Camera.CAM_DISTANCE.LONG) return DEFAULT_LOOK;

    switch (currentOverlay)
    {
      case PLANTING_ZONE:
        if (distance == Camera.CAM_DISTANCE.CLOSE_UP) return new RegionNameView(PLANTING_VIEW);
        return PLANTING_VIEW;

      case HAPPINESS:
        if (distance == Camera.CAM_DISTANCE.CLOSE_UP) return HAPPINESS_WITH_NAME;
        return HAPPINESS_VIEW;

      default:
        if (distance == Camera.CAM_DISTANCE.CLOSE_UP) return new RegionNameView(DEFAULT_LOOK);
        return DEFAULT_LOOK;
    }
  }


  public enum Overlay
  {
    NONE,
    PLANTING_ZONE,
    HAPPINESS,
    MONTHLY_RAIL_FALL
  }
}
