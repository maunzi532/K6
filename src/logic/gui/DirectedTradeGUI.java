package logic.gui;

import inv.*;

public class DirectedTradeGUI extends XGUI
{
	private DoubleInv provide;
	private DoubleInv receive;

	public DirectedTradeGUI(DoubleInv provide, DoubleInv receive)
	{
		this.provide = provide;
		this.receive = receive;
		update();
	}

	public void update()
	{

	}

	@Override
	public int xw()
	{
		return 7;
	}

	@Override
	public int yw()
	{
		return 5;
	}

	@Override
	public void click(int x, int y)
	{

	}
}