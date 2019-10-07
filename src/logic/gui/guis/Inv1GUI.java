package logic.gui.guis;

import java.util.*;
import logic.*;
import logic.gui.*;
import item.inv.Inv;
import item.view.*;
import logic.xstate.*;

public class Inv1GUI extends XGUIState
{
	private static final CTile textInv = new CTile(2, 0, 2, 1);
	private static final CTile weight = new CTile(0, 0);

	protected Inv inv;
	protected InvNumView weightView;
	protected List<ItemView> itemsView;
	protected ScrollList<ItemView> invView;
	protected ScrollList<String> itemView;
	protected String name;
	protected List<String> baseInfo;
	protected ItemView viewing;

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
		invView = new ScrollList<>(0, 1, 2, 5, 2, 1);
		itemView = new ScrollList<>(3, 1, 3, 5, 1, 1);
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

	@Override
	protected void update()
	{
		initTiles();
		invView.elements = itemsView;
		itemView.elements = info();
		invView.update();
		itemView.update();
		invView.draw(tiles, GuiTile::itemViewView);
		itemView.draw(tiles, info -> new GuiTile[]{new GuiTile(info)});
		setEmptyTileAndFill(textInv, new GuiTile(name));
		setEmptyTileAndFill(weight, new GuiTile(weightView.currentWithLimit()));
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
		var result0 = invView.target(x, y, false);
		targeted = result0.targetTile;
		if(viewing != result0.target)
		{
			viewing = result0.target;
			update();
		}
	}

	@Override
	public void click(int x, int y, int key, XStateHolder stateHolder)
	{
		var result0 = invView.target(x, y, true);
		var result1 = itemView.target(x, y, true);
		if(result0.requiresUpdate || result1.requiresUpdate)
			update();
	}
}