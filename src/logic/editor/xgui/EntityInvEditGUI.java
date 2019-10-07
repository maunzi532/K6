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
	private InvNumView weightView;
	private ScrollList<ItemView> invView;
	private ScrollList<String> infoView;
	private ScrollList<Item> allItemsView;
	private String name;
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
		weightView = inv.viewInvWeight();
		invView = new ScrollList<>(0, 1, 2, 5, 2, 1);
		invView.elements = inv.viewItems(true);
		name = entity.name();
		info = entity.getStats().infoEdit();
		infoView = new ScrollList<>(2, 1, 3, 5, 1, 1);
		allItemsView = new ScrollList<>(5, 1, 3, 5, 1, 1);
		allItemsView.elements = mainState.combatSystem.allItems();
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
	private void update()
	{
		initTiles();
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
		invView.update();
		infoView.update();
		allItemsView.update();
		invView.draw(tiles, GuiTile::itemViewView);
		infoView.draw(tiles, e -> new GuiTile[]{new GuiTile(e)});
		allItemsView.draw(tiles, e -> new GuiTile[]{new GuiTile(null, e.image(), false, null)});
		setEmptyTileAndFill(textInv, new GuiTile(name));
		setEmptyTileAndFill(weight, new GuiTile(weightView.currentWithLimit()));
	}

	@Override
	public void target(int x, int y)
	{
		var result0 = invView.target(x, y, false);
		Item r0Item = Optional.ofNullable(result0.target).map(e -> e.item).orElse(null);
		if(viewing != r0Item)
		{
			viewing = r0Item;
			update();
		}
		if(result0.inside)
		{
			targeted = result0.targetTile;
			return;
		}
		var result2 = allItemsView.target(x, y, false);
		if(viewing != result2.target)
		{
			viewing = result2.target;
			update();
		}
		if(result2.inside)
		{
			targeted = result2.targetTile;
			return;
		}
		targeted = CTile.NONE;
	}

	@Override
	public void click(int x, int y, int key, XStateHolder stateHolder)
	{
		var result0 = invView.target(x, y, true);
		var result1 = infoView.target(x, y, true);
		var result2 = allItemsView.target(x, y, true);
		if(result0.requiresUpdate || result1.requiresUpdate || result2.requiresUpdate)
			update();
		if(result0.target != null)
		{
			otherItem = false;
			editItem = result0.target.item;
			update();
		}
		if(result2.target != null)
		{
			otherItem = true;
			editItem = result2.target;
			update();
		}
		if(result1.target != null)
		{
			if(editItem != null)
			{
				if(otherItem)
				{
					if(result1.target.equals("Add"))
					{
						inv.tryAdd(new ItemList(new ItemStack(editItem, 1)), false, CommitType.COMMIT);
						update();
					}
					else if(result1.target.equals("X"))
					{
						editItem = null;
						update();
					}
				}
				else
				{
					if(result1.target.equals("+"))
					{
						inv.tryAdd(new ItemList(new ItemStack(editItem, 1)), false, CommitType.COMMIT);
						update();
					}
					else if(result1.target.equals("-"))
					{
						inv.tryGive(new ItemList(new ItemStack(editItem, 1)), false, CommitType.COMMIT);
						if(!inv.tryGive(new ItemList(new ItemStack(editItem, 1)), false, CommitType.ROLLBACK))
						{
							editItem = null;
						}
						update();
					}
					else if(result1.target.equals("X"))
					{
						editItem = null;
						update();
					}
				}
			}
		}
	}
}