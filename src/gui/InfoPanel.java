package gui;

import model.AtomicRegion;
import model.RegionAttributes;
import model.RegionAttributes.PLANTING_ATTRIBUTES;
import testing.generators.AttributeGenerator;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.util.Random;


/**
 * Created by winston on 1/27/15.
 * Phase_01
 * CS 351 spring 2015
 */
public class InfoPanel extends JPanel
{

  private final static Color GUI_BACK_GROUND = new Color(231, 231, 231);
  private RegionAttributes activeAttributes;

  private JLabel activeRegionName;
  private JTabbedPane tabbedPane;

  private JPanel cropPanel;
  private JPanel attributePanel;
  private JList attributeList;
  private JList cropList;


  public InfoPanel()
  {
    cropList = new JList();

    setLayout(new BorderLayout());


    activeRegionName = new JLabel();
    activeRegionName.setHorizontalAlignment(JLabel.CENTER);
    add(activeRegionName, BorderLayout.NORTH);

    tabbedPane = new JTabbedPane();
    add(tabbedPane, BorderLayout.CENTER);

    attributePanel = getAttributePanel();
    tabbedPane.addTab("Attributes", getAttributePanel()); // I might not need a reference to the attribute pane.

    cropPanel = getCropPanel();
    tabbedPane.add("crops", cropPanel);
  }

  public static void main(String[] args)
  {

    AttributeGenerator attRando = new AttributeGenerator(new Random());
    JFrame frame = new JFrame();

    InfoPanel infoPanel = new InfoPanel();

    AtomicRegion testR = new AtomicRegion();
    testR.setName("TEST region");
    testR.setAttributes(attRando.nextAttributeSet());

    infoPanel.displayRegion(testR);

    frame.add(infoPanel);
    frame.setSize(1000, 300);
    frame.setResizable(false);
    frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    frame.setVisible(true);
  }

  private JPanel getCropPanel()
  {
    JPanel cropPanel = new JPanel();
    cropPanel.setBackground(GUI_BACK_GROUND);
    cropPanel.setLayout(new BorderLayout());

    // todo move this!
    final JLabel cropLable = new JLabel();
    cropLable.setHorizontalAlignment(SwingConstants.CENTER);

    cropList = new JList();
    cropList.setBackground(GUI_BACK_GROUND);
    cropList.setBorder(new EmptyBorder(10, 10, 10, 10));

    cropList.addListSelectionListener(new ListSelectionListener()
    {
      @Override
      public void valueChanged(ListSelectionEvent e)
      {
        if (activeAttributes == null) return;
        Double p = activeAttributes
                   .getCropP((String) cropList.getSelectedValue());

        cropLable.setText("% " + (p * 100)); // scaling the percentage value.
      }
    });


    cropPanel.add(cropLable, BorderLayout.CENTER);
    cropPanel.add(cropList, BorderLayout.WEST);

    return cropPanel;
  }

  private JPanel getAttributePanel()
  {
    JPanel attPanel = new JPanel();
    attPanel.setLayout(new BorderLayout());
    attPanel.setBackground(GUI_BACK_GROUND);

    final JLabel attributeLable = new JLabel(); //todo to this up to class level
    attributeLable.setHorizontalAlignment(SwingConstants.CENTER);

    attributeList = new JList(PLANTING_ATTRIBUTES.values());
    attributeList.setBackground(GUI_BACK_GROUND);
    attributeList.setBorder(new EmptyBorder(10, 10, 10, 10));
    attributeList.addListSelectionListener(new ListSelectionListener()
    {
      @Override
      public void valueChanged(ListSelectionEvent e)
      {
        if (activeAttributes == null) return;
        String res = attributeList.getSelectedValue().toString();
        res += ": " + activeAttributes.getAttribute((PLANTING_ATTRIBUTES) attributeList.getSelectedValue());
        attributeLable.setText(res);
      }
    });

    attPanel.add(attributeList, BorderLayout.WEST);

    attPanel.add(attributeLable, BorderLayout.CENTER);

    return attPanel;
  }


  /**
   * Convenience method. wraps the following to method calls:
   *   1) setActiveRegionName(String)
   *   2) displayCrops(RegionAttributes)
   *
   *
   * @param testR
   */
  public void displayRegion(AtomicRegion testR)
  {
    setActiveRegionName(testR.getName());
    displayCrops(testR.getAttributes());
  }

  /**
   * set the Info Pane Title to the name of the specified region.
   * @param name of the region to display
   */
  public void setActiveRegionName(String name)
  {
    activeRegionName.setText(name);
  }


  private void setActiveAttributes(RegionAttributes attributes)
  {
    activeAttributes = attributes;
  }


  public void displayCrops(RegionAttributes attributes)
  {
    setActiveAttributes(attributes);
    DefaultListModel cropModel = new DefaultListModel();

    for (String crop : attributes.getAllCropsPercentage().keySet())
    {
      cropModel.addElement(crop);
    }

    cropList.setModel(cropModel);
  }

}
