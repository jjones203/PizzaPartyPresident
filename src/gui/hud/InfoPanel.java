package gui.hud;

import gui.GUIRegion;
import gui.WorldPresenter;
import gui.regionlooks.PlantingZoneView;
import model.RegionAttributes;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.geom.Area;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import static gui.ColorsAndFonts.BAR_GRAPH_NEG;
import static model.RegionAttributes.PLANTING_ATTRIBUTES;

/**
 * Created by winston on 2/3/15.
 */
public class InfoPanel extends JPanel implements Observer
{
  private MiniViewBox miniViewBox;
  private StatPane attributeStats;
  private StatPane cropStatPane;
  private DisplayUnitConverter converter;
  private Dimension size = new Dimension(1000, 220);
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

  public void showAttributes(RegionAttributes regionAttributes)
  {
    attributeStats.clearBarPlots();
    displayAttributes(regionAttributes, attributeStats);
    attributeStats.revalidate();

    cropStatPane.clearBarPlots();
    diplayCropState(regionAttributes, cropStatPane);
    cropStatPane.revalidate();
  }

  /**
   * Controls the presentation logic of building up the crop percentages section
   * of the GUI info pane.
   *
   * @param atts     data that will be extracted and displayed.
   * @param statPane GUI element to 'write' to.
   */
  private void diplayCropState(RegionAttributes atts, StatPane statPane)
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
    String Primarylable = att.toString();
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
        secondaryLabel = secondaryLabel + " " + getConverter().getIncheSymbol();
        break;

      case MONTHLY_RAINFALL:
        secondaryLabel = secondaryLabel + " " + getConverter().getIncheSymbol();
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

    return new BarPanel(barColor, ratio, Primarylable, secondaryLabel);
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
    }
    else
    {
      setTitle("HI DAVID!");
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
      return;
    }
    else displayAllGUIRegions(activeRegions);
  }
}
