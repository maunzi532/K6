package logic.guis;

import item.*;
import java.util.*;
import logic.*;
import gui.*;

public class Inv1GUI extends XGUIState
{
	private static final CTile textInv = new CTile(2, 0, 2, 1);

	protected Inv inv;
	protected List<NumberedStack> itemsView;
	protected TargetScrollList<NumberedStack> invView;
	protected ScrollList<CharSequence> itemView;
	protected CharSequence name;
	protected List<? extends CharSequence> baseInfo;

	public Inv1GUI(Inv inv, CharSequence name, List<? extends CharSequence> baseInfo)
	{
		this.inv = inv;
		this.name = name;
		this.baseInfo = baseInfo;
	}

	@Override
	public void onEnter(MainState mainState)
	{
		itemsView = inv.viewItems();
		invView = new TargetScrollList<>(0, 1, 2, 5, 2, 1, null,
				GuiTile::itemStackView, null);
		elements.add(invView);
		itemView = new ScrollList<>(3, 1, 3, 5, 1, 1, null,
				GuiTile::textView, null);
		elements.add(itemView);
		elements.add(new CElement(textInv, new GuiTile(name)));
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

	protected List<? extends CharSequence> info()
	{
		if(invView.getTargeted() != null)
			return List.of(invView.getTargeted().item().info());
		else
			return baseInfo;
	}
}