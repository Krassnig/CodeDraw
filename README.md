# CodeDraw

CodeDraw is an easy-to-use drawing library.
It is made for beginners that understand little about programming
and makes it very simple to draw and animate various shapes and images to a canvas.

## How to install

Go to [releases](https://github.com/Krassnig/CodeDrawForJava/releases) and download the newest CodeDraw.jar.

### Intellij

Open Intellij with the project where you would like to add CodeDraw. Click on **File > Project Structure...**.
In **Project Settings** select **Modules**.
At the bottom left click on the small **plus icon** and select the **JARs or Directories...** option.
Goto the downloaded CodeDraw.jar and select it and then press **OK**.
Now you can import CodeDraw with ```import codedraw.*;``` at the top of your Java files.

### Eclipse

Open Eclipse with the project where you would like to add CodeDraw. Right click on your Project > **Properties > Java Build Path > Add External JARs...**
Go to the downloaded CodeDraw.jar and select it and then press **OK**.
Now you can import CodeDraw with ```import codedraw.*;``` at the top of your Java files.


## Example

```java
CodeDraw cd = new CodeDraw(300, 300); // creates a canvas of size 300x300 pixel

// All following drawn objects will be red,
// until the color is set to a different color.
cd.setColor(Palette.RED);

// draws a red circle at the center of the canvas with a radius of 50 pixel.
// The circle is not yet displayed!
cd.drawCircle(150, 150, 50);

// Must be called to display everything that has been drawn until now!
cd.show();
```
# ❗ Don't forget to call .show() ❗

![An illustration of what happens when the code above gets executed](https://github.com/Krassnig/CodeDrawForJava/blob/450ce7d17d071a3f74168fa459eae28aa519b4de/illustrations/basics.png)

### Animation Example

```java
import codedraw.*;

class Main {
	static void main(String[] args) {
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

## Concepts

### Canvas

Is the rectangle on the screen that is used for drawing. It's origin
point (0, 0) is at the top left. Everything is drawn front the top left to the bottom right.
Once the size is set via the constructor the size of the canvas remains fixed.

### Frame

Is the frame surrounding the canvas. It is larger than the size given to the constructor
of CodeDraw. It contains the closing and minimize button, the title and the CodeDraw icon.

## Api

### Properties

- width
- height
- framePosition
- canvasPosition
- title
- format
- color
- lineWidth

### Methods

- show: Displays the drawn shapes and images on the canvas.
- asImage: Copies the canvas as an Image.
- dispose: Closes the canvas and frees all associated resources.
- clear: Clear fills the entire canvas with white (or with a color of your choosing).

### Drawing Methods

Draw methods draw just the outline of the shape while fill methods draw the shape and fill their contents.
The origin points for non-circular shapes is the top-left corner, while for circular shapes it is the center.

- drawText
- drawPoint
- drawLine
- drawCurve
- drawBezier
- drawSquare
- drawRectangle
- drawCircle
- drawEllipse
- drawArc
- drawTriangle
- drawPolygon
- drawImage
- fillSquare
- fillRectangle
- fillCircle
- fillEllipse
- fillArc
- fillTriangle
- fillPolygon


### Events

An event is something that occurs based on user input like the user
pressing a button or moving the mouse. You can subscribe to an Event
by passing a method reference or lambda to CodeDRaw.
All events start with the 'on' keyword.
Subscribing to an event method will return a Subscription which
can be used to unsubscribe from the event.

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
- onFrameMove

#### Example Event

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
