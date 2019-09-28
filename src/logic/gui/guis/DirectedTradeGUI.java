package logic.gui.guis;

import building.transport.*;
import entity.*;
import item.*;
import item.view.*;
import java.util.*;
import javafx.scene.paint.*;
import logic.*;
import logic.gui.*;
import logic.xstate.*;

public class DirectedTradeGUI extends XGUIState
{
	private static final CTile nameProvide = new CTile(0, 0, 4, 1);
	private static final CTile nameReceive = new CTile(5, 0, 4, 1);
	private static final CTile more = new CTile(4, 1, new GuiTile("More"));
	private static final CTile less = new CTile(4, 3, new GuiTile("Less"));
	private static final CTile transfer = new CTile(4, 2);
	private static final CTile ok = new CTile(4, 4, new GuiTile("OK"));

	private DoubleInv provide;
	private DoubleInv receive;
	private XHero takeAp;
	private ScrollList<ItemView> provideView;
	private ScrollList<ItemView> receiveView;
	private Item provideMarked;
	private int amount;
	private boolean changed;

	public DirectedTradeGUI(DoubleInv provide, DoubleInv receive, XHero takeAp)
	{
		this.provide = provide;
		this.receive = receive;
		this.takeAp = takeAp;
	}

	@Override
	public void onEnter(MainState mainState)
	{
		mainState.sideInfoFrame.clearSideInfo();
		provideView = new ScrollList<>(0, 1, 4, 5, 2, 1);
		receiveView = new ScrollList<>(5, 1, 4, 5, 2, 1);
		provideView.elements = provide.outputInv().viewItems(false);
		receiveView.elements = receive.inputInv().viewItems(true);
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
		provideView.update();
		receiveView.update();
		provideView.draw(tiles, e -> itemView(e, true));
		receiveView.draw(tiles, e -> itemView(e, false));
		setEmptyTileAndFill(nameProvide, new GuiTile(provide.name(), null, false, Color.BLUE));
		setEmptyTileAndFill(nameReceive, new GuiTile(receive.name(), null, false, Color.BLUE));
		setFilledTile(more);
		setEmptyTileAndFill(transfer, new GuiTile(String.valueOf(amount), ARROW, false, null));
		setFilledTile(less);
		setFilledTile(ok);
	}

	private GuiTile[] itemView(ItemView view, boolean provideInv)
	{
		Color color = provideInv && view.item == provideMarked ? Color.CYAN : null;
		return new GuiTile[]
				{
						new GuiTile(view.currentWithLimit(), null, false, color),
						new GuiTile(null, view.item.image(), false, color)
				};
	}

	@Override
	public void target(int x, int y)
	{
		var result0 = provideView.target(x, y, false);
		if(result0.inside)
		{
			targeted = result0.targetTile;
			return;
		}
		var result1 = receiveView.target(x, y, false);
		if(result1.inside)
		{
			targeted = result1.targetTile;
			return;
		}
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
	public void click(int x, int y, int key, XStateHolder stateHolder)
	{
		var result0 = provideView.target(x, y, true);
		var result1 = receiveView.target(x, y, true);
		if(result0.target != null)
		{
			provideMarked = result0.target.item;
			update();
		}
		if(result0.scrolled || result1.scrolled)
			update();
		if(!result0.inside && !result1.inside)
		{
			if(more.contains(x, y))
			{
				amount++;
				update();
			}
			else if(amount > 1 && less.contains(x, y))
			{
				amount--;
				update();
			}
			else if(provideMarked != null && transfer.contains(x, y))
			{
				List<ItemView> provideItems = provideView.elements;
				ItemStack items = new ItemStack(provideItems.stream().filter(e -> e.item == provideMarked).findFirst().orElseThrow().item, amount);
				if(provide.outputInv().canGive(items, false) && receive.inputInv().canAdd(items, false))
				{
					provide.outputInv().give(items, false);
					receive.inputInv().add(items, false);
					provideView.elements = provide.outputInv().viewItems(false);
					receiveView.elements = receive.inputInv().viewItems(true);
					provideMarked = provideView.elements.stream().filter(e -> e.item.equals(items.item)).findFirst().map(e -> e.item).orElse(null);
					changed = true;
					update();
				}
			}
			else if(ok.contains(x, y))
			{
				if(changed)
				{
					provide.outputInv().commit();
					receive.inputInv().commit();
					provide.afterTrading();
					receive.afterTrading();
					if(takeAp != null)
					{
						takeAp.takeAp(1);
						takeAp.irreversible();
					}
				}
				stateHolder.setState(NoneState.INSTANCE);
			}
		}
	}

	@Override
	public void close(XStateHolder stateHolder, boolean setState)
	{
		provide.outputInv().rollback();
		receive.inputInv().rollback();
		super.close(stateHolder, setState);
	}
}