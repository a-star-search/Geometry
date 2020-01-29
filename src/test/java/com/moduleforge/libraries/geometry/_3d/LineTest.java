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

/*
 * Copyright (c) 2018.  This file is subject to the terms and conditions defined in file 'LICENSE.txt', which is part of this source code package.
 */

package com.moduleforge.libraries.geometry._3d;

import org.junit.Test;

import static com.moduleforge.libraries.geometry.Geometry.almostZero;
import static com.moduleforge.libraries.geometry.GeometryConstants.TOLERANCE_EPSILON;
import static com.moduleforge.libraries.geometry._3d.Line.linePassingBy;
import static java.lang.Math.PI;
import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.closeTo;
import static org.junit.Assert.*;

public class LineTest {
   private static final double STRAIGHT_ANGLE = PI / 2.0;
   @Test
   public void contains_WhenPointInLine_ReturnsTrue(){
      //line defined by two random points
      Point a = new Point(4, 32, -9);
      Point b = new Point(-5, 2.8, 0.1);
      Point midpoint = Point.midPoint(a, b);
      Line l = linePassingBy(a, b);
      assertTrue(l.contains(midpoint));
   }
   @Test
   public void contains_WhenPointNotInLine_ReturnsFalse(){
      //line defined by two random points
      Point a = new Point(4, 32, -9);
      Point b = new Point(-5, 2.8, 0.1);
      Vector aToB = a.vectorTo(b);
      Vector aToPointNotInLine = new Vector(aToB.x(), aToB.y(), aToB.z() + 1); //mess up one of the coordinates
      Point notInLine = a.translate(aToPointNotInLine);
      Line l = linePassingBy(a, b);
      assertFalse(l.contains(notInLine));
   }
   @Test
   public void containsAll_WhenListOfCreationPointsAndItsMidpoint_True(){
      //line defined by two random points
      Point a = new Point(4, 32, -9);
      Point b = new Point(-5, 2.8, 0.1);
      Point midpoint = Point.midPoint(a, b);
      Line l = linePassingBy(a, b);
      assertTrue(l.containsAll(asList(a, b, midpoint)));
   }
   @Test
   public void movePointAcross_XAxis_Calculated(){
      Line xAxis = linePassingBy(new Point(0, 0, 0), new Point(1, 0, 0));
      Point randomPoint = new Point(-4, 9, 2);
      Point movedAcross = xAxis.movePointAcross(randomPoint);
      assertThat(randomPoint.x(), closeTo(movedAcross.x(), TOLERANCE_EPSILON));
      assertThat(-1.0 * randomPoint.y(), closeTo(movedAcross.y(), TOLERANCE_EPSILON));
      assertThat(-1.0 * randomPoint.z(), closeTo(movedAcross.z(), TOLERANCE_EPSILON));
   }
   @Test
   public void movePointAcross_YAxis_Calculated(){
      Line yAxis = linePassingBy(new Point(0, 0, 0), new Point(0, 1, 0));
      Point randomPoint = new Point(-4, 9, 2);
      Point movedAcross = yAxis.movePointAcross(randomPoint);
      assertThat(randomPoint.y(), closeTo(movedAcross.y(), TOLERANCE_EPSILON));
      assertThat(-1.0 * randomPoint.x(), closeTo(movedAcross.x(), TOLERANCE_EPSILON));
      assertThat(-1.0 * randomPoint.z(), closeTo(movedAcross.z(), TOLERANCE_EPSILON));
   }
   @Test
   public void movePointAcross_ZAxis_Calculated(){
      Line zAxis = linePassingBy(new Point(0, 0, 0), new Point(0, 0, 1));
      Point randomPoint = new Point(-4, 9, 2);
      Point movedAcross = zAxis.movePointAcross(randomPoint);
      assertThat(randomPoint.z(), closeTo(movedAcross.z(), TOLERANCE_EPSILON));
      assertThat(-1.0 * randomPoint.x(), closeTo(movedAcross.x(), TOLERANCE_EPSILON));
      assertThat(-1.0 * randomPoint.y(), closeTo(movedAcross.y(), TOLERANCE_EPSILON));
   }
   @Test
   public void rotatePointAround_WhenLineIsYAxis_PointIsOnZAxis_AndStraightAngle_RotatedToXAxis(){
      Line yAxis = linePassingBy(new Point(0, 0, 0), new Point(0, 1, 0));
      Point pointOnZAxis = new Point(0, 0, 1);
      Vector towardsXPos = new Vector(1, 0, 0);
      Vector towardsXNeg = new Vector(-1, 0, 0);
      Point rotatedToXPos = yAxis.rotatePointAround(pointOnZAxis, STRAIGHT_ANGLE, towardsXPos);
      assertTrue(rotatedToXPos.epsilonEquals(new Point(1, 0, 0)));
      Point rotatedToXNeg = yAxis.rotatePointAround(pointOnZAxis, STRAIGHT_ANGLE, towardsXNeg);
      assertTrue(rotatedToXNeg.epsilonEquals(new Point(-1, 0, 0)));
   }
   //as the previous but line and point are shifted
   @Test
   public void rotatePointAround_WhenLineDoesNotPassThroughOrigin_RotatedCorrectly(){
      Line parallelToYAxis = linePassingBy(new Point(1, 0, 0), new Point(1, 1, 0));
      Point p = new Point(1, 0, 1);
      Vector towardsXPos = new Vector(1, 0, 0);
      Vector towardsXNeg = new Vector(-1, 0, 0);
      Point rotatedToXPos = parallelToYAxis.rotatePointAround(p, STRAIGHT_ANGLE, towardsXPos);
      assertTrue(rotatedToXPos.epsilonEquals(new Point(2, 0, 0)));
      Point rotatedToXNeg = parallelToYAxis.rotatePointAround(p, STRAIGHT_ANGLE, towardsXNeg);
      assertTrue(rotatedToXNeg.epsilonEquals(new Point(0, 0, 0)));
   }
   //as the previous but line and point are shifted
   @Test
   public void rotatePointAround_WhenLineDoesNotPassThroughOrigin_RotatedCorrectly_SecondCase(){
      Line parallelToYAxis = linePassingBy(new Point(-1, -1, 10), new Point(-1, 0, 10)); //notice same slope as in previous cases, that is, along y axis
      Point p = new Point(-1, -3, 11); //y will remain unchanged rotating along this line
                                                // z will decrease by 1 when rotating straight angle
      Vector towardsXPos = new Vector(1, 0, 0);
      Vector towardsXNeg = new Vector(-1, 0, 0);
      Point rotatedToXPos = parallelToYAxis.rotatePointAround(p, STRAIGHT_ANGLE, towardsXPos);
      assertTrue(rotatedToXPos.epsilonEquals(new Point(0, -3, 10)));
      Point rotatedToXNeg = parallelToYAxis.rotatePointAround(p, STRAIGHT_ANGLE, towardsXNeg);
      assertTrue(rotatedToXNeg.epsilonEquals(new Point(-2, -3, 10)));
   }
   @Test
   public void rotatePointAround_WhenPointIsOnLine_ReturnsSamePoint(){
      //line defined by two random points
      Point a = new Point(4, 32, -9);
      Point b = new Point(-5, 2.8, 0.1);
      Point midpoint = Point.midPoint(a, b);
      Line l = linePassingBy(a, b);
      Point rotated = l.rotatePointAround(midpoint, 4.3, new Vector());
      assertEquals(midpoint, rotated);
   }
   @Test
   public void testClosestPoint_ShouldBeCloserThanPointsUpOrDownTheLineFromIt(){
      Line l = linePassingBy(new Point(-2, 10, 0), new Point(1, 1, 1));
      /*
      I'm afraid this is an ugly way to check it.
      We are testing a method that has no reason ever not to return a number with double precision.
      That's the highest possible precision for floating points, any tolerance epsilon is definitely
      bigger than that and thus, a valid number to test as increment.
       */
      Point p = new Point(1, 0.5, 2);
      Point closestPoint = l.closestPoint(p);
      //this is the minimum increment that produces detectable square distance change for double precis, you can do the math
      //and with some luck detectable distance too (that is, doing sqrt)
      //it is approximately the sqrt of double precision which is approximately 16 digits or 1E-16 for small numbers
      //like in this example
      double incrementAlongLine = 1e-7;
      Vector v1 = l.getDirection().scale(incrementAlongLine);
      Point closeToClosestInOneDirection = closestPoint.translate(v1);
      Vector v2 = l.getDirection().negate().scale(incrementAlongLine);
      Point closeToClosestInTheOtherDirection = closestPoint.translate(v2);
      double distance = closestPoint.distance(p);
      double closeToClosestInOneDirectionDistance = closeToClosestInOneDirection.distance(p);
      double closeToClosestInTheOtherDirectionDistance = closeToClosestInTheOtherDirection.distance(p);
      assertTrue(distance < closeToClosestInOneDirectionDistance);
      assertTrue(distance < closeToClosestInTheOtherDirectionDistance);
   }
   @Test
   public void testClosestPoint_PointInTheLine_ShouldReturnSame(){
      Line l = linePassingBy(new Point(0, 0, 0), new Point(1, 0.5, 1));
      Point p = new Point(0.7, 0.7 * 0.5, 0.7);
      Point closestPoint = l.closestPoint(p);
      //we can ask from double precision if it's on the line
      assertTrue(closestPoint.epsilonEquals(p));
   }
   @Test
   public void clockwisePointRotationsAroundXAxis_XPositiveDirection_Calculated(){
      Line l = linePassingBy(new Point(0,0,0), new Point(1,0,0));
      Point p = new Point(1, 0, 1);
      Point rotated = l.rotatePointAroundClockwise(p, STRAIGHT_ANGLE);
      Point expected = new Point(1, -1, 0);
      assertTrue(almostZero(rotated.distance(expected)));
      Point pXNeg = new Point(-1, 0, 1);
      Point rotatedXNeg = l.rotatePointAroundClockwise(pXNeg, STRAIGHT_ANGLE);
      expected = new Point(-1, -1, 0);
      assertTrue(almostZero(rotatedXNeg.distance(expected)));
   }
   @Test
   public void clockwisePointRotationsAroundXAxis_XPositiveDirection_NegativeAngle_Calculated(){
      Line l = linePassingBy(new Point(0,0,0), new Point(1,0,0));
      Point p = new Point(1, 0, 1);
      Point rotated = l.rotatePointAroundClockwise(p, 3 * STRAIGHT_ANGLE);
      Point expected = new Point(1, 1, 0);
      assertTrue(almostZero(rotated.distance(expected)));
      Point pXNeg = new Point(-1, 0, 1);
      Point rotatedXNeg = l.rotatePointAroundClockwise(pXNeg, -STRAIGHT_ANGLE);
      expected = new Point(-1, 1, 0);
      assertTrue(almostZero(rotatedXNeg.distance(expected)));
   }
   @Test
   public void clockwisePointRotationsAroundXAxis_XNegativeDirection_Calculated(){
      Line l = linePassingBy(new Point(0,0,0), new Point(-1,0,0));
      Point p = new Point(1, 0, 1);
      Point rotated = l.rotatePointAroundClockwise(p, STRAIGHT_ANGLE);
      Point expected = new Point(1, 1, 0);
      assertTrue(almostZero(rotated.distance(expected)));
   }
   @Test
   public void clockwisePointRotationsAroundXAxis_XNegativeDirection_NegativeAngle_Calculated(){
      Line l = linePassingBy(new Point(0,0,0), new Point(-1,0,0));
      Point p = new Point(1, 0, 1);
      Point rotated = l.rotatePointAroundClockwise(p, -STRAIGHT_ANGLE);
      Point expected = new Point(1, -1, 0);
      assertTrue(almostZero(rotated.distance(expected)));
   }
   @Test
   public void clockwisePointRotationsAroundYAxis_Calculated(){
      Line l = linePassingBy(new Point(0,0,0), new Point(0,1,0));
      Point p = new Point(0, 1, 1);
      Point rotated = l.rotatePointAroundClockwise(p, STRAIGHT_ANGLE);
      Point expected = new Point(1, 1, 0);
      assertTrue(almostZero(rotated.distance(expected)));
   }
   @Test
   public void clockwisePointRotationsAroundYAxis_NegativeAngle_Calculated(){
      Line l = linePassingBy(new Point(0,0,0), new Point(0,1,0));
      Point p = new Point(0, 1, 1);
      Point rotated = l.rotatePointAroundClockwise(p, -STRAIGHT_ANGLE);
      Point expected = new Point(-1, 1, 0);
      assertTrue(almostZero(rotated.distance(expected)));
   }
   @Test
   public void clockwisePointRotationsAroundZAxis_Calculated(){
      Line l = linePassingBy(new Point(0,0,0), new Point(0,0,1));
      Point p = new Point(0, 1, 1);
      Point rotated = l.rotatePointAroundClockwise(p, STRAIGHT_ANGLE);
      Point expected = new Point(-1, 0, 1);
      assertTrue(almostZero(rotated.distance(expected)));
   }
   @Test
   public void clockwisePointRotationsAroundZAxis_NegativeAngle_Calculated(){
      Line l = linePassingBy(new Point(0,0,0), new Point(0,0,1));
      Point p = new Point(0, 1, 1);
      Point rotated = l.rotatePointAroundClockwise(p, -STRAIGHT_ANGLE);
      Point expected = new Point(1, 0, 1);
      assertTrue(almostZero(rotated.distance(expected)));
   }
}
