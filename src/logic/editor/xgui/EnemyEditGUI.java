package logic.editor.xgui;

import entity.*;
import logic.*;
import logic.gui.*;
import logic.xstate.*;

public class EnemyEditGUI extends XGUIState
{
	private final XEnemy enemy;

	public EnemyEditGUI(XEnemy enemy)
	{
		this.enemy = enemy;
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

	@Override
	public String text()
	{
		return "Settings";
	}

	@Override
	public String keybind()
	{
		return "Enemy Settings";
	}

	@Override
	public XMenu menu()
	{
		return XMenu.entityEditMenu(enemy);
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