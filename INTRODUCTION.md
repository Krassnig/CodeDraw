# Introduction to CodeDraw

## What is CodeDraw

CodeDraw is an easy-to-use drawing library where you use code to create pictures and animations.
It is made for beginners that understand little about programming
and makes it very simple to draw and animate various shapes and images to a canvas.


A list of all methods can be found in the [Api section in the CodeDraw repository](https://github.com/Krassnig/CodeDraw#api).


The full documentation can be found in the sources included in the [CodeDraw.jar](https://github.com/Krassnig/CodeDraw/releases)
or as [JavaDoc](https://krassnig.github.io/CodeDrawJavaDoc/).

## Getting Started

Instruction on how to install CodeDraw can be found
in the [How to install section in the CodeDraw repository](https://github.com/Krassnig/CodeDraw#how-to-install).

Start of by creating an empty java file with the name 'MyProgram' and
then copy the following code into your file:
```java
import codedraw.*;
// Imports CodeDraw and all it's classes.
// Without this CodeDraw cannot be used in your program.

public class MyProgram {
	public static void main(String[] args) {
		// Instantiates a new CodeDraw window with the size of 600x600 pixel
		CodeDraw cd = new CodeDraw();
		// The created window can now be accessed through the cd variable.
		// By calling the method *setColor* the rectangle
		// and the square will be drawn in the color red.
		cd.setColor(Palette.RED);
		// When setColor is called all shapes that are drawn after
		// will have the given color, until *setColor* is called again
		// with a different color.
		cd.drawRectangle(100, 100, 200, 100);
		// drawRectangle draws the outline of a rectangle,
		// offset by 100 pixel from the top left corner.
		// The Rectangle will have a width of 200 pixel
		// and a height of 100 pixel.
		cd.fillSquare(180, 150, 80);
		// The filled square will be offset to the left by 180 pixel
		// and 150 pixel to the top. Its size will be 80x80 pixel.
		
		// The next line changes the color to light blue.
		cd.setColor(Palette.LIGHT_BLUE);
		// fillCircle draws a filled circle where its center is
		// offset by 300 pixel to the left and 200 pixel to the top.
		// The radius of the circle will have a size of 50 pixel.
		cd.fillCircle(300, 200, 50);
		// Shapes that are drawn later will be drawn over
		// the shapes that are drawn earlier.
		
		cd.show();
		// Finally, the method show must be called
		// to display the drawn shapes in the CodeDraw window.
	}
}
```
![basic](https://user-images.githubusercontent.com/24553082/132953425-dd003617-865e-4589-8ccc-67f42e2ae2a9.png)

When you execute this program, you should see window with the outlines of a red rectangle,
another filled rectangle and a filled light blue circle.

## The coordinate system

In mathematics the origin coordinate (0, 0) is usually in the **bottom-left** corner.
This is different in computers.
Computers usually use the **top-left** corner as their origin coordinate.
It is the same with CodeDraw.
For Example: *cd.fillSquare(150, 150, 50);* will start at the **top-left** corner,
go 150 pixel to the right, 150 pixel down and start drawing a 50 by 50 pixel square
to the bottom-right of that point.
The pixel coordinate (150, 150) is part of that rectangle.
Rectangular Shapes like the Square have their starting point in the top left corner of their shape.
Circular Shapes have their starting point in their center.

## Properties

CodeDraw has a number of properties that change the way shapes are drawn.
You can access these properties through their getter and setter.
For example: You can read the currently used color by calling the *getColor* method
and change the color by calling the *setColor* method.
*setCorner* changes the way corners of lines and shapes are drawn.
*setLineWidth* can be used to change the thickness of lines,
and the thickness of the outlines of shapes.

For a full list of properties see the [CodeDraw Api](https://github.com/Krassnig/CodeDraw#api).

## Canvas and Window

The canvas is the rectangle on the screen that is used for drawing.
The width and the height of the canvas cannot be changed once a CodeDraw window has been created.
They can only be set when you create a CodeDraw window by passing the desired size to the constructor.
For example: The following code creates a CodeDraw window with a canvas of the size 300x300 pixel.
```java
CodeDraw cd = new CodeDraw(300, 300);
```

The window is the frame surrounding the canvas. It is larger than the size given to the constructor
of CodeDraw. It contains the closing and minimize button, the title and the CodeDraw icon.

## Points and Lines

A pixel can be drawn with *drawPixel*.
A point can be drawn with *drawPoint*, points change their size based on the *lineWidth* property.
A line can be drawn with *drawLine*.
A curve can be drawn with *drawCurve*.
It has one controlX/Y parameter that specifies in what way the curve will be bent.
The draw method for a curve with two control parameters is called *drawBezier()*.

## Outline and filled Shapes

In general CodeDraw has two kinds of drawing methods. *Fill*-methods and *draw*-methods.
Fill methods always completely fill a shape and draw only draws their outline.
Rectangular Shapes like the Square have their starting point in the top left corner of their shape.
Circular Shapes have their starting point in their center.

Rectangular shapes can be drawn with *drawSquare*, *fillSquare*, *drawRectangle* and *fillRectangle*.

Circular shapes can be drawn with *drawCircle*, *fillCircle*, *drawEllipse* and *fillEllipse*.

Partial circular shapes can be drawn with *drawArc*, *drawPie*, *fillPie*. 
Pie and arc start at the 12 o'clock position offset by the startRadians parameter.
The total length of the pie and arc is defined by the sweepRadians parameter.

Triangles can be drawn with *drawTriangle* and *fillTriangle*.

Polygons can be drawn with *drawPolygon* and *fillPolygon*.

## Drawing Images

*drawImage* can be used to draw images from file to the CodeDraw canvas.
A path to the image file can be passed to drawImage.
Optionally, width and height parameters can be passed to *drawImage* to rescale the image.  

## Drawing Text

*drawText* can be used to draw Text.
To modify font properties such a text size, font type and font weight CodeDraw has a textFormat property.
The textFormat property can be accessed through *getTextFormat* and *setTextFormat*.
Inside the textFormat property are all the properties regarding text formatting.
Available formatting options are *fontName*, *fontSize*, *underline*, *isBold*, *isItalic* and *isStrikethrough*.
The alignment of the text in relation to the origin of the origin coordinate can be defined with
*horizontalAlign* and *verticalAlign*.

TextFormat must be imported separately.

```java
import codedraw.*;
import codedraw.textformat.*;

public class MyProgram {
	public static void main(String[] args) {
		CodeDraw cd = new CodeDraw();

		TextFormat format = cd.getTextFormat();
		format.setFontSize(40);
		format.setVerticalAlign(VerticalAlign.MIDDLE);
		format.setHorizontalAlign(HorizontalAlign.CENTER);
		format.isBold(true);
		
		cd.drawText(100, 100, "Hello World!");
		cd.show();
	}
}
```

## Animations

Animation are created by drawing a "frame" and then waiting a certain period of time,
then drawing another frame and so on.
Before each frame is drawn the *clear*-method is called to clear the entire canvas. 
The example below draws a clock, and every time the loop goes for another iteration,
it adds another 1/60th to the process of the clock.
By giving show a number as an argument you can instruct CodeDraw to wait before continuing
with the execution of you program.

```java
import codedraw.*;

public class MyProgram {
	public static void main(String[] args) {
		CodeDraw cd = new CodeDraw();

		for (double sec = -Math.PI / 2; true; sec += Math.PI / 30) {
			// clears the entire canvas
			cd.clear();
			// draws the second hand
			cd.drawLine(300, 300, Math.cos(sec) * 100 + 300, Math.sin(sec) * 100 + 300);

			// draws the twelve dots
			for (double j = 0; j < Math.PI * 2; j += Math.PI / 6) {
				cd.fillCircle(Math.cos(j) * 100 + 300, Math.sin(j) * 100 + 300, 4);
			}

			// displays the drawn objects and waits 1 second (or 1000 milliseconds)
			cd.show(1000);
		}
	}
}
```

https://user-images.githubusercontent.com/24553082/122690522-3d124900-d22a-11eb-863f-ffdb3f3f8017.mp4

## Events

An event is something that occurs based on user input like the user
pressing a button or moving the mouse. You can subscribe to an Event
by passing a method reference or lambda to CodeDraw.
All events start with the 'on' keyword (e.g. *onKeyPress* or *onMouseMove*).
By subscribing to an event will return a Subscription which
can be used to unsubscribe from the event.


Available events:
- onMouseClick
- onMouseMove
- onMouseDown
- onMouseUp
- onMouseEnter
- onMouseLeave
- onMouseWheel
- onKeyDown
- onKeyUp
- onKeyPress
- onWindowMove


Event Example:
```java
import java.awt.event.MouseEvent;
import codedraw.*;

public class MyProgram {
	public static void main(String[] args) {
		CodeDraw cd = new CodeDraw();

		cd.drawText(200, 200, "Move your mouse over here.");
		cd.show();
		cd.setColor(Palette.RED);

		cd.onMouseMove(MyProgram::handleMouse);
	}

	// This method will be called by CodeDraw everytime the user moves their mouse
	static void handleMouse(CodeDraw cd, MouseEvent me) {
		cd.fillSquare(me.getX() - 2, me.getY() - 2, 4);
		cd.show();
	}
}
```

https://user-images.githubusercontent.com/24553082/122690528-4a2f3800-d22a-11eb-9a8d-72162af9c50f.mp4
