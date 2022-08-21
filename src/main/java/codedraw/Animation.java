package codedraw;

import codedraw.drawing.Image;
import codedraw.events.*;

public interface Animation {
	void draw(Image canvas);

	default void simulate() { }

	default void onMouseClick(MouseClickEvent event) { }
	default void onMouseMove(MouseMoveEvent event) { }
	default void onMouseDown(MouseDownEvent event) { }
	default void onMouseUp(MouseUpEvent event) { }
	default void onMouseEnter(MouseEnterEvent event) { }
	default void onMouseLeave(MouseLeaveEvent event) { }
	default void onMouseWheel(MouseWheelEvent event) { }
	default void onKeyDown(KeyDownEvent event) { }
	default void onKeyUp(KeyUpEvent event) { }
	default void onKeyPress(KeyPressEvent event) { }
	default void onWindowMove(WindowMoveEvent event) { }
	default void onWindowClose(WindowCloseEvent event) { }
}
