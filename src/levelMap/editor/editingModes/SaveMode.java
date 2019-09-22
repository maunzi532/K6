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
			String[] texts = mainState.levelMap.saveDataJSON(mainState.itemLoader);
			Files.write(new FileChooser().showSaveDialog(null).toPath(), texts[0].getBytes());
			Files.write(new FileChooser().showSaveDialog(null).toPath(), texts[1].getBytes());
			state = 1;
		}catch(IOException e)
		{
			state = 2;
		}
	}

	@Override
	public void onMapClick(MainState mainState, Tile tile, int mouseKey){}
}