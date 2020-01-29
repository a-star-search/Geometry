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

import java.util.List;

import static com.moduleforge.libraries.geometry.GeometryConstants.MINIMUM_POINT_SEPARATION;
import static com.moduleforge.libraries.geometry.GeometryConstants.TOLERANCE_EPSILON;
import static com.moduleforge.libraries.geometry._3d.Plane.Side.*;
import static com.moduleforge.libraries.geometry._3d.Plane.planeFromOrderedPoints;
import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.closeTo;
import static org.junit.Assert.*;

@SuppressWarnings("static-method")
public class PlaneTest {
   @Test
   public final void whichSide_PointsOnDifferentSides_Calculated() {
      List<Point> planePoints = asList(new Point(0, 0, 1), new Point(1, 0, 0), new Point(0, 0, -1));
      Plane planeXZ = planeFromOrderedPoints(planePoints);
      Point point1 = new Point(0, 2, 0);
      Point point2 = new Point(0, -2, 0);
      Plane.Side sidePoint1 = planeXZ.whichSide(point1);
      Plane.Side sidePoint2 = planeXZ.whichSide(point2);
      assertNotEquals(sidePoint1, sidePoint2);
   }
   @Test
   public final void whichSide_PointsOnSameSide_Calculated() {
      List<Point> planePoints = asList(new Point(0, 0, 1), new Point(1, 0, 0), new Point(0, 0, -1));
      Plane planeXZ = planeFromOrderedPoints(planePoints);
      Point point1 = new Point(0, 2, 0);
      Point point2 = new Point(0, 3, 0);
      Plane.Side sidePoint1 = planeXZ.whichSide(point1);
      Plane.Side sidePoint2 = planeXZ.whichSide(point2);
      assertEquals(sidePoint1, sidePoint2);
   }
   @Test
   public final void closestPoint_PointOverOriginPointAndXZPlane() {
      List<Point> planePoints = asList(new Point(0, 0, 1), new Point(1, 0, 0), new Point(0, 0, -1));
      Plane planeXZ = planeFromOrderedPoints(planePoints);
      Point point = new Point(0, 2, 0);
      Point closestPoint = planeXZ.closestPoint(point); 
      Point expectedClosestPoint = new Point(0, 0, 0);
      assertTrue(closestPoint.epsilonEquals(expectedClosestPoint));
   }
   /**
    Find the distance from the point P=(4,−4,3) to the plane 2x−2y+5z+8=0

    From the equation for the plane we substitute A=2, B=−2, C=5, D=8. From the point P,
    we substitute x1=4, y1=−4, and z1=3. The distance from P to the plane is

    Tex commands:

    \begin{align*}
    d = \frac{|2 \cdot 4 + (-2)\cdot(-4) + 5 \cdot 3 + 8 |}
    {\sqrt{2^2+(-2)^2 + 5^2}}
    = \frac{39}{\sqrt{33}} \approx 6.8
    \end{align*}

    */
   @Test
   public final void testPlaneEquation_AsPerJavadoc_Calculated(){
      //points that satisfy the equation
      List<Point> planePoints = asList(new Point(0, 0, -8.0 / 5.0), new Point(0, 4, 0), new Point(-4, 0, 0));
      Plane plane = planeFromOrderedPoints(planePoints);
      List<Double> equation = plane.getEquation();
      //test a / d
      assertThat(equation.get(0) / equation.get(3), closeTo(2.0 / 8.0, TOLERANCE_EPSILON));
      //test b / d
      assertThat(equation.get(1) / equation.get(3), closeTo(-2.0 / 8.0, TOLERANCE_EPSILON));
      //test c / d
      assertThat(equation.get(2) / equation.get(3), closeTo(5.0 / 8.0, TOLERANCE_EPSILON));
   }
   /**
    Find the distance from the point P=(4,−4,3) to the plane 2x−2y+5z+8=0

    From the equation for the plane we substitute A=2, B=−2, C=5, D=8. From the point P,
    we substitute x1=4, y1=−4, and z1=3. The distance from P to the plane is

    Tex commands:

    \begin{align*}
    d = \frac{|2 \cdot 4 + (-2)\cdot(-4) + 5 \cdot 3 + 8 |}
    {\sqrt{2^2+(-2)^2 + 5^2}}
    = \frac{39}{\sqrt{33}} \approx 6.8
    \end{align*}

    */
   @Test
   public final void testDistanceToPlane_AsPerJavadoc_Calculated(){
      //points that satisfy the equation
      List<Point> planePoints = asList(new Point(0, 0, -8.0 / 5.0), new Point(0, 4, 0), new Point(-4, 0, 0));
      Plane plane = planeFromOrderedPoints(planePoints);
      Point p = new Point(4, -4, 3);
      Point closest = plane.closestPoint(p);
      double distanceToPlane = p.distance(closest);
      double expectedApproxDistance = 6.8; //plug value calculated in javadoc
      assertThat(distanceToPlane, closeTo(expectedApproxDistance, 0.1));
   }
   @Test
   public final void isOnPlane_PointOverXZPlane_AndXZPlane_ShouldReturnFalse() {
      List<Point> planePoints = asList(new Point(0, 0, 1), new Point(1, 0, 0), new Point(0, 0, -1));
      Plane planeXZ = planeFromOrderedPoints(planePoints);
      Point point = new Point(0, 2, 0);
      assertFalse(planeXZ.contains(point));
   }
   @Test
   public final void isOnPlane_PointOnXZPlane_AndXZPlane_ShouldReturnTrue() {
      List<Point> planePoints = asList(new Point(0, 0, 1), new Point(1, 0, 0), new Point(0, 0, -1));
      Plane planeXZ = planeFromOrderedPoints(planePoints);
      Point point = new Point(10, 0, 10);
      assertTrue(planeXZ.contains(point));
   }
   @Test
   public final void isOnPlane_PointOverXZPlaneBySixDecimalPoints_AndXZPlane_ShouldReturnFalse() {
      List<Point> planePoints = asList(new Point(0, 0, 1), new Point(1, 0, 0), new Point(0, 0, -1));
      Plane planeXZ = planeFromOrderedPoints(planePoints);
      Point point = new Point(10, 1e-6, 10);
      assertFalse(planeXZ.contains(point));
   }
   @Test
   public final void equationPlaneParallelToXY() {
      Point p1 = new Point(0, 0, 1);
      Point p2 = new Point(1, 0, 1);
      Point p3 = new Point(1, 1, 1);
      List<Point> planePoints = asList(p1, p2, p3);
      Plane planeXY = planeFromOrderedPoints(planePoints);
      List<Double> equation = planeXY.getEquation();
      double a = equation.get(0);
      double b = equation.get(1);
      double c = equation.get(2);
      double d = equation.get(3);
      double equationValueP1 = a*p1.x() + b*p1.y() + c*p1.z() + d;
      assertThat(equationValueP1, closeTo(0, TOLERANCE_EPSILON));
      double equationValueP2 = a*p2.x() + b*p2.y() + c*p2.z() + d;
      assertThat(equationValueP2, closeTo(0, TOLERANCE_EPSILON));
      double equationValueP3 = a*p3.x() + b*p3.y() + c*p3.z() + d;
      assertThat(equationValueP3, closeTo(0, TOLERANCE_EPSILON));
   }
   @Test
   public final void equationRandomPlane_EquationAndRandomPointsOnPlaneShouldBeCongruent() {
      //just some random plane
      List<Point> planePoints = asList( new Point(1.0, 1.0, 7.0), new Point(2.0, 2.0, 7.75), new Point(0.0, 2.0, 9.75));
      List<Point> otherPointsInPlane = asList( new Point(1.5, 1.5, 7.375), new Point(3.0, 3.0, 8.5), new Point(2.0, 3.0, 9.5));
      List<Double> equation = planeFromOrderedPoints(planePoints).getEquation();
      double a = equation.get(0);
      double b = equation.get(1);
      double c = equation.get(2);
      double d = equation.get(3);
      for(Point p : otherPointsInPlane){
         double equationValue = a * p.x() + b * p.y() + c * p.z() + d;
         assertThat(equationValue, closeTo(0, TOLERANCE_EPSILON));
      }
   }
   @Test(expected = IllegalArgumentException.class)
   public void makePlaneFromThreePoints_WhenTwoPointsAreEqual_ThrowsException() {
      Point pointA = new Point(1, -7, 4);
      Point pointB = new Point(-1, 2, 3);
      Point pointC = new Point(-1, 2, 3);
      planeFromOrderedPoints(pointA, pointB, pointC);
   }
   @Test
   public void makePlaneFromThreePoints_WhenTwoPointsAreCloseButDistantEnough_NoExceptions() {
      Point pointA = new Point(1, -7, 4);
      Point pointB = new Point(-1, 2, 3);
      Point pointC = new Point(-1, 2, (3.0 + MINIMUM_POINT_SEPARATION));
      planeFromOrderedPoints(pointA, pointB, pointC);
      //passes successfully with no exception
   }
   @Test
   public void twoPlanesApproximatelyFacingTheSameWay_ShouldReturnTrue() {
      Plane p1 = planeFromOrderedPoints( new Point(2, -3.7, 8),
              new Point(43, 38.7, 8.2), new Point(1, 0, 17));
      //I just change a decimal
      Plane similarToP1 = planeFromOrderedPoints( new Point(2.1, -3.8, 8.1),
              new Point(43.1, 38.8, 8.3), new Point(1.1, 0.1, 17.1));
      assertTrue(p1.approximatelyFacingTheSameWay(similarToP1));
   }
   //bad test name pattern, but sometimes it's difficult to have proper names
   @Test
   public void testFunctions_FacingTheOtherWay_And_FacingTheSameWay_ShouldBeCongruent() {
      Plane somePlane = planeFromOrderedPoints( new Point(2, -3.7, 8),
              new Point(43, 38.7, 8.2), new Point(1, 0, 17));
      Plane facingTheOtherWay = somePlane.facingTheOtherWay();
      assertTrue(facingTheOtherWay.facingAwayFromEachOther(somePlane));
      assertTrue(somePlane.facingAwayFromEachOther(facingTheOtherWay));
   }
   @Test
   public void testIf_PlaneCreationPoints_AreAllOnPlane_ShouldReturnTrue() {
      Point p1 = new Point(2, -3.7, 8), p2 = new Point(43, 38.7, 8.2), p3 = new Point(1, 0, 17);
      Plane somePlane = planeFromOrderedPoints(p1, p2, p3);
      assertTrue(somePlane.contains(asList(p1, p2, p3)));
   }
   @Test
   public void testIf_PlaneCreationPointsPlusPointNotOnPlane_AreAllOnPlane_ShouldReturnFalse() {
      Point p1 = new Point(2, -3.7, 8), p2 = new Point(43, 38.7, 8.2), p3 = new Point(1, 0, 17);
      Plane somePlane = planeFromOrderedPoints(p1, p2, p3);
      assertFalse(somePlane.contains(asList(p1, p2, new Point(14, -56, 2))));
   }
   @Test
   public void pointsAtPositiveNegativeAndNoSide_Calculated() {
      Point p1 = new Point(0, 0, 1);
      Point p2 = new Point(1, 0, 1);
      Point p3 = new Point(1, 1, 1);
      Plane somePlane = planeFromOrderedPoints(p1, p2, p3);
      Point pointAtOneSide = new Point(-1, 0, -5); //z < 1
      Point pointAtOtherSide = new Point(-1, 0, 20); //z > 1
      Point pointAtNoSide = new Point(-1, 0, 1); //z == 1
      assertEquals(somePlane.whichSide(pointAtNoSide), None);
      assertEquals(somePlane.whichSide(pointAtOneSide), Negative);
      assertEquals(somePlane.whichSide(pointAtOtherSide), Positive);
   }
}
