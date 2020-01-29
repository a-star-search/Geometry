Small 2D and 3D geometry java library and facade build over other libraries with double precision arithmetic with a
variety of operations and components such as points, lines, planes.

This small library is partly a facade for other libraries, or, in some cases, I simply re-used existing code from widely
used libraries that I didn't want to import fully (no reason to reinvent the wheel).

Unfortunately, mathematical, geometrical, scientific and apache libraries tend to have horrible interfaces, inconsistent
 and not documented. Building facades for such libraries is a worthy pursuit.


It has a focus on 3D geometry, of which there aren't so many libraries available. Therefore, I am not naming these 3D
geometry classes with suffixes or prefixes, (such as 'threeD' or similar) however I do use them for 2D classes.
For example the class "Point" represents a point in three dimensions but it's not called Point3D.

The interfaces are clear, the public methods are documented, the code is legible and unit tested. Also the different
geometrical objetcs are immutable and points use reference equality. A point equality should be done by a coordinate
comparison within an epsilon or with referencial equality. Surprisingly, no other library does it. It doesn't usually
have serious consequences,though.

As a reminder, a Java double has an approximate precision of 16 digits. A few complex mathematical operations yield
results with an error epsilon of about a couple of orders of magnitude higher than that. That epsilon should generally
be documented per public method.
