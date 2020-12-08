package logic.editor.xgui;

import gui.*;
import item.*;
import java.util.*;
import logic.*;

public class InvEditGUI extends XGUIState
{
	private static final CTile textInv = new CTile(2, 0, 2, 1);
	//private static final CTile weight = new CTile(0, 0);

	private final Inv inv;
	private final CharSequence name;
	private final List<? extends CharSequence> defaultInfo;
	private TargetScrollList<NumberedStack> invView;
	private ScrollList<CharSequence> infoView;
	private TargetScrollList<Item> allItemsView;
	//private CElement weightElement;
	private boolean otherItem;
	private NumberedStack editItem;

	public InvEditGUI(Inv inv, CharSequence name, List<? extends CharSequence> defaultInfo)
	{
		this.inv = inv;
		this.name = name;
		this.defaultInfo = defaultInfo;
	}

	@Override
	public boolean editMode()
	{
		return true;
	}

	@Override
	public void onEnter(MainState mainState)
	{
		invView = new TargetScrollList<>(0, 1, 2, 5, 2, 1, null,
				GuiTile::itemStackView, this::itemClick1);
		elements.add(invView);
		infoView = new ScrollList<>(2, 1, 3, 5, 1, 1, null,
				GuiTile::textView, this::onClickInfoView);
		elements.add(infoView);
		allItemsView = new TargetScrollList<>(5, 1, 3, 5, 1, 1,
				mainState.systemScheme().allItems, e -> GuiTile.cast(new GuiTile(e.name(), e.image(), false, null)),
				this::itemClick2);
		elements.add(allItemsView);
		elements.add(new CElement(textInv, new GuiTile(name)));
		//weightElement = new CElement(weight);
		//elements.add(weightElement);
		update();
	}

	@Override
	public int xw()
	{
		return 8;
	}

	@Override
	public int yw()
	{
		return 6;
	}

	@Override
	protected void updateBeforeDraw()
	{
		invView.elements = inv.viewItems();
		if(editItem != null)
		{
			if(otherItem)
				infoView.elements = List.of("gui.edit.inv.add", "gui.edit.inv.stop");
			else
				infoView.elements = List.of("gui.edit.inv.increase", "gui.edit.inv.decrease", "gui.edit.inv.stop");
		}
		else if(invView.getTargeted() != null && !invView.getTargeted().item().info().toString().isBlank())
		{
			infoView.elements = List.of();//invView.getTargeted().item().info();
		}
		else if(allItemsView.getTargeted() != null && !allItemsView.getTargeted().info().toString().isBlank())
		{
			infoView.elements = List.of();//allItemsView.getTargeted().info();
		}
		else
		{
			infoView.elements = defaultInfo;
		}
		//weightElement.fillTile = new GuiTile(inv.viewInvWeight().currentWithLimit());
	}

	private void itemClick1(NumberedStack target)
	{
		otherItem = false;
		editItem = target;
	}

	private void itemClick2(Item target)
	{
		otherItem = true;
		editItem = new NumberedStack(target, 1, false, false, null, -1);
	}

	private void onClickInfoView(CharSequence target)
	{
		if(editItem != null)
		{
			switch(target.toString())
			{
				case "gui.edit.inv.add", "gui.edit.inv.increase" ->
				{
					inv.tryAdd(new ItemStack(editItem.item(), 1));
					update();
				}
				case "gui.edit.inv.decrease" ->
				{
					inv.takeNum(editItem.num(), 1);
					if(editItem.count() <= 1)
						editItem = null;
					update();
				}
				case "gui.edit.inv.stop" ->
				{
					editItem = null;
					update();
				}
			}
		}
	}
}