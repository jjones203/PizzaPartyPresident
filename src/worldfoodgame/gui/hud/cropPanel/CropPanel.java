package worldfoodgame.gui.hud.cropPanel;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import worldfoodgame.common.EnumCropType;
import worldfoodgame.model.Country;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by winston on 3/12/15.
 */
public class CropPanel extends JFrame
{
  private BufferedImage logo;
  private Collection<Country> countries = new ArrayList<>();
  private CropData data;


  public CropPanel(CropData data) throws HeadlessException
  {
    this.data = data;
  }

}

/* just a tuple basically*/
class CropData
{

  public static CropData extractData(Collection<Country> countries, int year)
  {
    //todo implement
    throw new NotImplementedException();
  }

  public static CropData extractData(Country country, int year)
  {
    //todo implement
    throw new NotImplementedException();
  }

  public void writeCropData(Country country, int year)
  {
    //todo implement
    throw new NotImplementedException();
  }

  EnumCropType type;
  double production, exported, imported, usedLand, yield, need;


  public static void main(String[] args)
  {
    CropData data = new CropData();
    data.type = EnumCropType.CORN;
    data.production = 10;
    data.exported = 15;
    data.imported = 56;
    data.usedLand = 31;
    data.yield = 15;
    data.need = 80;
  }
}

