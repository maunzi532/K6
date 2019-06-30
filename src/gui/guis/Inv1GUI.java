package gui.guis;

import gui.*;
import item.inv.Inv;
import item.view.*;
import java.util.List;
import logic.xstate.*;

public class Inv1GUI extends XGUI implements InvGUI
{
	private static final CTile textInv = new CTile(2, 0, 2, 1);
	private static final CTile weight = new CTile(0, 0);

	private final InvNumView weightView;
	private final List<ItemView> itemsView;
	private final InvGUIPart invView;
	private final InvGUIPart itemView;
	private final String name;
	private final List<String> baseInfo;
	private ItemView viewing;
	private boolean changed;

	public Inv1GUI(Inv inv, String name, List<String> baseInfo)
	{
		weightView = inv.viewInvWeight();
		itemsView = inv.viewItems(true);
		invView = new InvGUIPart(0, 0, 1, 1, 5, 2, 1);
		this.name = name;
		this.baseInfo = baseInfo;
		itemView = new InvGUIPart(1, 3, 1, 3, 5, 1, 1);
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
		itemView.addToGUI(info().size(), this);
		setTile(textInv, new GuiTile(name));
		setTile(weight, new GuiTile(weightView.currentWithLimit()));
	}

	@Override
	public void itemView(int invID, int x, int y1, int index)
	{
		if(invID == 0)
		{
			ItemView itemView = itemsView.get(index);
			tiles[x][y1] = new GuiTile(itemView.currentWithLimit());
			tiles[x + 1][y1] = new GuiTile(null, itemView.item.image(), false, null);
		}
		else
		{
			if(index < info().size())
				tiles[x][y1] = new GuiTile(info().get(index));
		}
	}

	private List<String> info()
	{
		if(viewing != null)
			return viewing.item.info();
		else
			return baseInfo;
	}

	@Override
	public void target(int x, int y)
	{
		if(!invView.target(x, y, itemsView.size(), this) || getTargeted() == CTile.NONE)
		{
			setTargeted(CTile.NONE);
			if(viewing != null)
				changed = true;
			viewing = null;
		}
		if(changed)
		{
			changed = false;
			update();
		}
	}

	@Override
	public void onTarget(int invID, int num, int xi, int yi, CTile cTile)
	{
		setTargeted(cTile);
		if(invID == 0)
		{
			if(viewing != itemsView.get(num))
			{
				changed = true;
				viewing = itemsView.get(num);
			}
		}
	}

	@Override
	public boolean click(int x, int y, int key, XStateHolder stateHolder)
	{
		return false;
	}
}