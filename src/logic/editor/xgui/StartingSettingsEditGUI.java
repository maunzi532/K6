package logic.editor.xgui;

import entity.*;
import logic.*;
import gui.*;
import logic.xstate.*;

public final class StartingSettingsEditGUI extends XGUIState
{
	private static final CTile isLocationLocked = new CTile(0, 1, 2, 1);
	private static final CTile isInventoryLocked = new CTile(2, 1, 2, 1);

	private final XCharacter character;
	private CElement locationLockedElement;
	private CElement inventoryLockedElement;

	public StartingSettingsEditGUI(XCharacter character)
	{
		this.character = character;
	}

	@Override
	public boolean editMode()
	{
		return true;
	}

	@Override
	public void onEnter(MainState mainState)
	{
		/*locationLockedElement = new CElement(isLocationLocked, true, null,
				() -> character.saveSettings().startLocked = !character.saveSettings().startLocked);
		elements.add(locationLockedElement);
		inventoryLockedElement = new CElement(isInventoryLocked, true, null,
				() -> character.saveSettings().startInvLocked = !character.saveSettings().startInvLocked);
		elements.add(inventoryLockedElement);*/
		update();
	}

	@Override
	protected void updateBeforeDraw()
	{
		/*locationLockedElement.fillTile = new GuiTile("gui.edit.start.locklocation", null, false,
				character.saveSettings().startLocked ? "gui.background.active" : null);
		inventoryLockedElement.fillTile = new GuiTile("gui.edit.start.lockinventory", null, false,
				character.saveSettings().startInvLocked ? "gui.background.active" : null);*/
	}

	@Override
	public CharSequence text()
	{
		return "menu.edit.start";
	}

	@Override
	public String keybind()
	{
		return "state.edit.start";
	}

	@Override
	public XMenu menu()
	{
		return XMenu.entityEditMenu(character);
	}

	@Override
	public boolean keepInMenu(MainState mainState)
	{
		return false;//character.saveSettings() != null;
	}

	@Override
	public int xw()
	{
		return 6;
	}

	@Override
	public int yw()
	{
		return 4;
	}
}