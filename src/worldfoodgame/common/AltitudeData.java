package worldfoodgame.common;

/**
 @author david
 created: 2015-03-19

 description: 
 giving this a shot...
 */
public class AltitudeData extends AbstractAltitudeData
{


 /**
  AltDataKey class provides static accessors to a singleton object that
  acts as a key into the HashMap holding the altitude data. 
  */
 private static class AltDataKey
 {
  private static final AltDataKey key = new AltDataKey(0);
  
  private double lat = 0;
  private double lon = 0;
  private int hash;
  private AltDataKey (int hash) { this.hash = hash;}
  
  @Override
  public int hashCode()
  {
   return hash;
  }
  
  public static AltDataKey get(double lat, double lon)
  {
   /*
    todo: set up binning mechanism for lat and lon
    */
   return null;
  }
 }
 
 @Override
 public float getAltitude(float latitude, float longitude)
 {
  return 0;
 }
}
