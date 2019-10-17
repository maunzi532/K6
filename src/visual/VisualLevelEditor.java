package visual;

import geom.*;
import geom.d1.*;
import java.util.*;
import logic.editor.*;
import visual.gui.*;

public class VisualLevelEditor
{
	private List<VisualGUI> visualSlots;

	public VisualLevelEditor(XGraphics graphics)
	{
		visualSlots = new ArrayList<>();
		for(int i = 0; i < LevelEditor.SLOT_COUNT; i++)
		{
			visualSlots.add(new VisualGUIHex(graphics, new HexCamera(graphics, (i + 0.5) / LevelEditor.SLOT_COUNT * 2, 1.75,
					graphics.xHW() / 8, graphics.yHW() / 8, 0,  0, HexMatrix.LP)));
		}
	}

	public int editorClickNum(double x, double y, LevelEditor levelEditor)
	{
		for(int i = 0; i < LevelEditor.SLOT_COUNT; i++)
		{
			if(visualSlots.get(i).inside(x, y, levelEditor.editorSlots.get(i).gui))
				return i;
		}
		return -1;
	}

	public void draw(LevelEditor levelEditor)
	{
		if(levelEditor.mainState.stateHolder.getState().editMode())
		{
			for(int i = 0; i < LevelEditor.SLOT_COUNT; i++)
			{
				EditorSlot editorSlot = levelEditor.editorSlots.get(i);
				editorSlot.gui.setTile(editorSlot.mode.guiTile(), levelEditor.getCurrentSlot() == i);
				visualSlots.get(i).draw(editorSlot.gui);
			}
		}
	}
}