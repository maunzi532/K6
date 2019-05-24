package gui.guis;

import entity.*;
import gui.*;
import item.*;
import item.inv.*;
import item.view.*;
import java.util.*;
import logic.*;
import logic.xstate.*;

public class EntityInvEditGUI extends XGUI implements InvGUI
{
	private static final CTile textInv = new CTile(2, 0, 2, 1);
	private static final CTile weight = new CTile(0, 0);

	private final InvEntity entity;
	private final Inv inv;
	private final InvNumView weightView;
	private List<ItemView> items;
	private final List<Item> allItems;
	private final InvGUIPart invView;
	private final InvGUIPart infoView;
	private final InvGUIPart allItemsView;
	private final String name;
	private List<String> info;
	private Item viewing;
	private boolean targetChanged;
	private boolean otherItem;
	private int editItemNum;

	public EntityInvEditGUI(InvEntity entity, List<Item> allItems)
	{
		this.entity = entity;
		inv = entity.inputInv();
		weightView = entity.inputInv().viewInvWeight();
		items = entity.inputInv().viewItems(true);
		invView = new InvGUIPart(0, 0, 1, 1, 5, 2, 1);
		name = entity.name();
		info = entity.getStats().infoEdit();
		infoView = new InvGUIPart(1, 2, 1, 3, 5, 1, 1);
		this.allItems = allItems;
		allItemsView = new InvGUIPart(2, 5, 1, 3, 5, 1, 1);
		editItemNum = -1;
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

	private void update()
	{
		initTiles();
		if(editItemNum >= 0)
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
		items = entity.inputInv().viewItems(true);
		invView.addToGUI(items.size(), this);
		infoView.addToGUI(info.size(), this);
		allItemsView.addToGUI(allItems.size(), this);
		setTile(textInv, new GuiTile(name));
		setTile(weight, new GuiTile(weightView.currentWithLimit()));
	}

	@Override
	public void itemView(int invID, int x, int y1, int index)
	{
		if(invID == 0)
		{
			ItemView itemView = items.get(index);
			tiles[x][y1] = new GuiTile(itemView.currentWithLimit());
			tiles[x + 1][y1] = new GuiTile(null, itemView.item.image(), false, null);
		}
		else if(invID == 1)
		{
			if(index < info.size())
				tiles[x][y1] = new GuiTile(info.get(index));
		}
		else
		{
			tiles[x][y1] = new GuiTile(null, allItems.get(index).image(), false, null);
		}
	}

	@Override
	public void target(int x, int y)
	{
		boolean tiv = invView.target(x, y, items.size(), this) || allItemsView.target(x, y, allItems.size(), this);;
		if(!tiv || getTargeted() == CTile.NONE)
		{
			setTargeted(CTile.NONE);
			if(viewing != null)
				targetChanged = true;
			viewing = null;
		}
		if(targetChanged)
		{
			targetChanged = false;
			update();
		}
		if(tiv)
			return;
		infoView.target(x, y, info.size(), this);
	}

	@Override
	public void onTarget(int invID, int num, int xi, int yi, CTile cTile)
	{
		setTargeted(cTile);
		if(editItemNum < 0)
		{
			if(invID == 0)
			{
				if(viewing != items.get(num).item)
				{
					targetChanged = true;
					viewing = items.get(num).item;
				}
			}
			else if(invID == 2)
			{
				if(viewing != allItems.get(num))
				{
					targetChanged = true;
					viewing = allItems.get(num);
				}
			}
		}
	}

	@Override
	public boolean click(int x, int y, int key, XStateControl stateControl)
	{
		invView.checkClick(x, y, items.size(), this);
		allItemsView.checkClick(x, y, allItems.size(), this);
		infoView.checkClick(x, y, info.size(), this);
		if(invView.updateGUIFlag() | allItemsView.updateGUIFlag() | infoView.updateGUIFlag())
			update();
		return false;
	}

	@Override
	public void onClickItem(int invID, int num, int xi, int yi)
	{
		if(invID == 0)
		{
			otherItem = false;
			editItemNum = num;
			update();
		}
		if(invID == 2)
		{
			otherItem = true;
			editItemNum = num;
			update();
		}
		if(invID == 1)
		{
			if(editItemNum >= 0)
			{
				if(otherItem)
				{
					if(num == 0)
					{
						inv.tryAdd(new ItemList(new ItemStack(allItems.get(editItemNum), 1)), false, CommitType.COMMIT);
						editItemNum = -1;
						update();
					}
					else if(num == 1)
					{
						editItemNum = -1;
						update();
					}
				}
				else
				{
					if(num == 0)
					{
						inv.tryAdd(new ItemList(new ItemStack(items.get(editItemNum).item, 1)), false, CommitType.COMMIT);
						editItemNum = -1;
						update();
					}
					else if(num == 1)
					{
						inv.tryGive(new ItemList(new ItemStack(items.get(editItemNum).item, 1)), false, CommitType.COMMIT);
						editItemNum = -1;
						update();
					}
					else if(num == 2)
					{
						editItemNum = -1;
						update();
					}
				}
			}
		}
	}

	@Override
	public void close(XStateControl stateControl, boolean setState)
	{
		stateControl.setState(EditingState.INSTANCE);
	}
}