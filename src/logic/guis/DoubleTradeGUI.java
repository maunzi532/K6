package logic.guis;

import doubleinv.*;
import entity.*;
import gui.*;
import item4.*;
import logic.*;
import logic.xstate.*;

public final class DoubleTradeGUI extends XGUIState
{
	private static final CTile name1 = new CTile(0, 0, 4, 1);
	private static final CTile name2 = new CTile(5, 0, 4, 1);
	private static final CTile sendAll12 = new CTile(4, 2, new GuiTile("gui.trade.all", "gui.trade.arrow", false, null));
	private static final CTile sendAll21 = new CTile(4, 2, new GuiTile("gui.trade.all", "gui.trade.arrow", true, null));
	private static final CTile sendHalf12 = new CTile(4, 3, new GuiTile("gui.trade.half", "gui.trade.arrow", false, null));
	private static final CTile sendHalf21 = new CTile(4, 3, new GuiTile("gui.trade.half", "gui.trade.arrow", true, null));
	private static final CTile sendOne12 = new CTile(4, 4, new GuiTile("gui.trade.one", "gui.trade.arrow", false, null));
	private static final CTile sendOne21 = new CTile(4, 4, new GuiTile("gui.trade.one", "gui.trade.arrow", true, null));
	private static final CTile swap = new CTile(4, 2, new GuiTile("gui.trade.swap", null, false, null));

	//Send all
	//Send one
	//Send half
	//Swap

	private final InvHolder inv1;
	private final InvHolder inv2;
	private ScrollList<NumberedStack4> view1;
	private ScrollList<NumberedStack4> view2;
	private NumberedStack4 marked1;
	private NumberedStack4 marked2;

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
		elements.add(new CElement(name1, new GuiTile(inv1.name(), null, false, "gui.trade.name.background")));
		elements.add(new CElement(name2, new GuiTile(inv2.name(), null, false, "gui.trade.name.background")));
		elements.add(new CElement(sendAll12, true, () -> marked1 != null && marked2 == null, () -> trySend12(marked1.count())));
		elements.add(new CElement(sendAll21, true, () -> marked2 != null && marked1 == null, () -> trySend21(marked2.count())));
		elements.add(new CElement(sendHalf12, true, () -> marked1 != null && marked2 == null && marked1.count() >= 3,
				() -> trySend12(marked1.count() - marked1.count() / 2)));
		elements.add(new CElement(sendHalf21, true, () -> marked2 != null && marked1 == null && marked2.count() >= 3,
				() -> trySend21(marked2.count() - marked2.count() / 2)));
		elements.add(new CElement(sendOne12, true, () -> marked1 != null && marked2 == null && marked1.count() >= 2, () -> trySend12(1)));
		elements.add(new CElement(sendOne21, true, () -> marked2 != null && marked1 == null && marked2.count() >= 2, () -> trySend21(1)));
		elements.add(new CElement(swap, true, () -> marked1 != null && marked2 != null, this::swap));
		update();
	}

	private boolean sameItem(NumberedStack4 a1, NumberedStack4 a2)
	{
		if(a1 == null || a2 == null)
			return false;
		return a1.num() == a2.num();
	}

	private void trySend12(int amount)
	{
		boolean keep = amount < marked1.count();
		int num = marked1.num();
		if(inv2.inv().canAddAll(inv1.inv().takeableNum(num, amount)))
		{
			inv2.inv().tryAdd(inv1.inv().takeNum(num, amount));
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
		if(inv1.inv().canAddAll(inv2.inv().takeableNum(num, amount)))
		{
			inv1.inv().tryAdd(inv2.inv().takeNum(num, amount));
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
		ItemStack4 stack1 = inv1.inv().takeNum(marked1.num(), marked1.count());
		ItemStack4 stack2 = inv2.inv().takeNum(marked2.num(), marked2.count());
		inv1.inv().tryAdd(stack2);
		inv2.inv().tryAdd(stack1);
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
		return new XMenu(new TagInvGUI((XCharacter) inv1), this, new EndTurnState());
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
		view1.elements = inv1.inv().viewItems();
		view2.elements = inv2.inv().viewItems();
	}

	/*private GuiTile[] itemView(NumberedStack4 view, boolean active)
	{
		String color = active ? "gui.background.active" : null;
		return new GuiTile[]
				{
						new GuiTile(view.viewText(), null, false, color),
						new GuiTile(null, view.item().image(), false, color)
				};
	}*/
}