package codedraw;

import codedraw.drawing.Image;
import codedraw.events.*;

/**
 * The animation interface can be implemented to do animations and interaction with the user.
 * Pass an instance of the Animation interface to {@link CodeDraw#run(Animation)},
 * {@link BorderlessWindow#run(Animation)} or {@link FullScreen#run(Animation)}.
 * CodeDraw at first always calls the event functions like {@link #onMouseMove(MouseMoveEvent)}
 * then the {@link #simulate()} function and then if there is enough time the {@link #draw(Image)} function.
 */
public interface Animation {
	/**
	 * This function gets called to draw content to the canvas.
	 * The calling frequency is defined when calling a run method and is 60 per default.
	 * If the computer is under heavy load it might not achieve the desired frequency and draw is not called.
	 * @param canvas the canvas to draw on.
	 */
	void draw(Image canvas);

	/**
	 * This function is used to execute the application independent of the draw method.
	 * Draw calls might be dropped but simulate will not be dropped.
	 * The default frequency is 60 times per second.
	 */
	default void simulate() { }

	/**
	 * This function will be called once when a mouse button is pressed down and quickly released again.
	 * @param event the data of the event.
	 */
	default void onMouseClick(MouseClickEvent event) { }
	/**
	 * This function will be called continuously while the mouse is being moved.
	 * @param event the data of the event.
	 */
	default void onMouseMove(MouseMoveEvent event) { }
	/**
	 * This function will be called exactly once when a mouse button is pressed down.
	 * @param event the data of the event.
	 */
	default void onMouseDown(MouseDownEvent event) { }
	/**
	 * This function will be called exactly once when a mouse button is released.
	 * @param event the data of the event.
	 */
	default void onMouseUp(MouseUpEvent event) { }
	/**
	 * This function will be called when the mouse enters the canvas.
	 * @param event the data of the event.
	 */
	default void onMouseEnter(MouseEnterEvent event) { }
	/**
	 * This function will be called when the mouse leaves the canvas.
	 * @param event the data of the event.
	 */
	default void onMouseLeave(MouseLeaveEvent event) { }
	/**
	 * This function will be called each time the mouse wheel is turned.
	 * @param event the data of the event.
	 */
	default void onMouseWheel(MouseWheelEvent event) { }
	/**
	 * This function will be called exactly once when a key is pressed down.
	 * @param event the data of the event.
	 */
	default void onKeyDown(KeyDownEvent event) { }
	/**
	 * This function will be called exactly once when a key is released.
	 * @param event the data of the event.
	 */
	default void onKeyUp(KeyUpEvent event) { }
	/**
	 * This function will be called continuously while a key is being held down.
	 * @param event the data of the event.
	 */
	default void onKeyPress(KeyPressEvent event) { }
	/**
	 * This function will be called every time the CodeDraw window is moved.
	 * @param event the data of the event.
	 */
	default void onWindowMove(WindowMoveEvent event) { }
	/**
	 * This function will be called exactly once when the user closes the window or {@link codedraw.CodeDraw#close()} is called.
	 * @param event the data of the event.
	 */
	default void onWindowClose(WindowCloseEvent event) { }
}
