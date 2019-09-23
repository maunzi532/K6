package visual;

import javafx.scene.input.*;

public interface XInputInterface
{
	void frameTime(long nanoFrameTime);

	void mousePosition(double xMouse, double yMouse, boolean inside, boolean moved, boolean drag, int mouseKey);

	void dragPosition(boolean active, double xStart, double yStart,
			double xMoved, double yMoved, int mouseKey, boolean finished);

	void handleKey(KeyCode keyCode);

	void tick();
}