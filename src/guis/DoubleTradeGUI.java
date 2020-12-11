package guis;

import entity.*;
import gui.*;
import item.*;
import java.util.*;
import logic.*;
import xstate.*;

public final class DoubleTradeGUI extends GUIState
{
	private static final AreaTile name1 = new AreaTile(0, 0, 4, 1);
	private static final AreaTile name2 = new AreaTile(5, 0, 4, 1);
	private static final AreaTile
			sendAll12 = new AreaTile(4, 2, new GuiTile("gui.trade.all", "gui.trade.arrow", false, null));
	private static final AreaTile
			sendAll21 = new AreaTile(4, 2, new GuiTile("gui.trade.all", "gui.trade.arrow", true, null));
	private static final AreaTile
			sendHalf12 = new AreaTile(4, 3, new GuiTile("gui.trade.half", "gui.trade.arrow", false, null));
	private static final AreaTile
			sendHalf21 = new AreaTile(4, 3, new GuiTile("gui.trade.half", "gui.trade.arrow", true, null));
	private static final AreaTile
			sendOne12 = new AreaTile(4, 4, new GuiTile("gui.trade.one", "gui.trade.arrow", false, null));
	private static final AreaTile
			sendOne21 = new AreaTile(4, 4, new GuiTile("gui.trade.one", "gui.trade.arrow", true, null));
	private static final AreaTile swap = new AreaTile(4, 2, new GuiTile("gui.trade.swap", null, false, null));

	//Send all
	//Send one
	//Send half
	//Swap

	private final InvHolder inv1;
	private final InvHolder inv2;
	private ScrollList<NumberedStack> view1;
	private ScrollList<NumberedStack> view2;
	private NumberedStack marked1;
	private NumberedStack marked2;

	public DoubleTradeGUI(InvHolder inv1, InvHolder inv2)
	{
		this.inv1 = inv1;
		this.inv2 = inv2;
	}

	@Override
	public void onEnter(MainState mainState)
	{
		mainState.side().clearSideInfo();
		view1 = new ScrollList<>(0, 1, 4, 5, 2, 1, null,
				view -> GuiTile.itemStackView(view, sameItem(view, marked1)), target -> marked1 = (sameItem(target, marked1) ? null : target));
		elements.add(view1);
		view2 = new ScrollList<>(5, 1, 4, 5, 2, 1, null,
				view -> GuiTile.itemStackView(view, sameItem(view, marked2)), target -> marked2 = (sameItem(target, marked2) ? null : target));
		elements.add(view2);
		elements.add(new TileElement(name1, new GuiTile(inv1.name(), null, false, "gui.trade.name.background")));
		elements.add(new TileElement(name2, new GuiTile(inv2.name(), null, false, "gui.trade.name.background")));
		elements.add(new TileElement(sendAll12, true, () -> marked1 != null && marked2 == null, () -> trySend12(marked1.count())));
		elements.add(new TileElement(sendAll21, true, () -> marked2 != null && marked1 == null, () -> trySend21(marked2.count())));
		elements.add(new TileElement(sendHalf12, true, () -> marked1 != null && marked2 == null && marked1.count() >= 3,
				() -> trySend12(marked1.count() - marked1.count() / 2)));
		elements.add(new TileElement(sendHalf21, true, () -> marked2 != null && marked1 == null && marked2.count() >= 3,
				() -> trySend21(marked2.count() - marked2.count() / 2)));
		elements.add(new TileElement(sendOne12, true, () -> marked1 != null && marked2 == null && marked1.count() >= 2, () -> trySend12(1)));
		elements.add(new TileElement(sendOne21, true, () -> marked2 != null && marked1 == null && marked2.count() >= 2, () -> trySend21(1)));
		elements.add(new TileElement(swap, true, () -> marked1 != null && marked2 != null, this::swap));
		update();
	}

	private boolean sameItem(NumberedStack a1, NumberedStack a2)
	{
		if(a1 == null || a2 == null)
			return false;
		return a1.num() == a2.num();
	}

	private void trySend12(int amount)
	{
		boolean keep = amount < marked1.count();
		int num = marked1.num();
		Optional<ItemStack> canTake1 = inv1.inv().canTake(num, amount);
		if(canTake1.isPresent() && inv2.inv().canAdd(canTake1.orElseThrow().item(), canTake1.orElseThrow().count()))
		{
			ItemStack take1 = inv1.inv().take(num, amount);
			inv2.inv().add(take1.item(), take1.count());
		}
		marked1 = null;
		update();
		if(keep)
		{
			marked1 = view1.elements.get(num);
		}
	}

	private void trySend21(int amount)
	{
		boolean keep = amount < marked2.count();
		int num = marked2.num();
		Optional<ItemStack> canTake2 = inv2.inv().canTake(num, amount);
		if(canTake2.isPresent() && inv1.inv().canAdd(canTake2.orElseThrow().item(), canTake2.orElseThrow().count()))
		{
			ItemStack take2 = inv2.inv().take(num, amount);
			inv1.inv().add(take2.item(), take2.count());
		}
		marked2 = null;
		update();
		if(keep)
		{
			marked2 = view2.elements.get(num);
		}
	}

	private void swap()
	{
		ItemStack stack1 = inv1.inv().take(marked1.num(), Math.min(marked1.count(), marked1.item().stackLimit()));
		ItemStack stack2 = inv2.inv().take(marked2.num(), Math.min(marked2.count(), marked2.item().stackLimit()));
		inv1.inv().add(stack2.item(), stack2.count());
		inv2.inv().add(stack1.item(), stack1.count());
		marked1 = null;
		marked2 = null;
	}

	@Override
	public CharSequence text()
	{
		return "menu.trade";
	}

	@Override
	public String keybind()
	{
		return null;
	}

	@Override
	public XMenu menu()
	{
		return new XMenu(new CharacterInfoGUI((XCharacter) inv1), this, new EndTurnState());
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
		setTargeted(AreaTile.NONE);
		view1.elements = inv1.inv().viewItems();
		view2.elements = inv2.inv().viewItems();
	}
}