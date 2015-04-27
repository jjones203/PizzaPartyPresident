package worldfoodgame.gui;

import org.apache.commons.csv.*;

import worldfoodgame.IO.CSVhelpers.CSVParsingException;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Vector;

/**
 * Dialog window for correcting errors found when parsing csv file of country data.
 * @author jessica
 * @version 20-Mar-2015
 */
public class CSVEditor extends JDialog implements ActionListener
{
  private String[] headers;
  private List<CSVRecord> records;
  private CSVParsingException exception;
  private CSVTableModel tableModel;
  private JTable table;
  
  /**
   * Make and display new CSVEditor
   * @param headers   string array of column names
   * @param records   list of CSVRecords from file
   * @param exception CSVParsingException thrown when parsing
   */
  public CSVEditor(String[] headers, List<CSVRecord> records, CSVParsingException exception)
  {
    this.headers = headers;
    this.records = records;
    this.exception = exception;
    
    setModalityType(Dialog.DEFAULT_MODALITY_TYPE);
    setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    
    tableModel = makeTableModel(headers, records);
    table = new JTable(tableModel);
    
    TableCellRenderer hRend = table.getTableHeader().getDefaultRenderer();
    for (int i = 0; i < headers.length; ++i)
    {
      TableColumn tableCol = table.getColumnModel().getColumn(i);
      tableCol.setHeaderRenderer(hRend);
      tableCol.sizeWidthToFit();
    }
    table.setDefaultRenderer(String.class, new CustomCellRenderer(exception, headers));
    table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS); // set resize mode to get horizontal scrolling to work
          
    JScrollPane scrollPane = new JScrollPane(table);
    table.setPreferredSize(new Dimension(1200,400));
    table.setFillsViewportHeight(true);
    scrollPane.setPreferredSize(new Dimension(900,200));
    scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
    scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
    
    JButton saveButton = new JButton("SAVE AND CONTINUE");
    saveButton.addActionListener(this);
    
    // text area with info on errors
    String colName = exception.field;
    long recNum = exception.record.getRecordNumber()-1;
    String errMsg = new String("Error in "+colName+" column of Record "+recNum);
    JTextArea text = new JTextArea(errMsg);
    
    Container contentPane = getContentPane();
    contentPane.add(text,BorderLayout.PAGE_START);
    contentPane.add(scrollPane,BorderLayout.CENTER);
    contentPane.add(saveButton,BorderLayout.PAGE_END);
    
    this.setContentPane(contentPane);
    this.pack();
    table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);  // reset resize mode to get horizontal scrolling to work
    //scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
    //scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
    
    this.setVisible(true);
    
  }
  
  /* 
   * actionPerformed is called when user clicks "SAVE AND CONTINUE" button.
   * Saves to csv file associated with this editor's exception member variable.
   * (non-Javadoc)
   * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
   */
  public void actionPerformed(ActionEvent e)
  {
    try
    {
      stopEditing();
      FileWriter writer = new FileWriter(exception.csvFile);
      CSVPrinter printer = CSVFormat.DEFAULT.withHeader(headers).print(writer);
      Vector<Vector<String>> dataVector = tableModel.getDataVector();
      for (Vector<String> nestedVector:dataVector)
      {
        printer.printRecord(nestedVector);
      }
      printer.close();
      writer.close();
      dispose();
    }
    catch (IOException exception)
    {
      exception.printStackTrace();
    }
  }
  
  
  private CSVTableModel makeTableModel(String[] headers, List<CSVRecord> records)
  {
    int cols = headers.length;
    int rows = records.size();
    String[][] tableArray = new String[rows][cols];
    // convert records to 2D string array
    for (int i = 0; i < rows; i++)
    {
      CSVRecord currRecord = records.get(i);
      for (int j = 0; j < cols; j++)
      {
        tableArray[i][j] = currRecord.get(j);
      }
    }
    CSVTableModel tableModel = new CSVTableModel(tableArray, headers);
    return tableModel;
  }
  
  private void stopEditing()
  {
    if (table.getCellEditor() != null) table.getCellEditor().stopCellEditing();
 }

  
  /**
   * Class extends DefaultTableModel; use in creating table containing csv data.
   * Prevents user from editing row of table with data types.
   * @author jessica
   * @version 18-Mar-15
   */
  private class CSVTableModel extends DefaultTableModel
  {
    public CSVTableModel(Object[][] data, Object[] columnNames)
    {
      super(data,columnNames);
    }
    
    public boolean isCellEditable(int row, int column)
    {
      // user can't edit row with data types
      if (row != 0) return true;
      else return false;
    }
    
    public Class<?> getColumnClass(int columnIndex)
    {
      return String.class;
    }
    
  }
  
  /**
   * CustomCellRenderer highlights cell with error in yellow.
   * @author jessica
   */
  private class CustomCellRenderer extends DefaultTableCellRenderer
  {
    long rowNumber;
    int columnNumber;

    CustomCellRenderer(CSVParsingException exception, String[] headers)
    {
      super();
      rowNumber = exception.record.getRecordNumber() - 1;
      for (int i = 0; i < headers.length; i++)
      {
        if (headers[i].equals(exception.field))
        {
          columnNumber = i;
          break;
        }
      }
    }

    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
    {
      Component cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
      if (row == rowNumber && column == columnNumber)
      {
        cell.setBackground(Color.yellow);
      }
      else cell.setBackground(Color.white);
      return cell;
    }
  }
  
}
