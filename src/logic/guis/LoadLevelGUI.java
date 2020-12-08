package logic.guis;

import gui.*;
import java.io.*;
import java.util.*;
import java.util.stream.*;
import logic.*;
import logic.xstate.*;
import text.*;

public class LoadLevelGUI extends XGUIState
{
	private static final CTile savesText = new CTile(2, 0, new GuiTile("_Saves"), 2, 1);
	private static final CTile worldsText = new CTile(4, 0, new GuiTile("_Worlds"), 2, 1);

	private ScrollList<File> saves;
	private ScrollList<File> worlds;

	@Override
	public void onEnter(MainState mainState)
	{
		saves = new ScrollList<>(2, 1, 2, 5, 2, 1,
				filesInDirectory("saves").filter(e -> !e.isDirectory()).collect(Collectors.toList()), LoadLevelGUI::fileView, e -> loadSave(e, mainState));
		elements.add(saves);
		worlds = new ScrollList<>(4, 1, 2, 5, 2, 1,
				filesInDirectory("worlds").filter(File::isDirectory).collect(Collectors.toList()), LoadLevelGUI::fileView, e -> loadWorld(e , mainState));
		elements.add(worlds);
		elements.add(new CElement(savesText));
		elements.add(new CElement(worldsText));
		update();
	}

	public void loadSave(File file, MainState mainState)
	{
		mainState.worldControl().loadFile(file.getPath());
		mainState.stateHolder().setState(NoneState.INSTANCE);
	}

	public void loadWorld(File file, MainState mainState)
	{
		mainState.worldControl().loadFile(Arrays.stream(Objects.requireNonNull(file.listFiles()))
				.filter(e -> e.getName().equals("World")).findFirst().orElseThrow().getPath());
		mainState.stateHolder().updateLevel(mainState.worldControl().createLevel());
		mainState.stateHolder().setState(NoneState.INSTANCE);
	}

	@Override
	public int xw()
	{
		return 6;
	}

	@Override
	public int yw()
	{
		return 6;
	}

	private static Stream<File> filesInDirectory(String name)
	{
		File directory = new File(name);
		if(directory.exists() && directory.isDirectory())
		{
			return Arrays.stream(Objects.requireNonNull(directory.listFiles()));
		}
		else
		{
			return Stream.of();
		}
	}

	private static GuiTile[] fileView(File file)
	{
		return new GuiTile[]
				{
						new GuiTile(new NameText(file.getName())),
						new GuiTile("_")
				};
	}
}