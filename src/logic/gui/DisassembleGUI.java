package logic.gui;

import entity.hero.XHero;
import logic.*;

public class DisassembleGUI extends XGUI
{
	private XHero character;

	public DisassembleGUI(XHero character)
	{
		this.character = character;
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
	public void click(int x, int y, int key, XStateControl stateControl)
	{

	}

	@Override
	public void close(XStateControl stateControl)
	{
		stateControl.setState(XState.NONE);
	}
}