package gui.guis;

import gui.*;
import java.util.*;
import levelMap.editor.*;
import logic.xstate.*;

public class EditingModeGUI extends XGUI implements InvGUI
{
	private static final CTile textInv = new CTile(2, 0, new GuiTile("Editing modes"), 2, 1);

	private final LevelEditor editor;
	private final List<EditingMode> modes;
	private final InvGUIPart modesView;
	private EditingMode chosen;

	public EditingModeGUI(LevelEditor editor)
	{
		this.editor = editor;
		modes = editor.getModes();
		modesView = new InvGUIPart(0, 0, 1, 6, 5, 1, 1);
		update();
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

	private void update()
	{
		initTiles();
		modesView.addToGUI(modes.size(), this);
		setTile(textInv);
	}

	@Override
	public void itemView(int invID, int x, int y1, int index)
	{
		tiles[x][y1] = modes.get(index).guiTile();
	}

	@Override
	public void target(int x, int y)
	{
		if(modesView.target(x, y, modes.size(), this))
			return;
		setTargeted(CTile.NONE);
	}

	@Override
	public void onTarget(int invID, int num, int xi, int yi, CTile cTile)
	{
		setTargeted(cTile);
	}

	@Override
	public void click(int x, int y, int key, XStateHolder stateHolder)
	{
		modesView.checkClick(x, y, modes.size(), this);
		if(chosen != null)
		{
			editor.setCurrentSlot(chosen);
			stateHolder.setState(EditingState.INSTANCE);
		}
		else if(modesView.updateGUIFlag())
		{
			update();
		}
	}

	@Override
	public void onClickItem(int invID, int num, int xi, int yi)
	{
		chosen = modes.get(num);
	}

	@Override
	public void close(XStateHolder stateHolder, boolean setState)
	{
		if(setState)
			stateHolder.setState(EditingState.INSTANCE);
	}
}