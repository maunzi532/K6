package visual1;

import geom.*;
import java.util.*;
import java.util.function.*;
import logic.*;
import logic.editor.*;
import visual1.gui.*;

public class VisualLevelEditor
{
	private final XGraphics graphics;
	private final List<TileCamera> cameras;
	private final List<VisualGUI> visualSlots;

	public VisualLevelEditor(XGraphics graphics, Function<Double, TileCamera> cameraSupplier)
	{
		this.graphics = graphics;
		cameras = new ArrayList<>();
		visualSlots = new ArrayList<>();
		for(int i = 0; i < LevelEditor.SLOT_COUNT; i++)
		{
			TileCamera camera = cameraSupplier.apply((i + 0.5) / LevelEditor.SLOT_COUNT * 2);
			cameras.add(camera);
			visualSlots.add(VisualGUI.forCamera(graphics, camera));
		}
	}

	public int editorClickNum(double x, double y, LevelEditor levelEditor)
	{
		for(int i = 0; i < LevelEditor.SLOT_COUNT; i++)
		{
			if(visualSlots.get(i).inside(cameras.get(i), x, y, levelEditor.editorSlots.get(i).gui))
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
				visualSlots.get(i).locateAndDraw(cameras.get(i), editorSlot.gui, scheme);
			}
		}
	}
}