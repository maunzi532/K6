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
	private ScrollList<ItemView> invView;
	private ScrollList<String> infoView;
	private List<String> info;
	private Item viewing;
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
		inv = this.entity.inputInv();
		InvNumView weightView = inv.viewInvWeight();
		invView = new ScrollList<>(0, 1, 2, 5, 2, 1, null,
				GuiTile::itemViewView, this::itemTarget1, this::itemClick1);
		elements.add(invView);
		info = entity.getStats().infoEdit();
		infoView = new ScrollList<>(2, 1, 3, 5, 1, 1, null,
				e -> new GuiTile[]{new GuiTile(e)}, null, this::onClickInfoView);
		elements.add(infoView);
		ScrollList<Item> allItemsView = new ScrollList<Item>(5, 1, 3, 5, 1, 1,
				mainState.combatSystem.allItems(), e -> new GuiTile[]{new GuiTile(null, e.image(), false, null)},
				this::itemTarget2, this::itemClick2);
		elements.add(allItemsView);
		elements.add(new CElement(textInv, new GuiTile("name")));
		elements.add(new CElement(weight, new GuiTile(weightView.currentWithLimit())));
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
		else if(viewing != null)
			info = viewing.info();
		else
			info = entity.getStats().infoEdit();
		invView.elements = entity.inputInv().viewItems(true);
		infoView.elements = info;
	}

	private Boolean itemTarget1(ItemView target)
	{
		Item item = Optional.ofNullable(target).map(e -> e.item).orElse(null);
		if(viewing != item)
		{
			viewing = item;
			return true;
		}
		return false;
	}

	private Boolean itemTarget2(Item target)
	{
		if(viewing != target)
		{
			viewing = target;
			return true;
		}
		return false;
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
			if(otherItem)
			{
				if(target.equals("Add"))
				{
					inv.tryAdd(new ItemList(new ItemStack(editItem, 1)), false, CommitType.COMMIT);
					update();
				}
				else if(target.equals("X"))
				{
					editItem = null;
					update();
				}
			}
			else
			{
				if(target.equals("+"))
				{
					inv.tryAdd(new ItemList(new ItemStack(editItem, 1)), false, CommitType.COMMIT);
					update();
				}
				else if(target.equals("-"))
				{
					inv.tryGive(new ItemList(new ItemStack(editItem, 1)), false, CommitType.COMMIT);
					if(!inv.tryGive(new ItemList(new ItemStack(editItem, 1)), false, CommitType.ROLLBACK))
					{
						editItem = null;
					}
					update();
				}
				else if(target.equals("X"))
				{
					editItem = null;
					update();
				}
			}
		}
	}
}