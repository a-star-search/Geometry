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
import java.util.ArrayList;
import java.util.List;

import static com.moduleforge.libraries.geometry.GeometryConstants.TOLERANCE_EPSILON;
import static com.moduleforge.libraries.geometry._2d.PolarPoint.polarFromCartesian;
import static java.lang.Math.PI;
import static java.util.Arrays.asList;
import static java.util.concurrent.ThreadLocalRandom.current;
import static org.hamcrest.Matchers.closeTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class PolarPointTest {
   @Test
   public final void transformPolarPointsToCartesianAndBack_SamePointWithinDoublePrecision(){
      List<Double> radius = asList(1.0, 2.0);
      List<Double> theta = asList(0.0, PI / 4, PI / 2, 3 * PI / 4, PI, 5 * PI / 4, 3 * PI / 2, 7 * PI / 2, 2 * PI);
      for(double t: theta){
         for(double r: radius){
            PolarPoint p = new PolarPoint(r, t);
            Point2d cartesian = p.asCartesian();
            PolarPoint andBack = polarFromCartesian(cartesian);
            assertTrue(p.epsilonEquals(andBack, TOLERANCE_EPSILON));
         }
      }
   }
   @Test
   public final void compareDistanceBetweenPolarAndTheCartesianConversion_ShouldReturnSameDistance(){
      List<Double> radius = asList(1.0, 2.0);
      List<Double> theta = asList(0.0, PI / 4, PI / 2, 3 * PI / 4, PI, 5 * PI / 4, 3 * PI / 2, 7 * PI / 2, 2 * PI);
      List<PolarPoint> polarPoints = new ArrayList<>();
      for(double t: theta)
         for(double r: radius)
            polarPoints.add(new PolarPoint(r, t));
      int pointCount = polarPoints.size();
      for(int i = 0; i < 1000; i++){
         PolarPoint pp1 = polarPoints.get(current().nextInt(pointCount));
         PolarPoint pp2 = polarPoints.get(current().nextInt(pointCount));
         Point2d p1 = pp1.asCartesian();
         Point2d p2 = pp2.asCartesian();
         double polarDistance = pp1.distance(pp2);
         double cartesianDistance = p1.distance(p2);
         assertThat(polarDistance, closeTo(cartesianDistance, TOLERANCE_EPSILON));
      }
   }
}
