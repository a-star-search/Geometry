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

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

public class PointTest {

   @Test
   public void pointsWithTheSameCoordinates_AreNotEquals(){
      Set<Point> pointsWithSameCoordinates = new HashSet<>();
      int pointCount = 1000;
      int x = 1, y = 1, z = 1; //random values
      for (int i = 0; i < pointCount; i++) {
         //create always a point with the same coordinates
         pointsWithSameCoordinates.add(new Point(x, y, z));
      }
      assertEquals(pointsWithSameCoordinates.size(), pointCount);
   }
}
