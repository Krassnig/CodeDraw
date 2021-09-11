# CodeDraw

CodeDraw is an easy-to-use drawing library where you use code to create pictures and animations.
It is made for beginners that understand little about programming
and makes it very simple to draw and animate various shapes and images to a canvas.

Read the [introduction to CodeDraw](https://github.com/Krassnig/CodeDraw/blob/master/INTRODUCTION.md)
to learn how to use this library.

For a C# version of CodeDraw go to the [CodeDrawProject repository](https://github.com/Krassnig/CodeDrawProject).

## How to install

Go to [releases](https://github.com/Krassnig/CodeDraw/releases) and download the newest CodeDraw.jar.

### Intellij

Open Intellij with the project where you would like to add CodeDraw. Click on **File > Project Structure...**.
Under **Project Settings** select **Libraries**.
At the top left click on the small **plus icon** and select the **Java** option.
Goto the downloaded CodeDraw.jar and select it and then press **OK**.
Now you can import CodeDraw with ```import codedraw.*;``` at the top of your Java files.

### Eclipse

Open Eclipse with the project where you would like to add CodeDraw. Right click on your Project > **Properties > Java Build Path > Add External JARs...**
Go to the downloaded CodeDraw.jar and select it and then press **OK**.
Now you can import CodeDraw with ```import codedraw.*;``` at the top of your Java files.


## Examples

```java
import codedraw.*;

public class MyProgram {
	public static void main(String[] args) {
		// Creates a new CodeDraw window with the size of 600x600 pixel
		CodeDraw cd = new CodeDraw();

		// Sets the drawing color to red
		cd.setColor(Palette.RED);
		// Draws the outline of a rectangle
		cd.drawRectangle(100, 100, 200, 100);
		// Draws a filled Square
		cd.fillSquare(180, 150, 80);

		// Changes the color to light blue
		cd.setColor(Palette.LIGHT_BLUE);
		cd.fillCircle(300, 200, 50);

		// Finally, the method show must be called
		// to display the drawn shapes in the CodeDraw window.
		cd.show();
	}
}
```
![basics](https://user-images.githubusercontent.com/24553082/131073128-238c3b5c-0632-4614-a7d8-ac86271ea9d6.png)

# ❗ Don't forget to call .show() ❗

### Animations

Animation can be created by repeatedly rendering different images and waiting after renders.

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

			// displays the drawn objects and waits 1 second
			cd.show(1000);
		}
	}
}
```

https://user-images.githubusercontent.com/24553082/122690522-3d124900-d22a-11eb-863f-ffdb3f3f8017.mp4

## Api

### Properties

* color
* lineWidth
* corner
* isAntiAliased
* textFormat
	* fontName
	* fontSize
	* horizontalAlign
	* verticalAlign
	* underline
	* isBold
	* isItalic
	* isStrikethrough


* title
* width/height
* windowPosition
* canvasPosition

### Methods

* show: Displays the drawn shapes and images on the canvas.
* saveCanvas: Copies the canvas as an Image.
* dispose: Closes the canvas and frees all associated resources.
* clear: Clear fills the entire canvas with white (or with a color of your choosing).

### Drawing Methods

Draw methods draw just the outline of the shape while fill methods draw the shape and fill their contents.
The origin points for non-circular shapes is the top-left corner, while for circular shapes it is the center.

* drawText
* drawImage
  

* drawPixel
* drawPoint
* drawLine
* drawCurve
* drawBezier
  

* drawSquare
* drawRectangle
* drawCircle
* drawEllipse
* drawArc
* drawTriangle
* drawPolygon
  

* fillSquare
* fillRectangle
* fillCircle
* fillEllipse
* fillArc
* fillTriangle
* fillPolygon

### Events

An event is something that occurs based on user input like the user
pressing a button or moving the mouse. You can subscribe to an Event
by passing a method reference or lambda to CodeDraw.
All events start with the 'on' keyword (e.g. *onKeyPress* or *onMouseMove*).
By subscribing to an event will return a Subscription which
can be used to unsubscribe from the event.

```java
import java.awt.event.MouseEvent;
import codedraw.*;

public class Main {
	public static void main(String[] args) {
		CodeDraw cd = new CodeDraw();

		cd.drawText(200, 200, "Move your mouse over here.");
		cd.show();
		cd.setColor(Palette.RED);

		cd.onMouseMove(Main::draw);
	}

	// This method will be called by CodeDraw everytime the user moves their mouse
	static void draw(CodeDraw cd, MouseEvent me) {
		cd.fillSquare(me.getX() - 2, me.getY() - 2, 4);
		cd.show();
	}
}
```

https://user-images.githubusercontent.com/24553082/122690528-4a2f3800-d22a-11eb-9a8d-72162af9c50f.mp4
