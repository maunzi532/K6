package levelMap.editor.editingModes;

import geom.f1.*;
import gui.*;
import java.io.*;
import java.nio.file.*;
import javafx.stage.*;
import levelMap.editor.*;
import logic.*;

public class SaveMode implements EditingMode
{
	private static final GuiTile[] GUI_TILES = new GuiTile[]{new GuiTile("Save"), new GuiTile("Saved"), new GuiTile("Error")};

	private int state;

	@Override
	public GuiTile guiTile()
	{
		return GUI_TILES[state];
	}

	@Override
	public void onEnter(MainState mainState)
	{
		state = 0;
	}

	@Override
	public void onClick(MainState mainState, int mouseKey)
	{
		try
		{
			Files.write(new FileChooser().showSaveDialog(null).toPath(), mainState.levelMap.saveData());
			state = 1;
		}catch(IOException e)
		{
			state = 2;
		}
	}

	@Override
	public boolean onMapClick(MainState mainState, Tile tile, int mouseKey)
	{
		return false;
	}
}