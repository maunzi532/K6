package logic;

import javafx.scene.input.*;

public interface XInputInterface
{
	void frameTime(long nanoFrameTime);

	void mousePosition(double xMouse, double yMouse);

	void dragPosition(double xStart, double yStart, double xMoved, double yMoved, int mouseKey);

	void handleMouseMove(double x, double y);

	void handleClick(double x, double y, int mouseKey);

	void handleDrag(double xStart, double yStart, double xEnd, double yEnd, int mouseKey);

	void handleKey(KeyCode keyCode);

	void tick();
}