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
import static model.RegionAttributes.PLANTING_ATTRIBUTES;

/**
 * Created by winston on 2/3/15.
 */
public class InfoPanel extends JPanel implements Observer
{
  private final static Dimension size = new Dimension(1000, 220);
  private MiniViewBox miniViewBox;
  private StatPane attributeStats;
  private StatPane cropStatPane;
  private DisplayUnitConverter converter;
  private WorldPresenter presenter;


  public InfoPanel()
  {
    // init
    miniViewBox = new MiniViewBox("REGION NAME");
    attributeStats = new StatPane("ATTRIBUTES:");
    cropStatPane = new StatPane("CROPS:");

    //config
    this.setLayout(new GridLayout(1, 3));
    this.setMinimumSize(size);
    this.setPreferredSize(size);

    //wire
    this.add(miniViewBox);
    this.add(attributeStats);
    this.add(cropStatPane);


    getInputMap(WHEN_IN_FOCUSED_WINDOW)
      .put(KeyStroke.getKeyStroke("M"), "switchToMetric");
    getActionMap().put("switchToMetric", new AbstractAction()
    {
      @Override
      public void actionPerformed(ActionEvent e)
      {
        System.out.println("switch to metric");

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
        System.out.println("american unites selected");
        setConverter(new AmericanUniteConverter());
        update(null, null);
      }
    });

  }

  public WorldPresenter getPresenter()
  {
    if (presenter == null)
    {
      throw new NullPointerException("World Presenter is null in InfoPanel!");
    }
    return presenter;
  }

  public void setPresenter(WorldPresenter presenter)
  {
    this.presenter = presenter;
    presenter.addObserver(this);
  }

  public DisplayUnitConverter getConverter()
  {
    // defaults to AmericanUniteConverter
    if (converter == null) converter = new AmericanUniteConverter();
    return converter;
  }

  public void setConverter(DisplayUnitConverter converter)
  {
    this.converter = converter;
  }

  public void setTitle(String title)
  {
    miniViewBox.setTitle(title);
  }

  /**
   * Display the Specified Attribute object in the info panel.
   *
   * @param regionAttributes
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
   * Controls the presentation logic for displaying the the soil attributes
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

  private BarPanel getBarPanel(final RegionAttributes attributesSet, PLANTING_ATTRIBUTES att)
  {
    RegionAttributes converted = getConverter().convertAttributes(attributesSet);
    final int FULL_BAR = 1;
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

  private Color getHappyColor(double ratio)
  {
    return ratio < 0.5 ? Color.red : Color.cyan;
  }

  public void clearDisplay()
  {
    miniViewBox.setTitle(" ");
    miniViewBox.setDrawableRegions(null);
    cropStatPane.clearBarPlots();
    attributeStats.clearBarPlots();
  }

  public void displayAllGUIRegions(List<GUIRegion> regions)
  {
    if (regions.size() == 1)
    {
      setTitle(regions.get(0).getName());
      showAttributes(regions.get(0).getRegion().getAttributes());
      miniViewBox.setAlph(0.0f);
    }
    else
    {
      clearDisplay();
      setTitle("SPACIAL SUM:");
      miniViewBox.setAlph(1f);
      if (!presenter.isActivelyDraging())
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
    
    /* for performance checking */
    System.out.println("summing " + numRegions + " regions");

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
