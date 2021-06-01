# CodeDraw

CodeDraw is an easy-to-use drawing library.

## How to install

Go to https://github.com/Krassnig/CodeDrawForJava/releases and download the newest CodeDraw.jar .

### Intellij

Open Intellij with the project where you would like to add CodeDraw. Click on **File > Project Structure...**.
In **Project Settings** select **Modules**.
At the bottom left click on the small **plus icon** and select the **JARs or Directories...** option.
Goto the downloaded CodeDraw.jar and select it and then press **OK**.
Now you can import CodeDraw with ```import CodeDraw.*;``` at the top of your Java files.

### Eclipse

Open Eclipse with the project where you would like to add CodeDraw. Right click on your Project > **Properties > Java Build Path > Add External JARs...**
Go to the downloaded CodeDraw.jar and select it and then press **OK**.
Now you can import CodeDraw with ```import CodeDraw.*;``` at the top of your Java files.


## Example

```java
CodeDraw cd = new CodeDraw(); // creates a canvas of size 600x600 pixel

// All following drawn objects will be red,
// until the color is set to a different color.
cd.setColor(Color.RED);

// draws a red circle at the center of the canvas with a radius of 50 pixel.
// The circle is not yet displayed!
cd.drawCircle(300, 300, 50);

// Must be called to display everything that has been drawn until now!
cd.show();
```

## Concepts

### Canvas

Is the rectangle on the screen that is used for drawing. It's origin
point (0, 0) is at the top left. Everything is drawn front the top left to the bottom right.
Once the size is set via the constructor the size of the canvas remains fixed.

### Frame

Is the frame surrounding the canvas. It is larger than the size given
to the constructor of CodeDraw. It contains the closing and minimize button.

## Api

### Properties

- Height
- Width
- FramePosition
- Title
- Font
- Color
- LineSize

### Methods

- Show
- Clear
- AsImage
- Dispose

### Drawing Methods

- [DrawText](https://github.com/Krassnig/CodeDrawForJava/blob/3b3f0d94ab674e355c17e1f2f7fa30ab1efc442f/src/CodeDraw/CodeDraw.java#L194)
- DrawPoint
- DrawLine
- DrawCurve
- DrawBezier
- DrawSquare
- DrawRectangle
- DrawCircle
- DrawEllipse
- DrawArc
- DrawTriangle
- DrawPolygon
- DrawImage

### Fill Methods

- FillSquare
- FillRectangle
- FillCircle
- FillEllipse
- FillArc
- FillTriangle
- FillPolygon

### Events

An event is something that occurs based on user input like the user
pressing a button or moving the mouse. You can subscribe to an Event
by passing a method reference or lambda to CodeDraw. All events are
marked by starting with the 'on' keyword.

- OnMouseClick
- OnMouseMove
- OnMouseDown
- OnMouseUp
- OnMouseEnter
- OnMouseLeave
- OnMouseWheel
- OnKeyDown
- OnKeyUp
- OnKeyPress
- OnFrameMove
