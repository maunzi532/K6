package logic.editor.xgui;

import entity.*;
import item.*;
import item.inv.*;
import item.view.*;
import java.util.*;
import javafx.scene.input.*;
import logic.*;
import logic.gui.*;
import logic.xstate.*;

public class EntityInvEditGUI extends XGUIState
{
	private static final CTile textInv = new CTile(2, 0, 2, 1);
	private static final CTile weight = new CTile(0, 0);

	private final InvEntity entity;
	private Inv inv;
	private TargetScrollList<ItemView> invView;
	private ScrollList<String> infoView;
	private TargetScrollList<Item> allItemsView;
	private CElement weightElement;
	private List<String> info;
	private boolean otherItem;
	private Item editItem;

	public EntityInvEditGUI(InvEntity entity)
	{
		this.entity = entity;
	}

	@Override
	public boolean editMode()
	{
		return true;
	}

	@Override
	public void onEnter(MainState mainState)
	{
		inv = entity.inputInv();
		invView = new TargetScrollList<>(0, 1, 2, 5, 2, 1, null,
				GuiTile::itemViewView, this::itemClick1);
		elements.add(invView);
		info = entity.getStats().infoEdit();
		infoView = new ScrollList<>(2, 1, 3, 5, 1, 1, null,
				GuiTile::textView, this::onClickInfoView);
		elements.add(infoView);
		allItemsView = new TargetScrollList<Item>(5, 1, 3, 5, 1, 1,
				mainState.combatSystem.allItems(), e -> GuiTile.cast(new GuiTile(null, e.image(), false, null)),
				this::itemClick2);
		elements.add(allItemsView);
		elements.add(new CElement(textInv, new GuiTile(entity.name())));
		weightElement = new CElement(weight);
		elements.add(weightElement);
		update();
	}

	@Override
	public String text()
	{
		return "Edit Inv.";
	}

	@Override
	public KeyCode keybind()
	{
		return KeyCode.I;
	}

	@Override
	public XMenu menu()
	{
		return XMenu.entityEditMenu(entity);
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
		if(editItem != null)
		{
			if(otherItem)
				info = List.of("Add", "X");
			else
				info = List.of("+", "-", "X");
		}
		else if(invView.getTargeted() != null)
		{
			info = invView.getTargeted().item.info();
		}
		else if(allItemsView.getTargeted() != null)
		{
			info = allItemsView.getTargeted().info();
		}
		else
		{
			info = entity.getStats().infoEdit();
		}
		invView.elements = entity.inputInv().viewItems(true);
		infoView.elements = info;
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