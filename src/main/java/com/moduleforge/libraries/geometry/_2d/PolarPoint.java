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

package com.moduleforge.libraries.geometry._2d;

import javax.vecmath.Point2d;
import java.text.DecimalFormat;

import static java.lang.Math.*;

/**
 * Do not override equals, as it usual in point classes.
 */
public class PolarPoint {
   private double r;
   private double theta;
   public PolarPoint(double r, double theta){
      this.r = r;
      this.theta = theta;
   }
   public double distance(PolarPoint other){
      return sqrt(r*r + other.r*other.r - 2*r*other.r*cos(theta - other.theta));
   }
   public boolean epsilonEquals(PolarPoint other, double epsilon) {
      return equals(other) || distance(other) <= epsilon;
   }
   public double getR() {
      return r;
   }
   public double getTheta(){
      return theta;
   }
   public static PolarPoint polarFromCartesian(Point2d point){
      double r = sqrt(point.x*point.x + point.y*point.y);
      double theta = atan2(point.y, point.x);
      return new PolarPoint(r, theta);
   }
   public Point2d asCartesian(){
      double x = r*cos(theta);
      double y = r*sin(theta);
      return new Point2d(x, y);
   }
   /** Radius and theta formatted to the first six decimal places*/
   @Override public String toString(){
      DecimalFormat df = new DecimalFormat("#.######");
      return "(r = " + df.format(r) + ", theta = " + df.format(theta) + ")";
   }
}
