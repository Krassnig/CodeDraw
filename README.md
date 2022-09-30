# CodeDraw

CodeDraw is an easy-to-use drawing library where you use code to create pictures and animations.
It is made for beginners that understand little about programming
and makes it very simple to draw and animate various shapes and images to a canvas.

Read the [introduction to CodeDraw](https://github.com/Krassnig/CodeDraw/blob/master/INTRODUCTION.md)
for a beginners guide to CodeDraw. It also gives an overview of the features available in CodeDraw.

The JavaDoc for CodeDraw can be found [here](https://krassnig.github.io/CodeDrawJavaDoc/).

For a C# version of CodeDraw go to the [CodeDrawProject repository](https://github.com/Krassnig/CodeDrawProject).

## How to Install

### Intellij

Go to [releases](https://github.com/Krassnig/CodeDraw/releases) and download the newest CodeDraw.jar.

Open Intellij with the project where you would like to add CodeDraw. Click on **File > Project Structure...**.
Under **Project Settings** select **Libraries**.
At the top left click on the small **plus icon** and select the **Java** option.
Goto the downloaded CodeDraw.jar and select it and then press **OK**.
Now you can import CodeDraw with ```import codedraw.*;``` at the top of your Java files.

### Eclipse

Go to [releases](https://github.com/Krassnig/CodeDraw/releases) and download the newest CodeDraw.jar.

Open Eclipse with the project where you would like to add CodeDraw. Right-click on your Project > **Properties > Java Build Path > Add External JARs...**
Go to the downloaded CodeDraw.jar and select it and then press **OK**.
Now you can import CodeDraw with ```import codedraw.*;``` at the top of your Java files.


### Maven
To use CodeDraw as a Maven dependency add the following lines to your `pom.xml`.
```xml
<repositories>
	<repository>
		<id>jitpack.io</id>
		<url>https://jitpack.io</url>
	</repository>
</repositories>
```

```xml
<dependency>
	<groupId>com.github.Krassnig</groupId>
	<artifactId>CodeDraw</artifactId>
	<version>2.1.0</version>
</dependency>
```

### Gradle
To use CodeDraw as a Gradle dependency add the following lines to your `build.gradle`.
```groovy
allprojects {
	repositories {
		maven { url 'https://jitpack.io' }
	}
}
```

```groovy
dependencies {
	implementation 'com.github.Krassnig:CodeDraw:2.1.0'
}
```

## Static Images

Here is a quick illustration on how to create a static image with CodeDraw.

```java
import codedraw.*;

public class Main {
	public static void main(String[] args) {
		// Creates a new CodeDraw window with the size of 400x400 pixel
		CodeDraw cd = new CodeDraw(400, 400);

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
# ❗ Don't forget to call .show() ❗

![static_image](https://user-images.githubusercontent.com/24553082/153450298-403d3adc-87f9-476e-82a4-48aeac21ec90.png)

## Animations

Animation can be created by repeatedly rendering different images and waiting after through the `show(1000)` method.

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

https://user-images.githubusercontent.com/24553082/153450395-71f69b67-9b86-4f16-b0b6-e88c85650391.mp4

## Interactive Programs

Interactive programs can be created by reading events from the EventScanner.
For each event the type of event has to be checked through a `has****()` methods and
if the desired event is found the event can be extracted by using the corresponding `next****()` method.
The EventScanner works very similarly to the `java.util.Scanner` in Java.

```java
import codedraw.*;

public class Main {
	public static void main(String[] args) {
		CodeDraw cd = new CodeDraw();
		EventScanner es = new EventScanner(cd);

		cd.drawText(200, 200, "Move your mouse over here.");
		cd.show();

		cd.setColor(Palette.RED);

		// creates an endless loop (until you close the window)
		while (!es.isClosed()) {
			// creates a loop that consumes all the currently available events
			while (es.hasEventNow()) {
				// if the next event is a mouse move event a red square will be drawn at its location
				if (es.hasMouseMoveEvent()) {
					MouseMoveEventArgs a = es.nextMouseMoveEvent();
					cd.fillSquare(a.getX() - 5, a.getY() - 5, 10);
				} else {
					// removes the event from the EventScanner since it is not a MouseMoveEvent
					es.nextEvent();
				}
			}

			// shows the red squares that have been drawn until now
			cd.show(16);
		}
	}
}
```

https://user-images.githubusercontent.com/24553082/153450427-b9091fb7-3b1e-413b-b01e-6b89bf50d447.mp4

## Inversion of Control

All examples can also be created using the `Animation` interface. An instance of the `Animation` interface
can be passed to CodeDraw where CodeDraw then calls the methods you implement. 

```Java
import codedraw.*;

public class InversionOfControl implements Animation {
	public static void main(String[] args) {
		CodeDraw.run(new InversionOfControl());
	}

	private int x = 50;
	private int y = 50;

	@Override
	public void onMouseMove(MouseMoveEvent event) {
		x = event.getX();
		y = event.getY();
	}

	@Override
	public void draw(Image canvas) {
		canvas.fillCircle(x, y, 10);
	}
}
```