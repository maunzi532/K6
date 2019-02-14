package logic.gui;

import inv.DoubleInv;
import logic.*;

public class DirectedTradeGUI extends XGUI implements InventoryView
{
	private DoubleInv provide;
	private DoubleInv receive;
	private int provideScroll;
	private int receiveScroll;

	public DirectedTradeGUI(DoubleInv provide, DoubleInv receive)
	{
		super();
		this.provide = provide;
		this.receive = receive;
		update();
	}

	private void update()
	{
		addInventoryView(tiles, 0, 0, yw(), provideScroll, provide.outputInv(), false, provide.name());
		addInventoryView(tiles, 4, 0, yw(), receiveScroll, receive.inputInv(), true, receive.name());
		tiles[2][1] = new GuiTile("More");
		tiles[3][2] = new GuiTile("Arrow");
		tiles[2][3] = new GuiTile("Less");
	}

	@Override
	public int xw()
	{
		return 6;
	}

	@Override
	public int yw()
	{
		return 5;
	}

	@Override
	public void click(int x, int y, int key, XStateControl stateControl)
	{

	}

	@Override
	public void close(XStateControl stateControl)
	{
		provide.outputInv().rollback();
		receive.inputInv().rollback();
		stateControl.setState(XState.NONE);
	}
}