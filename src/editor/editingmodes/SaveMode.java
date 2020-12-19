package editor.editingmodes;

import geom.tile.*;
import gui.*;
import java.io.*;
import java.nio.file.*;
import javafx.stage.*;
import load.*;
import logic.*;
import editor.edit.*;

public final class SaveMode implements EditingMode
{
	private static final GuiTile[] GUI_TILES =
			{
					new GuiTile("editmode.save"),
					new GuiTile("editmode.save.saved"),
					new GuiTile("editmode.save.error")
			};

	private int state;

	@Override
	public void onEnter(MainState mainState)
	{
		state = 0;
	}

	@Override
	public GuiTile guiTile()
	{
		return GUI_TILES[state];
	}

	@Override
	public void onClick(MainState mainState, XKey key)
	{
		try
		{
			var a1 = LoadHelper.startSave();
			mainState.levelMap().save(a1, mainState.worldSettings());
			String text1 = LoadHelper.endSave(a1);
			Path path = new FileChooser().showSaveDialog(null).toPath();
			Files.writeString(path, text1);
			state = 1;
			/*var a1 = getA1();
			mainState.levelMap().saveMapNC(a1, mainState.itemLoader());
			String text1 = a1.end().finish();
			Path path = new FileChooser().showSaveDialog(null).toPath();
			Files.writeString(path, text1);
			var a2 = getA1();
			mainState.levelMap().saveTeamStartNC(a2, path.getParent().getFileName().toString(), path.getFileName().toString(), mainState.itemLoader());
			String text2 = a2.end().finish();
			Files.writeString(new FileChooser().showSaveDialog(null).toPath(), text2);
			state = 1;*/
		}catch(IOException e)
		{
			state = 2;
		}
	}

	@Override
	public void onMapClick(MainState mainState, Tile tile, XKey key){}
}