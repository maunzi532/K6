package levelMap.editor;

import draw.*;
import geom.*;
import geom.d1.*;
import geom.f1.*;
import java.util.*;
import levelMap.*;
import levelMap.editor.editingModes.*;

public class LevelEditor
{
	private static final int SLOT_COUNT = 5;

	private LevelMap levelMap;
	private int currentSlot;
	private List<EditorSlot> editorSlots;

	public LevelEditor(XGraphics graphics, LevelMap levelMap)
	{
		this.levelMap = levelMap;
		currentSlot = -1;
		editorSlots = new ArrayList<>();
		for(int i = 0; i < SLOT_COUNT; i++)
		{
			editorSlots.add(new EditorSlot(new VisualGUIHex(graphics, new HexCamera(graphics, (i + 0.5) / SLOT_COUNT * 2, 1.75,
					graphics.xHW() / 8, graphics.yHW() / 8, 0,  0, HexMatrix.LP)),
					new FloorSetMode(FloorTileType.values()[i % FloorTileType.values().length])));
		}
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

	public void onEditorClick(int num, int mouseKey)
	{
		editorSlots.get(num).onClick(mouseKey, num == currentSlot);
		currentSlot = num;
	}

	public void onMapClick(Tile tile, int mouseKey)
	{
		if(currentSlot >= 0)
			editorSlots.get(currentSlot).onMapClick(tile, levelMap, mouseKey);
	}
}