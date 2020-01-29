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

import org.javatuples.Pair;


/**
 * Segment of a line in two dimensions. Read the Line javadoc.
 * 
 * @see Line2D
 * 
 */
public class LineSegment2D {

   private static final double MIN_POINT_SEPARATION = 1e-7;

   /**
    * The first point is the one with the lesser
    *         'x' coordinate. If the segment is vertical then the first point is the
    *         one with the lesser 'y' coordinate. Read the class javadoc to find out
    *         more about the coordinate system used (which is different from javafx's)
    * @see PlanePointsOrdering#X_AND_Y_AXES
    */
   private Point2d _firstPoint;
   private Point2d _secondPoint;
   private transient Line2D _line;

   private LineSegment2D() {
      _firstPoint = new Point2d(0, 0);
      _secondPoint = new Point2d(1, 0);
   }

   public static LineSegment2D from(Line2D line, double x, double xDistance) {
      if (Math.abs(xDistance) < Line2D.MIN_DELTA || line.isVertical()) {
         throw new IllegalArgumentException("vertical segment cannot be created this way");
      }
      double x0 = Math.min(x, x + xDistance);
      double x1 = Math.max(x, x + xDistance);
      LineSegment2D segment = new LineSegment2D();
      segment._firstPoint = new Point2d(x0, line.getY(x0));
      segment._secondPoint = new Point2d(x1, line.getY(x1));
      segment._line = line;
      return segment;
   }

   public static LineSegment2D from(Point2d pointA, Point2d pointB) {
      return from(pointA.getX(), pointA.getY(), pointB.getX(), pointB.getY());
   }

   public static LineSegment2D from(double x0, double y0, double x1, double y1) {
      validatePointDistance(x0, y0, x1, y1);
      return fromValidated(x0, y0, x1, y1);
   }

   private static LineSegment2D fromValidated(double x0, double y0, double x1, double y1) {
      double xDelta = Math.abs(x0 - x1);
      boolean vertical = xDelta < Line2D.MIN_DELTA;
      if (vertical) {
         return vertical(new Point2d(x0, Math.min(y0, y1)), Math.abs(y1 - y0));
      }
      boolean pointsInOrder = x0 < x1;
      return pointsInOrder ? fromOrderedAndNonVertical(x0, y0, x1, y1) : fromValidated(x1, y1, x0, y0);
   }

   private static LineSegment2D fromOrderedAndNonVertical(double x0, double y0, double x1, double y1) {
      LineSegment2D segment = new LineSegment2D();
      segment._firstPoint = new Point2d(x0, y0);
      segment._secondPoint = new Point2d(x1, y1);
      segment._line = Line2D.from(x0, y0, x1, y1);
      return segment;
   }

   /**
    * the second point of the segment will have a higher 'y' coordinate than
    * the one passed as argument
    */
   public static LineSegment2D vertical(Point2d point, double length) {
      if (length < 0f) {
         throw new IllegalArgumentException("argument 'length' must be positive");
      }
      Point2d zeroCoords = new Point2d(0f, 0f);
      Point2d atDistance = new Point2d(0f, length);
      validatePointDistance(zeroCoords, atDistance);
      
      LineSegment2D segment = new LineSegment2D();
      double x = point.getX();
      double y = point.getY();
      segment._firstPoint = new Point2d(x, y);
      segment._secondPoint = new Point2d(x, y + length);
      segment._line = Line2D.vertical(x);
      return segment;
   }

   public static LineSegment2D horizontal(Point2d point, double length) {
      if (length < 0f) {
         throw new IllegalArgumentException("argument 'length' must be positive");
      }
      Point2d zeroCoords = new Point2d(0f, 0f);
      Point2d atDistance = new Point2d(length, 0f);
      validatePointDistance(zeroCoords, atDistance);

      LineSegment2D segment = new LineSegment2D();
      double x = point.getX();
      double y = point.getY();
      segment._firstPoint = new Point2d(x, y);
      segment._secondPoint = new Point2d(x + length, y);
      segment._line = Line2D.horizontal(y);
      return segment;
   }

   /**
    * @return A pair of points. The first point returned is the one with the lesser
    *         'x' coordinate. If the segment is vertical then the first point is the
    *         one with the lesser 'y' coordinate. Read the class javadoc to find out
    *         more about the coordinate system used (which is different from javafx's)
    */
   public Pair<Point2d, Point2d> getOrderedPoints() {
      Pair<Point2d, Point2d> points = new Pair<>(_firstPoint, _secondPoint);
      return points;
   }

   public boolean isVertical() {
      return _line.isVertical();
   }
   
   public Line2D getLine() {
      return _line;
   }

   public boolean containsX( double x) {
      return this._firstPoint.getX() < x && x < this._secondPoint.getX();
   }
   
   public boolean containsY( double y) {
      return this._firstPoint.getY() < y && y < this._secondPoint.getY();
   }

   private static void validatePointDistance(double x0, double y0, double x1, double y1) {
      validatePointDistance(new Point2d(x0, y0), new Point2d(x1, y1));
   }

   private static void validatePointDistance(Point2d point0, Point2d point1) {
      boolean pointsTooClose = point0.distance(point1)  < MIN_POINT_SEPARATION;
      if (pointsTooClose) {
         throw new IllegalArgumentException("points too close together");
      }
   }
}
