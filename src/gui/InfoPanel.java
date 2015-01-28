package gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.util.Random;

import gui.views.MapView;
import model.RegionAttributes;
import model.RegionAttributes.PLANTING_ATTRIBUTES;
import testing.generators.AttributeGenerator;


/**
 * Created by winston on 1/27/15.
 * Phase_01
 * CS 351 spring 2015
 */
public class InfoPanel extends JPanel
{
  private RegionAttributes activeAttributes;
  private MapView mapView;
  private JLabel activeRegionName;
  private JTabbedPane tabbedPane;

  private JPanel cropPanel;
  private JPanel attributePanel;
  private JList  attributeList;

  private DefaultListModel cropList;

  public InfoPanel()
  {
    setLayout(new BorderLayout());

    activeRegionName = new JLabel("REGION NAME");
    activeRegionName.setHorizontalAlignment(JLabel.CENTER);
    add(activeRegionName, BorderLayout.NORTH);

    tabbedPane = new JTabbedPane();
    add(tabbedPane, BorderLayout.CENTER);

    attributePanel = getAttributePanel();
    tabbedPane.addTab("Attributes", attributePanel);

    cropPanel = getCropPanel();
    tabbedPane.add("crops", cropPanel);
  }

  private JPanel getCropPanel()
  {
    JPanel cropPanel = new JPanel();
    cropPanel.setLayout(new BorderLayout());
    cropPanel.setBackground(Color.orange);

    return cropPanel;
  }

  private JPanel getAttributePanel()
  {
    //todo move this color! whats is it doing here?
    Color guiBackGround = new Color(231, 231, 231);

    JPanel attPanel = new JPanel();
    attPanel.setLayout(new BorderLayout());
    attPanel.setBackground(guiBackGround);

    attributeList = new JList(PLANTING_ATTRIBUTES.values());
    attributeList.setBackground(guiBackGround);
    attributeList.setBorder(new EmptyBorder(10, 10, 10, 10));
    attributeList.addListSelectionListener(new ListSelectionListener()
    {
      @Override
      public void valueChanged(ListSelectionEvent e)
      {
        if (attributeList.getValueIsAdjusting())
        {
          System.out.println(attributeList.getSelectedValue().toString());
        }
      }
    });

    attPanel.add(attributeList, BorderLayout.WEST);


    JPanel centerInfoPanel = new JPanel();
    centerInfoPanel.setBackground(Color.orange);
    attPanel.add(centerInfoPanel);

    return attPanel;
  }


  public void displayRegionAttributes(RegionAttributes regionAttributes)
  {
    setCropListModel(regionAttributes);
    setAttributeListModel(regionAttributes);
    this.activeAttributes = regionAttributes;
  }

  private void setAttributeListModel(RegionAttributes regionAttributes)
  {

  }

  private void setCropListModel(RegionAttributes regionAttributes)
  {
    if (cropList == null) cropList = new DefaultListModel();
    for (String cropName : regionAttributes.getAllCropsPercentage().keySet())
    {
      cropList.addElement(cropName);
    }
    attributeList.setModel(cropList);
  }

  public static void main(String[] args)
  {

    AttributeGenerator attRando = new AttributeGenerator(new Random());
    JFrame frame = new JFrame();

    InfoPanel infoPanel = new InfoPanel();
    infoPanel.displayRegionAttributes(attRando.nextAttributeSet());

    frame.add(infoPanel);
    frame.setSize(1000, 300);
    frame.setResizable(false);
    frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    frame.setVisible(true);
  }
}
