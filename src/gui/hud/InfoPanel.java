package gui.hud;

import gui.GUIRegion;
import gui.WorldPresenter;
import gui.displayconverters.AmericanUniteConverter;
import gui.displayconverters.DisplayUnitConverter;
import gui.displayconverters.MetricDisplayConverter;
import gui.regionlooks.PlantingZoneView;
import model.RegionAttributes;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.*;
import java.util.List;

import static gui.ColorsAndFonts.BAR_GRAPH_NEG;
import static gui.ColorsAndFonts.GUI_BACKGROUND;
import static gui.ColorsAndFonts.OCEANS;
import static model.RegionAttributes.PLANTING_ATTRIBUTES;

/**
 * Created by winston on 2/3/15.
 *
 * Class responsible for creating and maintaining all the elements that
 * constitute the info panel. Used for viewing attributes about what is selected
 * in the map, and given feel back on what is selected.
 */
public class InfoPanel extends JPanel implements Observer
{
  /* width = 1 allows panel to be resized intelligently upon container
     instantiation.  Height is important, however, to ensure the child components
     are able to display their info correctly */
  private final static Dimension size = new Dimension(1, 220);
  private MiniViewBox miniViewBox;
  private StatPane attributeStats;
  private StatPane cropStatPane;
  private DisplayUnitConverter converter;
  private WorldPresenter presenter;


  public InfoPanel()
  {
    // init
    miniViewBox = new MiniViewBox(" ");
    attributeStats = new StatPane("ATTRIBUTES:");
    cropStatPane = new StatPane("CROPS:");

    //config
    this.setLayout(new GridLayout(1, 3));
    this.setMinimumSize(size);
    this.setPreferredSize(size);
    this.setBackground(GUI_BACKGROUND);

    //wire
    this.add(miniViewBox);
    this.add(attributeStats);
    this.add(cropStatPane);


    // key binding to switch between different Unite Display objects.
    getInputMap(WHEN_IN_FOCUSED_WINDOW)
      .put(KeyStroke.getKeyStroke("M"), "switchToMetric");
    getActionMap().put("switchToMetric", new AbstractAction()
    {
      @Override
      public void actionPerformed(ActionEvent e)
      {
        setConverter(new MetricDisplayConverter());
        update(null, null);
      }
    });


    getInputMap(WHEN_IN_FOCUSED_WINDOW)
      .put(KeyStroke.getKeyStroke("A"), "switchToAmerican");
    getActionMap().put("switchToAmerican", new AbstractAction()
    {
      @Override
      public void actionPerformed(ActionEvent e)
      {
        setConverter(new AmericanUniteConverter());
        update(null, null);
      }
    });

  }

  /**
   * Retuns the word presenter object, provides some safety if the object has
   * not been set.
   *
   * @return word presenter object.
   */
  private WorldPresenter getPresenter()
  {
    if (presenter == null)
    {
      throw new NullPointerException("World Presenter is null in InfoPanel!");
    }
    return presenter;
  }

  /**
   * Set the corresponding word presenter. This object is used to know what
   * the info panel should be presenting and how.
   * @param presenter to get regions selection information from.
   */
  public void setPresenter(WorldPresenter presenter)
  {
    this.presenter = presenter;
    presenter.addObserver(this);
  }

  /**
   * Returns the current DisplayConverter object.
   */
  public DisplayUnitConverter getConverter()
  {
    // defaults to AmericanUniteConverter
    if (converter == null) converter = new AmericanUniteConverter();
    return converter;
  }

  /**
   * set the Converter object to use with displaying region information.
   */
  public void setConverter(DisplayUnitConverter converter)
  {
    this.converter = converter;
  }

  /**
   * sets the Info panel title lable, generally used to display the name of the
   * selected region.
   * @param title to be displayed in the info panel.
   */
  public void setTitle(String title)
  {
    miniViewBox.setTitle(title);
  }

  /**
   * Display the Specified Attribute object in the info panel. This method
   * delegates and handles clearing the previously displayed information.
   *
   * @param regionAttributes to render in the display.
   */
  public void showAttributes(RegionAttributes regionAttributes)
  {
    attributeStats.clearBarPlots();
    displayAttributes(regionAttributes, attributeStats);
    attributeStats.revalidate();

    cropStatPane.clearBarPlots();
    displayCropState(regionAttributes, cropStatPane);
    cropStatPane.revalidate();
  }

  /**
   * Controls the presentation logic of building up the crop percentages section
   * of the GUI info pane.
   *
   * @param atts     data that will be extracted and displayed.
   * @param statPane GUI element to 'write' to.
   */
  private void displayCropState(RegionAttributes atts, StatPane statPane)
  {
    for (String cropName : atts.getAllCrops())
    {
      BarPanel bp = new BarPanel(
        BAR_GRAPH_NEG,
        atts.getCropP(cropName),
        cropName,
        "%" + String.format("%.2f", atts.getCropP(cropName) * 100)
      );
      statPane.addBar(bp);
    }
  }

  /**
   * Controls the presentation logic for displaying the soil attributes
   * in the info panel for the specified region.
   *
   * @param atts     Attribute set to be displayed.
   * @param statPane GUI element to 'write' to.
   */
  private void displayAttributes(RegionAttributes atts, StatPane statPane)
  {
    if (atts == null)
    {
      System.err.println("atts for region are null.");
      return;
    }

    for (PLANTING_ATTRIBUTES att : PLANTING_ATTRIBUTES.values())
    {
      BarPanel bp = getBarPanel(atts, att);
      statPane.addBar(bp);
    }
  }

  /**
   * long (and ugly) method to handel the tedious logic of displaying each
   * region attribute in the appropriate way, with the appropriate color.
   *<p>
   * ie. $ 2.23 for money, 34.23 F° for temperature etc...
   */
  private BarPanel getBarPanel(final RegionAttributes attributesSet, PLANTING_ATTRIBUTES att)
  {
    // somewhat sensible defaults.
    RegionAttributes converted = getConverter().convertAttributes(attributesSet);
    int FULL_BAR = 1; // TO CREATE A LABEL, overloading the concept of bar.
    String PrimaryLabel = att.toString();
    Color barColor = BAR_GRAPH_NEG;
    double ratio = attributesSet.getAttribute(att) / RegionAttributes.LIMITS.get(att);
    String secondaryLabel = String.format("%.2f", converted.getAttribute(att));

    switch (att)
    {
      case PLANTING_ZONE:
        barColor = PlantingZoneView.getPlantingColor(converted.getAttribute(att));
        ratio = FULL_BAR;
        secondaryLabel = "ZONE: " + (int) (double) converted.getAttribute(att);
        break;

      case PROFIT_FROM_CROPS:
        barColor = Color.green;
        secondaryLabel = getConverter().getCurrencySymbol() + " " + secondaryLabel;
        break;

      case COST_OF_CROPS:
        barColor = Color.red;
        secondaryLabel = getConverter().getCurrencySymbol() + " " + secondaryLabel;
        break;

      case HAPPINESS:
        ratio = converted.getAttribute(att);
        barColor = getHappyColor(ratio);
        secondaryLabel = getHappyLabel(ratio);
        break;

      case ANNUAL_RAINFALL:
        secondaryLabel = secondaryLabel + " " + getConverter().getInchSymbol();
        break;

      case MONTHLY_RAINFALL:
        secondaryLabel = secondaryLabel + " " + getConverter().getInchSymbol();
        break;

      case POPULATION:
        secondaryLabel = "" + (int) (double) converted.getAttribute(att);
        break;

      case AVE_MONTH_TEMP_HI:
        secondaryLabel = secondaryLabel + " " + getConverter().getTmpSymbol();
        barColor = Color.red;
        break;

      case AVE_MONTH_TEMP_LO:
        ratio = Math.abs(ratio);
        secondaryLabel = secondaryLabel + " " + getConverter().getTmpSymbol();
        barColor = BAR_GRAPH_NEG;
        break;

      case ELEVATION:
        secondaryLabel = secondaryLabel + " " + getConverter().getFeetSymbol();
        break;

      case SOIL_TYPE:
        secondaryLabel += " ph";
        break;

      default:
        // no nothing, fall back on the above default values.

    }

    return new BarPanel(barColor, ratio, PrimaryLabel, secondaryLabel);
  }

  /**
   * Convert the happiness double into a string for gui presentation.
   */
  private String getHappyLabel(double ratio)
  {
    if (ratio < 0.25)
    {
      return "DESPONDENT";
    }
    else if (ratio < 0.5)
    {
      return "UNHAPPY";
    }
    else if (ratio < 0.75)
    {
      return "HAPPY";
    }
    else
    {
      return "ECSTATIC";
    }
  }

  /* display color as a function of happiness */
  private Color getHappyColor(double ratio)
  {
    return ratio < 0.5 ? Color.red : Color.cyan;
  }

  /**
   * Resets the info panel, used when no information should be in the Display.
   */
  public void clearDisplay()
  {
    miniViewBox.setTitle(" ");
    miniViewBox.setDrawableRegions(null);
    cropStatPane.clearBarPlots();
    attributeStats.clearBarPlots();
  }

  /**
   * Renders the Stats for the list of regions given. This is also used
   * when only drawing a single region (i.e. length of collection = 1).
   *
   * @param regions List of regions to display state for.
   */
  public void displayAllGUIRegions(List<GUIRegion> regions)
  {
    // single region display logic
    if (regions.size() == 1)
    {
      setTitle(regions.get(0).getName());
      showAttributes(regions.get(0).getRegion().getAttributes());
      miniViewBox.setAlph(0.0f);
    }
    else  // multi region display logic.
    {
      clearDisplay();
      setTitle("SPACIAL SUM:");
      miniViewBox.setAlph(1f);
      if (!presenter.isActivelyDragging()) // delays summation until drag is over.
      {
        showAttributes(sumAttributes(regions));
      }
    }
    miniViewBox.setDrawableRegions(regions);
  }

  /**
   * This method is called whenever the observed object is changed. An
   * application calls an <tt>Observable</tt> object's
   * <code>notifyObservers</code> method to have all the object's
   * observers notified of the change.
   *
   * @param o   the observable object.
   * @param arg an argument passed to the <code>notifyObservers</code>
   */
  @Override
  public void update(Observable o, Object arg)
  {
    List<GUIRegion> activeRegions = getPresenter().getActiveRegions();
    if (activeRegions == null)
    {
      clearDisplay();
    }
    else
    {
      displayAllGUIRegions(activeRegions);
    }
  }


  /*
    returns a RegionAttributes instance representing a collection of regions
    
    The following attributes are averaged:
    Annual rainfall,
    Monthly rainfall,
    Soil type (acidity),
    Average monthly high,
    Average monthly low,
    Elevation,
    Happiness
    
    The following attributes are summed:
    Population
    Cost of crops
    Profit from crops
    All crop growth
    
    The Planting Zone is the median zone of all the regions in the List
   */
  private RegionAttributes sumAttributes(List<GUIRegion> regions)
  {
    /* Planting zone is represented as a median across multiple regions */
    Map<Integer, Integer> zoneMap = new HashMap<>();

    /* init map */
    for (int i = 1; i <= 13; i++)
    {
      zoneMap.put(i, 0);
    }
    
    /* everything else is either a sum or average */
    Map<PLANTING_ATTRIBUTES, Double> attribMap = new HashMap<>();
    
    /* init map */
    for (PLANTING_ATTRIBUTES att : PLANTING_ATTRIBUTES.values())
    {
      attribMap.put(att, .0);
    }
    
    /* crops are summed separately in a map */
    Map<String, Double> cropMap = new HashMap<>();

    int numRegions = regions.size();
    
    for (GUIRegion gr : regions)
    {
      RegionAttributes attribs = gr.getRegion().getAttributes();
      for (PLANTING_ATTRIBUTES att : PLANTING_ATTRIBUTES.values())
      {
        switch (att)
        {
          /* averaged attributes */
          case AVE_MONTH_TEMP_LO:
          case AVE_MONTH_TEMP_HI:
          case ANNUAL_RAINFALL:
          case MONTHLY_RAINFALL:
          case HAPPINESS:
          case ELEVATION:
          case SOIL_TYPE:
            attribMap.put(att,
              attribMap.get(att) + attribs.getAttribute(att) / numRegions);
            break;
          
          /* summed attributes */
          case PROFIT_FROM_CROPS:
          case COST_OF_CROPS:
          case POPULATION:
            attribMap.put(att,
              attribMap.get(att) + attribs.getAttribute(att));
            break;
          
          /* median'd attributes */
          case PLANTING_ZONE:
            Integer zone = attribs.getAttribute(att).intValue();
            zoneMap.put(zone, zoneMap.get(zone) + 1);
            break;
          default:
            /* never reached */
            System.err.println("Problems in Attribute Summation");
            break;
        }
      }

      /* sum the crops */
      for (String crop : attribs.getAllCrops())
      {
        if (!cropMap.containsKey(crop))
        {
          cropMap.put(crop, attribs.getCropGrowth(crop));
        }
        else
        {
          cropMap.put(crop, cropMap.get(crop) + attribs.getCropGrowth(crop));
        }
      }
    }

    /* find the most common planting zone */
    Integer maxKey = 1, maxVal = 0;

    for (Integer i : zoneMap.keySet())
    {
      Integer current = zoneMap.get(i);
      if (current > maxVal)
      {
        maxVal = current;
        maxKey = i;
      }
    }

    /* build the new attribute set */
    RegionAttributes rAtts = new RegionAttributes();
    for (PLANTING_ATTRIBUTES att : PLANTING_ATTRIBUTES.values())
    {
      /* make sure to use the median value from zoneMap, NOT the value in
         attribMap (still 0, probably) */
      if (att == PLANTING_ATTRIBUTES.PLANTING_ZONE)
      {
        rAtts.setAttribute(att, maxKey);
      }
      else
      {
        rAtts.setAttribute(att, attribMap.get(att));
      }
    }

    for (String crop : cropMap.keySet()) rAtts.setCrop(crop, cropMap.get(crop));

    return rAtts;
  }
}
