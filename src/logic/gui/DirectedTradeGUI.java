package logic.gui;

import inv.*;
import logic.*;

public class DirectedTradeGUI extends XGUI implements InvGUI
{
	private DoubleInv provide;
	private DoubleInv receive;
	private InvView provideView;
	private InvView receiveView;

	public DirectedTradeGUI(DoubleInv provide, DoubleInv receive)
	{
		this.provide = provide;
		this.receive = receive;
		provideView = new InvView(0, 0, 0, 2, yw(), provide.outputInv().viewItems(false), provide.name());
		receiveView = new InvView(1, 4, 0, 2, yw(), receive.inputInv().viewItems(true), receive.name());
		update();
	}

	private void update()
	{
		initTiles();
		provideView.addToGUI(tiles, this);
		receiveView.addToGUI(tiles, this);
		tiles[2][1] = new GuiTile("More");
		tiles[3][2] = new GuiTile("Arrow");
		tiles[2][3] = new GuiTile("Less");
	}

	@Override
	public void itemView(int x, int y1, ItemView view)
	{
		tiles[x][y1] = new GuiTile(view.currentWithLimit());
		tiles[x + 1][y1] = new GuiTile(null, view.item.image(), null);
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
		provideView.checkClick(x, y, this);
		if(provideView.updateInvViewFlag())
			provideView.setInvView(provide.outputInv().viewItems(false));
		receiveView.checkClick(x, y, this);
		if(receiveView.updateInvViewFlag())
			receiveView.setInvView(receive.inputInv().viewItems(true));
		if(provideView.updateGUIFlag() || receiveView.updateGUIFlag())
			update();
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
	public void onClickItem(int invID, int num)
	{
		System.out.println("invID = " + invID);
		System.out.println("num = " + num);
	}
}