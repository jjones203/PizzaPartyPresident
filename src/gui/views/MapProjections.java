package gui.views;

import model.Region;

import java.awt.geom.Area;

/**
 * Created by winston on 1/23/15.
 * Phase_01
 * CS 351 spring 2015
 */
public interface MapProjections
{
  public Area regionToArea(Region r);
}
