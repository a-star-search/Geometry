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

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static com.moduleforge.libraries.geometry.Geometry.almostZero;
import static com.moduleforge.libraries.geometry.Geometry.epsilonEquals;
import static com.moduleforge.libraries.geometry._3d.Rotator.rotatePointAroundLinePassingThroughOrigin_Clockwise;
import static java.lang.Math.PI;
import static java.lang.Math.sin;

public class Line {
   private static final Point COORD_SYSTEM_ORIGIN = new Point(0, 0, 0);
   private Point origin;
   /** it's a normal */
   private Vector direction;
   public static final Line X_AXIS = linePassingBy(COORD_SYSTEM_ORIGIN, new Point(1, 0, 0));
   public static final Line Y_AXIS = linePassingBy(COORD_SYSTEM_ORIGIN, new Point(0, 1, 0));
   public static final Line Z_AXIS = linePassingBy(COORD_SYSTEM_ORIGIN, new Point(0, 0, 1));
   /**
    * returns a line that fits these two points
    */
   public static Line linePassingBy(Point originPoint, Point finalPoint) {
      Vector _directionNormal = originPoint.vectorTo(finalPoint).normalize();
      return new Line(originPoint, _directionNormal);
   }
   /**
    * origin and direction are set to (0,0,0).
    *
    */
   private Line() {
   }
   /**
    * @param origin the origin of the line.
    * @param directionNormal the direction of the line.
    */
   private Line(Point origin, Vector directionNormal) {
      if(!almostZero(directionNormal.length() - 1.0))
         throw new IllegalArgumentException("Vector not normalized.");
      this.origin = origin;
      this.direction = directionNormal;
   }
   /** is a normal */
   public Vector getDirection() {
      return direction;
   }
   public boolean containsAll(List<Point> points) {
      for(Point point : points)
         if(! point.epsilonEquals(closestPoint(point)))
            return false;
      return true;
   }
   public boolean containsAll(Set<Point> points) {
      return containsAll(new ArrayList<>(points));
   }
   public boolean contains(Point point, double precision) {
      return point.epsilonEquals(closestPoint(point), precision);
   }
   public boolean contains(Point point) {
      return point.epsilonEquals(closestPoint(point));
   }
   public double distanceWith(Point point) {
      return point.distance(closestPoint(point));
   }
   /**
    * return the closest point in the line to the point passed as argument
    */
   // this formula could also be used: https://math.stackexchange.com/questions/1521128/given-a-line-and-a-point-in-3d-how-to-find-the-closest-point-on-the-line
   // but currently it works with good precision
   public Point closestPoint(Point p){
      if(p.epsilonEquals(origin))
         return p;
      Vector fromOrigin = origin.vectorTo(p);
      double angle = fromOrigin.angle(direction);
      if(epsilonEquals(angle, PI / 2))
         return origin;
      boolean oppositeDirection = angle > PI / 2;
      if(oppositeDirection)
         angle = PI - angle;
      //using the sin rule for triangles
      double distanceToClosest = p.distance(origin) * sin((PI/2) - angle);
      if(oppositeDirection)
         return origin.translate(direction.negate().scale(distanceToClosest));
      return origin.translate(direction.scale(distanceToClosest));
   }
   /**
    * Exactly the same as moving the point across the line
    * Including this function just for the case
    * it makes some code more readable that calling it "move across line"
    */
   public Point rotatePointPIRadians(Point point){
      return movePointAcross(point);
   }
   /**
    * Rotates the point pi rad, or 180 grad or accross the line.
    */
   public Point movePointAcross(Point point){
      if(contains(point))
         return point;
      Point closest = closestPoint(point);
      Vector toTheLine = point.vectorTo(closest);
      return closest.translate(toTheLine);
   }
   /**
    * If the point is on the line, just return the same point.
    *
    * The vector argument might be confusing, since a vector can go in many directions. One possibility could have
    * been to restrict it to the two possible vectors that are tangential to the circular rotation movement.
    *
    * In this case the direction of rotation is simply that where the point ends up closer to the position if would
    * have ended up for the same distance travelled but in a straight line in the vector's direction.
    *
    * A big reason why this method is convenient is that usually the line and point are on the same plane, and a
    * plane has a normal that can readily be used as the rotation vector.
    *
    * This might just be practical for some users.
    *
    */
   public Point rotatePointAround(Point point, double angle, Vector rotationDirection) {
      if(angle <= -2 * PI)
         throw new RuntimeException("Why?, seriously.");
      if(angle < 0)
         angle += 2 * PI;
      double adjustedAngle = angle % (2 * PI);
      Point p = rotatePointAround_SpecialCases(point, adjustedAngle);
      boolean isSpecialCase = p != null;
      if(isSpecialCase) return p;
      return rotatePointAroundNoChecks(point, adjustedAngle, rotationDirection);
   }
   /**
    * In this function, instead of receiving the approximate direction in which a point rotates,
    * we are told something much more orthodox and elegant: to rotate clockwise as indicated by the direction of this line
    *
    * Clockwise order in relation to the direction of the line that the interface of this class exposes.
    *
    * If the angle is negative, then is the same as if rotating counter clockwise with a positive angle.
    *
    */
   public Point rotatePointAroundClockwise(Point point, double angle) {
      if(angle <= -2 * PI)
         throw new RuntimeException("really?");
      if(angle < 0)
         angle += 2 * PI;
      double adjustedAngle = angle % (2 * PI);
      Point p = rotatePointAround_SpecialCases(point, adjustedAngle);
      boolean isSpecialCase = p != null;
      if(isSpecialCase) return p;
      return rotatePointAroundClockwiseNoChecks(point, adjustedAngle);
   }
   private Point rotatePointAround_SpecialCases(Point point, double adjustedAngle){
      if(contains(point))
         return point;
      if(epsilonEquals(adjustedAngle, 0.0))
         return point;
      if(epsilonEquals(adjustedAngle, PI))
         return movePointAcross(point);
      return null;
   }
   //angle will be between 0 and 2PI (both ends excluded)
   private Point rotatePointAroundClockwiseNoChecks(Point point, double angle) {
      //I'm sure there is a more lightweight implementation than this
      Point rotated = translateToLinePassingThroughOriginAndRotate(point, -angle);
      Point closest = closestPoint(point);
      Vector toPoint = closest.vectorTo(point);
      Vector toRotated = closest.vectorTo(rotated);
      Vector crossProduct = toPoint.cross(toRotated);
      if(angle > PI)
         crossProduct = toRotated.cross(toPoint);
      boolean correctRotation = crossProduct.normalize().distance(direction) < 1.0;
      if(correctRotation)
         return rotated;
      return translateToLinePassingThroughOriginAndRotate(point, angle);
   }
   /*
   A note about the implementation:
   There are two ways to do this. One is calculate the clockwise order of the rotation given by the vector with respect
   to the line so that we can do the same rotation after the coordinate transformation.
   The other is to do rotations both ways and calculate which one it is, based on the distance as if it had traveled
   in a straight line. This way is simpler if less elegant, and that's the way it's implemented, unless I changed it
   and forgot to update this comment.
    */
   private Point rotatePointAroundNoChecks(Point point, double angle, Vector rotationDirection) {
      Point rotated1 = translateToLinePassingThroughOriginAndRotate(point, angle);
      Point rotated2 = translateToLinePassingThroughOriginAndRotate(point, -angle);
      Point pointForComparison = makePointForRotationDirectionComparison(point, angle, rotationDirection);
      //Total KLUDGE but I was struggling with the right formula for the direction of the rotation, so I try both
      if(pointForComparison.distance(rotated1) < pointForComparison.distance(rotated2))
         return rotated1;
      return rotated2;
   }
   private Point makePointForRotationDirectionComparison(Point pointToRotate, double angle, Vector rotationDirection) {
      double radius = distanceWith(pointToRotate);
      double distanceRotated = radius * angle;
      return pointToRotate.translate(rotationDirection.withLength(distanceRotated));
   }
   private Point translateToLinePassingThroughOriginAndRotate(Point point, double angle) {
      if(contains(COORD_SYSTEM_ORIGIN))
         return rotatePointAroundLinePassingThroughOrigin_Clockwise(point, direction, angle);
      Point closestToOriginInLine = closestPoint(COORD_SYSTEM_ORIGIN);
      Point relativeToALineThatPassesByOrigin = closestToOriginInLine.vectorTo(point);
      Vector restorationVector = closestToOriginInLine.vectorFromOrigin();
      Point rotatedAroundOrigin =
              rotatePointAroundLinePassingThroughOrigin_Clockwise(relativeToALineThatPassesByOrigin, direction, angle);
      return rotatedAroundOrigin.translate(restorationVector);
   }
}
