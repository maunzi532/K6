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
		this.provide = provide;
		this.receive = receive;
		update();
	}

	private void update()
	{
		initTiles();
		addInventoryView(tiles, 0, 0, 0, yw(), provideScroll, provide.outputInv(), false, provide.name());
		addInventoryView(tiles, 1, 4, 0, yw(), receiveScroll, receive.inputInv(), true, receive.name());
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
		return 4;
	}

	@Override
	public boolean click(int x, int y, int key, XStateControl stateControl)
	{
		checkInventoryViewClick(x, y, 0, 0, 0, yw(), 2, provideScroll, provide.outputInv(), false);
		checkInventoryViewClick(x, y, 1, 4, 0, yw(), 2, receiveScroll, receive.inputInv(), true);
		return false;
	}

	@Override
	public void close(XStateControl stateControl)
	{
		provide.outputInv().rollback();
		receive.inputInv().rollback();
		stateControl.setState(XState.NONE);
	}

	@Override
	public void changeScroll(int invID, int scrollChange)
	{
		if(invID == 0)
			provideScroll += scrollChange;
		else if(invID == 1)
			receiveScroll += scrollChange;
		update();
	}

	@Override
	public void onClickItem(int invID, int num)
	{
		System.out.println("invID = " + invID);
		System.out.println("num = " + num);
	}
}