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

import org.junit.Test;

import javax.vecmath.Quat4d;
import java.util.List;

import static com.moduleforge.libraries.geometry.Geometry.almostZero;
import static com.moduleforge.libraries.geometry.Geometry.epsilonEquals;
import static com.moduleforge.libraries.geometry._3d.CartesianAxis.*;
import static com.moduleforge.libraries.geometry._3d.Rotator.*;
import static java.lang.Math.PI;
import static java.lang.Math.abs;
import static java.util.Arrays.asList;
import static org.junit.Assert.*;

@SuppressWarnings({ "static-method", "nls" })
public class RotatorTest {
   private static final double STRAIGHT_ANGLE = PI / 2.0; //90 degrees
   private static final Point ORIGIN = new Point(0, 0, 0);

   /* to visualize and confirm the following test, I advise using a piece of paper and patiently drawing
   four points around an axis for each axis and thinking what the clockwise direction would be, when we rotated each
   point 90 deg (or PI/2 rad)
    */
   @Test
   public void rotationsAroundAxes_Calculated() {
      Vector v1AlongX = new Vector(10, 0, 1);
      Vector v2AlongX = new Vector(10, 1, 0);
      Vector v3AlongX = new Vector(10, 0, -1);
      Vector v4AlongX = new Vector(10, -1, 0);
      List<Vector> aroundXAxisClockwise = asList(v1AlongX, v4AlongX, v3AlongX, v2AlongX);
      for(int i = 0; i < aroundXAxisClockwise.size(); i++) {
         Vector currentRotated = rotateVectorAroundInClockwiseDirection(aroundXAxisClockwise.get(i), X_AXIS, STRAIGHT_ANGLE);
         Vector next = aroundXAxisClockwise.get((i + 1) % aroundXAxisClockwise.size());
         assertTrue(currentRotated.epsilonEquals(next));
      }
      Vector v1AlongY = new Vector(0, 10, 1);
      Vector v2AlongY = new Vector(1, 10, 0);
      Vector v3AlongY = new Vector(0, 10, -1);
      Vector v4AlongY = new Vector(-1, 10, 0);
      List<Vector> aroundYAxisClockwise = asList(v1AlongY, v2AlongY, v3AlongY, v4AlongY);
      for(int i = 0; i < aroundYAxisClockwise.size(); i++) {
         Vector currentRotated = rotateVectorAroundInClockwiseDirection(aroundYAxisClockwise.get(i), Y_AXIS, STRAIGHT_ANGLE);
         Vector next = aroundYAxisClockwise.get((i + 1) % aroundYAxisClockwise.size());
         assertTrue(currentRotated.epsilonEquals(next));
      }
      Vector v1AlongZ = new Vector(0, 1, 10);
      Vector v2AlongZ = new Vector(1, 0, 10);
      Vector v3AlongZ = new Vector(0, -1, 10);
      Vector v4AlongZ = new Vector(-1, 0, 10);
      List<Vector> aroundZAxisClockwise = asList(v1AlongZ, v4AlongZ, v3AlongZ, v2AlongZ);
      for(int i = 0; i < aroundZAxisClockwise.size(); i++) {
         Vector currentRotated = rotateVectorAroundInClockwiseDirection(aroundZAxisClockwise.get(i), Z_AXIS, STRAIGHT_ANGLE);
         Vector next = aroundZAxisClockwise.get((i + 1) % aroundZAxisClockwise.size());
         assertTrue(currentRotated.epsilonEquals(next));
      }
   }
   //same examples as the previous test but on the negative part of the axis
   //nothing should change, except the coordinate to put it in the negative part of the space
   // the rotation direction is the same (ie not specular or any other, but simply the same)
   @Test
   public void rotationsAroundAxes_NegativePartOfTheAxis_Calculated() {
      Vector v1AlongX = new Vector(-10, 0, 1);
      Vector v2AlongX = new Vector(-10, 1, 0);
      Vector v3AlongX = new Vector(-10, 0, -1);
      Vector v4AlongX = new Vector(-10, -1, 0);
      List<Vector> aroundXAxisClockwise = asList(v1AlongX, v4AlongX, v3AlongX, v2AlongX);
      for(int i = 0; i < aroundXAxisClockwise.size(); i++) {
         Vector currentRotated = rotateVectorAroundInClockwiseDirection(aroundXAxisClockwise.get(i), X_AXIS, STRAIGHT_ANGLE);
         Vector next = aroundXAxisClockwise.get((i + 1) % aroundXAxisClockwise.size());
         assertTrue(currentRotated.epsilonEquals(next));
      }
      Vector v1AlongY = new Vector(0, -10, 1);
      Vector v2AlongY = new Vector(1, -10, 0);
      Vector v3AlongY = new Vector(0, -10, -1);
      Vector v4AlongY = new Vector(-1, -10, 0);
      List<Vector> aroundYAxisClockwise = asList(v1AlongY, v2AlongY, v3AlongY, v4AlongY);
      for(int i = 0; i < aroundYAxisClockwise.size(); i++) {
         Vector currentRotated = rotateVectorAroundInClockwiseDirection(aroundYAxisClockwise.get(i), Y_AXIS, STRAIGHT_ANGLE);
         Vector next = aroundYAxisClockwise.get((i + 1) % aroundYAxisClockwise.size());
         assertTrue(currentRotated.epsilonEquals(next));
      }
      Vector v1AlongZ = new Vector(0, 1, -10);
      Vector v2AlongZ = new Vector(1, 0, -10);
      Vector v3AlongZ = new Vector(0, -1, -10);
      Vector v4AlongZ = new Vector(-1, 0, -10);
      List<Vector> aroundZAxisClockwise = asList(v1AlongZ, v4AlongZ, v3AlongZ, v2AlongZ);
      for(int i = 0; i < aroundZAxisClockwise.size(); i++) {
         Vector currentRotated = rotateVectorAroundInClockwiseDirection(aroundZAxisClockwise.get(i), Z_AXIS, STRAIGHT_ANGLE);
         Vector next = aroundZAxisClockwise.get((i + 1) % aroundZAxisClockwise.size());
         assertTrue(currentRotated.epsilonEquals(next));
      }
   }
   @Test
   public void rotationAroundAxis_AngleBiggerThan2PI_Calculated() {
      Vector v = new Vector(1, 1, 1);
      double randomSmallAngle = PI / 5;
      Vector rotated = rotateVectorAroundInClockwiseDirection(v, X_AXIS, randomSmallAngle);
      Vector rotatedPlus2PI = rotateVectorAroundInClockwiseDirection(v, X_AXIS, randomSmallAngle + 2 * PI);
      assertTrue(rotated.epsilonEquals(rotatedPlus2PI));
   }
   @Test
   public void rotationsAroundAxis_NegativeAngle_Calculated() {
      Vector v = new Vector(10, 0, 1);
      double randomSmallNegativeAngle = -PI / 5;
      double positiveEquivalentAngle = (2 * PI) + randomSmallNegativeAngle;
      Vector rotated1 = rotateVectorAroundInClockwiseDirection(v, X_AXIS, randomSmallNegativeAngle);
      Vector rotated2 = rotateVectorAroundInClockwiseDirection(v, X_AXIS, positiveEquivalentAngle);
      assertTrue(rotated1.epsilonEquals(rotated2));
   }
   @Test
   public void calculateYAxisRotationAngle_VectorInXZPositiveRegion_XYCoordsAreZeroAndZCoordNonZero() {
      Vector vectorOnXZPlane = new Vector(3, 0, 3); //arbitrary length of vector
      double rotation = calculateYAxisRotationAngle(vectorOnXZPlane);
      Vector rotated = rotateVectorAroundInClockwiseDirection(vectorOnXZPlane, Y_AXIS, rotation);
      assertTrue("rotated.x: " + rotated.x(), almostZero(rotated.x()));
      assertTrue("rotated.y: " + rotated.y(), almostZero(rotated.y()));
      assertFalse("rotated.z: " + rotated.z(), almostZero(rotated.z()));
   }
   @Test
   public void calculateYAxisRotationAngle_VectorXPositiveZNegativeRegion_XYCoordsAreZeroAndZCoordNonZero() {
      Vector vectorOnXZPlane = new Vector(3, 0, -3); //arbitrary length of vector
      double rotation = calculateYAxisRotationAngle(vectorOnXZPlane);
      Vector rotated = rotateVectorAroundInClockwiseDirection(vectorOnXZPlane, Y_AXIS, rotation);
      assertTrue("rotated.y: " + rotated.y(), almostZero(rotated.y()));
      assertTrue("rotated.x: " + rotated.x(), almostZero(rotated.x()));
      assertFalse("rotated.z: " + rotated.z(), almostZero(rotated.z()));
   }
   @Test
   public void calculateYAxisRotationAngle_VectorXZNegativeRegion_XYCoordsAreZeroAndZCoordNonZero() {
      Vector vectorOnXZPlane = new Vector(-3, 0, -3); //arbitrary length of vector
      double rotation = calculateYAxisRotationAngle(vectorOnXZPlane);
      Vector rotated = rotateVectorAroundInClockwiseDirection(vectorOnXZPlane, Y_AXIS, rotation);
      assertTrue("rotated.y: " + rotated.y(), almostZero(rotated.y()));
      assertTrue("rotated.x: " + rotated.x(), almostZero(rotated.x()));
      assertFalse("rotated.z: " + rotated.z(), almostZero(rotated.z()));
   }
   @Test
   public void calculateYAxisRotationAngle_VectorOnXAxis_RotateStraightAngle() {
      Vector vectorOnXZPlane = new Vector(-3, 0, 0); //arbitrary length of vector
      double rotation = calculateYAxisRotationAngle(vectorOnXZPlane);
      assertTrue("rotation " + rotation, epsilonEquals(abs(rotation), PI / 2.0));
   }
   @Test
   public void calculateYAxisRotationAngle_VectorLengthIsSame() {
      Vector vectorOnXZPlane = new Vector(4, 0, 5); //arbitrary length of vector
      double rotation = calculateYAxisRotationAngle(vectorOnXZPlane);
      Vector rotated = rotateVectorAroundInClockwiseDirection(vectorOnXZPlane, Y_AXIS, rotation);
      assertTrue(epsilonEquals(vectorOnXZPlane.length(), rotated.length()));
   }
   @Test
   public void calculateXAxisRotationAngle_VectorInXYZPositiveRegion_YCoordIsZeroAndXZCoordNonZero() {
      Vector vector = new Vector(3, 4, 7); //arbitrary length of vector
      double rotation = calculateXAxisRotationAngle(vector);
      Vector rotated = rotateVectorAroundInClockwiseDirection(vector, X_AXIS, rotation);
      assertFalse("rotated.x: " + rotated.x(), almostZero(rotated.x()));
      assertTrue("rotated.y: " + rotated.y(), almostZero(rotated.y()));
      assertFalse("rotated.z: " + rotated.z(), almostZero(rotated.z()));
   }
   @Test
   public void calculateXAxisRotationAngle_VectorInXYPositiveZNegativeRegion_YCoordIsZeroAndXZCoordNonZero() {
      Vector vector = new Vector(3, 4, -7); //arbitrary length of vector
      double rotation = calculateXAxisRotationAngle(vector);
      Vector rotated = rotateVectorAroundInClockwiseDirection(vector, X_AXIS, rotation);
      assertFalse("rotated.x: " + rotated.x(), almostZero(rotated.x()));
      assertTrue("rotated.y: " + rotated.y(), almostZero(rotated.y()));
      assertFalse("rotated.z: " + rotated.z(), almostZero(rotated.z()));
   }
   @Test
   public void calculateXAxisRotationAngle_VectorInXPositiveYZNegativeRegion_YCoordIsZeroAndXZCoordNonZero() {
      Vector vector = new Vector(3, -4, -7); //arbitrary length of vector
      double rotation = calculateXAxisRotationAngle(vector);
      Vector rotated = rotateVectorAroundInClockwiseDirection(vector, X_AXIS, rotation);
      assertFalse("rotated.x: " + rotated.x(), almostZero(rotated.x()));
      assertTrue("rotated.y: " + rotated.y(), almostZero(rotated.y()));
      assertFalse("rotated.z: " + rotated.z(), almostZero(rotated.z()));
   }
   @Test
   public void calculateXAxisRotationAngle_VectorInXYZNegativeRegion_YCoordIsZeroAndXZCoordNonZero() {
      Vector vector = new Vector(-3, -4, -7); //arbitrary length of vector
      double rotation = calculateXAxisRotationAngle(vector);
      Vector rotated = rotateVectorAroundInClockwiseDirection(vector, X_AXIS, rotation);
      assertFalse("rotated.x: " + rotated.x(), almostZero(rotated.x()));
      assertTrue("rotated.y: " + rotated.y(), almostZero(rotated.y()));
      assertFalse("rotated.z: " + rotated.z(), almostZero(rotated.z()));
   }
   @Test
   public void calculateXAxisRotationAngle_VectorInZPositiveXYNegativeRegion_YCoordIsZeroAndXZCoordNonZero() {
      Vector vector = new Vector(-3, -4, 7); //arbitrary length of vector
      double rotation = calculateXAxisRotationAngle(vector);
      Vector rotated = rotateVectorAroundInClockwiseDirection(vector, X_AXIS, rotation);
      assertFalse("rotated.x: " + rotated.x(), almostZero(rotated.x()));
      assertTrue("rotated.y: " + rotated.y(), almostZero(rotated.y()));
      assertFalse("rotated.z: " + rotated.z(), almostZero(rotated.z()));
   }
   @Test
   public void calculateXAxisRotationAngle_VectorInXZPositiveYNegativeRegion_YCoordIsZeroAndXZCoordNonZero() {
      Vector vector = new Vector(3, -4, 7); //arbitrary length of vector
      double rotation = calculateXAxisRotationAngle(vector);
      Vector rotated = rotateVectorAroundInClockwiseDirection(vector, X_AXIS, rotation);
      assertFalse("rotated.x: " + rotated.x(), almostZero(rotated.x()));
      assertTrue("rotated.y: " + rotated.y(), almostZero(rotated.y()));
      assertFalse("rotated.z: " + rotated.z(), almostZero(rotated.z()));
   }
   @Test
   public void calculateXAxisRotationAngle_VectorInXZPlane_RotationIsZero() {
      Vector vector = new Vector(3, 0, 7); //arbitrary length of vector
      double rotation = calculateXAxisRotationAngle(vector);
      assertTrue(almostZero(rotation));
   }
   @Test
   public void calculateXAxisRotationAngle_VectorInXYPlane_RotationIsHalfPI() {
      Vector vector = new Vector(3, 2, 0); //arbitrary length of vector
      double absRotation = abs(calculateXAxisRotationAngle(vector));
      assertTrue(epsilonEquals(absRotation, PI / 2));
   }
   @Test
   public void calculateXAxisRotationAngle_VectorYAndZSame_RotationIsQuaterPIOrThreeQuartersPI() {
      Vector vector = new Vector(3, 7, 7); //arbitrary length of vector
      double absRotation = abs(calculateXAxisRotationAngle(vector));
      assertTrue(epsilonEquals(absRotation, PI / 4) || epsilonEquals(absRotation, PI * 3 / 4) );
   }
   @Test
   public void calculateXAxisRotationAngle_VectorYAndZSameModButNegated_RotationIsQuaterPIOrThreeQuartersPI() {
      Vector vector = new Vector(3, -7, 7); //arbitrary length of vector
      double absRotation = abs(calculateXAxisRotationAngle(vector));
      assertTrue(epsilonEquals(absRotation, PI / 4) || epsilonEquals(absRotation, PI * 3 / 4) );
   }
   @Test
   public void calculateXAxisRotationAngle_SameVectorLength() {
      Vector vector = new Vector(3, 4, 7); //arbitrary length of vector
      double rotation = calculateXAxisRotationAngle(vector);
      Vector rotated = rotateVectorAroundInClockwiseDirection(vector, X_AXIS, rotation);
      assertTrue(epsilonEquals(vector.length(), rotated.length()));
   }
   @Test
   public void calculateAxisRotationQuaternion_RandomPointInLineThatContainsVectorFromWhichQuaternionsAreCalculated_IsRotatedCorrectly() {
      Vector vector = new Vector(4, 2, 1);
      Point pointOnLine = new Point(13.6, 6.8, 3.4);
      Quat4d firstQuaternion = calculateAxisRotationQuaternion(vector, X_AXIS);
      Vector rotatedVector = mult(firstQuaternion, vector);
      Quat4d secondQuaternion = calculateAxisRotationQuaternion(rotatedVector , Y_AXIS);
      Quat4d combinedQuaternion = mult(secondQuaternion, firstQuaternion);
      Vector secondRotatedPoint = mult(combinedQuaternion, pointOnLine.asVector());
      assertTrue(almostZero(secondRotatedPoint.x()));
      assertTrue(almostZero(secondRotatedPoint.y()));
      assertFalse(almostZero(secondRotatedPoint.z()));
      assertTrue(epsilonEquals(pointOnLine.asVector().length(), secondRotatedPoint.length() ));
   }
   @Test
   public void calculateAxisRotationQuaternion_RandomPointInLineThatContainsVectorFromWhichQuaternionsAreCalculated_IsRotatedCorrectly2() {
      Vector vector = new Vector(4, 2, 1);
      Quat4d firstQuaternion = calculateAxisRotationQuaternion(vector, X_AXIS);
      Vector rotatedVector = mult(firstQuaternion, vector);
      Quat4d secondQuaternion = calculateAxisRotationQuaternion(rotatedVector, Y_AXIS);
      Quat4d combinedQuaternion = mult(secondQuaternion, firstQuaternion);
      Point pointOnLine = new Point(-2, -1, -0.5);
      Vector secondRotatedPoint = mult(combinedQuaternion, pointOnLine.asVector());
      assertTrue(almostZero(secondRotatedPoint.x()));
      assertTrue(almostZero(secondRotatedPoint.y()));
      assertFalse(almostZero(secondRotatedPoint.z()));
      assertTrue(epsilonEquals(pointOnLine.asVector().length(), secondRotatedPoint.length() ));
   }
}
