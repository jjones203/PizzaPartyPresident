package cameratest;

import java.awt.*;
import java.util.Random;

/**
 * Created by winston on 1/24/15.
 * CameraTest
 * CS 351 spring 2015
 */
public class RandoPolygonGen
{
  private static final int MIN_SIDE = 3;
  private Random rand;
  private Point topLeft;
  private Point bottomRight;

  public RandoPolygonGen(long seed, Point topLeft, Point bottomRight)
  {
    this.rand = new Random(seed);
    this.topLeft = topLeft;
    this.bottomRight = bottomRight;
  }

  public Polygon genRandpolygon(int limit)
  {
    int numOfSides = rand.nextInt((limit - MIN_SIDE) + 1) + MIN_SIDE;

    Polygon p = new Polygon();
    for (int i = 0; i <= numOfSides; i++)
    {
      p.addPoint(
          rand.nextInt((bottomRight.x - topLeft.x) + 1) + topLeft.x,
          rand.nextInt((bottomRight.y - topLeft.y) + 1) + bottomRight.y
      );
    }
    return p;
  }
}
