package worldfoodgame.gui.regionlooks;

import worldfoodgame.common.EnumCropType;
import worldfoodgame.gui.ColorsAndFonts;
import worldfoodgame.model.TradingOptimizer;

import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

/**
 @author david
 created: 2015-04-06

 description:
 TradingRouteOverlay takes advantage of the RasterViz abstract class to draw
 an animation showing the trade connections for each crop for a given year.
 */
public class TradingRouteOverlay extends RasterViz
{
 private static List<TradingOptimizer.TradePair>[] tradePairs = new ArrayList[]{
   new ArrayList(), new ArrayList(), new ArrayList(), new ArrayList(), new ArrayList()
 };

 private final double step = 0.02d;
 private double currentStep = 0d;
 private double maxStep = 1;

 /* constants used to scale the width of the stroke representing the amount traded */
 private static final double MAX_TRADE_EXP = 8;
 private static final double MIN_TRADE_EXP = 3;
 private static double MAX_TRADE_AMNT = Math.pow(10, MAX_TRADE_EXP);
 private static double MIN_TRADE_AMNT = Math.pow(10, MIN_TRADE_EXP);
 
 private static float MAX_STROKE_WIDTH = 10;


 /**
  This always calls down to makeImage() for animation effects.
  @return next frame of trading animation
  */
 @Override
 public BufferedImage getRasterImage()
 {
  return makeImage();
 }

 /**
  Currently always steps the image into the next frame of the animation, could
  be changed later
  @return next frame of trading animation
  */
 @Override
 protected BufferedImage makeImage()
 {
  double next = currentStep + step;
  currentStep = next > maxStep ? 0 : next; /* loop stepping value */
  
  bufferedImage = new BufferedImage(IMG_WIDTH, IMG_HEIGHT, BufferedImage.TYPE_INT_ARGB);

  Graphics2D g2 = (Graphics2D) bufferedImage.getGraphics();
  
  /* make it kinda pretty, scale the context appropriately */
  g2.getRenderingHints().put(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
  g2.translate(IMG_WIDTH / 2, IMG_HEIGHT / 2);
  
  for(EnumCropType crop : EnumCropType.values())
  {
   g2.setColor(ColorsAndFonts.CROP_COLORS[crop.ordinal()]);
   for(TradingOptimizer.TradePair pair : tradePairs[crop.ordinal()])
   {
    drawPair(pair, g2);
   }
  }
  return bufferedImage;
 }

 /* draw a section of the line between the two countries in the TradePair
    based on the current step in the animation  */
 private void drawPair(TradingOptimizer.TradePair pair, Graphics2D g2)
 {
  Point2D expPoint = getPoint(pair.exporter.getCapitolLocation());
  Point2D impPoint = getPoint(pair.importer.getCapitolLocation());
  
  double endX = expPoint.getX() + (impPoint.getX() - expPoint.getX())*currentStep;
  double endY = expPoint.getY() + (impPoint.getY() - expPoint.getY())*currentStep;
  
  Line2D line = new Line2D.Double(expPoint.getX(), expPoint.getY(), endX, endY);
  
  /* limit the amount traded to reasonable values */
  double amount = Math.min(MAX_TRADE_AMNT, Math.max(MIN_TRADE_AMNT, pair.getAmountTraded()));
  
  g2.setStroke(new BasicStroke(getTradeStroke(amount), BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND));
  g2.draw(line);
 }

 /* return a float value representing the width of a stroke based on an amount traded */
 private float getTradeStroke(double amount)
 {
  return (float) ((Math.log10(amount) - MIN_TRADE_EXP)/(MAX_TRADE_EXP-MIN_TRADE_EXP)) * MAX_STROKE_WIDTH + 1;
 }

 /**
  Update the static class variables holding the TradePairs.  Should be called
  with each step of the world to keep information accurate
  @param pairs Array of TradePair lists to use for Overlay
  */
 public static void updateTrades(List<TradingOptimizer.TradePair>[] pairs)
 {
  if (pairs != null)
  {
   if (pairs.length != EnumCropType.SIZE)
   {
    System.err.println("Bad trade list sent to TradingRouteOverlay");
   }
   tradePairs = pairs;
  }
  else
  {
   /* make empty lists */
   for(List<TradingOptimizer.TradePair> l : pairs) l = new ArrayList<>();
  }
 }

 public boolean animationDone()
 {
  return currentStep >= maxStep;
 }
}
