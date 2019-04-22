package levelMap;

import draw.*;
import geom.*;
import geom.f1.*;
import gui.guis.*;
import java.util.*;

public class LevelEditor
{
	private LevelMap levelMap;
	private int currentSector;
	private List<FloorTileType> slots;
	private int currentSlot;
	private List<VisualGUI> visualGUIs;
	private List<EditingGUI> editingGUIs;

	public LevelEditor(XGraphics graphics, LevelMap levelMap)
	{
		this.levelMap = levelMap;
		slots = new ArrayList<>();
		visualGUIs = new ArrayList<>();
		visualGUIs.add(new VisualGUIHex(graphics));
		editingGUIs = new ArrayList<>();
		editingGUIs.add(new EditingGUI());
	}

	public int editorClickNum(double x, double y)
	{
		for(int i = 0; i < visualGUIs.size(); i++)
		{
			if(visualGUIs.get(i).inside(x, y, editingGUIs.get(i)))
				return i;
		}
		return -1;
	}

	public void draw()
	{
		for(int i = 0; i < visualGUIs.size(); i++)
		{
			visualGUIs.get(i).draw(editingGUIs.get(i));
		}
	}

	public void onEditorClick(int num, int mouseKey)
	{
		System.out.println(num);
	}

	public void onMapClick(Tile tile, int mouseKey)
	{
		System.out.println("W");
	}
}