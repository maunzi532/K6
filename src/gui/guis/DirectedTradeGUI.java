package gui.guis;

import gui.*;
import inv.*;
import java.util.List;
import javafx.scene.paint.Color;
import logic.*;

public class DirectedTradeGUI extends XGUI implements InvGUI
{
	private static final CTile nameProvide = new CTile(0, 0, 4, 1);
	private static final CTile nameReceive = new CTile(5, 0, 4, 1);
	private static final CTile more = new CTile(4, 1, new GuiTile("More"));
	private static final CTile less = new CTile(4, 3, new GuiTile("Less"));
	private static final CTile transfer = new CTile(4, 2);
	private static final CTile ok = new CTile(4, 4, new GuiTile("OK"));

	private final DoubleInv provide;
	private final DoubleInv receive;
	private List<ItemView> provideItems;
	private List<ItemView> receiveItems;
	private final InvGUIPart provideView;
	private final InvGUIPart receiveView;
	private int provideMarked;
	private int amount;

	public DirectedTradeGUI(DoubleInv provide, DoubleInv receive)
	{
		this.provide = provide;
		this.receive = receive;
		provideItems = provide.outputInv().viewItems(false);
		receiveItems = receive.inputInv().viewItems(true);
		provideView = new InvGUIPart(0, 0, 1, 1, 3, 2, 1);
		receiveView = new InvGUIPart(1, 5, 1, 2, 5, 2, 1);
		provideMarked = -1;
		amount = 1;
		update();
	}

	@Override
	public int xw()
	{
		return 9;
	}

	@Override
	public int yw()
	{
		return 6;
	}

	private void update()
	{
		setTargeted(CTile.NONE);
		initTiles();
		provideView.addToGUI(provideItems.size(), this);
		receiveView.addToGUI(receiveItems.size(), this);
		setTile(nameProvide, new GuiTile(provide.name(), null, Color.BLUE));
		setTile(nameReceive, new GuiTile(receive.name(), null, Color.BLUE));
		setTile(more);
		setTile(transfer, new GuiTile("Transfer " + amount));
		setTile(less);
		setTile(ok);
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
	public void target(int x, int y)
	{
		if(provideView.target(x, y, provideItems.size(), this))
			return;
		if(receiveView.target(x, y, receiveItems.size(), this))
			return;
		if(more.contains(x, y))
			setTargeted(more);
		else if(less.contains(x, y))
			setTargeted(less);
		else if(transfer.contains(x, y))
			setTargeted(transfer);
		else if(ok.contains(x, y))
			setTargeted(ok);
		else
			setTargeted(CTile.NONE);
	}

	@Override
	public void onTarget(int invID, int num, int xi, int yi, CTile cTile)
	{
		setTargeted(cTile);
	}

	@Override
	public boolean click(int x, int y, int key, XStateControl stateControl)
	{
		provideView.checkClick(x, y, provideItems.size(), this);
		receiveView.checkClick(x, y, receiveItems.size(), this);
		if(provideView.updateGUIFlag() | receiveView.updateGUIFlag())
			update();
		else if(more.contains(x, y))
		{
			amount++;
			update();
		}
		else if(amount > 1 && less.contains(x, y))
		{
			amount--;
			update();
		}
		else if(provideMarked >= 0 && transfer.contains(x, y))
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
		else if(ok.contains(x, y))
		{
			provide.outputInv().commit();
			receive.inputInv().commit();
			stateControl.setState(XState.NONE);
			return true;
		}
		return false;
	}

	@Override
	public void onClickItem(int invID, int num, int xi, int yi)
	{
		if(invID == 0)
		{
			provideMarked = num;
			update();
		}
	}

	@Override
	public void close(XStateControl stateControl)
	{
		provide.outputInv().rollback();
		receive.inputInv().rollback();
		stateControl.setState(XState.NONE);
	}
}