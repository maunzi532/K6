package levelMap.editor;

import draw.*;
import geom.*;
import geom.d1.*;
import geom.f1.*;
import java.util.*;
import levelMap.*;
import levelMap.editor.editingModes.*;
import logic.*;
import logic.xstate.*;

public class LevelEditor
{
	private static final int SLOT_COUNT = 5;

	private MainState mainState;
	private int currentSlot;
	private List<EditingMode> modes;
	private List<EditorSlot> editorSlots;
	private EditingModeState editingModeState;

	public LevelEditor(XGraphics graphics, MainState mainState)
	{
		this.mainState = mainState;
		currentSlot = -1;
		modes = new ArrayList<>();
		modes.add(BCEditMode.INSTANCE);
		for(int i = 0; i < FloorTileType.values().length; i++)
		{
			modes.add(new FloorSetMode(FloorTileType.values()[i]));
		}
		editorSlots = new ArrayList<>();
		for(int i = 0; i < SLOT_COUNT; i++)
		{
			editorSlots.add(new EditorSlot(new VisualGUIHex(graphics, new HexCamera(graphics, (i + 0.5) / SLOT_COUNT * 2, 1.75,
					graphics.xHW() / 8, graphics.yHW() / 8, 0,  0, HexMatrix.LP)), modes.get(i % modes.size())));
		}
		editingModeState = new EditingModeState(this);
	}

	public List<EditingMode> getModes()
	{
		return modes;
	}

	public int editorClickNum(double x, double y)
	{
		for(int i = 0; i < SLOT_COUNT; i++)
		{
			if(editorSlots.get(i).isClicked(x, y))
				return i;
		}
		return -1;
	}

	public void draw()
	{
		editorSlots.forEach(EditorSlot::draw);
	}

	public boolean onEditorClick(int num, int mouseKey)
	{
		if(mouseKey == 1 && num == currentSlot)
			editorSlots.get(currentSlot).onClick(mainState, mouseKey);
		currentSlot = num;
		if(mouseKey == 3)
		{
			mainState.stateControl.setState(editingModeState);
			return true;
		}
		return false;
	}

	public boolean onMapClick(Tile tile, int mouseKey)
	{
		if(currentSlot >= 0)
		{
			return editorSlots.get(currentSlot).onMapClick(mainState, tile, mouseKey);
		}
		return false;
	}

	public void setCurrentSlot(EditingMode mode)
	{
		if(currentSlot >= 0)
			editorSlots.get(currentSlot).setMode(mode);
	}
}