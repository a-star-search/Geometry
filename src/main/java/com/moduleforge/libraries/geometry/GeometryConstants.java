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

public class GeometryConstants {
   /**
    * A difference smaller than this makes two numbers be the same as far as our
    * geometry applications are concerned.
    * 
    * Important: the purpose of this variable is to avoid considering two numbers
    * (or coordinates) different simply because of rounding in calculation. Notice
    * that that is different from the other constant defined in this class.
    * 
    * Another important thing to note is that a java "float" has roughly 7 decimal
    * digits of precision. Not 7 decimals, but 7 digit positions.
    * That means this is an appropriate delta for the case that floats are used.
    *
    * Again, note that float has about 7 digit precision, while this constant assumes 6 or 7 **decimal** precision.
    * This assumption is of course only true if we work with small number in the units order of magnitude. But this is
    * not the way this constant should be used, it should be multiplied by the log base 10 of one of the numbers
    * to compare.
    * 
    */
   public static final double TOLERANCE_EPSILON_FLOAT_PRECISION = 5e-6;
   /**
    * Tolerance epsilon for rounding errors.
    *
    * Almost all of the time we use double precision in this library, and this is the default tolerance.
    * A double has around 16 digit precision.
    *
    * The choice of 12 decimals is also more or less arbitrary but not quite, because if it's not close enough to
    * the actual precision of floating-point operations with double precision, which is about 15 digits,it can lead
    * to some hard to find bugs by introducing errors of value 1e-12 (which then can compound in turn).
    */
   public static final double TOLERANCE_EPSILON = 1e-13;
   /**
    * When we require two numbers to be different enough so that rounding in
    * calculations doesn't have an adverse effect in the result.
    * 
    * An example of this could be calculating a plane from three points that have
    * to be different enough.
    *
    * Distances that fall between the tolerance epsilon and this constant mean
    * that the points are not the same but the method may throw an exception
    * due to points that are too close to each other
    *
    * This constant presupposes we are dealing with small numbers (between 0 and 10)
    *
    * This constant is mostly useful to the origami application, and probably won't make a lot of sense outside of it.
    *
    */
   public static final double MINIMUM_POINT_SEPARATION = 1e-4;
   
}
