package logic.editor.editingmodes;

import geom.tile.*;
import java.io.*;
import java.nio.file.*;
import javafx.stage.*;
import logic.*;
import logic.editor.*;
import logic.gui.*;

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
	public void onClick(MainState mainState, XKey key)
	{
		try
		{
			/*String[] texts = mainState.levelMap().saveDataJSON(mainState.itemLoader());
			Files.writeString(new FileChooser().showSaveDialog(null).toPath(), texts[0]);
			Files.writeString(new FileChooser().showSaveDialog(null).toPath(), texts[1]);*/
			String text1 = mainState.levelMap().saveMap(mainState.itemLoader());
			Path path = new FileChooser().showSaveDialog(null).toPath();
			Files.writeString(path, text1);
			String text2 = mainState.levelMap().saveTeamStart(path.toString(), mainState.itemLoader());
			Files.writeString(new FileChooser().showSaveDialog(null).toPath(), text2);
			state = 1;
		}catch(IOException e)
		{
			state = 2;
		}
	}

	@Override
	public void onMapClick(MainState mainState, Tile tile, XKey key){}
}