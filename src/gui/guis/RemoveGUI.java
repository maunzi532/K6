package gui.guis;

import building.Buildable;
import entity.XHero;
import gui.*;
import item.ItemList;
import item.inv.CommitType;
import item.view.*;
import java.util.List;
import logic.*;
import logic.xstate.*;

public class RemoveGUI extends XGUI implements InvGUI
{
	private static final CTile textInv = new CTile(0, 0, new GuiTile("Remove Building?"), 4, 1);
	private static final CTile weight = new CTile(4, 0, 2, 1);
	private static final CTile remove = new CTile(2, 5, new GuiTile("Remove"), 2, 1);

	private final XHero character;
	private final Buildable building;
	private final ItemList refunds;
	private final InvNumView weightView;
	private final List<ItemView> itemsView;
	private final InvGUIPart invView;

	public RemoveGUI(XHero character, Buildable building)
	{
		this.character = character;
		this.building = building;
		refunds = building.getRefundable();
		character.inputInv().tryAdd(refunds, true, CommitType.LEAVE);
		weightView = character.inputInv().viewInvWeight();
		itemsView = character.inputInv().viewItems(true);
		invView = new InvGUIPart(0, 0, 1, 3, 4, 2, 1);
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

	private void update()
	{
		initTiles();
		invView.addToGUI(itemsView.size(), this);
		setTile(textInv);
		setTile(weight, new GuiTile(weightView.baseAndCurrentWithLimit()));
		setTile(remove);
	}

	@Override
	public void itemView(int invID, int x, int y1, int index)
	{
		ItemView itemView = itemsView.get(index);
		tiles[x][y1] = new GuiTile(itemView.baseAndCurrentWithLimit());
		tiles[x + 1][y1] = new GuiTile(null, itemView.item.image(), null);
	}

	@Override
	public void target(int x, int y)
	{
		if(invView.target(x, y, itemsView.size(), this))
			return;
		if(remove.contains(x, y))
			setTargeted(remove);
		else
			setTargeted(CTile.NONE);
	}

	@Override
	public void onTarget(int invID, int num, int xi, int yi, CTile cTile)
	{
		setTargeted(cTile);
	}

	@Override
	public boolean click(int x, int y, int key, XStateControl stateControl)
	{
		if(remove.contains(x, y) && character.inputInv().ok())
		{
			character.inputInv().commit();
			building.remove();
			stateControl.setState(NoneState.INSTANCE);
			return true;
		}
		return false;
	}

	@Override
	public void close(XStateControl stateControl)
	{
		character.inputInv().rollback();
		super.close(stateControl);
	}
}