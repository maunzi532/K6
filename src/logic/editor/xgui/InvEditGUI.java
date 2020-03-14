package logic.editor.xgui;

import item.*;
import item.inv.*;
import item.view.*;
import java.util.*;
import logic.*;
import logic.gui.*;

public class InvEditGUI extends XGUIState
{
	private static final CTile textInv = new CTile(2, 0, 2, 1);
	private static final CTile weight = new CTile(0, 0);

	private final Inv inv;
	private final String name;
	private final List<String> defaultInfo;
	private TargetScrollList<ItemView> invView;
	private ScrollList<String> infoView;
	private TargetScrollList<Item> allItemsView;
	private CElement weightElement;
	private boolean otherItem;
	private Item editItem;

	public InvEditGUI(Inv inv, String name, List<String> defaultInfo)
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
				GuiTile::itemViewView, this::itemClick1);
		elements.add(invView);
		infoView = new ScrollList<>(2, 1, 3, 5, 1, 1, null,
				GuiTile::textView, this::onClickInfoView);
		elements.add(infoView);
		allItemsView = new TargetScrollList<>(5, 1, 3, 5, 1, 1,
				mainState.combatSystem.allItems(), e -> GuiTile.cast(new GuiTile(null, e.image(), false, null)),
				this::itemClick2);
		elements.add(allItemsView);
		elements.add(new CElement(textInv, new GuiTile(name)));
		weightElement = new CElement(weight);
		elements.add(weightElement);
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
		invView.elements = inv.viewItems(true);
		if(editItem != null)
		{
			if(otherItem)
				infoView.elements = List.of("Add", "X");
			else
				infoView.elements = List.of("+", "-", "X");
		}
		else if(invView.getTargeted() != null && !invView.getTargeted().item.info().isEmpty())
		{
			infoView.elements = invView.getTargeted().item.info();
		}
		else if(allItemsView.getTargeted() != null && !allItemsView.getTargeted().info().isEmpty())
		{
			infoView.elements = allItemsView.getTargeted().info();
		}
		else
		{
			infoView.elements = defaultInfo;
		}
		weightElement.fillTile = new GuiTile(inv.viewInvWeight().currentWithLimit());
	}

	private void itemClick1(ItemView target)
	{
		otherItem = false;
		editItem = target.item;
	}

	private void itemClick2(Item target)
	{
		otherItem = true;
		editItem = target;
	}

	private void onClickInfoView(String target)
	{
		if(editItem != null)
		{
			switch(target)
			{
				case "Add", "+" ->
				{
					inv.tryAdd(new ItemList(new ItemStack(editItem, 1)), false, CommitType.COMMIT);
					update();
				}
				case "-" ->
				{
					inv.tryGive(new ItemList(new ItemStack(editItem, 1)), false, CommitType.COMMIT);
					if(!inv.tryGive(new ItemList(new ItemStack(editItem, 1)), false, CommitType.ROLLBACK))
					{
						editItem = null;
					}
					update();
				}
				case "X" ->
				{
					editItem = null;
					update();
				}
			}
		}
	}
}