package gui;

import java.awt.*;

/**
 * Created by winston on 1/23/15.
 * Phase_01
 * CS 351 spring 2015
 *
 * Global set of Color for all things gui
 */
public interface  ColorSchemes
{

  Color REGION_NAME_FONT_C = new Color(0x5A2C18);

  Color OCEANS = new Color(0x626060);

  Color ACTIVE_REGION = new Color(0xDEB564);
  Color ACTIVE_REGION_OUTLINE = new Color(0xE37956);

  Color PASSIVE_REGION = new Color(0xCCB79B);
  Color PASSIVE_REGION_OUTLINE = new Color(0x767979);

  Color XML_ERROR = new Color(255, 129, 78, 212);

  Color MAP_GRID = new Color(255, 255, 255, 70);

  Color GUI_BACKGROUND = new Color(58, 56, 56);
  Color GUI_TEXT_COLOR = new Color(235, 235, 235);
  Font GUI_FONT = new Font("SansSerif", Font.PLAIN, 12);


}
