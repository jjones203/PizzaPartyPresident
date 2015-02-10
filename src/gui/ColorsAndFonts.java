package gui;

import java.awt.*;

/**
 * Created by winston on 1/23/15.
 * Phase_01
 * CS 351 spring 2015
 * <p>
 * Global set of Color for all things gui
 */
public interface ColorsAndFonts
{

  Color REGION_NAME_FONT_C = new Color(0x6C111C);

  Color OCEANS = new Color(0x626060);

  Color ACTIVE_REGION = new Color(0xDEB564);
  
  /* changed from 0xE37956, now is same as PASSIVE_REGION_OUTLINE */
  Color ACTIVE_REGION_OUTLINE = new Color(0x767979); 

  Color SELECT_RECT_OUTLINE = new Color(94, 78, 114);
  Color SELECT_RECT_FILL = new Color(SELECT_RECT_OUTLINE.getRGB() & 0x50FFFFFF, true);


  Color PASSIVE_REGION = new Color(0xCCB79B);
  Color PASSIVE_REGION_OUTLINE = new Color(0x767979);

  Color XML_ERROR = new Color(255, 129, 78, 212);

  Color MAP_GRID = new Color(255, 255, 255, 70);

  Color GUI_BACKGROUND = new Color(58, 56, 56);
  Color GUI_TEXT_COLOR = new Color(235, 235, 235);

  Font GUI_FONT = new Font("SansSerif", Font.PLAIN, 11);
//  Font TOP_FONT = GUI_FONT.deriveFont(18f);
  Font TOP_FONT = new Font("SansSerif", Font.PLAIN, 14);
  Font HUD_TITLE = new Font("SansSerif", Font.PLAIN, 14);
  Font NAME_VIEW = new Font("SansSerif", Font.PLAIN, 12);


  Color MINI_BOX_REGION = new Color(222, 183, 106);

  Color[] PlantingZoneColors = {
    new Color(0x36F03A),
    new Color(0x67001f),
    new Color(0x67001f),
    new Color(0x67001f),
    new Color(0xb2182b),
    new Color(0xd6604d),
    new Color(0xf4a582),
    new Color(0xfddbc7),
    new Color(0xf7f7f7),
    new Color(0xd1e5f0),
    new Color(0x92c5de),
    new Color(0x4393c3),
    new Color(0x2166ac),
    new Color(0x053061),
    new Color(0x053061),
  };


  Color[] RAIN_FALL = {
    new Color(0xf7fbff),
    new Color(0xdeebf7),
    new Color(0xc6dbef),
    new Color(0x9ecae1),
    new Color(0x6baed6),
    new Color(0x4292c6),
    new Color(0x2171b5),
    new Color(0x08519c),
    new Color(0x08306b),
  };

  Color BAR_GRAPH_NEG = Color.cyan;

  class colorConverter
  {
    /**
     * get either black or white depending on the luminosity of the specified color.
     *
     * @param color to be 'inverted'
     * @return
     */
    public static Color getContrastColor(Color color)
    {
      double y = (299 * color.getRed() + 587 * color.getGreen() + 114 * color.getBlue()) / 1000;
      return y >= 128 ? Color.black : Color.white;
    }
  }
}
