package levelMap.editor;

import draw.*;
import geom.f1.*;
import gui.guis.*;
import logic.*;

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

	public void onEnter(MainState mainState)
	{
		mode.onEnter(mainState);
	}

	public void onClick(MainState mainState, int mouseKey)
	{
		mode.onClick(mainState, mouseKey);
	}

	public void onMapClick(MainState mainState, Tile tile, int mouseKey)
	{
		mode.onMapClick(mainState, tile, mouseKey);
	}
}