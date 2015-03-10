package worldfoodgame.orthograph;

import worldfoodgame.gui.displayconverters.MapConverter;
import worldfoodgame.model.MapPoint;
import worldfoodgame.model.Region;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import static java.lang.Math.*;

/**
 @author david
 created: 2015-02-11

 description: */
public class OrthographicConverter extends MapConverter
{

 public static final double EARTH_RAD = 600; /* arbitrary */
 
 private double height = 1; /* arbitrary, for now */
 private double scale = 1/Math.pow(2, height);
 
 private double pitch;
 private double roll;
 private double yaw;
 private Container container;
 
 public OrthographicConverter(Container c)
 {
  container = c; /* used to properly center projection */
  
 }
 


 public boolean mapPointToCartPoint(MapPoint src, Point2D dst)
 {
  double lon0 = toRadians(yaw);
  double lat0 = toRadians(pitch);
  double lon = toRadians(src.getLon());
  double lat = toRadians(src.getLat());
  double cosLat0 = cos(lat0);
  double cosLat  = cos(lat);
  double sinLat0 = sin(lat0);
  double sinLat  = sin(lat);
  double cosDLon = cos(lon - lon0);
  double sinDLon = sin(lon - lon0);
  
  /* if this number is negative, point is on opposite hemisphere of projection */
  if(sinLat0 * sinLat + cosLat0 * cosLat * cosDLon < 0) return false;
  
  double x, y;

  x = EARTH_RAD * cosLat * sinDLon;
  y = EARTH_RAD * (cosLat0 * sinLat - sinLat0 * cosLat * cosDLon);
  dst.setLocation(x, y);
  
  return true;
 }

 /**
  Returns a list of Path2D objects representing the visible borders of a set
  of Regions
  @param col
  @return
  */
 public List<Path2D> convertRegions(Collection<Region> col)
 {
  List<MapPoint> perim;
  List<Path2D> projection = new ArrayList<>();

  Point2D dst = new Point2D.Double();
  for(Region r : col)
  {
   
   Path2D path = new Path2D.Double();
   perim = r.getPerimeter();
   
   boolean first = true;
   boolean exists = false;
   boolean firstInvisible = false;
   boolean firstVisible = false;
   boolean visible;
   
   AffineTransform transform = makeTransform();
   

   Point2D crv0 = new Point2D.Double();
   Point2D crv1 = new Point2D.Double();
   Point2D ctrl0 = new Point2D.Double();
   Point2D ctrl1 = new Point2D.Double();
   
   for(MapPoint src : perim)
   {
    visible = mapPointToCartPoint(src, dst);
    
    if(visible)
    {
//     if(firstInvisible && !firstVisible)
//     {
//      firstVisible = true;
//      crv1 = dst;
//      makeCurve(crv0, crv1);
//     }
     exists = true;
     
     if(first){ path.moveTo(dst.getX(), dst.getY()); first = false; }
     else
     {
      path.lineTo(dst.getX(), dst.getY());
     }
    }
//    else
//    {
//     if(!firstInvisible)
//     {
//      firstInvisible = true;
//      crv0 = dst;
//      continue;
//     }
//    }
   }
   if(exists) { path.closePath(); projection.add(path); }
  }
  
 return projection;
 }


 public void rotate(double yaw, double pitch, double roll, double height)
 {
  setPosition(this.yaw + yaw, this.pitch + pitch, this.roll + roll, this.height + height);
 }

 private void setPosition(double yaw, double pitch, double roll, double height)
 {
  this.yaw = yaw;
  this.pitch = pitch;
  this.roll = roll;
  this.height = height;
  scale = 1/Math.pow(2, height);
 }

 AffineTransform makeTransform()
 {
  AffineTransform at = new AffineTransform();
  
  double dx = container.getWidth()/2;
  double dy = container.getHeight()/2;

  at.translate(dx, dy);

  at.scale(scale, -scale);
  return at;
 }
 
 private Path2D makeCurve(Point2D crv0, Point2D crv1)
 {
  double vertexDist = PI * EARTH_RAD * 2 * scale;
  Path2D path = new Path2D.Double();
  path.moveTo(crv0.getX(), crv0.getY());
  
  return null;
 }

 @Override
 public double getScale()
 {
  return 0;
 }

 @Override
 public double latToY(double lat)
 {
  return 0;
 }

 @Override
 public double lonToX(double lon)
 {
  return 0;
 }

 @Override
 public Point mapPointToPoint(MapPoint mp)
 {
  return null;
 }

 @Override
 public MapPoint pointToMapPoint(Point p)
 {
  return null;
 }

 @Override
 public List<Line2D> getLatLonGrid()
 {
  return null;
 }
}
