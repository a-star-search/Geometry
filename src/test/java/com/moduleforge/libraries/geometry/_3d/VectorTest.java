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

import static com.moduleforge.libraries.geometry.GeometryConstants.TOLERANCE_EPSILON;
import static java.lang.Math.PI;
import static org.hamcrest.Matchers.closeTo;
import static org.junit.Assert.assertThat;

public class VectorTest {
   @Test
   public final void angleBetweenTwoVectors_ShouldBeIrrespectiveOfLengthOfVectors(){
      Vector alongXAxis1 = new Vector(1, 0, 0);
      Vector alongXAxis2 = new Vector(10, 0, 0);
      Vector atPIFourths = new Vector(3, 3, 0);
      Vector atPIFourths2 = new Vector(25, 25, 0);
      double angle1 = alongXAxis1.angle(atPIFourths);
      double angle2 = alongXAxis2.angle(atPIFourths2);
      assertThat(angle1, closeTo(angle2, TOLERANCE_EPSILON));
   }
   @Test
   public final void angleCalculation_ShouldBeCommutative(){
      Vector alongXAxis = new Vector(1, 0, 0);
      Vector atPIFourths_YPos = new Vector(1, 1, 0);
      Vector atPIFourths_YNeg = new Vector(1, -1, 0);
      double angle1 = alongXAxis.angle(atPIFourths_YPos);
      double angle2 = alongXAxis.angle(atPIFourths_YNeg);
      assertThat(angle1, closeTo(angle2, TOLERANCE_EPSILON));
      //the same thing, but proper, actual commutation
      double angle1OtherDirection = atPIFourths_YPos.angle(alongXAxis);
      assertThat(angle1, closeTo(angle1OtherDirection, TOLERANCE_EPSILON));
   }
   @Test
   public final void someBasicCases_Calculated(){
      Vector alongXAxis = new Vector(1, 0, 0);
      double angle = alongXAxis.angle(alongXAxis);
      assertThat(angle, closeTo(0, TOLERANCE_EPSILON));
      Vector atPIFourths_YPos = new Vector(1, 1, 0);
      angle = alongXAxis.angle(atPIFourths_YPos);
      assertThat(angle, closeTo(PI / 4, TOLERANCE_EPSILON));
      Vector alongYAxis = new Vector(0, 1, 0);
      angle = alongXAxis.angle(alongYAxis);
      assertThat(angle, closeTo(PI / 2, TOLERANCE_EPSILON));
      Vector alongXAxis_Opposite = new Vector(-1, 0, 0);
      angle = alongXAxis.angle(alongXAxis_Opposite);
      assertThat(angle, closeTo(PI, TOLERANCE_EPSILON));
   }
}
