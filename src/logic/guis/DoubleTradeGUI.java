package logic.guis;

import doubleinv.*;
import item4.*;
import logic.*;
import gui.*;

public final class DoubleTradeGUI extends XGUIState
{
	private static final CTile name1 = new CTile(0, 0, 4, 1);
	private static final CTile name2 = new CTile(5, 0, 4, 1);
	private static final CTile sendAll12 = new CTile(4, 2, new GuiTile("All", "gui.trade.arrow", false, null));
	private static final CTile sendAll21 = new CTile(4, 2, new GuiTile("All", "gui.trade.arrow", true, null));
	private static final CTile swap = new CTile(4, 2, new GuiTile("Swap", null, false, null));

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
				view -> itemView(view, sameItem(view, marked1)), target -> marked1 = (sameItem(target, marked1) ? null : target));
		elements.add(view1);
		view2 = new ScrollList<>(5, 1, 4, 5, 2, 1, null,
				view -> itemView(view, sameItem(view, marked2)), target -> marked2 = (sameItem(target, marked2) ? null : target));
		elements.add(view2);
		elements.add(new CElement(name1, new GuiTile(inv1.name(), null, false, "gui.trade.name.background")));
		elements.add(new CElement(name2, new GuiTile(inv2.name(), null, false, "gui.trade.name.background")));
		elements.add(new CElement(sendAll12, true, () -> marked1 != null && marked2 == null, () -> System.out.println("W12")));
		elements.add(new CElement(sendAll21, true, () -> marked2 != null && marked1 == null, () -> System.out.println("W21")));
		elements.add(new CElement(swap, true, () -> marked1 != null && marked2 != null, () -> System.out.println("WS")));
		update();
	}

	private boolean sameItem(NumberedStack4 a1, NumberedStack4 a2)
	{
		if(a1 == null || a2 == null)
			return false;
		return a1.num() == a2.num();
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

	private GuiTile[] itemView(NumberedStack4 view, boolean active)
	{
		String color = active ? "gui.background.active" : null;
		return new GuiTile[]
				{
						new GuiTile(view.viewText(), null, false, color),
						new GuiTile(null, view.item().image(), false, color)
				};
	}
}