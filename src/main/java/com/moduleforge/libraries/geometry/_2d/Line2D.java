/*
 *    This file is part of "Origami".
 *
 *     Origami is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Origami is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with Origami.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.moduleforge.libraries.geometry._2d;

import javax.vecmath.Point2d;

import static com.moduleforge.libraries.geometry.Geometry.*;

/**
 * Line in two dimension
 * 
 * Note that is a cartesian space where y grows upwards
 * 
 * In the JavaFX space 'y' grows downwards. If your application relies on JavaFX
 * (or any other library with a different coordinate system for that matter) and
 * you are using this library, bear that in mind.
 * 
 */
public class Line2D {

   static final double MIN_DELTA = 1e-7;

   // line equation: y = mx + b
   private double m, b;

   // only used for vertical lines:
   private double xInVertical;
   private boolean isVertical;

   private Line2D(double m, double b, boolean isVertical) {
      this.m = m;
      this.b = b;
      this.isVertical = isVertical;
   }

   /**
    * line equation: y = mx + b
    */
   public static Line2D fromEquation(double m, double b) {
      return new Line2D(m, b, false);
   }

   public static Line2D from(double x0, double y0, double x1, double y1) {
      if (Math.abs(x0 - x1) < MIN_DELTA) {
         throw new IllegalArgumentException("vertical line cannot be created from two points.");
      }
      double m = (y1 - y0) / (x1 - x0);
      double b = y1 - m * x1;
      return new Line2D(m, b, false);
   }

   public static Line2D vertical(double x) {
      Line2D l = new Line2D(0f, 0f, true);
      l.xInVertical = x;
      return l;
   }

   public static Line2D horizontal(double y) {
      Line2D l = new Line2D(0f, y, false);
      return l;
   }

   /**
    * @return null if the lines are parallel
    */
   public Point2d intersect(Line2D line) {
      if (isVertical && line.isVertical)
         return null;
      if (isVertical)
         return line.getPoint(xInVertical);
      if (line.isVertical)
         return getPoint(line.xInVertical);

      double x = (line.b - this.b) / (this.m - line.m);
      double y = this.m * x + this.b;
      return new Point2d(x, y);
   }

   public boolean contains(Point2d point) {
      if (isVertical)
         return epsilonEquals(point.getX(), this.xInVertical);
      double y = getY(point.getX());
      return epsilonEquals(point.getY(), y);
   }

   /**
    * Formula taken from:
    * https://en.wikipedia.org/wiki/Distance_from_a_point_to_a_line#Line_defined_by_an_equation
    * 
    * @return the closest point in the line to the point passed as argument
    */
   public Point2d closestPoint(Point2d point) {

      if (isVertical)
         return new Point2d(xInVertical, point.getY());

      double pointX = point.getX();
      double pointY = point.getY();

      // formula below is for the equation: ax + by + c = 0
      // The class' equation is: y = mx + b == -mx + y - b = 0
      // Using the first equation we get these values for a, b and c: a = -m, b = 1, c
      // = -b
      double _a = -m, _b = 1, _c = -b;
      // https://en.wikipedia.org/wiki/Distance_from_a_point_to_a_line#Line_defined_by_an_equation
      double x = (_b * (_b * pointX - _a * pointY) - _a * _c) / (_a * _a + _b * _b);
      double y = (_a * (-_b * pointX + _a * pointY) - _b * _c) / (_a * _a + _b * _b);
      return new Point2d(x, y);
   }

   /**
    * https://wikimedia.org/api/rest_v1/media/math/render/svg/baff35b2a747a59d5a113dcabea8fda7d2c09288
    */
   public double distanceTo(Point2d point) {

      if (isVertical)
         return Math.abs(point.getX() - xInVertical);

      double pointX = point.getX();
      double pointY = point.getY();

      // formula below is for the equation: ax + by + c = 0
      // The class' equation is: y = mx + b == -mx + y - b = 0
      // Using the first equation we get these values for a, b and c: a = -m, b = 1, c
      // = -b
      double _a = -m, _b = 1, _c = -b;
      double distance = Math.abs(_a * pointX + _b * pointY + _c) / Math.sqrt(_a * _a + _b * _b);
      return distance;
   }

   /**
    * @return null if the line does not intersect with the segment
    */
   public Point2d intersect(LineSegment2D segment) {
      if (isVertical && segment.isVertical()) {
         return null;
      }
      Point2d intersection = intersect(segment.getLine());
      if (isVertical) {
         return segment.containsX(xInVertical) ? segment.getLine().getPoint(xInVertical) : null;
      }
      if (segment.isVertical()) {
         double x = segment.getLine().xInVertical;
         return segment.containsY(getY(x)) ? getPoint(x) : null;
      }
      return segment.containsX(intersection.getX()) ? intersection : null;
   }

   public double getY(double x) {
      if (isVertical)
         throw new RuntimeException();
      return this.m * x + this.b;
   }

   public Point2d getPoint(double x) {
      if (isVertical)
         throw new RuntimeException();
      return new Point2d(x, getY(x));
   }

   public boolean isVertical() {
      return isVertical;
   }

}
