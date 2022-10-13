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

	/**
	 * Multiple animations can be combined into a single animation.
	 * The animations given as a parameter are called in the order they are given in.
	 * @param animations list of animations.
	 * @return A new animation.
	 */
	static Animation combine(Animation... animations) {
		return new Animation() {
			@Override
			public void draw(Image canvas) {
				for (Animation a : animations) {
					canvas.resetProperties();
					a.draw(canvas);
				}
			}

			@Override
			public void simulate() {
				for (Animation a : animations) {
					a.simulate();
				}
			}

			@Override
			public void onMouseClick(MouseClickEvent event) {
				for (Animation a : animations) {
					a.onMouseClick(event);
				}
			}

			@Override
			public void onMouseMove(MouseMoveEvent event) {
				for (Animation a : animations) {
					a.onMouseMove(event);
				}
			}

			@Override
			public void onMouseDown(MouseDownEvent event) {
				for (Animation a : animations) {
					a.onMouseDown(event);
				}
			}

			@Override
			public void onMouseUp(MouseUpEvent event) {
				for (Animation a : animations) {
					a.onMouseUp(event);
				}
			}

			@Override
			public void onMouseEnter(MouseEnterEvent event) {
				for (Animation a : animations) {
					a.onMouseEnter(event);
				}
			}

			@Override
			public void onMouseLeave(MouseLeaveEvent event) {
				for (Animation a : animations) {
					a.onMouseLeave(event);
				}
			}

			@Override
			public void onMouseWheel(MouseWheelEvent event) {
				for (Animation a : animations) {
					a.onMouseWheel(event);
				}
			}

			@Override
			public void onKeyDown(KeyDownEvent event) {
				for (Animation a : animations) {
					a.onKeyDown(event);
				}
			}

			@Override
			public void onKeyUp(KeyUpEvent event) {
				for (Animation a : animations) {
					a.onKeyUp(event);
				}
			}

			@Override
			public void onKeyPress(KeyPressEvent event) {
				for (Animation a : animations) {
					a.onKeyPress(event);
				}
			}

			@Override
			public void onWindowMove(WindowMoveEvent event) {
				for (Animation a : animations) {
					a.onWindowMove(event);
				}
			}

			@Override
			public void onWindowClose(WindowCloseEvent event) {
				for (Animation a : animations) {
					a.onWindowClose(event);
				}
			}
		};
	}
}
