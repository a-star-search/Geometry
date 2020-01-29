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

import com.google.common.annotations.VisibleForTesting;

import javax.vecmath.AxisAngle4d;
import javax.vecmath.Quat4d;
import static com.google.common.base.Preconditions.checkArgument;
import static com.moduleforge.libraries.geometry.Geometry.almostZero;
import static com.moduleforge.libraries.geometry.Geometry.isZero;
import static com.moduleforge.libraries.geometry._3d.CartesianAxis.*;
import static java.lang.Math.*;

/**
 * The precision of the operations of this class
 */
enum Rotator {
   INSTANCE;
   /**
    * It's the same operation as the similarly named method for vectors (notice there is no difference
    * between rotating a point and rotating a vector, but for the class' user it might be useful to think
    * in either vectors or points.
    *
    * I\m using vecmath/Quat4d instead of JME3 for improved precision in this method, with double precision results
    */
   public static Point rotatePointAroundInClockwiseDirection(Point point, CartesianAxis axis, double angle) {
      return rotateVectorAroundInClockwiseDirection(point.vectorFromOrigin(), axis, angle);
   }
   /**
    * Rotates the vector around the axis in a clockwise direction for any of the axes.
    *
    * Meaning of "clockwise direction":
    * Clockwise as it appears looking in the natural direction of the axis, that is,
    * in the direction that the coordinates grow.
    *
    * I\m using vecmath/Quat4d instead of JME3 for improved precision in this method, with double precision results
    */
   public static Vector rotateVectorAroundInClockwiseDirection(Vector vector, CartesianAxis axis, double angle) {
      if(almostZero(angle % (2 * PI)))
         return vector;
      if(axis.equals(X_AXIS))
         if(almostZero(vector.y()) && almostZero(vector.z()))
            return vector;
      if(axis.equals(Y_AXIS))
         if(almostZero(vector.x()) && almostZero(vector.z()))
            return vector;
      if(axis.equals(Z_AXIS))
         if(almostZero(vector.x()) && almostZero(vector.y()))
            return vector;
      return rotateVectorAroundInClockwiseDirectionNoChecks(vector, axis, angle);
   }
   private static Vector rotateVectorAroundInClockwiseDirectionNoChecks(Vector vector, CartesianAxis axis, double angle) {
      Quat4d q = new Quat4d();
      AxisAngle4d axisAngle = new AxisAngle4d(axis.normal.x(), axis.normal.y(), axis.normal.z(), angle);
      q.set(axisAngle);
      return mult(q, vector);
   }
   /**
    * Rotates the point counterclockwise around a vector that passes through origin.
    *
    * Counter clockwise order as observed looking in the direction of the vector
    */
   public static Point rotatePointAroundLinePassingThroughOrigin_Clockwise(Point point, Vector lineDirection, double angle) {
      Vector normalDirection = lineDirection.normalize();
      Quat4d xyRotations = doXAxisAndYAxisRotations(normalDirection);
      Point pointAfterXYRotations = mult(xyRotations, point.asVector());
      Point pointRotatedAroundZ = rotatePointAroundInClockwiseDirection(pointAfterXYRotations, Z_AXIS, angle);
      xyRotations.inverse();
      return mult(xyRotations, pointRotatedAroundZ.asVector());
   }
   @VisibleForTesting
   static Quat4d doXAxisAndYAxisRotations(Vector directionNormal) {
      //alreadyOnXAxis means nothing to rotate around X
      boolean alreadyOnXAxis = almostZero(directionNormal.y()) && almostZero(directionNormal.z());
      if(alreadyOnXAxis)
         return calculateAxisRotationQuaternion(directionNormal, Y_AXIS); //return only the y rotation quaternion
      Quat4d firstQuaternion = calculateAxisRotationQuaternion(directionNormal, X_AXIS);
      Vector rotatedVector = mult(firstQuaternion, directionNormal);
      Quat4d secondQuaternion = calculateAxisRotationQuaternion(rotatedVector, Y_AXIS);
      return mult(secondQuaternion, firstQuaternion);
   }
   /**
    * Use this method wisely:
    * 
    * The vector is supposed to pass by the origin point
    * 
    * If the axis is Y, the vector is supposed to be in XZ plane
    * 
    * There is no rotation for the Z axis
    * 
    * For the X axis, it returns a quaternion that puts the vector in XZ plane
    * For the Y axis, it returns a quaternion that puts the vector on the Z axis
    * 
    */
   @VisibleForTesting
   static Quat4d calculateAxisRotationQuaternion(Vector vector, CartesianAxis axis) {
      double axisRotation;
      if(axis.equals(X_AXIS))
         axisRotation = calculateXAxisRotationAngle(vector);
      else if(axis.equals(Y_AXIS))
         axisRotation = calculateYAxisRotationAngle(vector);
      else throw new RuntimeException("not applicable");
      Quat4d q = new Quat4d();
      AxisAngle4d axisAngle = new AxisAngle4d(axis.normal.x(), axis.normal.y(), axis.normal.z(), axisRotation);
      q.set(axisAngle);
      return q;
   }
   /**
    * This function receives a vector that passes by the origin point
    * and returns an angle in radians that
    * puts the vector in the XZ plane when rotating the space on the 'x' axis
    * 
    * The method is indifferent to the direction of the vector with which the line was created,
    * since a line has no direction. In other words, a line can be created with a vector or its
    * negated and this method will return the same value.
    * 
    *  However there's no guarantee as to which direction the vector is rotated,
    *  in other words, the only promise is that the vector will end up in the XZ plane
    * 
    * The value returned ranges between -pi and pi
    */
   @VisibleForTesting
   static double calculateXAxisRotationAngle(Vector vector) {
      double x = vector.x();
      double y = vector.y();
      double z = vector.z();
      if(x < 0 ){
         y = -y;
         z = -z;
      }
      Vector vProjectionOnYZPlane = new Vector(0, y, z).normalize();
      return signum(y) * vProjectionOnYZPlane.angle(Z_AXIS.normal);
   }
   /**
    * A rotation that puts the vector on a xz plane on the z line
    *
    * The rotation works counterclockwise when viewed from Y pos part of space (when viewed from above, if you will,
    * for a common space representation)
    *
    * For example that means that:
    *
    * p(100, 0, 0) ends in p(0, 0, -100)
    * p(1, 0, 100) ends in p(0, 0, -(~100))
    * p(-1, 0, 100) ends in p(0, 0, ~100)
    *
    */
   @VisibleForTesting
   static double calculateYAxisRotationAngle(Vector vectorOnXZPlane) {
      checkArgument(almostZero(vectorOnXZPlane.y()));
      double x = vectorOnXZPlane.x();
      double z = vectorOnXZPlane.z();
      if(isZero(x))
         return 0.0;
      if(isZero(z))
         return PI / 2.0;
      return -atan2(x, z);
   }
   //copied and adapted from JME3, don't ask me wat dis doin
   static Vector mult(Quat4d q, Vector v) {
      if (isZero(v.x()) && isZero(v.y())&& isZero(v.z()) )
         return new Vector(0.0, 0.0, 0.0);
      double vx = v.x(), vy = v.y(), vz = v.z();
      double qx = q.x, qy = q.y, qz = q.z,  qw = q.w;
      double storeX = qw*qw*vx + 2*qy*qw*vz - 2*qz*qw*vy + qx*qx*vx + 2*qy*qx*vy + 2*qz*qx*vz - qz*qz*vx - qy*qy*vx;
      double storeY = 2*qx*qy*vx + qy*qy*vy + 2*qz*qy*vz + 2*qw*qz*vx - qz*qz*vy + qw*qw*vy - 2*qx*qw*vz - qx*qx*vy;
      double storeZ = 2*qx*qz*vx + 2*qy*qz*vy + qz*qz*vz - 2*qw*qy*vx - qy*qy*vz + 2*qw*qx*vy - qx*qx*vz + qw*qw*vz;
      return new Vector(storeX, storeY, storeZ);
   }
   static Quat4d mult(Quat4d q1, Quat4d q2) {
      Quat4d res = new Quat4d();
      double qw = q2.w, qx = q2.x, qy = q2.y, qz = q2.z;
      res.x = q1.x * qw + q1.y * qz - q1.z * qy + q1.w * qx;
      res.y = -q1.x * qz + q1.y * qw + q1.z * qx + q1.w * qy;
      res.z = q1.x * qy - q1.y * qx + q1.z * qw + q1.w * qz;
      res.w = -q1.x * qx - q1.y * qy - q1.z * qz + q1.w * qw;
      return res;
   }
}
