package logic.gui;

import inv.*;
import java.util.List;
import javafx.scene.paint.Color;
import logic.*;

public class DirectedTradeGUI extends XGUI implements InvGUI
{
	private DoubleInv provide;
	private DoubleInv receive;
	private List<ItemView> provideItems;
	private List<ItemView> receiveItems;
	private InvGUIPart provideView;
	private InvGUIPart receiveView;
	private int provideMarked;
	private int amount;

	public DirectedTradeGUI(DoubleInv provide, DoubleInv receive)
	{
		this.provide = provide;
		this.receive = receive;
		provideItems = provide.outputInv().viewItems(false);
		receiveItems = receive.inputInv().viewItems(true);
		provideView = new InvGUIPart(0, 0, 0, 2, yw(), provide.name());
		receiveView = new InvGUIPart(1, 4, 0, 2, yw(), receive.name());
		provideMarked = -1;
		amount = 1;
		update();
	}

	private void update()
	{
		initTiles();
		provideView.addToGUI(tiles, provideItems.size(), this);
		receiveView.addToGUI(tiles, receiveItems.size(), this);
		tiles[2][1] = new GuiTile("More");
		tiles[3][2] = new GuiTile("Transfer " + amount);
		tiles[2][3] = new GuiTile("Less");
		tiles[3][4] = new GuiTile("OK");
	}

	@Override
	public void itemView(int invID, int x, int y1, int index)
	{
		ItemView view = (invID == 0 ? provideItems : receiveItems).get(index);
		Color color = invID == 0 && index == provideMarked ? Color.CYAN : null;
		tiles[x][y1] = new GuiTile(view.currentWithLimit(), null, color);
		tiles[x + 1][y1] = new GuiTile(null, view.item.image(), color);
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
	public boolean click(int x, int y, int key, XStateControl stateControl)
	{
		provideView.checkClick(x, y, provideItems.size(), this);
		if(provideView.updateInvViewFlag())
			provideItems = provide.outputInv().viewItems(false);
		receiveView.checkClick(x, y, receiveItems.size(), this);
		if(receiveView.updateInvViewFlag())
			receiveItems = receive.inputInv().viewItems(true);
		if(provideView.updateGUIFlag() || receiveView.updateGUIFlag())
			update();
		if(x == 2 && y == 1)
		{
			amount++;
			update();
		}
		if(amount > 1 && x == 2 && y == 3)
		{
			amount--;
			update();
		}
		if(provideMarked >= 0 && x == 3 && y == 2)
		{
			ItemStack items = new ItemStack(provideItems.get(provideMarked).item, amount);
			if(provide.outputInv().canDecrease(items) && receive.inputInv().canIncrease(items))
			{
				receive.inputInv().increase(provide.outputInv().decrease(items));
				provideItems = provide.outputInv().viewItems(false);
				receiveItems = receive.inputInv().viewItems(true);
				update();
			}
		}
		if(x == 3 && y == 4)
		{
			provide.outputInv().commit();
			receive.inputInv().commit();
			stateControl.setState(XState.NONE);
			return true;
		}
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
		if(invID == 0)
		{
			provideMarked = num;
			update();
		}
	}
}