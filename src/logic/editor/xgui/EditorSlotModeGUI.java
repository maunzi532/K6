package logic.editor.xgui;

import logic.*;
import logic.editor.*;
import logic.editor.xstate.*;
import logic.gui.*;

public class EditorSlotModeGUI extends XGUIState
{
	private static final CTile textInv = new CTile(2, 0, new GuiTile("Editing modes"), 2, 1);

	private final LevelEditor editor;

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
		elements.add(new ScrollList<>(0, 1, 6, 5, 1, 1, editor.getModes(),
				mode -> GuiTile.cast(mode.guiTile()), target -> click(target, mainState)));
		elements.add(new CElement(textInv));
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

	private void click(EditingMode target, MainState mainState)
	{
		editor.setCurrentSlot(target);
		mainState.stateHolder.setState(EditingState.INSTANCE);
	}
}