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

package com.moduleforge.libraries.geometry._3d;

import org.javatuples.Pair;

import java.util.*;

import static com.moduleforge.libraries.geometry.Geometry.*;
import static com.moduleforge.libraries.geometry._3d.Line.linePassingBy;

/**
 * Two segments are equals if the points at the ends are equal.
 * Note that the equality of points is reference equality.
 *
 * In other words, two segments that have ends in the same position might not be equal, but if the ends are the same
 * point objects, then they are equal, even if the segments are not the same object.
 *
 * In other words, we might have several points in a given position. But between two points there can be only one
 * unique segment object.
 *
 * This works well for the application this library was designed for (origami) but I believe it also makes a lot of
 * sense for almost any application. It's more
 * sensible
 * to have reference equality for floating-point-coordinate points and explicitly test position within an epsilon
 * and rely segment equality on the equality of the end point set. That makes sense from a geometry standpoint as
 * well as the origami application.
 */
public class LineSegment {
   protected Pair<Point, Point> pointPair;
   // this is only in order to delegate equals and hashcode, as the order of the
   // points is irrelevant
   protected Set<Point> pointSet;
   private Line line;

   @SuppressWarnings("unused")
   private LineSegment() {
      //prevent default instantiation
   }
   public LineSegment(Pair<Point, Point> points) {
      this(points.getValue0(), points.getValue1());
   }
   public LineSegment(Point pointA, Point pointB) {
      build(pointA, pointB);
   }
   private void build(Point pointA, Point pointB) {
      pointSet = new HashSet<>();
      pointPair = new Pair<>(pointA, pointB);
      pointSet.add(pointA);
      pointSet.add(pointB);
      line = linePassingBy(pointA, pointB);
   }
   /** matches if same end points (at same position within the library's epsilon) */
   public boolean matches(LineSegment other) {
      return isAtEndPosition(other.pointPair.getValue0()) && isAtEndPosition(other.pointPair.getValue1());
   }
   /** True if and only if the argument point is one of the two ends, false if it's neither of the end point objects
    * If they are simply at the same spatial position, then false is returned.
    * Admittedly, this could be confusing.
    */
   public boolean isAnEnd(Point point){
      return pointSet.contains(point);
   }
   /** The point shares the same position of one of the ends of this segment */
   public boolean isAtEndPosition(Point point) {
      if(isAnEnd(point))
         return true;
      return point.epsilonEquals(pointPair.getValue0()) || point.epsilonEquals(pointPair.getValue1());
   }
   /**
    * Returns a point in the segment from which a **perpendicular** line meets the
    * point passed as parameter
    * 
    * If no such point exists in the segment then return an empty object
    */
   public Optional<Point> perpendicularTo(Point point) {
      Point closestPointInLine = line.closestPoint(point);
      if (!contains(closestPointInLine))
         return Optional.empty();
      return Optional.of(closestPointInLine);
   }
   public Point getClosestPointInSegment(Point point) {
      return perpendicularTo(point).orElseGet( () -> getClosestEndOfSegment(point));
   }
   private Point getClosestEndOfSegment(Point point) {
      if(pointPair.getValue0().distance(point) < pointPair.getValue1().distance(point))
         return pointPair.getValue0();
      return pointPair.getValue1();
   }
   /**
    * True if the point is in the line (although not necessarily in the segment)
    */
   public boolean enclosingLineContains(Point point) {
      return line.contains(point);
   }
   /**
    * True if the segment contains the point, that is, if the point is on the line and within the boundaries of the segment.
    */
   public boolean contains(Point point) {
      if(isAtEndPosition(point))
         return true;
      if(! enclosingLineContains(point))
         return false;
      Point end1 = pointPair.getValue0();
      Point end2 = pointPair.getValue1();
      /*
      Note about this implementation:
      Normally it's not a good approach to test the sum of the distances to both ends against the length of the segment
      to determine if a segment is on the segment, because it requires a different epsilon of precision

      However in this case, we determine first that the point is on the line, and we can thus use the same
      precision to test the distances to both ends.
      */
      return epsilonEquals(point.distance(end1) + point.distance(end2), length());
   }
   public Point closestPointInEnclosingLine(Point other){
      return line.closestPoint(other);
   }
   /**
    within an epsilon (see the library's epsilon)
    null if no intersection
    */
   public Point intersectionPoint(LineSegment other) {
      LineSegment intersectionSegment = lineToLineIntersection(this, other);
      if (intersectionSegment == null) 
         return null;
      boolean linesIntersect = almostZero(intersectionSegment.length());
      if (!linesIntersect)
         return null;
      Point intersectionPoint = intersectionSegment.pointPair.getValue0(); //any of the points
      boolean intersectionIsContainedInBothSegments =
              other.contains(intersectionPoint) && contains(intersectionPoint);
      if(intersectionIsContainedInBothSegments)
         return intersectionPoint;
      return null;
   }
   public static LineSegment makeZeroLengthLineSegment(Point p){
      return new ZeroLengthLineSegment(p);
   }
   /**
    * Returns the distance from the point passed as argument to this segment
    * 
    * There has to be a point in the segment from which a
    * **perpendicular** line meets the point passed as parameter. Otherwise an
    * empty object is returned.
    */
   public Optional<Double> perpendicularDistanceFrom(Point point) {
      Optional<Point> closestPoint = perpendicularTo(point);
      if (!closestPoint.isPresent())
         return Optional.empty();
      double distance = point.distance(closestPoint.get());
      return Optional.of(distance);
   }
   public double distanceFrom(Point point) {
      Optional<Double> perpendicularDistance = perpendicularDistanceFrom(point);
      if(perpendicularDistance.isPresent())
         return perpendicularDistance.get();
      return point.distance(getClosestPointInSegment(point));
   }
   /**
    * IMPORTANT! Don't rely on the order of the segments!
    * The points of a segment have no order and the class makes no promises about the order of the points returned
    */
   public Pair<Point, Point> getPoints() {
      return pointPair;
   }
   public List<Point> getEndsAsList() {
      List<Point> l = new ArrayList<>();
      l.add(pointPair.getValue0());
      l.add(pointPair.getValue1());
      return l;
   }
   public double length() {
      return pointPair.getValue0().distance(pointPair.getValue1());
   }
   public Line getLine() {
      return line;
   }

   /**
    * true if, and only if, they share the same line and overlap each other
    */
   public boolean overlaps(LineSegment other) {
      if(this.equals(other))
         return true;
      boolean sameLine = other.line.containsAll(pointSet);
      if(! sameLine)
         return false;
      if(matches(other))
         return true;
      return containsAndNotAtEndPosition(other.pointPair.getValue0()) || containsAndNotAtEndPosition(other.pointPair.getValue1()) ||
              other.containsAndNotAtEndPosition(pointPair.getValue0()) || other.containsAndNotAtEndPosition(pointPair.getValue1());
   }
   public boolean contains(LineSegment other) {
      if(equals(other) || matches(other))
         return true;
      boolean sameLine = other.line.containsAll(pointSet);
      if(! sameLine)
         return false;
      Point p1 = other.pointPair.getValue0();
      Point p2 = other.pointPair.getValue1();
      return contains(p1) && contains(p2);
   }
   /**
    * Sometimes this function is useful
    * True if and only if the point is contained in the segment but is not at either end positions
    */
   public boolean containsAndNotAtEndPosition(Point p) {
      return contains(p) && !isAtEndPosition(p);
   }
   public boolean isCollinearWith(LineSegment other) {
      return line.containsAll(other.pointSet);
   }
   @Override
   public int hashCode() {
      return Objects.hashCode(pointSet);
   }
   /**
    * Two segments are equals if the points at the ends are equal,
    */
   @Override
   public boolean equals(Object obj) {
      if (obj == null) return false;
      if (getClass() != obj.getClass()) return false;
      final LineSegment other = (LineSegment) obj;
      return Objects.equals(pointSet, other.pointSet);
   }
   static class ZeroLengthLineSegment extends LineSegment {
      ZeroLengthLineSegment(Point p) {
         pointPair = new Pair<>(p, p);
         pointSet = new HashSet<>();
         pointSet.add(p);
         pointSet.add(p);
      }
      @Override
      public double length(){
         return 0D;
      }
   }
}
