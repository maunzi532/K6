package logic.xstate;

import gui.*;
import gui.guis.*;
import item.inv.transport.*;
import logic.*;

public class DirectedTradeState implements NGUIState
{
	private DoubleInv give;
	private DoubleInv take;

	public DirectedTradeState(DoubleInv give, DoubleInv take)
	{
		this.give = give;
		this.take = take;
	}

	@Override
	public String text()
	{
		return "Error";
	}

	@Override
	public XMenu menu()
	{
		return XMenu.NOMENU;
	}

	@Override
	public XGUI gui(MainState mainState)
	{
		return new DirectedTradeGUI(give, take);
	}
}