package logic.gui.guis;

import item.inv.*;
import item.view.*;
import java.util.*;
import logic.*;
import logic.gui.*;

public class Inv1GUI extends XGUIState
{
	private static final CTile textInv = new CTile(2, 0, 2, 1);
	private static final CTile weight = new CTile(0, 0);

	protected Inv inv;
	protected InvNumView weightView;
	protected List<ItemView> itemsView;
	protected TargetScrollList<ItemView> invView;
	protected ScrollList<String> itemView;
	protected String name;
	protected List<String> baseInfo;

	public Inv1GUI(Inv inv, String name, List<String> baseInfo)
	{
		this.inv = inv;
		this.name = name;
		this.baseInfo = baseInfo;
	}

	@Override
	public void onEnter(MainState mainState)
	{
		weightView = inv.viewInvWeight();
		itemsView = inv.viewItems(true);
		invView = new TargetScrollList<>(0, 1, 2, 5, 2, 1, null,
				GuiTile::itemViewView, null);
		elements.add(invView);
		itemView = new ScrollList<>(3, 1, 3, 5, 1, 1, null,
				GuiTile::textView, null);
		elements.add(itemView);
		elements.add(new CElement(textInv, new GuiTile(name)));
		elements.add(new CElement(weight, new GuiTile(weightView.currentWithLimit())));
		update();
	}

	@Override
	public int xw()
	{
		return 6;
	}

	@Override
	public int yw()
	{
		return 6;
	}

	@Override
	protected void updateBeforeDraw()
	{
		invView.elements = itemsView;
		itemView.elements = info();
	}

	protected List<String> info()
	{
		if(invView.getTargeted() != null)
			return invView.getTargeted().item.info();
		else
			return baseInfo;
	}
}