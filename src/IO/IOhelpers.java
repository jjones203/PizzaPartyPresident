package IO;

import java.io.File;

/**
 * Created by winston on 1/22/15.
 * ${PROJECT_NAME}
 * CS 351 spring 2015
 */
public class IOhelpers
{
  /**
   * given a file name returns the absolute path of that file in URL format.
   * taken from:
   * http://docs.oracle.com/javase/tutorial/jaxp/sax/parsing.html
   *
   * @param filename
   * @return
   */
  public static String convertToFileURL(String filename)
  {
    String path = new File(filename).getAbsolutePath();
    if (File.separatorChar != '/')
    {
      path = path.replace(File.separatorChar, '/');
    }

    if (!path.startsWith("/"))
    {
      path = "/" + path;
    }
    return "file:" + path;
  }
}