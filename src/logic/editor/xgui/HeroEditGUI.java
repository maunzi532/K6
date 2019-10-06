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
		update();
	}

	private void update()
	{
		initTiles();
		setEmptyTileAndFill(isLocationLocked, new GuiTile("Lock\nLocation", null, false, hero.isStartLocked() ? Color.CYAN : null));
		setEmptyTileAndFill(isInventoryLocked, new GuiTile("Lock\nInventory", null, false, hero.isStartInvLocked() ? Color.CYAN : null));
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

	@Override
	public void target(int x, int y)
	{
		if(isLocationLocked.contains(x, y))
			setTargeted(isLocationLocked);
		else if(isInventoryLocked.contains(x, y))
			setTargeted(isInventoryLocked);
		else
			setTargeted(CTile.NONE);
	}

	@Override
	public void click(int x, int y, int key, XStateHolder stateHolder)
	{
		if(isLocationLocked.contains(x, y))
		{
			hero.toggleStartLocked();
			update();
		}
		else if(isInventoryLocked.contains(x, y))
		{
			hero.toggleStartInvLocked();
			update();
		}
	}
}