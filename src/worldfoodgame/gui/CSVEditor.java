package worldfoodgame.gui;

import org.apache.commons.csv.*;

import worldfoodgame.IO.CSVParsingException;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import java.awt.Container;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

public class CSVEditor extends JDialog implements ActionListener
{
  private String[] headers;
  private List<CSVRecord> records;
  private CSVParsingException exception;
  private CSVTableModel tableModel;
  private JTable table;
  
  public CSVEditor(String[] headers, List<CSVRecord> records, CSVParsingException exception)
  {
    this.headers = headers;
    this.records = records;
    this.exception = exception;
    
    setModalityType(Dialog.DEFAULT_MODALITY_TYPE);
    setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    setMinimumSize(new Dimension(500,500));
    
    tableModel = makeTableModel(headers, records);
    table = new JTable(tableModel);
    
    JScrollPane scrollPane = new JScrollPane(table);
    table.setFillsViewportHeight(true);
    
    JButton saveButton = new JButton("SAVE AND CONTINUE");
    saveButton.addActionListener(this);
    
    Container contentPane = getContentPane();
    contentPane.add(scrollPane);
    contentPane.add(saveButton);
    contentPane.setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
    this.setContentPane(contentPane);
    this.setVisible(true);
    
  }
  
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
      //printer.print(dataVector);
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
  }
  
  
}
