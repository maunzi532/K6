package logic.gui;

public interface GuiElement
{
	void update();

	void draw(GuiTile[][] tiles);

	ElementTargetResult target(int x, int y, boolean click);
}