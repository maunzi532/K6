package levelMap.editor;

import draw.*;
import geom.f1.*;
import gui.guis.*;
import levelMap.*;

public class EditorSlot
{
	private final VisualGUI visualGUI;
	private final EditingGUI gui;
	private EditingMode mode;

	public EditorSlot(VisualGUI visualGUI, EditingMode mode)
	{
		this.visualGUI = visualGUI;
		gui = new EditingGUI();
		setMode(mode);
	}

	public void setMode(EditingMode mode)
	{
		this.mode = mode;
	}

	public boolean isClicked(double x, double y)
	{
		return visualGUI.inside(x, y, gui);
	}

	public void draw()
	{
		gui.setTile(mode.guiTile());
		visualGUI.draw(gui);
	}

	public void onClick(int mouseKey, boolean active)
	{
		mode.onClick(mouseKey, active);
	}

	public void onMapClick(Tile tile, LevelMap levelMap, int mouseKey)
	{
		mode.onMapClick(tile, levelMap, mouseKey);
	}
}