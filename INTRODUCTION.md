# How To Draw With Code

## What Is CodeDraw

CodeDraw is a beginner-friendly drawing library which can be used to create pictures, animations and even interactive applications.
It is designed for people who are just starting to learn programming, enabling them to create graphical applications.

The source code of CodeDraw is available in the [CodeDraw Github repository](https://github.com/Krassnig/CodeDraw).

The full documentation can be found in the sources included in the [CodeDraw.jar](https://github.com/Krassnig/CodeDraw/releases)
or as [JavaDoc](https://krassnig.github.io/CodeDrawJavaDoc/).

## Table Of Contents
- [Introduction To CodeDraw](#introduction-to-codedraw)
  * [What Is CodeDraw](#what-is-codedraw)
  * [Table Of Contents](#table-of-contents)
  * [Getting Started](#getting-started)
  * [The Coordinate System](#the-coordinate-system)
  * [Outlined And Filled Shapes](#outlined-and-filled-shapes)
  * [Points, Lines And Curves](#points-lines-and-curves)
  * [Styling Shapes](#styling-shapes)
  * [Drawing Text](#drawing-text)
  * [Canvas And Window](#canvas-and-window)
  * [Debugging CodeDraw](#debugging-codedraw)
  * [Images](#images)
  * [Creating Animations](#creating-animations)
  * [Handling Events](#handling-events)
    + [Enhanced EventScanner](#enhanced-eventscanner)
    + [Normal EventScanner](#normal-eventscanner)
  * [The Animation Interface](#the-animation-interface)
  * [GUI Development With CodeDraw](#gui-development-with-codedraw)

## Getting Started

Instruction on how to install CodeDraw can be found in the [INSTALL.md](./INSTALL.md).
To get started, install CodeDraw and then create an empty Java file with the name `MyProgram`.
Next, copy the following code into your file:

```java
import codedraw.*;
// Imports CodeDraw and all its classes.
// Without this, CodeDraw cannot be used in your program.

public class MyProgram {
    public static void main(String[] args) {
        // Instantiates a new CodeDraw window with the size of 600x600 pixel
        CodeDraw cd = new CodeDraw();
        // The created window can now be accessed through the cd variable.
        // By calling the method *setColor* the rectangle
        // and the square will be drawn in the color red.
        cd.setColor(Palette.RED);
        // If setColor is called, all shapes that are drawn after
        // will have the given color until *setColor* is called again
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
another filled rectangle, and a filled light blue circle as shown in the image below.

![01 Getting started](https://user-images.githubusercontent.com/24553082/153450652-8dff6b3f-17b6-40ba-b8e1-156b9e72ee26.png)

## The Coordinate System

In mathematics the origin coordinate (0, 0) is conventionally located in the **bottom-left** corner.
However, in computer graphics, the coordinate system usually start from the **top-left** corner.
For instance, when calling `cd.fillSquare(180, 150, 50);`, the draw process start in the **top-left** corner of the canvas.
It then moves 180 pixel to the right, 150 pixel down and starts drawing the 50 by 50 pixel square towards the bottom-right.
The pixel coordinate (180, 150) is part of that rectangle.

![02 The coordinate system](https://user-images.githubusercontent.com/24553082/153450673-0b3470cc-7548-4b92-b597-05b4dd13bc1d.png)


## Outlined And Filled Shapes

CodeDraw offers two kinds of drawing methods, `fill`-methods and `draw`-methods.
A `fill`-method completely fills its shape, while a `draw`-method only draws its outline.
Any drawing methods that do not neatly fit into these categories are prefixes with `draw` as well.
For instance, the method for drawing a line is called `drawLine` and the method to draw an image `drawImage`
also uses the `draw` prefix.

CodeDraw also categorizes two kinds of shapes: rectangles and circles.
A rectangular shape, such as the square has its origin point in its top left corner.
Conversely, a circular shape has its origin point at its center.

To create partial circular shapes, CodeDraw provides the following methods: `drawArc`, `drawPie` and `fillPie`.
Pies and arcs start at the 3 o'clock position, which can be changed by setting the `startRadians` parameter.
Angles in CodeDraw are specified in radians.
For example, to start at the 12 o'clock position, `startRadians` should be set to `-Math.PI / 2`.
The shape then proceeds in a clockwise direction for a specified length of `sweepRadians` radians.

Outlined shapes:
- `drawSquare(double x, double y, double sideLength)`
- `drawRectangle(double x, double y, double width, double height)`
- `drawCircle(double centerX, double centerY, double radius)`
- `drawEllipse(double centerX, double centerY, double horizontalRadius, double verticalRadius)`
- `drawPie(double centerX, double centerY, double radius, double startRadians, double sweepRadians)`
- `drawTriangle(double x1, double y1, double x2, double y2, double x3, double y3)`
- `drawPolygon(double... vertices)`

Filled shapes:
- `fillSquare(double x, double y, double sideLength)`
- `fillRectangle(double x, double y, double width, double height)`
- `fillCircle(double centerX, double centerY, double radius)`
- `fillEllipse(double centerX, double centerY, double horizontalRadius, double verticalRadius)`
- `fillPie(double centerX, double centerY, double radius, double startRadians, double sweepRadians)`
- `fillTriangle(double x1, double y1, double x2, double y2, double x3, double y3)`
- `fillPolygon(double... vertices)`


## Points, Lines And Curves

To draw a point, use the `drawPoint(double x, double y)` method.
The point's radius is determined by the `lineWidth` property.

For drawing lines, utilize the `drawLine()` method.
The styling of the endpoints is determined by the `corner` property.
If you set `cd.setCorner(Corner.SHARP)`, the line will have pointy endpoints.
Alternatively, setting `cd.setCorner(Corner.BEVEL)` will stop the endpoints at exactly the endpoints.
You can also `cd.setCorner(Corner.ROUND)` to get round endpoints.

The same principles apply to the `drawCurve()`, `drawBezier()` and `drawArc()` methods.
* `drawCurve()` has **one** control point which can be used to bend the curve in a desired direction.
* `drawBezier()` has **two** control points.
* `drawArc()` draws an arc around a central point.
Similarly to the `drawPie` method, a `startRadians` parameter defines the arc's offset,
and the `sweepRadians` parameter defines the arc's length.

```java
import codedraw.*;

public class Main {
    public static void main(String[] args) {
        CodeDraw cd = new CodeDraw();

        cd.drawCurve(100, 100, 250, 50, 200, 200);

        cd.setLineWidth(10);
        cd.setCorner(Corner.ROUND);

		// (100, 300) = start point
        // (250, 250) = control point
        // (200, 400) = end point
        cd.drawCurve(100, 300, 250, 250, 200, 400);

        // -Math.PI / 2 = going back a quarter of a circle (default starts is at 3 o'clock)
        // Math.PI going forward half a circle
        cd.drawArc(300, 300, 100, -Math.PI / 2, Math.PI);

        cd.show();
    }
}
```

![06 Points, lines and curves](https://user-images.githubusercontent.com/24553082/153450804-cc0835d2-e5d4-4ac2-9e49-0d4da78a60fe.png)

Points, lines and curves:
- `drawPoint(double x, double y)`
- `drawLine(double startX, double startY, double endX, double endY)`
- `drawCurve(double startX, double startY, double controlX, double controlY, double endX, double endY)`
- `drawBezier(double startX, double startY, double controlX1, double controlY1, double controlX2, double controlY2, double endX, double endY)`
- `drawArc(double centerX, double centerY, double radius, double startRadians, double sweepRadians)`

## Styling Shapes

CodeDraw has several properties that modify the way shapes are drawn.
You can access these properties through getters and setters.
Getters and Setter are methods that always start with the word `get` and `set`, respectively.
For example, you can retrieve the currently used color by calling the `getColor()` method
and change the color by calling the `setColor(Color newColor)` method.
The `setCorner(Corner newCorner)` method changes the way corners of lines and shapes are drawn.
To adjust the radius of corners on squares and rectangles, you can use the `setCornerRadius(double cornerRadius)` property.
The `setLineWidth(double newLineWidth)` method is used to alter the thickness of lines,
as well as the thickness of the outlines of shapes.

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

The program above will create a green rectangle with round corners,
and the outline of the rectangle will be 10 pixels wide.

List of drawing properties:
- `getColor()`/`setColor(Color newColor)`
- `getLineWidth()`/`setLineWidth(double newLineWidth)`
- `getCorner()`/`setCorner(Corner newCorner)`
- `getCornerRadius()`/`setCornerRadius(double cornerRadius)`
- `drawOver()`/`setDrawOver(boolean drawOver)`
- `isAntiAliased()`/`setAntiAliased(boolean isAntiAliased)`
- `getTransformation()`/`setTransformation(Matrix2D newTransformation)`

## Drawing Text

Text is drawn using the `drawText(double x, double y, String text)` method.
To customize the appearance of your text, use the `TextFormat` object.
You can access the `TextFormat` object through the `CodeDraw` class by calling the `.getTextFormat()` method.

Inside the `TextFormat` object, there are several of text formatting options, such as:
* text size `setFontSize(int)`
* font family `setFontName(String, String...)`
* boldness `setBold(boolean)`
* strikethrough `setStrikethrough(boolean)`
* italic `setItalic(boolean)`
* underline `setUnterline(Underline)`
* alignment `setTextOrigin(TextOrigin)`

The text is aligned relative to the position specified in `drawText(x, y, text)`.

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

By specifying the `TextOrigin.TOP_MIDDLE`,
the text is drawn at the center below origin point defined by the `x` and `y` variables in the `drawText(x, y, text)` method.
The origin point is highlighted as a red dot.
Furthermore, the text's size is adjusted, styled in italic
and contains newlines which displays it in separate lines.

The same text formatting configuration can be produced by "chaining" the options as follows:
```java
import codedraw.*;

public class Main {
    public static void main(String[] args) {
        CodeDraw cd = new CodeDraw(400, 400);
        cd.getTextFormat()
            .setTextOrigin(TextOrigin.TOP_MIDDLE)
            .setFontSize(20)
            .setItalic(true);

        cd.drawText(200, 100, "Hello World!\nMulti lines!");

        cd.setColor(Palette.RED);
        cd.fillCircle(200, 100, 5);

        cd.show();
    }
}
```

#### An additional note on font names:

Certain font name might not be available on a computer.
Therefore, you can set multiple font names using the `setFontName(fontName, ...fallbackFontNames)` method.
The first font name that is available in that list will be used to draw the text.
If none of the specified font names are available on the system, a neutral fallback font will be used through the
[logical font Dialog](https://docs.oracle.com/javase/8/docs/api/java/awt/Font.html).

```java
String[] installedFonts = TextFormat.getAllAvailableFontNames();
// installedFonts = new String[] { "Verdana", "Arial" };

textFormat.setFontName("JetBrains Mono", "Arial", "Verdana");
// The font Arial is set
```

In the example above, the only fonts installed are *Verdana* and *Arial*.
Since *JetBrains Mono* is not among the installed system fonts, *Arial* is automatically selected.

## Canvas And Window

The size of the canvas you draw on can be specified in the CodeDraw constructor `new CodeDraw(int width, int height)`.
However, once the size of a CodeDraw object is set, it can no longer be changed.
It can still be accessed through the getter methods `.getWidth()` and `.getHeight()`.
Surrounding the canvas is the window, which will always be slightly larger than your canvas.
Its size is depends on the platform your CodeDraw application is run on.

The example below creates a CodeDraw window with a canvas 300x100 pixel in size
and sets the title displayed on the CodeDraw window to **"Hello World!"**.

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

Methods that manipulate the behavior of the CodeDraw window:

* Title - Sets the title of the application in the top left corner of the window.<br>
&nbsp;&nbsp;&nbsp;&nbsp;`getTitle()`/`setTitle(String title)`

* AlwaysOnTop - Forces CodeDraw to be displayed on top of other windows.<br>
&nbsp;&nbsp;&nbsp;&nbsp;`isAlwaysOnTop()`/`setAlwaysOnTop(boolean isAlwaysOnTop)`

* InstantDraw - Immediately draws each shape, no `.show()` necessary.<br>
&nbsp;&nbsp;&nbsp;&nbsp;`isInstantDraw()`/`setInstantDraw(boolean isInstantDraw)`

* CanvasPosition - Gets and sets the canvas position relative to the main screen.<br>
&nbsp;&nbsp;&nbsp;&nbsp;`getCanvasPositionX()`/`setCanvasPositionX(int canvasPositionX)`<br>
&nbsp;&nbsp;&nbsp;&nbsp;`getCanvasPositionY()`/`getCanvasPositionY(int canvasPositionY)`

* WindowPosition - Gets and sets the window position relative to the main screen.<br>
&nbsp;&nbsp;&nbsp;&nbsp;`getWindowPositionX()`/`setWindowPositionX(int windowPositionX)`<br>
&nbsp;&nbsp;&nbsp;&nbsp;`getWindowPositionY()`/`setWindowPositionY(int windowPositionY)`

Note: The canvas position and window position are bound to each other; if you change one, the other will change as well.

#### Additional Display Options:

CodeDraw can also be used in fullscreen and borderless window mode.

![40 Display Options.png](illustrations%2Fintroduction%2F40%20Display%20Options.png)


```java
FullScreen fs = new FullScreen();
fs.drawText(100, 100, "Hello World!");
fs.show();
```

```java
BorderlessWindow bw = new BorderlessWindow();
bw.drawText(100, 100, "Hello World!");
bw.show();
```

Since those windows do not have a close button you can press `ALT`+`F4` instead.

## Debugging CodeDraw

The debugging experience with CodeDraw can be significantly improved.
By default, Intellij and the Java debugger halt the entire program, including the CodeDraw window.
Consequently, you can no longer interact with the CodeDraw window, making it appears as if it where just about to crash.
Fortunately, there is an option in Intellij to change this behavior:

1. Set a breakpoint anywhere in your code by left-clicking next to the line number.
2. Right-click on the breakpoint.
3. Select the **Thread** options (instead of **All**).
4. Click on **Make Default** and then **Done**.

![06 Debugging CodeDraw and InstantDraw](https://user-images.githubusercontent.com/24553082/193884905-515a41b0-1e45-4aae-948e-640c50dda99a.png)

Once this is set, everything should be as before, but now you can freely move and interact with the CodeDraw window.

#### InstantDraw

To better understand what is happening inside your application, CodeDraw has a build in **InstantDraw** mode.
The instant draw mode instantly draws each shape when the corresponding command is called and also guarantees that
once the `draw` or `fill` method returns, the shape is shown on the canvas.
However, the instant draw mode is very slow and should therefore only be used for debugging your application.

#### AlwaysOnTop

Always on top can be used in combination with instant draw to display the CodeDraw window on top of Intellij
while stepping through the lines with the debugger.

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

Images in CodeDraw can be handled through its custom `Image` class.
Its behavior is almost exactly the same as the `CodeDraw` class itself, just without any window attached to it.
Images can be created from files through static methods like `Image.fromFile("path/to/your/file.png")`.

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

The example above loads an image from the hard drive, displays it in three different ways and then saves the output
produced inside the CodeDraw window to the `new_image.png` file.

1. `drawImage(double x, double y, Image image)`
retains the width and height of the given image and draws it to the canvas.
2. `drawImage(double x, double y, double width, double height, Image image)`
rescales the image to fit inside the 200x200 bounds specified through the width and height parameters.
3. `drawImage(double x, double y, double width, double height, Image image, Interpolation interpolation)`
additionally specifies an interpolation method for rescaling the image.
If not specified, `Interpolation.Bicubic` is chosen by default.

Further details about Interpolation can be found in the CodeDraw
[Interpolation enum](https://github.com/Krassnig/CodeDraw/blob/master/src/main/java/codedraw/Interpolation.java)
or on the Wikipedia article on
[Bicubic Interpolation](https://en.wikipedia.org/wiki/Bicubic_interpolation) and
[Image Scaling](https://en.wikipedia.org/wiki/Image_scaling).

Lastly, use the `Image.save(Image image, String pathToImage, ImageFormat format)` method to save the produced image
into the `new_image.png` file.

There are other image creation methods:
- `Image.fromFile(String pathToImage)`
- `Image.fromUrl(String url)`
- `Image.fromResource(String resourceName)`
- `Image.fromBase64String(String base64)`
- `new Image(int width, int height)`
- `new Image(int width, int height, Color backgroundColor)`

Images posses the same capabilities as the `CodeDraw` class,
which makes it straightforward to draw on them.
The following example demonstrates loading an image, drawing an orange circle onto it,
and saving the result as an edited image.

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

#### Image editing:

The `Image` class also has build in image editing capabilities.

```java
Image myImage = Image.fromFile("plant.png");
Image cropped = Image.crop(myImage, 100, 100, 200, 100);
Image rotated = Image.rotateClockwise(cropped);
Image mirrored = Image.mirrorVertically(rotated);
```

![30 Image Editing.png](illustrations%2Fintroduction%2F30%20Image%20Editing.png)
The operations performed by the code above.

Image editing function:
- `Image.crop(Image source, int x, int y, int width, int height)`
- `Image.scale(Image source, double scale)`
- `Image.scale(Image source, double scale, Interpolation interpolation)`
- `Image.rotateClockwise(Image image)`
- `Image.rotateCounterClockwise(Image image)`
- `Image.mirrorHorizontally(Image image)`
- `Image.mirrorVertically(Image image)`

## Creating Animations

Animations are created by generating multiple frames with pauses in between each frame.
In CodeDraw, this is accomplished by creating a loop, where each iteration produces a single frame
and then waits a specific amount of time.

The animation below increases the `sec` variable by 1/60th of pi (equivalent to one second) each iteration.
Whenever the clock's second hand is updated, the entire canvas is cleared through `.clear()`.
Subsequently, the clock's second hand is drawn, along with the twelve dots that indicate the hour.
Finally, the `show(long waitMilliseconds)` method must be called to display the drawn frame and wait for 1 second.
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
such as the user pressing a key or moving the mouse.
CodeDraw provides 12 different events:

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

To create an application that is capable of handling user input,
it is essential to first determine which variables are necessary to store the state of your application.
In the example provided below, the mouse position and the amount of clicks is tracked.

In each iteration of the outer `while` loop, a single frame is drawn.
Within this loop, the inner `foreach` loop processes all events occurring between frames.
Depending on the type of event detected, different code is executed:

* If the event is a `MouseMoveEvent`, the `mouseX` and `mouseY` variables are updated to reflect the new mouse position.
* In the case of a `MouseClickEvent`, the `clickCount` is incremented by one.

Once the inner `foreach` loop has processed all currently available events, the updated event data is displayed.
The previously drawn image is cleared using the `.clear()` function, and the new data is drawn using `.drawText()`.

Finally, the `show(long waitMilliseconds)` function is called to display the updated image,
and a 16-millisecond delay is set, which results in roughly 60 frames per seconds being rendered.

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

If you are using an older version of Java, you can utilize the `java.util.Scanner`-like properties of the `EventScanner`.
The java `Scanner` and the `EventScanner` are both queues where the elements are put into the back of the queue automatically
and the program using the queue takes the elements out from the front of the queue.

Like in the previous section, we need a loop to take out all the available events.
In each iteration, a new event while be at the head of the queue, and the inner while loop will continue until all available events are consumed.
Within the inner loop, the selected branch of the `if`/`else if`/`else` statement depends on the type of event at the head of the queue.

If the head of the queue is a `MouseMoveEvent`, the `.hasMouseMoveEvent()` method will return true.
Consequently, the `nextMouseMoveEvent()` method will be called, which retrieves the `MouseMoveEvent` from the head of the queue.
When removing the head of the queue, all other events within the queue will shift forward,
and there will be a new event at the head of the queue.
It is crucial to call the corresponding `next` method, as failing to do so would result in an endless loop
because the same event would always remain at the head of the queue.

Once the `MouseMoveEvent` has been returned from the `nextMouseMoveEvent()` method,
it can be used to update the program's state.
In this case, it updates the current mouse position.
After processing all currently available events, the changes are displayed by clearing the canvas and then invoking the `drawText` method.

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

For a more in depth example using the `EventScanner` look at an implementation of [Conway's Game of Life](/src/examples/java/GameOfLife.java) using CodeDraw.

## The Animation Interface

The `Animation` interface offers similar capabilities to the `CodeDraw` class, with exception to some of its GUI features.
For instance, you cannot specify the window position or set the title of the CodeDraw window.

The `Animation` interface provides an object-oriented approach to using CodeDraw.
To begin, your class must inherit from the `Animation` interface.
Inside the `main` method, you then pass an instance of your `Animation` to the `CodeDraw.run(...)` method.
This will execute the `Animation` interface until the user closes the window.

Within your class, you have to define the state of you application.
In the example below, the x and y coordinate of a black circle is stored in two object variable.

Drawing is implemented by overriding the `draw` method,
which is automatically called 60 times per second.

For each event there is a corresponding `onEventName()` method that can optionally be overridden.
These methods are automatically executed whenever a user interaction occurs.
The only code that needs to be written is the code responsible for altering the application's state.

The `CodeDraw.run` method also offers overloads that let you set the size of the window and the frames per second using
`CodeDraw.run(Animation animation, int width, int height, int framesPerSecond)`.
The frames per second define how many times per second the `draw` method is called.

The main advantage of using the `Animation` interface is that it reduces the amount of loops and branches.
However, it also gives you less control over what happens when in your application.


```java
import codedraw.*;

public class MyAnimation implements Animation {
    public static void main(String[] args) {
        CodeDraw.run(new MyAnimation());
    }

    private int x = 50;
    private int y = 50;

    @Override
    public void onKeyDown(KeyDownEvent event) {
        if (event.getKey() == Key.W) {
            y -= 20;
        }
        else if (event.getKey() == Key.A) {
            x -= 20;
        }
        else if (event.getKey() == Key.S) {
            y += 20;
        }
        else if (event.getKey() == Key.D) {
            x += 20;
        }
    }

    @Override
    public void draw(Image canvas) {
        canvas.clear();
        canvas.fillCircle(x, y, 10);
    }
}
```

## GUI Development With CodeDraw

In general, I would advise against using CodeDraw for the implementation of even small graphical user interfaces (GUIs).
However, for a few small graphical components it should be sufficient.
The fundamental idea is to create multiple classes, each with a configurable x and y position.
All of these classes implement the `Animation` interface.
In the example below, both the `MyButton` and the `MyTextBox` classes implement the `Animation` interface.
The `Animation.combine` method is then used to create a single `Animation` that can be passed to `CodeDraw.run`.

```Java
import codedraw.*;

public class MyGUI implements Animation {
	public static void main(String[] args) {
		MyButton button = new MyButton(100, 100); // implements the Animation interface
		MyTextBox textBox = new MyTextBox(100, 200); // implements the Animation interface

		CodeDraw.run(Animation.combine(button, textBox));
	}
}
```

