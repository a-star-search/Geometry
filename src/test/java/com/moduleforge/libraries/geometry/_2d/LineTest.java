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

package com.moduleforge.libraries.geometry._2d;

import org.junit.Test;

import javax.vecmath.Point2d;

import static com.moduleforge.libraries.geometry.Geometry.epsilonEquals;
import static java.lang.Math.sqrt;
import static org.junit.Assert.assertTrue;

@SuppressWarnings({"nls", "static-method"})
public class LineTest {
   /*
    * https://www.wolframalpha.com/input/?i=line+Intersection
    * 
    * first line: y = 1 + 2 x
    * second line: y = -x
    * 
    */
   @Test
   public void testLineLineIntersection() {
      Line2D first = Line2D.fromEquation(2.0, 1.0);
      Line2D second = Line2D.fromEquation(-1.0, 0.0);
      Point2d actual = first.intersect(second);
      Point2d expected = new Point2d(-1.0/3.0, 1.0/3.0);
      assertTrue(epsilonEquals(expected, actual));
   }
   /*
    * tested with wolfram alpha
    * 
    * https://www.wolframalpha.com/input/?i=y-intercept+of+3x-4y%3D5&rawformassumption=%7B%22MC%22,+%22y-intercept%22%7D+-%3E+%7B%22Variable%22%7D
    * 
    */
   @Test
   public void testLineAndVerticalLineIntersection() {
      Line2D l = Line2D.fromEquation(3.0/4.0, -5.0/4.0);
      Line2D vertical = Line2D.vertical(0);
      Point2d actual = l.intersect(vertical);
      Point2d expected = new Point2d(0, -5.0/4.0);
      assertTrue(epsilonEquals(expected, actual));
   }
   /*
    * tested with wolfram alpha
    * 
    * https://www.wolframalpha.com/input/?i=x-intercept+of+3x-4y%3D5&rawformassumption=%7B%22MC%22,+%22y-intercept%22%7D+-%3E+%7B%22Variable%22%7D
    * 
    */
   @Test
   public void testLineAndHorizontalLineIntersection() {
      Line2D l = Line2D.fromEquation(3.0/4.0, -5.0/4.0);
      Line2D horizontal = Line2D.horizontal(0);
      Point2d actual = l.intersect(horizontal);
      Point2d expected = new Point2d(5.0/3.0, 0);
      assertTrue(epsilonEquals(expected, actual));
   }
   @Test
   public void testVerticalLineIsVertical() {
      Line2D vertical = Line2D.vertical(1f);
      assertTrue(vertical.isVertical());
   }
   /*
    * https://www.desmos.com/calculator/iz07az84f5
    * 
    * https://imgur.com/whXPMn0
    * 
    */
   @Test
   public void testClosestPoint() {
      double a = -1, b = -2, c = 3;
      // ax + by + c = 0 ===> y = -ax/b - c/b  ===> slope = -a/b, offset =  - c/b
      double slope = -a/b;
      double offset = -c/b;
      Line2D line = Line2D.fromEquation(slope, offset);
      Point2d closestPoint = line.closestPoint(new Point2d(2, 2));
      assertTrue(epsilonEquals(closestPoint, new Point2d(1.4, 0.8)));
   }
   /*
    * same line and point as previous method
    */
   @Test
   public void testDistanceToClosestPoint() {
      double a = -1, b = -2, c = 3;
      // ax + by + c = 0 ===> y = -ax/b - c/b  ===> slope = -a/b, offset =  - c/b
      double slope = -a / b;
      double offset = -c / b;
      Line2D line = Line2D.fromEquation(slope, offset);
      double actualDistanceToClosestPoint = line.distanceTo(new Point2d(2, 2));
      Point2d closestPoint = new Point2d(1.4, 0.8);
      double expectedDistance = sqrt( (2 - closestPoint.getX()) * (2 - closestPoint.getX()) +
                        (2 - closestPoint.getY()) * (2 - closestPoint.getY()) );
      assertTrue("Expected distance: " + expectedDistance + ". Actual distance: " + actualDistanceToClosestPoint, 
            epsilonEquals(expectedDistance, actualDistanceToClosestPoint));
   }
}
