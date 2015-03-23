package worldfoodgame.model;


import java.nio.ByteBuffer;

/**
 @author david
 created: 2015-03-21

 description: */
public class LandTile
{

 public void setElev(float elev)
 {
  elevation = elev;
 }

 public enum BYTE_DEF
 {
  LONGITUDE, LATITUDE, ELEVATION, 
  MAX_ANNUAL_TEMP, MIN_ANNUAL_TEMP, 
  AVG_DAY_TEMP, AVG_NIGHT_TEMP, 
  RAINFALL, 
  PROJ_MAX_ANNUAL_TEMP, PROJ_MIN_ANNUAL_TEMP,
  PROJ_AVG_DAY_TEMP, PROJ_AVG_NIGHT_TEMP, 
  PROJ_RAINFALL;

  int index() { return ordinal() * Float.SIZE/Byte.SIZE; }

  public static final int SIZE = values().length;

  public static final int SIZE_IN_BYTES = SIZE * Float.SIZE/Byte.SIZE;
 }
 
 private float elevation = 0;     /* in meters above sea level */
 private float maxAnnualTemp = 0; /* in degrees Celsius. */
 private float minAnnualTemp = 0; /* in degrees Celsius. */
 private float avgDayTemp = 0;    /* in degrees Celsius. */
 private float avgNightTemp = 0;  /* in degrees Celsius. */
 private float rainfall = 0;      /* in cm */
 private float proj_maxAnnualTemp = 0; /* in degrees Celsius. */
 private float proj_minAnnualTemp = 0; /* in degrees Celsius. */
 private float proj_avgDayTemp = 0;    /* in degrees Celsius. */
 private float proj_avgNightTemp = 0;  /* in degrees Celsius. */
 private float proj_rainfall = 0;      /* in cm */
 private MapPoint center;

 /**
  Constructor used for initial creation of dataset

  @param lon
  @param lat
  */
 public LandTile(double lon, double lat)
 {
  center = new MapPoint(lon, lat);
 }

 public LandTile(ByteBuffer buf)
 {
  int len = buf.array().length;
  if (len != BYTE_DEF.SIZE_IN_BYTES)
  {
   throw new IllegalArgumentException(
     String.format("ByteBuffer of incorrect size%n" +
       "Expected: %d%n" +
       "Received: %d%n", BYTE_DEF.SIZE_IN_BYTES, len));
  }

  float lon = buf.getFloat(BYTE_DEF.LONGITUDE.index());
  float lat = buf.getFloat(BYTE_DEF.LATITUDE.index());
  elevation = buf.getFloat(BYTE_DEF.ELEVATION.index());

  maxAnnualTemp = buf.getFloat(BYTE_DEF.MAX_ANNUAL_TEMP.index());
  minAnnualTemp = buf.getFloat(BYTE_DEF.MIN_ANNUAL_TEMP.index());
  avgDayTemp = buf.getFloat(BYTE_DEF.AVG_DAY_TEMP.index());
  avgNightTemp = buf.getFloat(BYTE_DEF.AVG_NIGHT_TEMP.index());
  rainfall = buf.getFloat(BYTE_DEF.RAINFALL.index());

  proj_maxAnnualTemp = buf.getFloat(BYTE_DEF.PROJ_MAX_ANNUAL_TEMP.index());
  proj_minAnnualTemp = buf.getFloat(BYTE_DEF.PROJ_MIN_ANNUAL_TEMP.index());
  proj_avgDayTemp = buf.getFloat(BYTE_DEF.PROJ_AVG_DAY_TEMP.index());
  proj_avgNightTemp = buf.getFloat(BYTE_DEF.PROJ_AVG_NIGHT_TEMP.index());
  proj_rainfall = buf.getFloat(BYTE_DEF.PROJ_RAINFALL.index());

  center = new MapPoint(lon, lat);
 }

 public ByteBuffer toByteBuffer()
 {
  ByteBuffer buf = ByteBuffer.allocate(BYTE_DEF.SIZE_IN_BYTES);

  buf.putFloat(BYTE_DEF.LONGITUDE.index(), (float)center.getLon());
  buf.putFloat(BYTE_DEF.LATITUDE.index(), (float)center.getLat());
  buf.putFloat(BYTE_DEF.ELEVATION.index(), elevation);

  buf.putFloat(BYTE_DEF.MAX_ANNUAL_TEMP.index(), maxAnnualTemp);
  buf.putFloat(BYTE_DEF.MIN_ANNUAL_TEMP.index(), minAnnualTemp);
  buf.putFloat(BYTE_DEF.AVG_DAY_TEMP.index(), avgDayTemp);
  buf.putFloat(BYTE_DEF.AVG_NIGHT_TEMP.index(), avgNightTemp);
  buf.putFloat(BYTE_DEF.RAINFALL.index(), rainfall);

  buf.putFloat(BYTE_DEF.PROJ_MAX_ANNUAL_TEMP.index(), proj_maxAnnualTemp);
  buf.putFloat(BYTE_DEF.PROJ_MIN_ANNUAL_TEMP.index(), proj_minAnnualTemp);
  buf.putFloat(BYTE_DEF.PROJ_AVG_DAY_TEMP.index(), proj_avgDayTemp);
  buf.putFloat(BYTE_DEF.PROJ_AVG_NIGHT_TEMP.index(), proj_avgNightTemp);
  buf.putFloat(BYTE_DEF.PROJ_RAINFALL.index(), proj_rainfall);

  return buf;
 }

 public double getLon()
 {
  return center.getLon();
 }
 
 public double getLat()
 {
  return center.getLat();
 }
 
 public MapPoint getCenter()
 {
  return center;
 }


 /*
    todo: interpolate data by year in accessors
  */

 public float getElevation()
 {
  return elevation;
 }

 public float getMaxAnnualTemp()
 {
  return maxAnnualTemp;
 }

 public float getMinAnnualTemp()
 {
  return minAnnualTemp;
 }

 public float getAvgDayTemp()
 {
  return avgDayTemp;
 }

 public float getAvgNightTemp()
 {
  return avgNightTemp;
 }

 public float getRainfall()
 {
  return rainfall;
 }
}