# Introduction to CodeDraw

## What is CodeDraw

CodeDraw is an easy-to-use drawing library where you use code to create pictures and animations.
It is made for beginners that understand little about programming
and makes it very simple to draw and animate various shapes and images to a canvas.

The source code is available in the [CodeDraw repository](https://github.com/Krassnig/CodeDraw).

The full documentation can be found in the sources included in the [CodeDraw.jar](https://github.com/Krassnig/CodeDraw/releases)
or as [JavaDoc](https://krassnig.github.io/CodeDrawJavaDoc/).

## Table of Contents
- [Introduction to CodeDraw](#introduction-to-codedraw)
  * [What is CodeDraw](#what-is-codedraw)
  * [Table of Contents](#table-of-contents)
  * [Getting Started](#getting-started)
  * [The Coordinate System](#the-coordinate-system)
  * [Outlined and Filled Shapes](#outlined-and-filled-shapes)
  * [Points, Lines and Curves](#points-lines-and-curves)
  * [Modifying the Way Things are Drawn](#modifying-the-way-things-are-drawn)
  * [Drawing text](#drawing-text)
  * [Canvas and Window](#canvas-and-window)
  * [Debugging CodeDraw and InstantDraw](#debugging-codedraw-and-instantdraw)
  * [Images](#images)
  * [Creating Animations](#creating-animations)
  * [Handling Events](#handling-events)
    + [Enhanced EventScanner](#enhanced-eventscanner)
    + [Normal EventScanner](#normal-eventscanner)

## Getting Started

Instruction on how to install CodeDraw can be found in the [INSTALL.md](./INSTALL.md)
section in the README of the CodeDraw repository.
Install CodeDraw and then create an empty Java file with the name `MyProgram` and
then copy the following code into your file:

```java
import codedraw.*;
// Imports CodeDraw and all its classes.
// Without this CodeDraw cannot be used in your program.

public class MyProgram {
    public static void main(String[] args) {
        // Instantiates a new CodeDraw window with the size of 600x600 pixel
        CodeDraw cd = new CodeDraw();
        // The created window can now be accessed through the cd variable.
        // By calling the method *setColor* the rectangle
        // and the square will be drawn in the color red.
        cd.setColor(Palette.RED);
        // If setColor is called all shapes that are drawn after
        // will have the given color, until *setColor* is called again
        // with a different color.
        cd.drawRectangle(100, 100, 200, 100);
        // drawRectangle draws the outline of a rectangle,
        // offset by 100 pixel from the top left corner.
        // The Rectangle will have a width of 200 pixel
        // and a height of 100 pixel.
        cd.fillSquare(180, 150, 80);
        // The filled square will be offset from the left by 180 pixel
        // and 150 pixel from the top. Its size will be 80x80 pixel.

        // The next line changes the color to light blue.
        cd.setColor(Palette.LIGHT_BLUE);
        // fillCircle draws a filled circle with its center at
        // the (300, 200) coordinate, around which the circle
        // will be drawn with a radius of 50 pixel.
        cd.fillCircle(300, 200, 50);
        // Shapes that are drawn later will be drawn over
        // the shapes that are drawn earlier.

        cd.show();
        // Finally, the method show() must be called
        // to display the drawn shapes in the CodeDraw window.
    }
}
```

After you execute this program, you should see window with the outlines of a red rectangle,
another filled rectangle and a filled light blue circle as shown in the image below.

![01 Getting started](https://user-images.githubusercontent.com/24553082/153450652-8dff6b3f-17b6-40ba-b8e1-156b9e72ee26.png)

## The Coordinate System

In mathematics the origin coordinate (0, 0) is usually placed in the **bottom-left** corner.
This is different in computer graphics, where coordinate system usually start in the **top-left** corner.
For Example: `cd.fillSquare(180, 150, 50);` will start in the **top-left** corner of the canvas,
go 180 pixel to the right, 150 pixel down and start drawing a 50 by 50 pixel square towards the bottom-right.
The pixel coordinate (180, 150) is part of that rectangle.

Note that rectangular and circular shapes have different origins as described in the next section.

![02 The coordinate system](https://user-images.githubusercontent.com/24553082/153450673-0b3470cc-7548-4b92-b597-05b4dd13bc1d.png)


## Outlined and Filled Shapes

CodeDraw has two general kinds of drawing methods, `fill`-methods and `draw`-methods.
A `fill`-method always completely fills its shape and a `draw`-method only draws its outline.
All drawing methods that do not fit into this categorization also use the `draw` prefix.
For example, the method to draw a line `drawLine` and the method to draw an image `drawImage`
also use the `draw` prefix.

CodeDraw has two general kinds of shapes, rectangular and circular.
A rectangular shape like the square has its starting point in its top left corner.
A circular shape has its starting point at its center.

Partial circular shapes can be drawn with the `drawArc`, `drawPie`, `fillPie` methods.
Pies and arcs start at the 3 o'clock position offset by the `startRadians` parameter.
The shape is then continued in a clockwise direction for a total length of `sweepRadians` radians.

Outlined Shapes:
- `drawSquare(double x, double y, double sideLength)`
- `drawRectangle(double x, double y, double width, double height)`
- `drawCircle(double centerX, double centerY, double radius)`
- `drawEllipse(double centerX, double centerY, double horizontalRadius, double verticalRadius)`
- `drawPie(double centerX, double centerY, double radius, double startRadians, double sweepRadians)`
- `drawTriangle(double x1, double y1, double x2, double y2, double x3, double y3)`
- `drawPolygon(double... vertices)`

Filled Shapes:
- `fillSquare(double x, double y, double sideLength)`
- `fillRectangle(double x, double y, double width, double height)`
- `fillCircle(double centerX, double centerY, double radius)`
- `fillEllipse(double centerX, double centerY, double horizontalRadius, double verticalRadius)`
- `fillPie(double centerX, double centerY, double radius, double startRadians, double sweepRadians)`
- `fillTriangle(double x1, double y1, double x2, double y2, double x3, double y3)`
- `fillPolygon(double... vertices)`


## Points, Lines and Curves

A point can be drawn by calling the `drawPoint(double x, double y)` method.
Its radius is determined by the set `lineWidth`.

A line can be drawn with the `drawLine()` method. The styling of the ends are determined by the `corner` property.
If corners are set to `cd.setCorner(Corner.SHARP)` the line will be drawn pointy around the endpoint.
However, if `cd.setCorner(Corner.BEVEL)` is set the line will stop at exactly the endpoint.
Additionally, `cd.setCorner(Corner.ROUND)` can also be set to get round endpoints.

The same applies to the `drawCurve()`, `drawBezier()` and `drawArc()` method.
The `drawCurve()` method has **one** control point which can be used to bend the curve in a desired direction.
The `drawBezier()` method has **two** control points.
The `drawArc()` methods draws an arc around a central point.
The `startRadians` parameter defines the offset of the arc in a clockwise direction and
the `sweepRadians` parameter defines the length of the arc.

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
- `drawPoint(double x, double y)`
- `drawLine(double startX, double startY, double endX, double endY)`
- `drawCurve(double startX, double startY, double controlX, double controlY, double endX, double endY)`
- `drawBezier(double startX, double startY, double controlX1, double controlY1, double controlX2, double controlY2, double endX, double endY)`
- `drawArc(double centerX, double centerY, double radius, double startRadians, double sweepRadians)`

## Modifying the Way Things are Drawn

CodeDraw has a number of properties that change the way shapes are drawn.
You can access these properties through getters and setters.
Getters and Setter are methods that always start with the word `get` and `set` respectively.
For example, you can read the currently used color by calling the `getColor()` method
and change the color by calling the `setColor(Color newColor)` method.
`setCorner(Corner newCorner)` changes the way corners of lines and shapes are drawn.
To change the radius of the corner on squares and rectangles
the `setCornerRadius(double cornerRadius)` property can be used.
`setLineWidth(double newLineWidth)` can be used to change the thickness of lines,
and the thickness of the outlines of shapes.

```java
import codedraw.*;

public class Main {
    public static void main(String[] args) {
        CodeDraw cd = new CodeDraw(400, 400);

        cd.setColor(Palette.GREEN);
        cd.setLineWidth(5);
		
        cd.setCorner(Corner.ROUND);
		cd.setCornerRadius(10);

        cd.drawRectangle(100, 100, 200, 100);

        cd.show();
    }
}
```

![03 Modifying the way things are drawn](https://user-images.githubusercontent.com/24553082/153450719-418eab8a-80ab-481d-9ac2-ca90dc4b2370.png)

The program above will create a green rectangle with round corners and
the outline of the rectangle will be 10 pixels wide.

List of drawing properties:
- `getColor()`/`setColor(Color newColor)`
- `getLineWidth()`/`setLineWidth(double newLineWidth)`
- `getCorner()`/`setCorner(Corner newCorner)`
- `getCornerRadius()`/`setCornerRadius(double cornerRadius)`
- `drawOver()`/`setDrawOver(boolean drawOver)`
- `isAntiAliased()`/`setAntiAliased(boolean isAntiAliased)`
- `getTransformation()`/`setTransformation(Matrix2D newTransformation)`

## Drawing text

Text is drawn with the `drawText(double x, double y, String text)` method.
The way the text is drawn can be defined through the `TextFormat` object.
First, get the `TextFormat` object from the `CodeDraw` class by calling the `getTextFormat()` method,
then the `TextFormat` variable can be used to change a variety of font properties like
text size `setFontSize(int)`, font family `setFontName(String)` and boldness `setBold(boolean)`.
Additionally, styling such as strikethrough `setStrikethrough(boolean)`, italics `setItalic(boolean)`
and underline `setUnterline(Underline)` can be set.
The placement and alignment relative to the point specified in `drawText(x, y, text)` can be changed
with the `setTextOigin(TextOrigin textOrigin)` property.

```java
import codedraw.*;

public class Main {
    public static void main(String[] args) {
        CodeDraw cd = new CodeDraw(400, 400);
        TextFormat format = cd.getTextFormat();

        format.setTextOrigin(TextOrigin.TOP_MIDDLE);
        format.setFontSize(20);
        format.setItalic(true);

        cd.drawText(200, 100, "Hello World!\nMulti lines!");

        cd.setColor(Palette.RED);
        cd.fillCircle(200, 100, 5);

        cd.show();
    }
}
```

![04 Drawing text](https://user-images.githubusercontent.com/24553082/153450747-27066c3f-a831-4961-91ec-295f40a26813.png)

This example draws the text in the middle below the origin point  in red, which is defined by the x and y variable,
in `drawText(double x, double y, String text)`.
The size of the text is changed and the text is styled italic.
Text that contains newlines is displayed in separate lines.

Additional Notes on `setFontName(String fontName, String... fallbackFontNames)`:
A certain font name might not be available on a computer.
To solve this problem multiple font names can be specified in the `setFontName` setter.
The first font that is installed on your system will be selected.
If none of the specified font names are installed on the system a neutral fallback font will be used.

```java
String[] installedFonts = TextFormat.getAllAvailableFontNames();
// installedFonts = new String[] { "Arial", "Verdana" };

textFormat.setFontName("JetBrains Mono", "Arial", "Verdana");
// The font Arial is set
```
In the example above the only fonts installed are *Arial* and *Verdana*.
Since *Arial* is the first font that is installed on the system, it would be select.

List of text format options:
- `getTextOrigin()`/`setTextOrigin(TextOrigin textOrigin)`
- `getFontSize()`/`setFontSize(int fontSize)`
- `getFontName()`/`setFontName(String fontName, String... fallbackFontNames)`
- `isBold()`/`setBold(boolean isBold)`
- `isItalic()`/`setItalic(boolean isItalic)`
- `getUnderline()`/`setUnderline(Underline underline)`
- `isStrikethrough()`/`setStrikethrough(boolean isStrikethrough)`

## Canvas and Window

The canvas is the section of the CodeDraw window on which you can draw.
The size of the canvas can be specified in the CodeDraw constructor `new CodeDraw(int width, int height)`,
but once it is set it cannot be changed anymore.
For example, the following code creates a CodeDraw window with a canvas of the size 300x100 pixel
and sets the title displayed on the CodeDraw window to *Hello World!*.

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
If you want the CodeDraw window to always be displayed on top of other windows you can call *codeDraw.setAlwaysOnTop(true);*.
ImmediateDraw will be covered in the next section.

Methods about the CodeDraw window:
- `getWidth()`
- `getHeight()`
- `getTitle()`/`setTitle(String title)`
- `isAlwaysOnTop()`/`setAlwaysOnTop(boolean isAlwaysOnTop)`
- `isInstantDraw()`/`setInstantDraw(boolean isInstantDraw)`
- `getWindowPositionX()`/`setWindowPositionX(int windowPositionX)`
- `getWindowPositionY()`/`setWindowPositionY(int windowPositionY)`
- `getCanvasPositionX()`/`setCanvasPositionX(int canvasPositionX)`
- `getCanvasPositionY()`/`getCanvasPositionY(int canvasPositionY)`

## Debugging CodeDraw and InstantDraw

If you debug CodeDraw with Intellij, Intellij stops the entire program including the CodeDraw window, which then freezes.
To stop the debugger from stopping all threads, only the main thread should be stopped.
This can be done by:

1. Set a breakpoint anywhere in your code by left-clicking next to the line number.
2. Right-click on the breakpoint.
3. Select the *Thread* options (instead of *All*).
4. Click on *Make Default* and *Done*.

![06 Debugging CodeDraw and InstantDraw](https://user-images.githubusercontent.com/24553082/193884905-515a41b0-1e45-4aae-948e-640c50dda99a.png)

After setting this, your code should behave as it did before but the CodeDraw window should no longer freeze.

To make the debugging experience even better CodeDraw has a build in InstantDraw mode,
which immediately draws all shapes to the canvas.
This can be used to better understand what is happening in your application
but also slows down drawing because CodeDraw has to display the changes each time a draw method is called.

Another useful tool is `setAlwaysOnTop(boolean)`, if `true` the CodeDraw window will always be placed on top
of all other windows. This should make it easier when switching between the IDE and the CodeDraw window.

```java
import codedraw.*;

public class Main {
    public static void main(String[] args) {
        CodeDraw cd = new CodeDraw();
        cd.setInstantDraw(true);
        cd.setAlwaysOnTop(true);

        cd.drawCircle(300, 300, 100);

        // The circle is displayed without calling
        // cd.show();
    }
}
```

## Images

Images can be handled through CodeDraw's custom `Image` class.
Images work very similarly to the `CodeDraw` class, they just don't have a window attached to them.

To draw images to the CodeDraw window you must first load the image into your program.
This can be done with the `CodeDrawImage.fromFile(path)` function.

```java
import codedraw.*;

public class Main {
    public static void main(String[] args) {
        CodeDraw cd = new CodeDraw();

        Image image = Image.fromFile("./path_to/image.png");

        cd.drawImage(50, 50, image);

        cd.drawImage(200, 50, 200, 200, image);

        cd.drawImage(200, 250, 200, 200, image, Interpolation.NEAREST_NEIGHBOR);

        cd.show();

        Image.save(cd, "./path_to/new_image.png", ImageFormat.PNG);
    }
}
```

![08 Images in CodeDraw](https://user-images.githubusercontent.com/24553082/193884951-83f293f6-e882-4dd3-8c3b-e935bbf8ec9e.png)

The first `drawImage(double x, double y, Image image)` method takes the width and height of the given image to draw the image.
The second `drawImage(double x, double y, double width, double height, Image image)` rescales the image to fit inside the 200x200 bounds.
The third `drawImage(double x, double y, double width, double height, Image image, Interpolation interpolation)` also rescales the image but also specifies how pixels are supposed to be interpolated.
For more information on interpolation read the CodeDraw documentation of the Interpolation enum or read the Wikipedia article on
[Bicubic Interpolation](https://en.wikipedia.org/wiki/Bicubic_interpolation) and
[Image Scaling](https://en.wikipedia.org/wiki/Image_scaling).

To save the `CodeDraw` canvas or an `Image` the `Image.save(Image image, String pathToImage, ImageFormat format)`
method can be called.

Image creation methods:
- `Image.fromFile(String pathToImage)`
- `Image.fromUrl(String url)`
- `Image.fromResource(String resourceName)`
- `Image.fromBase64String(String base64)`
- `new Image(int width, int height)`
- `new Image(int width, int height, Color backgroundColor)`

Since images have the same capabilities as the `CodeDraw` class it is easy to draw on them.
The example below loads an image then draws an orange circle onto the image and
saves it as an edited image.

```java
import codedraw.*;

public class Main {
    public static void main(String[] args) {
        Image image = Image.fromFile("./path_to/image.png");

        image.setColor(Palette.ORANGE);
        image.fillCircle(100, 100, 50);

        Image.save(image, "./path_to/edited_image.png", ImageFormat.PNG);
    }
}
```

The `Image` class also has image editing capabilities.

Image editing function:
- `Image.crop(Image source, int x, int y, int width, int height)`
- `Image.scale(Image source, double scale)`
- `Image.scale(Image source, double scale, Interpolation interpolation)`
- `Image.rotateClockwise(Image image)`
- `Image.rotateCounterClockwise(Image image)`
- `Image.mirrorHorizontally(Image image)`
- `Image.mirrorVertically(Image image)`

## Creating Animations

Animations are created by drawing multiple frames and then pausing in between those frames.
In CodeDraw this is achieved by creating a loop, in which each iteration draws one frame
and then waits a certain amount of time.

The animation below increases the `sec` variable by 1/60th (one second) of a circle each iteration.
Before drawing the entire canvas is cleared by calling the `clear()` method.
Then the clock's second hand is drawn and the twelve dots that display the hour.
Finally, the `show(long waitMilliseconds)` method must be called to display the frame and wait for 1 second.
The pause duration is specified in milliseconds, so 1000 milliseconds = 1 second.

```java
import codedraw.*;

public class Main {
    public static void main(String[] args) {
        CodeDraw cd = new CodeDraw(400, 400);

        for (double sec = -Math.PI / 2; !cd.isClosed(); sec += Math.PI / 30) {
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

## Handling Events

An event is something that occurs every time a user interacts with your application
like the user pressing a key or moving the mouse.
There are 12 different events in CodeDraw:

- `MouseClickEvent` happens once every time a mouse button is pressed down and quickly released again.
- `MouseMoveEvent` happens continuously while the mouse is being moved.
- `MouseDownEvent` happens exactly once every time a mouse button is pressed down.
- `MouseUpEvent` happens exactly once every time a mouse button is released.
- `MouseEnterEvent` happens every time the mouse enters the canvas.
- `MouseLeaveEvent` happens every time the mouse leaves the canvas.
- `MouseWheelEvent` happens each time the mouse wheel is turned.
- `KeyDownEvent` happens exactly once every time a key is pressed down.
- `KeyUpEvent` happens exactly once every time a key is released.
- `KeyPressEvent` happens continuously while a key is being held down.
- `WindowMoveEvent` happens every time the CodeDraw window is moved.
- `WindowCloseEvent` happens exactly once after the user closes the window or `cd.close()` is called.

### Enhanced EventScanner

To create an application that handles user input you first need to consider what variables are
necessary to store everything that is happening in your application.
In the example below the mouse position and click count needs to be tracked.
Each iteration in the outer `while` loop draws exactly one frame.
The inner `foreach` loop processes all events that happen in between frames.
Depending on the type of event different code is executed.
If the event is a `MouseMoveEvent` the position the `mouseX` and `mouseY` variables are updated.
If the event is a `MouseClickEvent` the `clickCount` is increased by one.
After the `foreach` loop is finished with all currently available events the updated event data is displayed.
The previously drawn image is removed by calling `clear()` and the new data is drawn with `drawText()`.
After that the `show(long waitMilliseconds)` is called to display the updated image
and sleep for 16 milliseconds.

```java
import codedraw.*;

public class Main {
    public static void main(String[] args) {
        CodeDraw cd = new CodeDraw();

        int mouseX = 0;
        int mouseY = 0;
        int clickCount = 0;

        while (!cd.isClosed()) {
            for (var e : cd.getEventScanner()) {
                switch (e) {
                    case MouseMoveEvent a -> {
                        mouseX = a.getX();
                        mouseY = a.getY();
                    }
                    case MouseClickEvent a -> clickCount++;
                    default -> { }
                }
            }

            cd.clear();
            cd.drawText(100, 100, "Position: " + mouseX + " " + mouseY + "\nClick: " + clickCount);
            cd.show(16);
        }
    }
}
```

### Normal EventScanner

If you use an older version of Java you can utilize the `java.util.Scanner` like properties of the EventScanner.

Much of the program remains exactly the same as with the [Enhanced EventScanner](#Enhanced EventScanner),
only the event handling part changes.
The inner loop in the example below is now a `while` loop.
In each iteration a new event will be at the head of the queue and the inner while loop
will only stop once all currently available events are consumed.
Inside the inner loop depending on which type of event is at the head of the queue
one branch of the `if`/`else if`/`else` will be selected.
If the head of the queue is a `MouseMoveEvent` event the `hasMouseMoveEvent()` method will return true and then the
`nextMouseMoveEvent()` method will be called which returns the MouseMoveEvent from the head of the queue.
All other events inside the queue will then shift forward and there will be a new event at the head of the queue.
Do not forget to call the next method because otherwise the program will enter an endless loop because
the same event will always be at the head of the queue.
After the `MouseMoveEvent` has been returned from the `nextMouseMoveEvent()` method
the event can be used to update the state of the program.
In this case it just updates the current mouse position.
After all currently available events are processed the changes are displayed by clearing the canvas and
then calling the drawText method.

https://user-images.githubusercontent.com/24553082/153450933-a957c3e9-6f60-4896-8b32-0acf2894393d.mp4

```java
import codedraw.*;

public class Main {
    public static void main(String[] args) {
        CodeDraw cd = new CodeDraw();
        EventScanner es = cd.getEventScanner();
        int x = 0;
        int y = 0;
        int clickCount = 0;

        while (!cd.isClosed()) {
            while (es.hasEventNow()) {
                if (es.hasMouseMoveEvent()) {
                    MouseMoveEvent a = es.nextMouseMoveEvent();
                    x = a.getX();
                    y = a.getY();
                }
                else if (es.hasMouseClickEvent()) {
                    es.nextMouseClickEvent();
                    clickCount++;
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

For a more interesting examples look at an implementation of [Conway's Game of Life](/src/examples/java/GameOfLife.java).

## The Animation Interface

