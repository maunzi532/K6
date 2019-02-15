package gui.guis;

import entity.hero.XHero;
import gui.XGUI;
import logic.*;

public class BuildGUI extends XGUI
{
	private XHero character;

	public BuildGUI(XHero character)
	{
		this.character = character;
		initTiles();
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

	@Override
	public boolean click(int x, int y, int key, XStateControl stateControl)
	{
		return false;
	}

	@Override
	public void close(XStateControl stateControl)
	{
		stateControl.setState(XState.NONE);
	}
}