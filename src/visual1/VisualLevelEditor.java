package visual1;

import geom.*;
import geom.d1.*;
import java.util.*;
import logic.*;
import logic.editor.*;
import visual1.gui.*;

public class VisualLevelEditor
{
	private static final double EDITOR_SCALE_X = 0.125;
	private static final double EDITOR_SCALE_Y = 0.1;

	private List<VisualGUI> visualSlots;
	private XGraphics graphics;

	public VisualLevelEditor(XGraphics graphics)
	{
		this.graphics = graphics;
		visualSlots = new ArrayList<>();
		for(int i = 0; i < LevelEditor.SLOT_COUNT; i++)
		{
			visualSlots.add(new VisualGUIHex(graphics, new HexCamera(graphics, (i + 0.5) / LevelEditor.SLOT_COUNT * 2, 1.75,
					EDITOR_SCALE_X, EDITOR_SCALE_Y, 0,  0, new HexMatrix(0.5))));
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

	public double takeY(MainState mainState)
	{
		return mainState.stateHolder.getState().editMode() ? graphics.scaleHW() * 0.35 : 0;
	}

	public void draw(LevelEditor levelEditor, Scheme scheme)
	{
		if(levelEditor.mainState.stateHolder.getState().editMode())
		{
			for(int i = 0; i < LevelEditor.SLOT_COUNT; i++)
			{
				EditorSlot editorSlot = levelEditor.editorSlots.get(i);
				editorSlot.gui.setTile(editorSlot.mode.guiTile(), levelEditor.getCurrentSlot() == i);
				visualSlots.get(i).locateAndDraw(editorSlot.gui, scheme);
			}
		}
	}
}