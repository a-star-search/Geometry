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

import com.moduleforge.libraries.geometry.Geometry;

import javax.vecmath.Vector3d;

import static java.lang.Math.acos;
import static java.lang.Math.sqrt;

/**
 * The code has been adapted from vecmath's Vector3d.
 *
 * The reason is that Vector3d was mutable and this class isn't.
 *
 * A vector's origin is (0, 0, 0).
 */
public class Vector extends Point {

   private Double length;

   public Vector(int a, int b, int c) {
      this((double)a, (double)b, (double)c);
   }
   public Vector(int a, int b, double c) {
      this((double)a, (double)b, c);
   }
   public Vector(double a, int b, int c) {
      this(a, (double)b, (double)c);
   }
   public Vector(double a, double b, int c) {
      this(a, b, (double)c);
   }
   public Vector(double a, int b, double c) {
      this(a, (double)b, c);
   }
   public Vector(int a, double b, double c) {
      this((double)a, b, c);
   }
   public Vector(double a, double b, double c) {
      super(a, b, c);
      length = sqrt(a * a + b * b + c * c);
   }
   public Vector(double[] v) {
      this(v[0], v[1], v[2]);
   }
   public Vector(Vector v) {
      this(v.x(), v.y(), v.z());
   }
   public Vector() {
      this(0.0D, 0.0D, 0.0D);
   }
   public Vector3d asVector3d(){
      return new Vector3d(x(), y(), z());
   }
   public Vector normalize() {
      if(Geometry.epsilonEquals(length, 1.0))
         return this;
      return new Vector(x() / length, y() / length, z() / length);
   }
   public double dot(Vector other) {
      return x() * other.x() + y() * other.y() + z() * other.z();
   }
   public double length() {
      return length;
   }
   public final double angle(Vector other) {
      double c = this.dot(other) / (length * other.length);
      if (c < -1.0D)
         c = -1.0D;
      if (c > 1.0D)
         c = 1.0D;
      return acos(c);
   }
   public final Vector cross(Vector other) {
      double x = y() * other.z() - z() * other.y();
      double y = other.x() * z() - other.z() * x();
      double z = x() * other.y() - y() * other.x();
      return new Vector(x, y, z);
   }
   @Override public Vector scale(double c) {
      return new Vector(x() * c,y() * c,z() * c);
   }
   /** Normalizing and scaling in one step */
   public Vector withLength(double l){
      return scale(l/length);
   }
   public boolean epsilonEquals(Vector other) {
      return super.epsilonEquals(other);
   }
   public boolean epsilonEquals(Vector other, double epsilon) {
      return super.epsilonEquals(other, epsilon);
   }
   @Override public Vector negate() {
      return new Vector(-x(), -y(), -z());
   }
}
