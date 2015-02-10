package gui.regionlooks;

import gui.GUIRegion;

import java.awt.*;

/**
 * Created by winston on 1/23/15.
 * Phase_01
 * CS 351 spring 2015
 * 
 * description:
 *  RegionView interface defines a single draw() method that can be used by
 *  a GUIRegion to draw the Region it wraps in the appropriate manner
 *  All model-specific graphics are defined in terms of RegionView implementations
 */
public interface RegionView
{
  void draw(Graphics g, GUIRegion gRegion);
}
