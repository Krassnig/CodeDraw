package codedraw;

/**
 * The animation interface can be implemented to create animations and interactive applications.
 * Pass an instance of the Animation interface to {@link CodeDraw#run(Animation)},
 * {@link BorderlessWindow#run(Animation)} or {@link FullScreen#run(Animation)}.
 * The functions in this interface are executed in order:
 * First, the event methods like {@link #onMouseMove(MouseMoveEvent)} are called.
 * Second, the {@link #simulate()} method is called.
 * Third, the {@link #draw(Image)} method is called.
 */
public interface Animation {
	/**
	 * This method is called in regular intervals to draw onto the canvas.
	 * The frequency is defined, by setting the frames per second on the {@link CodeDraw#run(Animation)} method.
	 * Per default the frequency is set to 60 frames per second.
	 * If the computer is under heavy load it might not achieve the desired frequency and draw is not called.
	 * @param canvas the canvas to draw on.
	 */
	void draw(Image canvas);

	/**
	 * This method is executed independently of the {@link #draw(Image)} method.
	 * While the {@link #draw(Image)} might not get executed when the user's computer is under load,
	 * this method will always get executed.
	 * Per default the frequency is set to 60 times per second.
	 */
	default void simulate() { }

	/**
	 * This method is called once every time a mouse button is pressed down and quickly released again.
	 * @param event the data of the event.
	 */
	default void onMouseClick(MouseClickEvent event) { }
	/**
	 * This method is called continuously while the mouse is being moved.
	 * @param event the data of the event.
	 */
	default void onMouseMove(MouseMoveEvent event) { }
	/**
	 * This method is called exactly once every time a mouse button is pressed down.
	 * @param event the data of the event.
	 */
	default void onMouseDown(MouseDownEvent event) { }
	/**
	 * This method is called exactly once every time a mouse button is released.
	 * @param event the data of the event.
	 */
	default void onMouseUp(MouseUpEvent event) { }
	/**
	 * This method is called every time the mouse enters the canvas.
	 * @param event the data of the event.
	 */
	default void onMouseEnter(MouseEnterEvent event) { }
	/**
	 * This method is called every time the mouse leaves the canvas.
	 * @param event the data of the event.
	 */
	default void onMouseLeave(MouseLeaveEvent event) { }
	/**
	 * This method is called every time the mouse wheel is turned.
	 * @param event the data of the event.
	 */
	default void onMouseWheel(MouseWheelEvent event) { }
	/**
	 * This method is called exactly once every time a key is pressed down.
	 * @param event the data of the event.
	 */
	default void onKeyDown(KeyDownEvent event) { }
	/**
	 * This method is called exactly once every time a key is released.
	 * @param event the data of the event.
	 */
	default void onKeyUp(KeyUpEvent event) { }
	/**
	 * This method is called continuously while a key is being held down.
	 * @param event the data of the event.
	 */
	default void onKeyPress(KeyPressEvent event) { }
	/**
	 * This method is called every time the CodeDraw window is moved.
	 * @param event the data of the event.
	 */
	default void onWindowMove(WindowMoveEvent event) { }
	/**
	 * This method is called exactly once after the user closes the window or {@link codedraw.CodeDraw#close()} is called.
	 * @param event the data of the event.
	 */
	default void onWindowClose(WindowCloseEvent event) { }
}
