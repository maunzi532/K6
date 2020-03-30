package logic.editor.xgui;

import entity.*;
import entity.sideinfo.*;
import levelMap.*;
import logic.*;
import logic.gui.*;
import logic.xstate.*;

public class StartingSettingsEditGUI extends XGUIState
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
	public void onEnter(SideInfoFrame side, LevelMap levelMap, MainState mainState)
	{
		locationLockedElement = new CElement(isLocationLocked, true, null,
				() -> character.saveSettings().startLocked = !character.saveSettings().startLocked);
		elements.add(locationLockedElement);
		inventoryLockedElement = new CElement(isInventoryLocked, true, null,
				() -> character.saveSettings().startInvLocked = !character.saveSettings().startInvLocked);
		elements.add(inventoryLockedElement);
		update();
	}

	@Override
	protected void updateBeforeDraw()
	{
		locationLockedElement.fillTile = new GuiTile("Lock\nLocation", null, false,
				character.saveSettings().startLocked ? "gui.background.active" : null);
		inventoryLockedElement.fillTile = new GuiTile("Lock\nInventory", null, false,
				character.saveSettings().startInvLocked ? "gui.background.active" : null);
	}

	@Override
	public String text()
	{
		return "Starting Settings";
	}

	@Override
	public String keybind()
	{
		return "Starting Settings";
	}

	@Override
	public XMenu menu()
	{
		return XMenu.entityEditMenu(character);
	}

	@Override
	public boolean keepInMenu(MainState mainState, LevelMap levelMap)
	{
		return character.saveSettings() != null;
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