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

import java.util.List;

import static com.moduleforge.libraries.geometry.GeometryConstants.*;
import static java.lang.Double.isNaN;
import static java.lang.Math.sqrt;
import static java.util.Arrays.asList;

/**
 * The code has been adapted from vecmath's Point3d.
 *
 * The reason is that Point3d was mutable and this class isn't.
 *
 * I'm not defining my own equals with coordinate comparison. Either compare the references or compare explicitly
 * within an epsilon.
 */
public class Point {
   private double x;
   private double y;
   private double z;
   private List<Double> coords;
   public Point(int a, int b, int c) {
      this((double)a, (double)b, (double)c);
   }
   public Point(int a, int b, double c) {
      this((double)a, (double)b, c);
   }
   public Point(double a, int b, int c) {
      this(a, (double)b, (double)c);
   }
   public Point(double a, double b, int c) {
      this(a, b, (double)c);
   }
   public Point(double a, int b, double c) {
      this(a, (double)b, c);
   }
   public Point(int a, double b, double c) {
      this((double)a, b, c);
   }
   public Point(int a, double b, int c) {
      this((double)a, b, (double)c);
   }
   public Point(double a, double b, double c) {
      x = a;
      y = b;
      z = c;
      coords = asList(x, y, z);
   }
   public Point(double[] v) {
      this(v[0], v[1], v[2]);
   }
   public Point(Point v) {
      this(v.x, v.y, v.z);
   }
   public Point() {
      this(0.0D, 0.0D, 0.0D);
   }
   /**
    * These getters with the more standard "get" prefix, are useful for libraries that look for such getters and
    * setters and public variables such as spring
    *
    */
   public double getX(){
      return x;
   }
   public double getY(){
      return y;
   }
   public double getZ(){
      return z;
   }

   public double x(){
      return x;
   }
   public double y(){
      return y;
   }
   public double z(){
      return z;
   }

   public List<Double> coordinates() {
      return coords;
   }
   public Point add(Point p) {
      return new Point(x + p.x, y + p.y, z + p.z);
   }
   public Point sub(Point p) {
      return new Point(x - p.x, y - p.y, z - p.z);
   }
   public Point negate() {
      return new Point(-x, -y, -z);
   }
   public Point scale(double c) {
      return new Point(x * c, y * c, z * c);
   }
   public Point translate(Vector v){
      return this.add(v);
   }
   /**
    * Allows for float type rounding errors
    */
   public boolean epsilonEqualsFloatPrecision(Point other) {
      return epsilonEquals(other, TOLERANCE_EPSILON_FLOAT_PRECISION);
   }
   /**
    * Allows for double type rounding errors
    */
   public boolean epsilonEquals(Point other) {
      return epsilonEquals(other, TOLERANCE_EPSILON);
   }
   public boolean epsilonEquals(Point other, double epsilon) {
      double diff = x - other.x;
      if (isNaN(diff))
         return false;
      if ((diff < 0.0D ? -diff : diff) > epsilon)
         return false;
      diff = y - other.y;
      if (isNaN(diff))
         return false;
      if ((diff < 0.0D ? -diff : diff) > epsilon)
         return false;
      diff = z - other.z;
      if (isNaN(diff))
         return false;
      return (diff < 0.0D ? -diff : diff) <= epsilon;
   }
   public double distance(Point other) {
      double diffX = (x - other.x);
      double diffY = (y - other.y);
      double diffZ = (z - other.z);
      double distanceSquared = diffX * diffX + diffY * diffY + diffZ * diffZ;
      return sqrt(distanceSquared);
   }
   double distanceSquared(Point other) {
      double diffX = (x - other.x);
      double diffY = (y - other.y);
      double diffZ = (z - other.z);
      return diffX * diffX + diffY * diffY + diffZ * diffZ;
   }
   public double distanceWithOrigin() {
      return sqrt( x * x + y * y + z * z);
   }
   /**
    * If the points are different enough to perform calculations where two points are required
    */
   public boolean distantEnough(Point other){
      return distance(other) > MINIMUM_POINT_SEPARATION;
   }
   /**
    * Returns a vector that goes from this point to the argument point
    */
   public Vector vectorTo(Point end) {
      double dx = end.x - x;
      double dy = end.y - y;
      double dz = end.z - z;
      return new Vector(dx, dy, dz);
   }
   public Vector vectorToOrigin() {
      return new Vector(-x, -y, -z);
   }
   public Vector vectorFromOrigin() {
      return new Vector(x, y, z);
   }
   /** Sometimes a point is actually a vector. */
   public Vector asVector(){ return new Vector(x, y, z); }
   public static Point midPoint(Point point1, Point point2) {
      double middleX = (point1.x + point2.x) / 2;
      double middleY = (point1.y + point2.y) / 2;
      double middleZ = (point1.z + point2.z) / 2;
      return new Point(middleX, middleY, middleZ);
   }
   @Override public String toString() {
      return "(" + this.x + ", " + this.y + ", " + this.z + ")";
   }
}
