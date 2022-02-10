# Introduction to CodeDraw

## What is CodeDraw

CodeDraw is an easy-to-use drawing library where you use code to create pictures and animations.
It is made for beginners that understand little about programming
and makes it very simple to draw and animate various shapes and images to a canvas.

The source code is available at the [CodeDraw repository](https://github.com/Krassnig/CodeDraw).

The full documentation can be found in the sources included in the [CodeDraw.jar](https://github.com/Krassnig/CodeDraw/releases)
or as [JavaDoc](https://krassnig.github.io/CodeDrawJavaDoc/).

## Table of contents
- [Introduction to CodeDraw](#introduction-to-codedraw)
    * [What is CodeDraw](#what-is-codedraw)
    * [Table of contents](#table-of-contents)
    * [Getting started](#getting-started)
    * [The coordinate system](#the-coordinate-system)
    * [Modifying the way things are drawn](#modifying-the-way-things-are-drawn)
    * [Drawing text](#drawing-text)
    * [Canvas and window](#canvas-and-window)
    * [Points, lines and curves](#points--lines-and-curves)
    * [Outline and filled shapes](#outline-and-filled-shapes)
    * [Images in CodeDraw](#images-in-codedraw)
    * [Animations](#animations)
    * [Events](#events)
        + [Events without EventScanner](#events-without-eventscanner)

## Getting started

Instruction on how to install CodeDraw can be found
in the [How to install section in the CodeDraw repository](https://github.com/Krassnig/CodeDraw#how-to-install).
Install CodeDraw and then create an empty Java file with the name 'MyProgram' and
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

When you execute this program, you should see window with the outlines of a red rectangle,
another filled rectangle and a filled light blue circle.

![01 Gettings started](https://user-images.githubusercontent.com/24553082/153450652-8dff6b3f-17b6-40ba-b8e1-156b9e72ee26.png)

## The coordinate system

In mathematics the origin coordinate (0, 0) is usually in the **bottom-left** corner.
This is different in computer graphics.
In computer graphics the **top-left** corner is used as the origin coordinate.
For Example: *cd.fillSquare(180, 150, 50);* will start at the **top-left** corner,
go 180 pixel to the right, 150 pixel down and start drawing a 50 by 50 pixel square
to the bottom-right of that point.
The pixel coordinate (180, 150) is part of that rectangle.
Rectangular Shapes like the Square have their starting point in the top left corner of their shape.
Circular Shapes have their starting point in their center.

![02 The coordinate system](https://user-images.githubusercontent.com/24553082/153450673-0b3470cc-7548-4b92-b597-05b4dd13bc1d.png)

## Modifying the way things are drawn

CodeDraw has a number of properties that change the way shapes are drawn.
You can access these properties through their getter and setter.
For example: You can read the currently used color by calling the *getColor* method
and change the color by calling the *setColor* method.
*setCorner* changes the way corners of lines and shapes are drawn.
*setLineWidth* can be used to change the thickness of lines,
and the thickness of the outlines of shapes.

```java
import codedraw.*;

public class Main {
	public static void main(String[] args) {
		CodeDraw cd = new CodeDraw(400, 400);
		
		cd.setColor(Palette.GREEN);
		cd.setCorner(Corner.ROUND);
		cd.setLineWidth(5);
		
		cd.drawRectangle(100, 100, 200, 100);
		
		cd.show();
	}
}
```

![03 Modifying the way things are drawn](https://user-images.githubusercontent.com/24553082/153450719-418eab8a-80ab-481d-9ac2-ca90dc4b2370.png)

For example, this program will create a green rectangle with round corners.
The outline of the rectangle will be 10 pixels wide. 

List of drawing properties:
 - getColor/setColor
 - getLineWidth/setLineWidth
 - getCorner/setCorner
 - isAntiAliased/setAntiAliased

## Drawing text

Text is drawn with the *drawText* method.
The way the text is drawn can be defined through the TextFormat object.
To modify font properties such a text size, font type and font weight CodeDraw has a textFormat property.
The textFormat property can be accessed through *getTextFormat* and *setTextFormat*.
Inside the textFormat property are all the properties regarding text formatting.
TextFormat must be imported separately.

```java
import codedraw.*;
import codedraw.textformat.*;

public class Main {
	public static void main(String[] args) {
		CodeDraw cd = new CodeDraw(400, 400);
		TextFormat format = cd.getTextFormat();

		format.setFontSize(20);
		format.setHorizontalAlign(HorizontalAlign.CENTER);
		format.setItalic(true);

		cd.drawText(200, 100, "Hello World!\nMulti lines!");

		cd.setColor(Palette.RED);
		cd.fillCircle(200, 100, 5);

		cd.show();
	}
}
```

![04 Drawing text](https://user-images.githubusercontent.com/24553082/153450747-27066c3f-a831-4961-91ec-295f40a26813.png)

This example draws the text horizontally centered below the origin point.
The origin point is indicated by the red dot.
Per default text is drawn to the bottom right of the origin point.
By setting the font size the size of the font can be increased.
By setting HorizontalAlign to center the text is horizontally centered.
By setting italic to true the text is tilted.
Text can also be draw over multiple lines by including a newline character.

List of text format options:
 - getHorizontalAlign/setHorizontalAlign
 - getVerticalAlign/setVerticalAlign
 - getFontSize/setFontSize
 - getFontName/setFontName
 - isBold/setBold
 - isItalic/setItalic
 - getUnderline/setUnderline
 - isStrikethrough/setStrikethrough

## Canvas and window

The canvas is the rectangle on the screen that is used for drawing.
The width and the height of the canvas cannot be changed once a CodeDraw window has been created.
They can only be set when you create a CodeDraw window by passing the desired size to the constructor.
For example: The following code creates a CodeDraw window with a canvas of the size 300x300 pixel.
```java
import codedraw.*;

public class Main {
	public static void main(String[] args) {
		CodeDraw cd = new CodeDraw(300, 100);
		cd.setTitle("Hello World!");
	}
}
```

![05 Canvas and window](https://user-images.githubusercontent.com/24553082/153450766-60aac8f5-ea8b-4701-b7c2-6b064fd373ca.png)

The width and height can be accessed through the *getWidth* and *getHeight* methods.
The window is the frame surrounding the canvas.
It contains the closing and minimize button, the title and the CodeDraw icon.
The window is larger than the size given to the constructor of CodeDraw, since it surrounds the canvas.
The position of the canvas and window can both be changed. Changing one also changes the other.
The title can also be changed with *setTitle*.

Methods about the CodeDraw window:
 - getWidth
 - getHeight
 - getTitle/setTitle
 - getWindowPositionX/setWindowPositionX
 - getWindowPositionY/setWindowPositionY
 - getCanvasPositionX/setCanvasPositionX
 - getCanvasPositionY/getCanvasPositionY

## Points, lines and curves

A pixel can be drawn with *drawPixel*.
A point can be drawn with *drawPoint*, points change their size based on the *lineWidth* property.
A line can be drawn with *drawLine*.
A curve can be drawn with *drawCurve*.
It has one controlX/Y parameter that specifies in what way the curve will be bent.
The draw method for a curve with two control parameters is called *drawBezier*.
To draw a curve like you would do with a compass use *drawArc*.
The ends of style of the ending of lines can be changed with *setCorner*.

```java
import codedraw.*;

public class Main {
	public static void main(String[] args) {
		CodeDraw cd = new CodeDraw();

		cd.drawCurve(100, 100, 250, 50, 200, 200);

		cd.setLineWidth(10);
		cd.setCorner(Corner.ROUND);

		cd.drawCurve(100, 300, 250, 250, 200, 400);

		cd.drawArc(300, 300, 100, -Math.PI / 2, Math.PI);

		cd.show();
	}
}
```

![06 Points, lines and curves](https://user-images.githubusercontent.com/24553082/153450804-cc0835d2-e5d4-4ac2-9e49-0d4da78a60fe.png)

Points, Lines and Curves:
 - drawPixel
 - drawPoint
 - drawLine
 - drawCurve
 - drawBezier
 - drawArc

## Outline and filled shapes

In general CodeDraw has two kinds of drawing methods. *Fill*-methods and *draw*-methods.
Fill methods always completely fill a shape and draw only draws their outline.
Rectangular Shapes like the Square have their starting point in the top left corner of their shape.
Circular Shapes have their starting point in their center.

Partial circular shapes can be drawn with *drawArc*, *drawPie*, *fillPie*.
Pie and arc start at the 3 o'clock position offset by the startRadians parameter.
The total length of the pie and arc is defined by the sweepRadians parameter and goes clockwise.

Outlined Shapes:
 - drawSquare
 - drawRectangle
 - drawCircle
 - drawEllipse
 - drawPie
 - drawTriangle
 - drawPolygon
 - drawArc (actually a line)

Filled Shapes:
 - fillSquare
 - fillRectangle
 - fillCircle
 - fillEllipse
 - fillPie
 - fillTriangle
 - fillPolygon

## Images in CodeDraw

Images in CodeDraw can be handled with the CodeDrawImage class.
CodeDrawImages work very similarly to the CodeDraw class, they just don't have a window attached to them.

To draw images to the CodeDraw window you must first load the image into your program.
This can be done with the *CodeDrawImage.fromFile* function.

```java
import codedraw.*;
import codedraw.images.*;

public class Main {
	public static void main(String[] args) {
		CodeDraw cd = new CodeDraw();
		
		CodeDrawImage image = CodeDrawImage.fromFile("./path_to/image.png");
		
		cd.drawImage(50, 50, image);
		
		cd.drawImage(200, 50, 200, 200, image);
		
		cd.drawImage(200, 250, 200, 200, image, Interpolation.NEAREST_NEIGHBOR);

		cd.show();
		
		CodeDrawImage.saveAs(cd.copyCanvas(), "./path_to/new_image.png", ImageFormat.PNG);
	}
}
```

![07 Images in CodeDraw](https://user-images.githubusercontent.com/24553082/153450852-bf25f473-225e-46bc-b6c5-192b0dcfdbdf.png)


The first *drawImage* method takes the width and height of the given image to draw the image.
The second *drawImage* rescales the image to fit inside the 200x200 bounds.
The third *drawImage* also rescales the image but also specifies how pixels are supposed to be interpolated.
Read the documentation or read the Wikipedia article on
[Bicubic Interpolation](https://en.wikipedia.org/wiki/Bicubic_interpolation) and 
[Image Scaling](https://en.wikipedia.org/wiki/Image_scaling).

To extract the current canvas from a CodeDraw object you can call *copyCanvas*.
The *CodeDrawImage.saveAs* method saves the image to the file system. 

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

public class Main {
	public static void main(String[] args) {
		CodeDraw cd = new CodeDraw(400, 400);

		for (double sec = -Math.PI / 2; true; sec += Math.PI / 30) {
			// clears the entire canvas
			cd.clear();
			// draws the second hand
			cd.drawLine(200, 200, Math.cos(sec) * 100 + 200, Math.sin(sec) * 100 + 200);

			// draws the twelve dots
			for (double j = 0; j < Math.PI * 2; j += Math.PI / 6) {
				cd.fillCircle(Math.cos(j) * 100 + 200, Math.sin(j) * 100 + 200, 4);
			}

			// displays the drawn objects and waits 1 second
			cd.show(1000);
		}
	}
}
```

https://user-images.githubusercontent.com/24553082/153450896-51e4de7b-d741-4832-b850-c77adb96d01b.mp4

## Events

An event is something that occurs based on user input like the user pressing a button or moving the mouse.
There are 12 events in CodeDraw:
 - MouseClickEvent
 - MouseMoveEvent
 - MouseDownEvent
 - MouseUpEvent
 - MouseEnterEvent
 - MouseLeaveEvent
 - MouseWheelEvent
 - KeyDownEvent
 - KeyUpEvent
 - KeyPressEvent
 - WindowMoveEvent
 - WindowCloseEvent

The easiest way to use events is to create an EventScanner.
The EventScanner is very similar to Scanner in Java.
The source of the events a CodeDraw object given to the constructor of the EventScanner.
The EventScanner remains open until the CodeDraw window is closed.
One approach is to create an endless loop until the CodeDraw window closes.
Then there are two stages inside the endless loop.
First process all the events that are in the EventScanner, then render a new frame.

To process the events you should only process the ones that are currently available.
*es.hasEventNow()* check whether there are currently events in the EventScanner.
*es.hasEvent()* waits until the next event is available to check whether there are more events.
Sometimes it might be right to wait for an event and only rerender when the user does something.
In general, it is best to just process all available.
An action like MouseMove will generate a lot of events and
showing the current image every single time will slow down you application.

Once you know that you have an event inside the EventScanner you can check which type of event it is.
Checks happen with the *es.has___Event()* methods.
When the event is the one you are looking for you can call *es.next___Event()* to take it out of the EventScanner.
Calling *es.nextEvent()* just skips the event. (*nextEvent* returns the event as an Object).

After processing the events display the new data based on *x*, *y* and *clickCount*.

```java
import codedraw.*;
import codedraw.events.*;

public class Main {
	public static void main(String[] args) {
		CodeDraw cd = new CodeDraw();
		EventScanner es = new EventScanner(cd);

		int x = 0;
		int y = 0;
		int clickCount = 0;

		while (!es.isClosed()) {
			while (es.hasEventNow()) {
				if (es.hasMouseMoveEvent()) {
					MouseMoveEventArgs a = es.nextMouseMoveEvent();
					x = a.getX();
					y = a.getY();
				}
				if (es.hasMouseClickEvent()) {
					clickCount++;
					es.nextEvent();
				}
				else {
					es.nextEvent();
				}
			}

			cd.clear();
			cd.drawText(100, 100, "Position: " + x + " " + y + "\nClick: " + clickCount);
			cd.show(16);
		}
	}
}
```

https://user-images.githubusercontent.com/24553082/153450933-a957c3e9-6f60-4896-8b32-0acf2894393d.mp4

For a more complicated and interesting examples look at a simple implementation of [Conway's Game of Life](https://github.com/Krassnig/CodeDraw/blob/cc2ed6eabc03c43c8538a6e95d5c85f43358cff2/src/examples/java/GameOfLife.java).

### Events without EventScanner

You can also subscribe to an event by passing a method reference or lambda to CodeDraw.
All events start with the 'on' keyword (e.g. *onKeyPress* or *onMouseMove*).
Given a method reference or lambda to an event will return a Subscription which can be used to unsubscribe from the event.

```java
import codedraw.*;

public class Main {
	private static int x = 0;
	private static int y = 0;
	
	public static void main(String[] args) {
		CodeDraw cd = new CodeDraw();

		cd.onMouseMove(a -> {
			x = a.getX();
			y = a.getY();
		});
		
		while (true) {
			cd.clear();
			cd.drawText(x, y, "The text will follow your mouse.");
			cd.show(16);
		}
	}
}
```

https://user-images.githubusercontent.com/24553082/153450985-499f540b-aec9-4578-b8ff-79da6a42ab84.mp4

Note! You are not allowed to call most methods of the CodeDraw class inside a CodeDraw event.
Something like the following would crash your program:

```java
import codedraw.CodeDraw;

public class Main {
	public static void main(String[] args) {
		CodeDraw cd = new CodeDraw();
		cd.onMouseMove(a -> {
			cd.drawSquare(a.getX() - 5, a.getY() - 5, 10);
			cd.show();
        });
	}
}
```

The only methods you can call inside an event are
*cd.getWidth()*, *cd.getHeight()* and all *cd.on___Event()* methods.
You should instead use the *on*-methods to change the state of you application.
The *on*-methods are executed on their own thread which can lead to concurrency issues when used incorrectly.
Blocking inside of events will also block all other events from being processed.
Be careful when using events directly.
