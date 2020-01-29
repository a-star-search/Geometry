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

package com.moduleforge.libraries.geometry;

import com.moduleforge.libraries.geometry._2d.PlanePointsOrdering;
import com.moduleforge.libraries.geometry._3d.LineSegment;
import com.moduleforge.libraries.geometry._3d.Point;
import com.moduleforge.libraries.geometry._3d.Vector;

import javax.vecmath.Point2d;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.moduleforge.libraries.geometry.GeometryConstants.*;
import static com.moduleforge.libraries.geometry._2d.PlanePointsOrdering.COUNTERCLOCKWISE;
import static com.moduleforge.libraries.geometry._3d.LineSegment.*;
import static com.moduleforge.libraries.geometry._3d.Plane.planeFromOrderedPoints;
import static java.lang.Double.MIN_VALUE;
import static java.lang.Math.*;
import static java.util.Collections.reverse;

public class Geometry {
   private final static String MSG_INVALID_POINTS_FOR_INTERSECTION_CALCULATION = 
         "The points cannot be used for the calculation of the intersection.";
   /**
    * check if the points lay all on the same plane
    */
   public static boolean inSamePlane(List<Point> points) {
      checkNotNull(points);
      if (points.size() <= 2)
         throw new IllegalArgumentException("Too few points");

      // For efficiency check first cases where the plane is perpendicular to one of
      // the axes
      boolean allEqualForSomeCoordinate = !isThereDifferent(points, Point::x)
            || !isThereDifferent(points, Point::y) || !isThereDifferent(points, Point::z);

      if (allEqualForSomeCoordinate)
         return true;

      Vector normalVectorFromAnyThreePoints = planeFromOrderedPoints(points.subList(0, 3)).getNormal();

      Point firstPoint = points.get(0);
      Point secondPoint = points.get(1);

      List<Point> pointsCopy = new ArrayList<>(points);
      // remove first three points
      for (int i = 0; i <= 2; i++)
         pointsCopy.remove(0);

      return inSamePlaneRecursive(pointsCopy, firstPoint, secondPoint, normalVectorFromAnyThreePoints);
   }
   private static boolean inSamePlaneRecursive(List<Point> points, Point planePoint1, Point planePoint2, Vector normalVector) {
      if (points.isEmpty())
         return true;
      Vector normalWithNextPoint = planeFromOrderedPoints(planePoint1, planePoint2, points.get(0)).getNormal();
      points.remove(0);
      return almostEqualInEitherDirection(normalVector, normalWithNextPoint) &&
              inSamePlaneRecursive(points, planePoint1, planePoint2, normalVector);
   }
   private static boolean almostEqualInEitherDirection(Vector vector1, Vector vector2) {
      return vector1.epsilonEquals(vector2) || vector1.epsilonEquals(vector2.negate());
   }
   private static boolean isThereDifferent(List<Point> points, Function<Point, Double> getACoordinate) {
      double firstVal = getACoordinate.apply(points.get(0));
      return points.parallelStream().anyMatch(p -> (epsilonDifferent(getACoordinate.apply(p), firstVal)));
   }
   private static boolean epsilonDifferent(double d1, double d2) {
      return !epsilonEquals(d1, d2);
   }
   public static boolean differentEnough(Point first, Point second) {
      return first.distance(second) >= MINIMUM_POINT_SEPARATION;
   }
   public static boolean differentEnough(Point2d first, Point2d second) {
      Point first3D = new Point(first.getX(), first.getY(), 0);
      Point second3D = new Point(second.getX(), second.getY(), 0);
      return differentEnough(first3D, second3D);
   }
   /** Equals with double precision*/
   public static boolean epsilonEquals(Point2d first, Point2d second) {
      Point first3D = new Point(first.getX(), first.getY(), 0);
      Point second3D = new Point(second.getX(), second.getY(), 0);
      return first3D.epsilonEquals(second3D);
   }
   /** Equals with double precision*/
   public static boolean epsilonEquals(double a, double b) {
      return epsilonEquals(a, b, TOLERANCE_EPSILON);
   }
   public static boolean epsilonEquals(double a, double b, double epsilon) {
      double diff = abs(a - b);
      if(abs(a) <= 10 || abs(b) <= 10)
         return diff < epsilon;
      int orderOfMagnitude = (int)floor(log10(abs(a)));
      double multiplier = pow(10, orderOfMagnitude);
      return diff < epsilon * multiplier;
   }
   //occasionally we might need this
   public static boolean epsilonEqualsFloatPrecision(double a, double b) {
      return abs(a - b) < TOLERANCE_EPSILON_FLOAT_PRECISION;
   }
   /** double precision*/
   public static boolean almostZero(double d) {
      return abs(d) < TOLERANCE_EPSILON;
   }
   //occasionally we might prefer this
   public static boolean almostZeroFloatPrecision(double d) {
      return abs(d) < TOLERANCE_EPSILON_FLOAT_PRECISION;
   }
   /**
    * This function should be used (instead of "almostZero" with double epsilon defined as constant in this module)
    * in order to avoid introducing any errors, typically when checking a parameter to a function
    * if it can handle any number close to zero and there is no need to introduce an error larger than necessary
    * by considering a value close to zero as if it were equal to zero.
    *
    * Remember that the epsilon constant is one or two orders of magnitude bigger than the actual double precision
    *
    * We can assume that usage of this function does not introduce any larger error than using any arithmetic
    * operation on the argument
    */
   public static boolean isZero(double d){
      double almostZeroMaxPrecision = 2 * MIN_VALUE;
      return abs(d) <= almostZeroMaxPrecision;
   }
   /**
    * Double precision
    * Error area = 2 * linear unit error * length + (linear unit error * linear unit error)
    * Ignoring the second part, the square, which is negligible, compared
    * to the rest of the error area: error area = 2 * linear unit error * length
    *
    * We don't have the length passed as parameter, so this method is not very rigorous, we just divide by two
    * at least we don't increase the error allowance, we restrict it
    * in general it would be bad to get anywhere near the error limit anyway
    */
   public static boolean areaIsAlmostZero(double errorArea) {
      double inLinearUnits = errorArea / 2;
      return almostZero(inLinearUnits);
   }
   public static List<Point2d> sort(Set<Point2d> points, PlanePointsOrdering ordering) {
      List<Point2d> l = new ArrayList<>();
      l.addAll(points);
      sort(l, ordering);
      return l;
   }
   /**
    * IMPORTANT: we are using here a coordinate system where y grows upwards (as
    * usual in geometry)
    * 
    * This is different from the system used in some libraries, JavaFX is an example of this
    */
   static void sort(List<Point2d> points, PlanePointsOrdering ordering) {
      switch (ordering) {
      case CLOCKWISE:
         sort(points, COUNTERCLOCKWISE);
         reverse(points);
         break;
      case COUNTERCLOCKWISE:
         points.sort((p1, p2) -> {
            double x1 = p1.getX(), x2 = p2.getX();
            double y1 = p1.getY(), y2 = p2.getY();
            double p1PolarTheta = atan2(y1, x1);
            double p1PolarThetaPositive = p1PolarTheta > 0 ? p1PolarTheta : p1PolarTheta + 2 * PI;
            double p2PolarTheta = atan2(y2, x2);
            double p2PolarThetaPositive = p2PolarTheta > 0 ? p2PolarTheta : p2PolarTheta + 2 * PI;

            if ((almostZero(p1PolarThetaPositive) || epsilonEquals(p1PolarThetaPositive, 2 * PI))
                  && (almostZero(p2PolarThetaPositive) || epsilonEquals(p2PolarThetaPositive, 2 * PI)))
               return 0;
            return epsilonEquals(p1PolarThetaPositive, p2PolarThetaPositive) ? 0
                  : Double.compare(p1PolarThetaPositive, p2PolarThetaPositive);
         });
         break;
      case X_AND_Y_AXES:
         points.sort((p1, p2) -> {
            double x1 = p1.getX(), x2 = p2.getX();
            double y1 = p1.getY(), y2 = p2.getY();
            return epsilonEquals(x1, x2) ? Double.compare(y1, y2) : Double.compare(x1, x2);
         });
         break;
      default:
         throw new RuntimeException();
      }
   }
   public static boolean facingEachOther(Vector v1, Vector v2){
      return v1.dot(v2) < 0;
   }
   public static boolean facingTheSameWay(Vector v1, Vector v2){
      return v1.dot(v2) > 0;
   }
   /**
    * Calculate the line segment that is the shortest route between the two lines
    * determined by the segments.
    * 
    * Even though we are passing segments as arguments the result is the intersection of the lines in which 
    * the segments are contained, not the intersection of the segments themselves.
    * 
    */
   public static LineSegment lineToLineIntersection(LineSegment segmentA, LineSegment segmentB) {
      checkNotNull(segmentA, "Segment cannot be null.");
      checkNotNull(segmentB, "Segment cannot be null.");
      Point p1 = segmentA.getPoints().getValue0();
      Point p2 = segmentA.getPoints().getValue1();
      Point p3 = segmentB.getPoints().getValue0();
      Point p4 = segmentB.getPoints().getValue1();
      Vector vecP1toP2 = p1.vectorTo(p2);
      Vector vecP3toP4 = p3.vectorTo(p4);
      if(min(vecP1toP2.length(), vecP3toP4.length()) < MINIMUM_POINT_SEPARATION)
         throw new RuntimeException(MSG_INVALID_POINTS_FOR_INTERSECTION_CALCULATION);
      Point p13 = p1.sub(p3);
      double d1343 = p13.x() * vecP3toP4.x() + p13.y() * vecP3toP4.y() + p13.z() * vecP3toP4.z();
      double d4321 = vecP3toP4.x() * vecP1toP2.x() + vecP3toP4.y() * vecP1toP2.y() + vecP3toP4.z() * vecP1toP2.z();
      double d4343 = vecP3toP4.x() * vecP3toP4.x() + vecP3toP4.y() * vecP3toP4.y() + vecP3toP4.z() * vecP3toP4.z();
      double d2121 = vecP1toP2.x() * vecP1toP2.x() + vecP1toP2.y() * vecP1toP2.y() + vecP1toP2.z() * vecP1toP2.z();
      double denom = d2121 * d4343 - d4321 * d4321;
      if(abs(denom) < TOLERANCE_EPSILON) //I think this is the parallel lines case (I copied the algorithm so I'm not sure)
         return null;
      double d1321 = p13.x() * vecP1toP2.x() + p13.y() * vecP1toP2.y() + p13.z() * vecP1toP2.z();

      double mua = (d1343 * d4321 - d1321 * d4343) / denom;
      double mub = (d1343 + d4321 * mua) / d4343;

      Point pointA = new Point(p1.x()+mua*vecP1toP2.x(),p1.y()+mua*vecP1toP2.y(),p1.z()+mua*vecP1toP2.z());
      Point pointB = new Point(p3.x()+mub*vecP3toP4.x(),p3.y()+mub*vecP3toP4.y(),p3.z()+mub*vecP3toP4.z());
      if(pointA.epsilonEquals(pointB))
         return makeZeroLengthLineSegment(pointA);
      return new LineSegment(pointA, pointB);
   }
}
