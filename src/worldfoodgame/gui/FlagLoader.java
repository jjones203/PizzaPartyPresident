package worldfoodgame.gui;

import worldfoodgame.IO.XMLparsers.CountryXMLparser;
import worldfoodgame.model.Country;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;

/**
 * Created by winston on 3/25/15.
 */
public class FlagLoader
{
  private static final String ROOT_DIR = "resources/flags/";
  private static FlagLoader flagLoader;
  private HashMap<String, BufferedImage> images;


  // single constructor
  private FlagLoader()
  {
    images = new HashMap<>();
  }

  public static FlagLoader getFlagLoader()
  {
    if (flagLoader == null) flagLoader = new FlagLoader();
    return flagLoader;
  }

  /**
   * Returns the countries associated flag as a biffered image
   * @param countryName
   * @return
   */
  public BufferedImage getFlag(String countryName)
  {
    if (! images.containsKey(countryName))
    {
      String fileLocation = ROOT_DIR + countryName.replace(" ", "") + ".png";
      try
      {
        BufferedImage flag = ImageIO.read(new File(fileLocation));
        images.put(countryName, flag);
      }
      catch (IOException e)
      {
        System.err.println("Error Loading flag for: " + countryName);
        e.printStackTrace();
        File f = new File(fileLocation);
        System.err.println(fileLocation + " exsists? " + f.exists());
        return null;
      }
    }

    return images.get(countryName);
  }


  public static void main(String[] args)
  {
    Collection<Country> countries = new CountryXMLparser().getCountries();


    FlagLoader flagLoader = getFlagLoader();
    for (Country country : countries)
    {
      System.out.println((flagLoader.getFlag(country.getName())));
    }
  }
}
