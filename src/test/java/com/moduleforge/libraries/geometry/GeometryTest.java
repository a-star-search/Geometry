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
import org.javatuples.Pair;
import org.junit.Test;

import javax.vecmath.Point2d;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.moduleforge.libraries.geometry.Geometry.*;
import static com.moduleforge.libraries.geometry.GeometryConstants.TOLERANCE_EPSILON;
import static com.moduleforge.libraries.geometry._2d.PlanePointsOrdering.CLOCKWISE;
import static com.moduleforge.libraries.geometry._3d.Plane.planeFromOrderedPoints;
import static org.hamcrest.Matchers.lessThan;
import static org.junit.Assert.*;

/**
 * I used wolfram alpha for all or most of these tests
 *
 */
@SuppressWarnings("static-method")
public class GeometryTest {
   private static final double DELTA_SQUARED = 0.01 * 0.01;
   @Test
   public void testCalculateNormalVectorOfPlaneGivenByThreePoints_LengthIsOne() {
      Point pointA = new Point(4, -7, 9);
      Point pointB = new Point(-2, -2, 3);
      Point pointC = new Point(65, 2, 0);
      Vector normalVector = planeFromOrderedPoints(pointA, pointB, pointC).getNormal();
      double delta = 1.0 - normalVector.length();
      assertThat(delta * delta, lessThan(DELTA_SQUARED));
   }
   @Test
   public void testSortListClockwise() {
      Point2d first = new Point2d(-1, 0);
      Point2d second = new Point2d(0, 1);
      Point2d third = new Point2d(1, 0);
      Point2d fourth = new Point2d(0, -1);
      List<Point2d> unordered = Arrays.asList(new Point2d[]{third, second, first, fourth});
      sort(unordered, CLOCKWISE);
      
      if(unordered.indexOf(first) < (unordered.size() - 1))
         assertEquals(unordered.indexOf(first), unordered.indexOf(second) - 1);
      else 
         assertEquals(0, unordered.indexOf(second));

      if(unordered.indexOf(second) < (unordered.size() - 1))
         assertEquals(unordered.indexOf(second), unordered.indexOf(third) - 1);
      else 
         assertEquals(0, unordered.indexOf(third));

      if(unordered.indexOf(third) < (unordered.size() - 1))
         assertEquals(unordered.indexOf(third), unordered.indexOf(fourth) - 1);
      else 
         assertEquals(0, unordered.indexOf(fourth));

      if(unordered.indexOf(fourth) < (unordered.size() - 1))
         assertEquals(unordered.indexOf(fourth), unordered.indexOf(first) - 1);
      else 
         assertEquals(0, unordered.indexOf(first));
   }
   @Test
   public void testSortListXAndY() {

      Point2d first = new Point2d(-1, 0);
      Point2d second = new Point2d(-0.5, 1);
      Point2d third = new Point2d(1, -1);
      Point2d fourth = new Point2d(1, 0);
      List<Point2d> unordered = Arrays.asList(new Point2d[]{third, second, fourth, first});

      sort(unordered, PlanePointsOrdering.X_AND_Y_AXES);
      
      if(unordered.indexOf(first) < (unordered.size() - 1))
         assertEquals(unordered.indexOf(first), unordered.indexOf(second) - 1);
      else 
         assertEquals(0, unordered.indexOf(second));

      if(unordered.indexOf(second) < (unordered.size() - 1))
         assertEquals(unordered.indexOf(second), unordered.indexOf(third) - 1);
      else 
         assertEquals(0, unordered.indexOf(third));

      if(unordered.indexOf(third) < (unordered.size() - 1))
         assertEquals(unordered.indexOf(third), unordered.indexOf(fourth) - 1);
      else 
         assertEquals(0, unordered.indexOf(fourth));

      if(unordered.indexOf(fourth) < (unordered.size() - 1))
         assertEquals(unordered.indexOf(fourth), unordered.indexOf(first) - 1);
      else 
         assertEquals(0, unordered.indexOf(first));
   }
   @Test
   public void testInSamePlane_NotInPlane() {
      List<Point> list = new ArrayList<>();
      list.add(new Point(0, 0, 0));
      list.add(new Point(0, 0, 100));
      list.add(new Point(20, 100, 100));
      list.add(new Point(0, 1000, 1));
      list.add(new Point(0, 1000, 2300));
      assertFalse(inSamePlane(list));
   }
   @Test
   public void testInSamePlane_PerpendicularToZ() {
      List<Point> list = new ArrayList<>();
      list.add(new Point(10, 0, 100));
      list.add(new Point(240, 100, 100));
      list.add(new Point(560, 1000, 100));
      list.add(new Point(0, 1000, 100));
      assertTrue(inSamePlane(list));
   }
   @Test
   public void testInSamePlane_PerpendicularToY() {
      List<Point> list = new ArrayList<>();
      list.add(new Point(0, 0, 100));
      list.add(new Point(20, 0, 100));
      list.add(new Point(0, 0, 1));
      list.add(new Point(0, 0, 2300));
      assertTrue(inSamePlane(list));
   }
   @Test
   public void testInSamePlane_PerpendicularToX() {
      List<Point> list = new ArrayList<>();
      list.add(new Point(0, 0, 0));
      list.add(new Point(0, 0, 100));
      list.add(new Point(0, 100, 100));
      list.add(new Point(0, 1000, 1));
      list.add(new Point(0, 1000, 2300));
      assertTrue(inSamePlane(list));
   }
   //if interested in visualizing this stuff: https://technology.cpm.org/general/3dgraph/
   @Test
   public void testInSamePlane_FourPointsInSamePlane() {
      List<Point> list = new ArrayList<>();
      double a = 3, b = -2, c = 5, d = -1;
      list.add(new Point(10, 0, getZOfPlane(10, 0, a, b, c, d) ));
      list.add(new Point(0, 10, getZOfPlane(0, 10, a, b, c, d)));
      list.add(new Point(10, 10, getZOfPlane(10, 10, a, b, c, d)));
      list.add(new Point(0, -10, getZOfPlane(0, -10, a, b, c, d)));
      assertTrue(inSamePlane(list));
   }
   // ax + by + cz + d = 0
   //z = -ax/c - by/c - d/c
   private static double getZOfPlane(double x, double y, double a, double b, double c, double d) {
      return (-a*x/c) - (b*y/c) - (d/c);
   }
   @Test
   public void lineAlongZAxis_LineAlongXAxis_ShouldCrossAtOrigin() {
      LineSegment segmentA = new LineSegment(new Point(1, 0, 0), new Point(3, 0, 0));
      LineSegment segmentB = new LineSegment(new Point(0, 0, -1), new Point(0, 0, 5));
      LineSegment segment = lineToLineIntersection(segmentA, segmentB);
      Pair<Point, Point> segmentPoints = segment.getPoints();
      Point expected = new Point(0, 0, 0);
      assertTrue( segmentPoints.getValue0().epsilonEquals(expected));
      assertTrue( segmentPoints.getValue1().epsilonEquals(expected));
   }
   @Test
   public void lineAlongZAxis_LineParallelXAxis_ShouldNotCross() {
      LineSegment segmentA = new LineSegment(new Point(1, 0, 0), new Point(3, 0, 0));
      LineSegment segmentB = new LineSegment(new Point(0, 1, -1), new Point(0, 1, 5));
      LineSegment segment = lineToLineIntersection(segmentA, segmentB);
      Pair<Point, Point> segmentPoints = segment.getPoints();
      Point expectedFrom = new Point(0, 0, 0);
      assertTrue( segmentPoints.getValue0().epsilonEquals(expectedFrom));
      Point expectedTo = new Point(0, 1, 0);
      assertTrue( segmentPoints.getValue1().epsilonEquals(expectedTo));
   }
   //I created this test by using 
   //https://technology.cpm.org/general/3dgraph/
   //it's pretty easy to create four points and play around until one can ensure that the lines approximately intersect
   //The calculations for creating intersecting examples are quite easy too, this just saved a little more time and it's good enough for me
   @Test
   public void testLineToLineIntersection_RandomLinesAlmostIntersect() {
      LineSegment segmentA = new LineSegment(new Point(-3, -2, 4), new Point(1, 3, 2));
      LineSegment segmentB = new LineSegment(new Point(-1, -2, 1), new Point(-1, 4, 6));
      LineSegment segment = lineToLineIntersection(segmentA, segmentB);
      assertTrue( segment.length() < 0.1);
   }
   @Test
   public void whenUsingEpsilonErrorForDistance_ForAreas_CanFail(){
      double sideLength = 1.0;
      double sideLengthWithError = sideLength + TOLERANCE_EPSILON * 0.9;
      assertTrue(epsilonEquals(sideLength, sideLengthWithError));
      double areaWithError = sideLengthWithError * sideLengthWithError;
      double areaWithNoError = 1.0;
      assertFalse(epsilonEquals(areaWithError, areaWithNoError));
   }
   @Test
   public void whenUsingEpsilonErrorForAreas_ForAreas_ShouldBeEpsilonEquals(){
      double sideLength = 1.0;
      double sideLengthWithError = sideLength + TOLERANCE_EPSILON * 0.9;
      double areaWithError = sideLengthWithError * sideLengthWithError;
      double areaWithNoError = 1.0;
      assertTrue(areaIsAlmostZero(areaWithError - areaWithNoError));
   }
   @Test
   public void testEpsilonEquals_WhenComparingTwoNumbersLowerThan10_DifferenceOfTwoTimesToleranceEpsilon_ShouldConsiderDifferent(){
      double a = 2;
      assertFalse(epsilonEquals(a - TOLERANCE_EPSILON, a + TOLERANCE_EPSILON));
      a = -2;
      assertFalse(epsilonEquals(a - TOLERANCE_EPSILON, a + TOLERANCE_EPSILON));
      a = 0;
      assertFalse(epsilonEquals(a - TOLERANCE_EPSILON, a + TOLERANCE_EPSILON));
   }
   @Test
   public void testEpsilonEquals_WhenComparingTwoNumbersBiggerThan10_DifferenceOfEpsilon_ShouldBeConsideredEqual(){
      double a = 11;
      assertTrue(epsilonEquals(a, a + TOLERANCE_EPSILON));
   }
   @Test
   public void testEpsilonEquals_WhenComparingTwoNumbersBiggerThan100_DifferenceOf10TimesEpsilon_ShouldBeConsideredEqual(){
      double a = 101;
      assertTrue(epsilonEquals(a, a + TOLERANCE_EPSILON * 10));
   }
   @Test
   public void testEpsilonEquals_WhenComparingTwoNumbersBiggerThan1000_DifferenceOf100TimesEpsilon_ShouldBeConsideredEqual(){
      double a = 1001;
      assertTrue(epsilonEquals(a, a + TOLERANCE_EPSILON * 100));
   }
   /** as opposed to edge cases */
   @Test
   public void testEpsilonEquals_SimpleCases(){
      //true
      assertTrue(epsilonEquals(2, 2));
      assertTrue(epsilonEquals(100, 100));
      assertTrue(epsilonEquals(1E10, 1E10 + 1E-5));
      //false
      assertFalse(epsilonEquals(1E10, 1E10 + 1E-2));
      assertFalse(epsilonEquals(2, -2));
      assertFalse(epsilonEquals(100, -100));
      assertFalse(epsilonEquals(100.1, 100));
      assertFalse(epsilonEquals(100, 100.1));
   }
}
