package worldfoodgame.IO;

import org.xml.sax.SAXException;
import worldfoodgame.gui.displayconverters.EquirectangularConverter;
import worldfoodgame.gui.displayconverters.MapConverter;
import worldfoodgame.model.MapPoint;
import worldfoodgame.model.Region;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;

/**
 * Created by winston on 1/25/15.
 * Phase_01 Utility class to validate regions at load time.
 * CS 351 spring 2015
 *
 */
public class RegionValidator
{
  private static final  MapConverter CONVERTER = new EquirectangularConverter();

  public boolean validate(Region region) throws SAXException
  {
    for (MapPoint mp : region.getPerimeter())
    {
      if (! isValidMapPoint(mp) ) throw new SAXException("Invalid Map Point.");
    }

    // check to make sure all region polygons are simple.
    Area area = new Area(CONVERTER.regionToPolygon(region));
    boolean isSingular = area.isSingular();

    if (! isSingular)
    {
      display(area);
      throw new SAXException("Invalid Region shape");
    }

    return true;
  }

  private void display(final Area area)
  {
    JDialog dialogPnael = new JDialog();

    AffineTransform transform = new AffineTransform();
    transform.translate(647,-547);

    area.createTransformedArea(transform);

    dialogPnael.add(new JPanel()
    {

      @Override
      protected void paintComponent(Graphics g)
      {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        g2d.translate(-647, 547);
        g2d.scale(100.0, 100.0);
        g2d.fill(area);

        System.out.println("bounds: " + area.getBounds());

//        g2d.fillRect(0, 0, 200, 200);
      }
    });

    dialogPnael.setModal(true);
    dialogPnael.setVisible(true);


  }


  private boolean isValidMapPoint(MapPoint mapPoint)
  {

    return Math.abs(mapPoint.getLat()) <= 90.00 &&
           Math.abs(mapPoint.getLon()) <= 180.00;

  }
}
