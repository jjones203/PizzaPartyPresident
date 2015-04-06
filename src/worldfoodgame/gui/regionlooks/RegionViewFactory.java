package worldfoodgame.gui.regionlooks;

import worldfoodgame.common.EnumCropType;

/**
 * Created by winston on 1/31/15.
 * <p/>
 * Class used to Generate various regions views. Basically a way to hide all the
 * specifics of this class from the calling context.
 */
public class RegionViewFactory
{

  /* view currently correspond to camera angles */
  private final static RegionView BG_VIEW = new BackGroundLook();
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


  /**
   * This class defines the name hook for registering overlays with the gui.
   */
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
    DAY_TEMP_AVE(new DayTempAve()),
    NIGHT_AVE_TMP(new NightTempAve()),
    CORN(new CropView(EnumCropType.CORN,(float)0.3)),
    SOY(new CropView(EnumCropType.SOY,(float)0.5)),
    RICE(new CropView(EnumCropType.RICE,(float)0.9)),
    WHEAT(new CropView(EnumCropType.WHEAT,(float)0.1)),
    OTHER_CROPS(new CropView(EnumCropType.OTHER_CROPS,(float)0.25)),
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
