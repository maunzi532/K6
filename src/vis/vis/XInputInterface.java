package vis.vis;

import logic.*;

public interface XInputInterface
{
	void frameTime(long nanoFrameTime);

	void mousePosition(double xMouse, double yMouse, boolean inside, boolean moved, boolean drag, XKey key);

	void dragPosition(boolean active, double xStart, double yStart,
			double xMoved, double yMoved, XKey key, boolean finished);

	void handleKey(XKey key);

	void tick();
}