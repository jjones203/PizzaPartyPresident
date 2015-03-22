package worldfoodgame.IO;

import worldfoodgame.model.CropZoneData;
import worldfoodgame.model.LandTile;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

/**
 @author david
 created: 2015-03-21

 description: */
public class CropZoneDataIO
{
 public static CropZoneData parseFile(String filename)
 {
  CropZoneData dataSet = new CropZoneData();
  
  try(FileInputStream in = new FileInputStream(filename))
  {
   byte bytes[] = new byte[LandTile.BYTE_DEF.SIZE_IN_BYTES];
   ByteBuffer buf = ByteBuffer.allocate(LandTile.BYTE_DEF.SIZE_IN_BYTES);
   LandTile tile;
   int tiles = 0;
   while(in.read(bytes) != -1)
   {
    tiles++;
    buf.clear();
    buf.put(bytes);
    tile = new LandTile(buf);
    dataSet.putTile(tile);
   }
   System.out.println(tiles);
  } 
  catch (FileNotFoundException e)
  {
   e.printStackTrace();
  } 
  catch (IOException e)
  {
   e.printStackTrace();
  }
  return dataSet;
 }
 
 public static void writeCropZoneData(CropZoneData data, String filename)
 {
  try(FileOutputStream out = new FileOutputStream(filename))
  {
   for(LandTile t : data.allTiles())
   {
    byte[] array = t.toByteBuffer().array();
    out.write(array);
   }
  } catch (FileNotFoundException e)
  {
   e.printStackTrace();
  } catch (IOException e)
  {
   e.printStackTrace();
  }
 }

}
