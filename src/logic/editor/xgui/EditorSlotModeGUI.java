package logic.editor.xgui;

import logic.*;
import logic.editor.*;
import logic.editor.xstate.*;
import logic.gui.*;
import logic.xstate.*;

public class EditorSlotModeGUI extends XGUIState
{
	private static final CTile textInv = new CTile(2, 0, new GuiTile("Editing modes"), 2, 1);

	private LevelEditor editor;
	private ScrollList<EditingMode> modesView;

	public EditorSlotModeGUI(LevelEditor editor)
	{
		this.editor = editor;
	}

	@Override
	public boolean editMode()
	{
		return true;
	}

	@Override
	public void onEnter(MainState mainState)
	{
		mainState.sideInfoFrame.clearSideInfo();
		modesView = new ScrollList<>(0, 1, 6, 5, 1, 1);
		modesView.elements = editor.getModes();
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
		modesView.update();
		modesView.draw(tiles, mode -> new GuiTile[]{mode.guiTile()});
		setFilledTile(textInv);
	}

	@Override
	public void target(int x, int y)
	{
		var result0 = modesView.target(x, y, false);
		targeted = result0.targetTile;
	}

	@Override
	public void click(int x, int y, int key, XStateHolder stateHolder)
	{
		var result0 = modesView.target(x, y, true);
		if(result0.target != null)
		{
			editor.setCurrentSlot(result0.target);
			stateHolder.setState(EditingState.INSTANCE);
		}
		else if(result0.scrolled)
			update();
	}
}