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

import java.util.Optional;

import static com.moduleforge.libraries.geometry.Geometry.epsilonEquals;
import static java.lang.Math.sqrt;
import static org.junit.Assert.*;

@SuppressWarnings("static-method")
public class LineSegmentTest {

   @Test
   public void intersectionPoint_LinesOfSegmentsDoNotMeet_ReturnsNull() throws Exception {
      LineSegment segment1 = new LineSegment(new Point(1, 0, 0), new Point(2, 1, 1));
      LineSegment segment2 = new LineSegment(new Point(1, 1, 0), new Point(2, 2, 1));
      Point intersection = segment1.intersectionPoint(segment2);
      assertNull(intersection);
   }
   @Test
   public void intersectionPoint_SegmentsIntersect_Calculated() throws Exception {
      LineSegment segment1 = new LineSegment(new Point(1, 0, 0), new Point(2, 1, 1));
      LineSegment segment2 = new LineSegment(new Point(1, 1, 1), new Point(2, 0, 0));
      Point intersection = segment1.intersectionPoint(segment2);
      assertTrue(intersection.epsilonEquals(new Point(1.5, 0.5, 0.5)));
   }
   @Test
   public final void getClosestPointInSegment_WhenPointInSegment_IsTheSamePoint() {
      Point pointBelongsInSegment = new Point(1, 1, 0);
      LineSegment segment = new LineSegment(new Point(0, 1, 0), new Point(2, 1, 0));
      Point closestPoint = segment.getClosestPointInSegment(pointBelongsInSegment);
      assertTrue( pointBelongsInSegment.epsilonEquals(closestPoint) );
   }
   @Test
   public final void getClosestPointInSegment_PointOutsideSegment_IsContainedInSegment() {
      Point pointNotInSegment = new Point(1, 2, 0);
      LineSegment segment = new LineSegment(new Point(0, 1, 0), new Point(2, 1, 0));
      Point closestPoint = segment.getClosestPointInSegment(pointNotInSegment);
      assertTrue(segment.contains(closestPoint));
   }
   @Test
   public final void getClosestPointInSegment_PointOutsideSegmentAlsoNotPerpendicularToSegment_IsAtEndPosition() {
      Point pointNotInSegment = new Point(100, 2, 0);
      LineSegment segment = new LineSegment(new Point(0, 1, 0), new Point(2, 1, 0));
      Point closestPoint = segment.getClosestPointInSegment(pointNotInSegment);
      assertTrue(segment.isAtEndPosition(closestPoint));
   }
   @Test
   public final void distance_WhenPointIsOneUnitFromSegment_Calculated() {
      Point pointNotInSegment = new Point(1, 2, 0);
      LineSegment segment = new LineSegment(new Point(0, 1, 0), new Point(2, 1, 0));
      Double distance = segment.distanceFrom(pointNotInSegment);
      assertTrue(epsilonEquals(distance, 1.0) );
   }
   //Look at the following link to validate the test:
   //https://www.wolframalpha.com/input/?i=distance+point+to+line&rawformassumption=%7B%22F%22,+%22PointLineDistanceCalculator%22,+%22point%22%7D+-%3E%22%7B3,+4%7D%22&rawformassumption=%7B%22F%22,+%22PointLineDistanceCalculator%22,+%22line%22%7D+-%3E%22y+%3D+3+%2B+x%22
   @Test
   public final void testDistanceFrom_SomePointToSomeSegment() {
      Point somePoint = new Point(3, 4, 0);
      // line equation: y = 3 + x
      LineSegment segment = new LineSegment(new Point(10, 13, 0), new Point(-3, 0, 0));
      Double distance = segment.distanceFrom(somePoint);
      assertTrue(epsilonEquals(distance, sqrt(2.0)) );
   }
   //Look at the following link to validate the test:
   //https://www.wolframalpha.com/input/?i=distance+point+to+line&rawformassumption=%7B%22F%22,+%22PointLineDistanceCalculator%22,+%22point%22%7D+-%3E%22%7B3,+4%7D%22&rawformassumption=%7B%22F%22,+%22PointLineDistanceCalculator%22,+%22line%22%7D+-%3E%22y+%3D+3+%2B+x%22
   @Test
   public final void testDistanceFrom_SomePointOutsideSomeSegment() {
      Point somePoint = new Point(3, 4, 0);
      // line equation: y = 3 + x
      LineSegment segment = new LineSegment(new Point(0, 3, 0), new Point(-3, 0, 0));
      Optional<Double> optionalDistance = segment.perpendicularDistanceFrom(somePoint);
      assertFalse( optionalDistance.isPresent() );
   }
   @Test
   public final void testIf_PointInSegment_IsContained_ShouldReturnTrue() {
      // line equation: y = 3 + x
      LineSegment segment = new LineSegment(new Point(10, 13, 0), new Point(-3, 0, 0));
      Point pointContained = new Point(4, 7, 0);
      assertTrue(segment.contains(pointContained));
   }
   @Test
   public final void testIf_PointInLineButNotInSegment_IsContained_ShouldReturnFalse() {
      // line equation: y = 3 + x
      LineSegment segment = new LineSegment(new Point(10, 13, 0), new Point(-3, 0, 0));
      Point pointContained = new Point(20, 23, 0);
      assertFalse(segment.contains(pointContained));
   }
   @Test
   public final void testIf_PointInLineButNotInSegment_IsContainedInEnclosingLine_ShouldReturnTrue() {
      // line equation: y = 3 + x
      LineSegment segment = new LineSegment(new Point(10, 13, 0), new Point(-3, 0, 0));
      Point pointContained = new Point(20, 23, 0);
      assertTrue(segment.enclosingLineContains(pointContained));
   }
   @Test
   public final void twoSegmentsCreatedWithSamePositions_ShouldNotBeEquals(){
      LineSegment segment = new LineSegment(new Point(10, 13, 0), new Point(-3, 0, 30));
      LineSegment segmentSamePositions = new LineSegment(new Point(10, 13, 0), new Point(-3, 0, 30));
      assertNotEquals(segment, segmentSamePositions);
   }
   @Test
   public final void twoSegmentsCreatedWithSamePoints_ShouldBeEquals(){
      Point a = new Point(10, 13, 0), b = new Point(-3, 0, 30);
      LineSegment segment = new LineSegment(a, b);
      LineSegment segment2 = new LineSegment(b, a);
      assertEquals(segment, segment2);
   }
   @Test
   public final void testIsContained_whenOtherSegmentIsContained_shouldReturnTrue(){
      Point a = new Point(0, 0, 0), b = new Point(1, 1, 1);
      LineSegment segment = new LineSegment(a, b);
      Point containedPoint1 = new Point(0.2, 0.2, 0.2), containedPoint2 = new Point(0.3, 0.3, 0.3);
      LineSegment containedSegment = new LineSegment(containedPoint1, containedPoint2);
      assertTrue(segment.contains(containedSegment));
      Point endPoint1 = new Point(a), endPoint2 = new Point(b);
      LineSegment containedAndAtOneEnd = new LineSegment(containedPoint1, endPoint1);
      assertTrue(segment.contains(containedAndAtOneEnd));
      LineSegment containedAndAtAnotherEnd = new LineSegment(containedPoint1, endPoint2);
      assertTrue(segment.contains(containedAndAtAnotherEnd));
   }
}
