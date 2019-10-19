package logic.editor;

import geom.f1.*;
import logic.editor.xgui.*;
import logic.editor.xstate.*;
import java.util.*;
import levelMap.*;
import logic.editor.editingModes.*;
import logic.*;

public class LevelEditor
{
	public static final int SLOT_COUNT = 5;

	public MainState mainState;
	private int currentSlot;
	private List<EditingMode> modes;
	public List<EditorSlot> editorSlots;
	private EditorSlotModeGUI editorSlotModeGUI;

	public LevelEditor(MainState mainState)
	{
		this.mainState = mainState;
		currentSlot = -1;
		modes = new ArrayList<>();
		modes.add(new SaveMode());
		modes.add(BCEditMode.INSTANCE);
		modes.add(BCCreateMode.INSTANCE);
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

	public void onEditorTarget(int num, int mouseKey)
	{
		if(mouseKey == 1 && num == currentSlot)
		{
			editorSlots.get(currentSlot).onClick(mainState, mouseKey);
		}
		if(mouseKey > 0)
		{
			if(currentSlot != num)
			{
				editorSlots.get(num).onEnter(mainState);
			}
			currentSlot = num;
		}
		if(mouseKey == 3)
		{
			mainState.stateHolder.setState(editorSlotModeGUI);
		}
		else if(mouseKey > 0 && !(mainState.stateHolder.getState() instanceof EditingState))
		{
			mainState.stateHolder.setState(EditingState.INSTANCE);
		}
	}

	public void onMapClick(Tile tile, int mouseKey)
	{
		if(currentSlot >= 0)
		{
			editorSlots.get(currentSlot).onMapClick(mainState, tile, mouseKey);
		}
	}

	public void onMapDrag(Tile tile1, Tile tile2, int mouseKey)
	{
		if(currentSlot >= 0)
		{
			editorSlots.get(currentSlot).onMapDrag(mainState, tile1, tile2, mouseKey);
		}
	}

	public void setCurrentSlot(EditingMode mode)
	{
		if(currentSlot >= 0)
			editorSlots.get(currentSlot).setMode(mode);
	}
}