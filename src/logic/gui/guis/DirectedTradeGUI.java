package logic.gui.guis;

import doubleinv.*;
import entity.*;
import item.*;
import item.view.*;
import java.util.*;
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

	private final DoubleInv provide;
	private final DoubleInv receive;
	private final TurnResources takeAp;
	private ScrollList<ItemView> provideView;
	private ScrollList<ItemView> receiveView;
	private CElement transferElement;
	private Item provideMarked;
	private int amount;
	private boolean changed;

	public DirectedTradeGUI(DoubleInv provide, DoubleInv receive, TurnResources takeAp)
	{
		this.provide = provide;
		this.receive = receive;
		this.takeAp = takeAp;
	}

	@Override
	public void onEnter(MainState mainState)
	{
		mainState.side.clearSideInfo();
		amount = 1;
		provideView = new ScrollList<>(0, 1, 4, 5, 2, 1, null,
				e -> itemView(e, true), target -> provideMarked = target.item);
		elements.add(provideView);
		receiveView = new ScrollList<>(5, 1, 4, 5, 2, 1, null,
				e -> itemView(e, false), null);
		elements.add(receiveView);
		elements.add(new CElement(nameProvide, new GuiTile(provide.name(), null, false, "gui.trade.name.background")));
		elements.add(new CElement(nameReceive, new GuiTile(receive.name(), null, false, "gui.trade.name.background")));
		elements.add(new CElement(more, true, null, () -> amount++));
		transferElement = new CElement(transfer, true, null, this::clickTransfer);
		elements.add(transferElement);
		elements.add(new CElement(less, true, null, () -> amount = Math.max(0, amount - 1)));
		elements.add(new CElement(ok, true, null, () -> clickOk(mainState.stateHolder)));
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

	@Override
	protected void updateBeforeDraw()
	{
		setTargeted(CTile.NONE);
		provideView.elements = provide.outputInv().viewItems(false);
		receiveView.elements = receive.inputInv().viewItems(true);
		transferElement.fillTile = new GuiTile(String.valueOf(amount), "gui.trade.arrow", false, null);
	}

	private GuiTile[] itemView(ItemView view, boolean provideInv)
	{
		String color = provideInv && view.item == provideMarked ? "gui.background.active" : null;
		return new GuiTile[]
				{
						new GuiTile(view.currentWithLimit(), null, false, color),
						new GuiTile(null, view.item.image(), false, color)
				};
	}

	private void clickTransfer()
	{
		if(provideMarked != null)
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
	}

	private void clickOk(XStateHolder stateHolder)
	{
		if(changed)
		{
			provide.outputInv().commit();
			receive.inputInv().commit();
			provide.afterTrading();
			receive.afterTrading();
			if(takeAp != null)
			{
				takeAp.action(false, 1);
			}
		}
		stateHolder.setState(NoneState.INSTANCE);
	}

	@Override
	public void close(XStateHolder stateHolder)
	{
		provide.outputInv().rollback();
		receive.inputInv().rollback();
		super.close(stateHolder);
	}
}