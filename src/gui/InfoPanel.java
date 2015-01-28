package gui;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;

import gui.views.MapView;
import model.RegionAttributes.PLANTING_ATTRIBUTES;

/**
 * Created by winston on 1/27/15.
 * Phase_01
 * CS 351 spring 2015
 */
public class InfoPanel extends JPanel
{
  private MapView mapView;
  private JLabel activeRegionName;
  private JTabbedPane tabbedPane;

  private JPanel cropPanel;
  private JPanel attributePanel;

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
    cropPanel.setBackground(Color.orange);
    return cropPanel;
  }

  private JPanel getAttributePanel()
  {
    //todo move this color! whats is doing here?
    Color guiBackGround = new Color(231, 231, 231);

    JPanel attPanel = new JPanel();
    attPanel.setLayout(new BorderLayout());
    attPanel.setBackground(guiBackGround);

    final JList list = new JList(PLANTING_ATTRIBUTES.values());
    list.setBackground(guiBackGround);
    list.setBorder(new EmptyBorder(10, 10, 10, 10));
    list.addListSelectionListener(new ListSelectionListener()
    {
      @Override
      public void valueChanged(ListSelectionEvent e)
      {
        if (list.getValueIsAdjusting())
        {
          System.out.println(list.getSelectedValue().toString());
        }
      }
    });

    attPanel.add(list, BorderLayout.WEST);


    JPanel centerInfoPanel = new JPanel();
    centerInfoPanel.setBackground(Color.orange);
    attPanel.add(centerInfoPanel);

    return attPanel;
  }


  public static void main(String[] args)
  {
    JFrame frame = new JFrame();

    frame.add(new InfoPanel());
    frame.setSize(1000, 300);
    frame.setResizable(false);
    frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    frame.setVisible(true);
  }
}
