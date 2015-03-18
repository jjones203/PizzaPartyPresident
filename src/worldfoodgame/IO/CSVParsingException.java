package worldfoodgame.IO;

import java.io.File;

import org.apache.commons.csv.*;

/**
 * Exception class for errors when parsing CSV file.
 * @author  jessica
 * @version Mar-15-2015
 */
public class CSVParsingException extends RuntimeException
{
  public CSVRecord record;
  public File csvFile;
  
  /**
   * Constructor takes info about the file and the particular record (i.e. row) causing error.
   * @param record    CSVRecord causing error
   * @param csvFile   File containing record
   */
  public CSVParsingException(CSVRecord record, File csvFile)
  {
    super();
    this.record = record;
    this.csvFile = csvFile;
  }
 
}
