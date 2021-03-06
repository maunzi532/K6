package editor.edit;

import geom.tile.*;
import java.util.*;
import levelmap.*;
import logic.*;
import editor.editingmodes.*;
import editor.xgui.*;
import editor.xstate.*;
import xstate.*;

public final class LevelEditor
{
	public static final int SLOT_COUNT = 5;

	private int currentSlot;
	private final List<EditingMode> modes;
	public List<EditorSlot> editorSlots;
	private final EditorSlotModeGUI editorSlotModeGUI;

	public LevelEditor()
	{
		currentSlot = -1;
		modes = new ArrayList<>();
		modes.add(new SaveMode());
		modes.add(EditMode.INSTANCE);
		modes.add(CreateMode.INSTANCE);
		for(int i = 0; i < FloorTileType.values().length; i++)
		{
			modes.add(new FloorSetMode(FloorTileType.values()[i]));
		}
		editorSlots = new ArrayList<>();
		for(int i = 0; i < SLOT_COUNT; i++)
		{
			editorSlots.add(new EditorSlot(modes.get(i % modes.size())));
		}
		editorSlotModeGUI = new EditorSlotModeGUI(this);
	}

	public List<EditingMode> getModes()
	{
		return modes;
	}

	public int getCurrentSlot()
	{
		return currentSlot;
	}

	public void onEditorTarget(int num, XKey key, XStateHolder stateHolder, MainState mainState)
	{
		if(key.hasFunction("choose") && num == currentSlot)
		{
			editorSlots.get(currentSlot).onClick(mainState, key);
		}
		if(key.canClick())
		{
			if(currentSlot != num)
			{
				editorSlots.get(num).onEnter(mainState);
			}
			currentSlot = num;
		}
		if(key.hasFunction("menu"))
		{
			stateHolder.setState(editorSlotModeGUI);
		}
		else if(key.canClick() && !(stateHolder.getState() instanceof EditingState))
		{
			stateHolder.setState(EditingState.INSTANCE);
		}
	}

	public void onMapClick(Tile tile, XKey key, MainState mainState)
	{
		if(currentSlot >= 0)
		{
			editorSlots.get(currentSlot).onMapClick(mainState, tile, key);
		}
	}

	public void onMapDrag(Tile tile1, Tile tile2, XKey key, MainState mainState)
	{
		if(currentSlot >= 0)
		{
			editorSlots.get(currentSlot).onMapDrag(mainState, tile1, tile2, key);
		}
	}

	public void setCurrentSlot(EditingMode mode)
	{
		if(currentSlot >= 0)
			editorSlots.get(currentSlot).setMode(mode);
	}
}