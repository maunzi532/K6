package logic.editor.xgui;

import entity.*;
import javafx.scene.input.*;
import javafx.scene.paint.*;
import logic.*;
import logic.gui.*;
import logic.xstate.*;

public class HeroEditGUI extends XGUIState
{
	private static final CTile isLocationLocked = new CTile(0, 1, 2, 1);
	private static final CTile isInventoryLocked = new CTile(2, 1, 2, 1);

	private XHero hero;
	private CElement locationLockedElement;
	private CElement inventoryLockedElement;

	public HeroEditGUI(XHero hero)
	{
		this.hero = hero;
	}

	@Override
	public boolean editMode()
	{
		return true;
	}

	@Override
	public void onEnter(MainState mainState)
	{
		locationLockedElement = new CElement(isLocationLocked, true, null, null, () -> hero.toggleStartLocked());
		elements.add(locationLockedElement);
		inventoryLockedElement = new CElement(isInventoryLocked, true, null, null, () -> hero.toggleStartInvLocked());
		elements.add(inventoryLockedElement);
		update();
	}

	@Override
	protected void updateBeforeDraw()
	{
		locationLockedElement.fillTile = new GuiTile("Lock\nLocation", null, false, hero.isStartLocked() ? Color.CYAN : null);
		inventoryLockedElement.fillTile = new GuiTile("Lock\nInventory", null, false, hero.isStartInvLocked() ? Color.CYAN : null);
	}

	@Override
	public String text()
	{
		return "Settings";
	}

	@Override
	public KeyCode keybind()
	{
		return KeyCode.S;
	}

	@Override
	public XMenu menu()
	{
		return XMenu.entityEditMenu(hero);
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