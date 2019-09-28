package logic.gui.guis;

import logic.*;
import logic.gui.*;
import item.inv.Inv;
import item.view.*;
import java.util.List;
import logic.xstate.*;

public class Inv1GUI extends XGUIState implements InvGUI
{
	private static final CTile textInv = new CTile(2, 0, 2, 1);
	private static final CTile weight = new CTile(0, 0);

	protected Inv inv;
	protected InvNumView weightView;
	protected List<ItemView> itemsView;
	protected InvGUIPart invView;
	protected InvGUIPart itemView;
	protected String name;
	protected List<String> baseInfo;
	protected ItemView viewing;
	protected boolean changed;

	public Inv1GUI(Inv inv, String name, List<String> baseInfo)
	{
		this.inv = inv;
		this.name = name;
		this.baseInfo = baseInfo;
	}

	@Override
	public void onEnter(MainState mainState)
	{
		weightView = inv.viewInvWeight();
		itemsView = inv.viewItems(true);
		invView = new InvGUIPart(0, 0, 1, 1, 5, 2, 1);
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

	protected void update()
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

	protected List<String> info()
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
	public void click(int x, int y, int key, XStateHolder stateHolder){}
}