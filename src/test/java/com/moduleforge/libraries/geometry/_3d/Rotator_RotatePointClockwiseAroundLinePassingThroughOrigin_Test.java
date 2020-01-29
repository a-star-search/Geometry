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

import static com.moduleforge.libraries.geometry.Geometry.epsilonEquals;
import static com.moduleforge.libraries.geometry._3d.Line.linePassingBy;
import static com.moduleforge.libraries.geometry._3d.Rotator.rotatePointAroundLinePassingThroughOrigin_Clockwise;
import static java.lang.Math.PI;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.lessThan;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class Rotator_RotatePointClockwiseAroundLinePassingThroughOrigin_Test {
   private static final double STRAIGHT_ANGLE = PI / 2.0; //90 degrees
   private static final Point ORIGIN = new Point(0, 0, 0);

   @Test
   public void lineGoing_UpRightBackQuadrant_PointRotatesClockwise(){
      //up, right, back -> x pos, y pos, z neg
      Point pointOnXZPlane = new Point(0.70710678118, 0.0, -0.70710678118);
      double coord = 0.57735026918;
      Vector lineDirectionNormal = new Vector(coord, coord, -coord); //normal vector from 000 towards 11-1
      Point rotated = rotatePointAroundLinePassingThroughOrigin_Clockwise(pointOnXZPlane, lineDirectionNormal, STRAIGHT_ANGLE);
      assertRotatedStraightAngle(pointOnXZPlane, rotated, lineDirectionNormal);
      //rotating to the left (clockwise) looking from the origin
      //I think checking a coordinate is enough, since we have already checked the angle
      assertThat(rotated.x(), lessThan(pointOnXZPlane.x()));
      assertThat(rotated.y(), greaterThan(pointOnXZPlane.y()));
      assertThat(rotated.z(), lessThan(pointOnXZPlane.z()));
   }
   @Test
   public void lineGoing_UpLeftBackQuadrant_PointRotatesClockwise(){
      //up, left, back -> x neg, y pos, z neg
      Point pointOnXZPlane = new Point(-0.70710678118, 0.0, -0.70710678118);
      double coord = 0.57735026918;
      Vector lineDirectionNormal = new Vector(-coord, coord, -coord); //normal vector from 000 towards -11-1
      Point rotated = rotatePointAroundLinePassingThroughOrigin_Clockwise(pointOnXZPlane, lineDirectionNormal, STRAIGHT_ANGLE);
      assertRotatedStraightAngle(pointOnXZPlane, rotated, lineDirectionNormal);
      //rotating to the left (clockwise) looking from the origin
      //I think checking a coordinate is enough, since we have already checked the angle
      assertThat(rotated.x(), lessThan(pointOnXZPlane.x()));
      assertThat(rotated.y(), greaterThan(pointOnXZPlane.y()));
      assertThat(rotated.z(), greaterThan(pointOnXZPlane.z()));
   }
   @Test
   public void lineGoing_DownLeftBackQuadrant_PointRotatesClockwise(){
      //down, left, back -> x neg, y neg, z neg
      Point pointOnXZPlane = new Point(-0.70710678118, 0.0, -0.70710678118);
      double coord = 0.57735026918;
      Vector lineDirectionNormal = new Vector(-coord, -coord, -coord); //normal vector from 000 towards -1-1-1
      Point rotated = rotatePointAroundLinePassingThroughOrigin_Clockwise(pointOnXZPlane, lineDirectionNormal, STRAIGHT_ANGLE);
      assertRotatedStraightAngle(pointOnXZPlane, rotated, lineDirectionNormal);
      //rotating to the left (clockwise) looking from the origin
      //I think checking a coordinate is enough, since we have already checked the angle
      assertThat(rotated.x(), greaterThan(pointOnXZPlane.x()));
      assertThat(rotated.y(), lessThan(pointOnXZPlane.y()));
      assertThat(rotated.z(), lessThan(pointOnXZPlane.z()));
   }
   @Test
   public void lineGoing_DownRightFrontQuadrant_PointRotatesClockwise(){
      //down, left, back -> x pos, y neg, z pos
      Point pointOnXZPlane = new Point(0.70710678118, 0.0, 0.70710678118);
      double coord = 0.57735026918;
      Vector lineDirectionNormal = new Vector(coord, -coord, coord); //normal vector from 000 towards 1-11
      Point rotated = rotatePointAroundLinePassingThroughOrigin_Clockwise(pointOnXZPlane, lineDirectionNormal, STRAIGHT_ANGLE);
      assertRotatedStraightAngle(pointOnXZPlane, rotated, lineDirectionNormal);
      //rotating to the left (clockwise) looking from the origin
      //I think checking a coordinate is enough, since we have already checked the angle
      assertThat(rotated.x(), lessThan(pointOnXZPlane.x()));
      assertThat(rotated.y(), lessThan(pointOnXZPlane.y()));
      assertThat(rotated.z(), greaterThan(pointOnXZPlane.z()));
   }
   private static void assertRotatedStraightAngle(Point p, Point rotated, Vector lineDirectionNormal) {
      Point closestPointInLine = linePassingBy(ORIGIN, lineDirectionNormal).closestPoint(p);
      Vector v1 = closestPointInLine.vectorTo(p);
      Vector v2 = closestPointInLine.vectorTo(rotated);
      double angleRotated = v1.angle(v2);
      assertTrue(epsilonEquals(angleRotated, STRAIGHT_ANGLE));
   }
}