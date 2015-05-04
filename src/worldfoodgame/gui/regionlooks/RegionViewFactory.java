package worldfoodgame.gui.regionlooks;

import worldfoodgame.common.EnumCropType;
import worldfoodgame.gui.ColorsAndFonts;

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
    POPULATION(new PopulationView()),
    MORTALITY(new MortalityRate()),
    MALNUTRITION(new Malnutrition()),
    CAPITAL(new CapitolView()),
    TRADING(new TradingRouteOverlay()),

    PEPPERS(new CropView(
            //CORN(new CropView(
            EnumCropType.CORN,
            ColorsAndFonts.colorConverter.extractHue(ColorsAndFonts.CORN_COLOR))),

    MUSHROOMS(new CropView(
    //SOY(new CropView(
                      EnumCropType.SOY,
        ColorsAndFonts.colorConverter.extractHue(ColorsAndFonts.SOY_COLOR)

    )),

    PINEAPPLE(new CropView(
    //RICE(new CropView(
                       EnumCropType.RICE,
         ColorsAndFonts.colorConverter.extractHue(ColorsAndFonts.RICE_COLOR)

    )),

    TOMATOES(new CropView(
    //WHEAT(new CropView(
                        EnumCropType.WHEAT,
          ColorsAndFonts.colorConverter.extractHue(ColorsAndFonts.WHEAT_COLOR)

    )),

    PEPPERONI(new CropView(
    //OTHER_CROPS(new CropView(
                              EnumCropType.OTHER_CROPS,
                ColorsAndFonts.colorConverter.extractHue(ColorsAndFonts.OTHER_CROP_COLOR)

    )),

    PRECIPITATION(new PrecipitationView()

    ),

    MAX_TMP(new MaxTemp()

    ),

    MIN_TMP(new MinTemp()

    ),

    DAY_TEMP_AVE(new DayTempAve()

    ),

    NIGHT_AVE_TMP(new NightTempAve()

    ),

    CROP_RASTER(new CropRasterLook()

    ),
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


    /**
     @return all Overlays that correspond to LandTile (raster) data
     */
    public static Overlay[] getRasterOverlays()
    {
      return new Overlay[]{
                            /*PRECIPITATION,
                            MAX_TMP,
                            MIN_TMP,
        DAY_TEMP_AVE,
        NIGHT_AVE_TMP,*/
          // only interested in crop raster for milestone 3
        CROP_RASTER};
    }


    /**
     @return  all Overlays that correspond to demographic information
     */
    public static Overlay[] getDemographicOverlays()
    {
      return new Overlay[]{
        
        //HAPPINESS,
        POPULATION,
        //MORTALITY,
        MALNUTRITION,
        //CAPITAL,
        TRADING};
    }


    /**
     @return  all Overlays that correspond to crop dedication by country
     */
    public static Overlay[] getCropDedicationOverlays()
    {
      return new Overlay[]
               {
                 TOMATOES,
                 PINEAPPLE,
                 PEPPERS,
                 MUSHROOMS,
                 PEPPERONI
               };
/*
      return new Overlay[]{
        CORN,
        SOY,
        RICE,
        WHEAT,
        OTHER_CROPS};
*/
      
    }
  
  public String toString()
  {
    if (name() == "CROP_RASTER") return "PLANTING";
    else return name();
  }
 }
}
