package gui.guis;

import gui.*;
import inv.*;
import java.util.List;
import logic.*;

public class Inv1GUI extends XGUI implements InvGUI
{
	private static final CTile textInv = new CTile(2, 0, new GuiTile("Inventory"), 2, 1);
	private static final CTile weight = new CTile(5, 0);

	private final InvWeightView weightView;
	private final List<ItemView> itemsView;
	private final InvGUIPart invView;

	public Inv1GUI(Inv inv)
	{
		weightView = inv.viewInvWeight();
		itemsView = inv.viewItems(true);
		invView = new InvGUIPart(0, 0, 1, 3, 5, 2, 1);
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
		setTile(weight, new GuiTile(weightView.currentWithLimit()));
	}

	@Override
	public void itemView(int invID, int x, int y1, int index)
	{
		ItemView itemView = itemsView.get(index);
		tiles[x][y1] = new GuiTile(itemView.currentWithLimit());
		tiles[x + 1][y1] = new GuiTile(null, itemView.item.image(), null);
	}

	@Override
	public void target(int x, int y)
	{
		if(invView.target(x, y, itemsView.size(), this))
			return;
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
		return false;
	}

	@Override
	public void close(XStateControl stateControl)
	{
		stateControl.setState(XState.NONE);
	}
}