package worldfoodgame.gui.regionlooks;

/**
 * Created by winston on 1/31/15.
 * <p/>
 * Class used to Generate various regions views. Basically a way to hide all the
 * specifics of this class from the calling context.
 */
public class RegionViewFactory
{

  /* view currently correspond to camera angles */
  private final static RegionView BG_VIEW = new backGroundLook();
  private final static RegionView DEFAULT_LOOK = new DefaultLook();
  private final static RegionView PLANTING_VIEW = new PlantingZoneView();
  private final static RegionView HAPPINESS_VIEW = new RegionHappyView();
  private final static RegionView RAIN_VIEW = new RainView();
  private Overlay currentOverlay;

  /**
   * Constructor for class. World Presenter class relies on this object for
   * region presentation logic.
   */
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
    return BG_VIEW;
  }



  //todo update overlays
  public enum Overlay
  {
    NONE(new DefaultLook()),
    HAPPINESS(new RegionHappyView()),
    CAPITAL(new CapitolView()),
    POPULATION(new PopulationView()),
    PERCIPITATION(new PrecipitationView()),
    MAX_TMP(new MaxTemp()),
    MIN_TMP(new MinTemp()),
    CROP_RASTER(new CropRasterLook()),
    MORTALITY(new MortalityRate()),
    ;


    /* class constructor and definition */

    private RegionView regionView;
    Overlay(RegionView regionView)
    {
      this.regionView = regionView;
    }

    public RegionView getRegionView()
    {
      return regionView;
    }

    public void setRegionView(RegionView regionView)
    {
      this.regionView = regionView;
    }
  }
}
