# CodeDraw

CodeDraw is an easy-to-use drawing library where you use code to create pictures and animations.
It is made for beginners that understand little about programming
and makes it very simple to draw and animate various shapes and images to a canvas.

Read the [Introduction to CodeDraw](./INTRODUCTION.md)
for a beginners guide to CodeDraw. It also gives an overview of the features available in CodeDraw.

The JavaDoc for CodeDraw can be found [here](https://krassnig.github.io/CodeDrawJavaDoc/).

For a C# version of CodeDraw go to the [CodeDrawProject repository](https://github.com/Krassnig/CodeDrawProject).

## How to Install

Go to [releases](https://github.com/Krassnig/CodeDraw/releases) and download the newest CodeDraw.jar.

Open Intellij with the project where you would like to add CodeDraw. Click on **File > Project Structure...**.
Under **Project Settings** select **Libraries**.
At the top left click on the small **plus icon** and select the **Java** option.
Goto the downloaded CodeDraw.jar and select it and then press **OK**.
Now you can import CodeDraw with `import codedraw.*;` at the top of your Java files.

To install CodeDraw with Eclipse, Maven or Gradle please read the [INSTALL.md](./INSTALL.md).

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
## ❗ Don't forget to call `show()` ❗

![static_image](https://user-images.githubusercontent.com/24553082/153450298-403d3adc-87f9-476e-82a4-48aeac21ec90.png)

## Animations

Animations are created by drawing multiple frames and then pausing in between those frames.
In CodeDraw this is achieved by creating a loop, in which each iteration draws one frame
and then waits a certain amount of time, in this case 1 second or 1000 milliseconds with the `show(1000)` command.

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

https://user-images.githubusercontent.com/24553082/153450395-71f69b67-9b86-4f16-b0b6-e88c85650391.mp4

## Interactive Programs

Interactive programs can be created by reading events from the EventScanner switching based on the type of event.
In older Java versions the EventScanner can also be used the same way as the `java.util.Scanner`
with *has* and *next* methods.
A more detailed explanation can be found in the 
[event handling section](./INTRODUCTION.md#handling-events) in the Introduction to CodeDraw.

```java
import codedraw.*;

public class Main {
    public static void main(String[] args) {
        CodeDraw cd = new CodeDraw();

        cd.drawText(200, 200, "Move your mouse over here.");
        cd.show();

        cd.setColor(Palette.RED);

        // creates an endless loop (until you close the window)
        while (!cd.isClosed()) {
            // creates a loop that consumes all the currently available events
            for (var e : cd.getEventScanner()) {
                switch (e) {
                    // if the event is a mouse move event a red square will be drawn at its location
                    case MouseMoveEvent a ->
                        cd.fillSquare(a.getX() - 5, a.getY() - 5, 10);
                    default -> { }
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

All examples can also be created using the `Animation` interface.
An instance of the `Animation` interface can be passed to CodeDraw
where CodeDraw then calls the methods you implement.
The example below lets you control a circle with the WASD keys. 

```Java
import codedraw.*;

public class InversionOfControl implements Animation {
    public static void main(String[] args) {
        CodeDraw.run(new InversionOfControl());
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

https://user-images.githubusercontent.com/24553082/193877472-17efc505-11a5-409e-951c-c095a4a8cb1b.mp4
