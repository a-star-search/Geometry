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

import javax.vecmath.Vector3d;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.google.common.collect.Lists.reverse;
import static com.moduleforge.libraries.geometry.Geometry.almostZero;
import static java.lang.Math.*;

/**
 * A plane in a 3D space. This plane has sides and a normal with a direction. The sides of the plane have to be
 * defined and documented for any arguments of any constructor of factory method. For example if the plane is defined
 * by a list of three points (see below)
 *
 * PLANE DEFINED BY A LIST OF THREE POINTS:
 *
 * Any three points not in a line define a plane (hopefully a exception is thrown by
 * any constructor where points in a line are passed!)
 *
 * If a Plane object is defined from a list of three points, if a viewer see that point arrangement to be
 * anticlockwise, then the normal of the plane points to the viewer.
 *
 * It can also be said that the viewer is on the positive side of the plane defined by the such point list perceived
 * to be in anticlockwise order.
 *
 * PRECISION:
 * As of this comment time, all operations are implemented in double precision and I do not rely on JME3 (float precision)
 * as I was doing when I started the library, and I fact I have removed it completely.
 * This is unlikely to change, since I probably have all operations I could need.
 *
 */
public class Plane {
   public enum Side {
      None,
      Positive,
      Negative;
      public Side opposite(){
         if(this == Negative)
            return Positive;
         if(this == Positive)
            return Negative;
         return this;
      }
   }
   private List<Point> creationPoints;
   private Vector normalVector;
   private double dConstant;
   private List<Double> equationParams;
   private Plane() {
      //prevent default construction
      creationPoints = new ArrayList<>();
   }
   /**
    * the order of the points define the direction of the plane
    * use exactly three points
    */
   public static Plane planeFromOrderedPoints(List<Point> points) {
      if(points.size() != 3)
         throw new IllegalArgumentException("Wrong number of arguments: A plane is defined by three points");
      return planeFromOrderedPoints(points.get(0), points.get(1), points.get(2));
   }
   /**
    * the order of the points define the direction of the plane
    */
   public static Plane planeFromOrderedPoints(Point pointA, Point pointB, Point pointC) {
      boolean allAreDistantEnough = pointB.distantEnough(pointA);
      allAreDistantEnough &= pointB.distantEnough(pointC);
      allAreDistantEnough &= pointA.distantEnough(pointC);
      if (!allAreDistantEnough)
         throw new IllegalArgumentException("At least two of the points were too close together.");
      Plane plane = new Plane();
      plane.creationPoints = new ArrayList<>();
      plane.creationPoints.add(pointA);
      plane.creationPoints.add(pointB);
      plane.creationPoints.add(pointC);
      Vector3d v1 = pointA.vectorTo(pointB).asVector3d();
      Vector3d v2 = pointA.vectorTo(pointC).asVector3d();
      Vector3d result = new Vector3d();
      result.cross(v1, v2);
      result.normalize();
      plane.normalVector = new Vector(result.x, result.y, result.z);
      plane.dConstant = -(result.x * pointA.x() + result.y * pointA.y() + result.z * pointA.z());
      plane.equationParams = new ArrayList<>();
      plane.equationParams.add(result.x);
      plane.equationParams.add(result.y);
      plane.equationParams.add(result.z);
      plane.equationParams.add(plane.dConstant);
      return plane;
   }
   public Plane facingTheOtherWay(){
      return planeFromOrderedPoints(reverse(creationPoints));
   }
   /**
    * a⋅x+ b⋅y + c⋅z  + d  = 0
    * returns a list of "a", "b", "c" and "d"
    */
   public List<Double> getEquation(){
      return equationParams;
   }
   public Plane shift(Vector v){
      List<Point> newPoints = new ArrayList<>();
      for(Point p: creationPoints)
         newPoints.add(p.translate(v));
      return planeFromOrderedPoints(newPoints);
   }
   public Side whichSide(Point point){
      if(contains(point))
         return Side.None;
      if (normalVector.dot(point.asVector()) < dConstant)
         return Side.Negative;
      return Side.Positive;
   }
   public Point closestPoint(Point point) {
      double t = dConstant + normalVector.dot(point.asVector());
      Vector scaled = normalVector.scale(-t);
      return scaled.add(point);
   }
   /**
    * Always returns a zero or positive number. It is the absolute distance, regardless of the side of the plane
    * the point is on.
    */
   public double distanceFrom(Point point) {
      if(contains(point))
         return 0.0;
      double t = dConstant + normalVector.dot(point.asVector());
      Vector scaled = normalVector.scale(-t);
      return scaled.length();
   }
   public boolean contains(Point point) {
      double x = normalVector.x();
      double y = normalVector.y();
      double z = normalVector.z();
      double equationError = x*point.x() + y*point.y() + z*point.z() + dConstant;
      double adjustedError = adjustErrorOfPointOnPlane(point.x(), point.y(), point.z(), equationError);
      return almostZero(adjustedError);
   }
   /**
   precision, precision, precision :)

   assuming valid error per coordinate, get avg error, instead of sum of errors
   and then correct for the magnitude of the biggest coordinate
   */
   private double adjustErrorOfPointOnPlane(double x, double y, double z, double error){
      double adjustedError = error / 3.0;
      double biggestMagnitude = max(max(abs(x),abs(y)),abs(z));
      if(biggestMagnitude < 10)
         return adjustedError;
      int orderOfMagnitude = (int)floor(log10(biggestMagnitude));
      double multiplier = pow(10, -orderOfMagnitude);
      return adjustedError * multiplier;
   }
   public boolean contains(Collection<Point> points) {
      for(Point p : points)
         if(!contains(p))
            return false;
      return true;
   }
   public boolean contains(LineSegment segment) {
      return contains(segment.pointSet);
   }
   public Vector getNormal() {
      return normalVector;
   }
   /**
    * True if its the same plane regardless of direction of the normal
    */
   public boolean isSamePlaneAnyDirection(Plane other) {
      Vector otherNormal = other.getNormal();
      Vector otherNormalNegated = otherNormal.negate();
      return (normalVector.epsilonEquals(otherNormal) || normalVector.epsilonEquals(otherNormalNegated));
   }
   /**
    * "Same direction" or "same way" here means normals of both planes form an acute angle.
    *
    * It is a laxer comparison of vectors.
    *
    * Such a weird method requires an explanation:
    * For all geometry operations we allow for a small epsilon of error.
    * In the case of planes, whether another plane is facing the same direction
    * or not is determined by comparing angles of normals.
    * Should the angles comply with that epsilon, since it's a different kind of mathematical operation?
    * Sometimes we just need to know if it's a direction or its opposite, we can allow a bigger angle,
    * even if the normals are supposed to match.
    */
   public boolean approximatelyFacingTheSameWay(Plane other) {
      double dotProduct = normalVector.dot(other.normalVector);
      /**
       * A property of the dot product is that it is positive if and only if the angle between
       * said vectors is smaller than 90 deg. (or PI/2)
       */
      return dotProduct > 0;
   }
   public boolean facingAwayFromEachOther(Plane other) {
      return ! approximatelyFacingTheSameWay(other);
   }
   public List<Point> getCreationPoints(){return creationPoints;}
}
