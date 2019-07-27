package logic.xstate;

import entity.*;
import gui.*;
import gui.guis.*;
import item.inv.transport.*;
import logic.*;

public class DirectedTradeState implements NGUIState
{
	private DoubleInv give;
	private DoubleInv take;
	private XHero takeAp;

	public DirectedTradeState(DoubleInv give, DoubleInv take, XHero takeAp)
	{
		this.give = give;
		this.take = take;
		this.takeAp = takeAp;
	}

	@Override
	public XMenu menu()
	{
		return XMenu.NOMENU;
	}

	@Override
	public XGUI gui(MainState mainState)
	{
		return new DirectedTradeGUI(give, take, takeAp);
	}
}