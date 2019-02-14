package logic.gui;

import inv.*;
import java.util.List;

public interface InventoryView
{
	default void addInventoryView(GuiTile[][] tiles, int invID, int x, int y, int yh, int scroll, Inv inv, boolean withEmpty, String name)
	{
		tiles[x][y] = new GuiTile(name);
		boolean canScrollUp = scroll > 0;
		boolean canScrollDown = scroll + yh - 1 < inv.viewCount(withEmpty);
		if(canScrollUp)
		{
			tiles[x + 1][y + 1] = new GuiTile("Scroll");
		}
		if(canScrollDown)
		{
			tiles[x + 1][y + yh - 1] = new GuiTile("Scroll");
		}
		int yShift = canScrollUp ? 2 : 1;
		List<ItemView> views = inv.viewItems(scroll + (canScrollUp ? 1 : 0),
				yh - yShift - (canScrollDown ? 1 : 0), withEmpty);
		for(int i = 0; i < views.size(); i++)
		{
			itemView(tiles, invID, x, y + yShift + i, views.get(i));
		}
	}

	default void itemView(GuiTile[][] tiles, int invID, int x, int y, ItemView view)
	{
		tiles[x][y] = new GuiTile(view.currentWithLimit());
		tiles[x + 1][y] = new GuiTile(null, view.item.image(), null);
	}

	default void checkInventoryViewClick(int xc, int yc, int invID, int x, int y, int yh, int xw, int scroll, Inv inv, boolean withEmpty)
	{
		if(xc < x || xc >= x + xw)
			return;
		int viewCount = inv.viewCount(withEmpty);
		boolean canScrollUp = scroll > 0;
		boolean canScrollDown = scroll + yh - 1 < viewCount;
		if(canScrollUp)
		{
			if(yc == y + 1)
				changeScroll(invID, -1);
		}
		if(canScrollDown)
		{
			if(yc == y + yh - 1)
				changeScroll(invID, 1);
		}
		if(yc >= y + (canScrollUp ? 2 : 1) && yc < y + yh - (canScrollDown ? 1 : 0))
		{
			int num = yc - y - 1 + scroll;
			if(num >= 0 && num < viewCount)
				onClickItem(invID, yc - y - 1 + scroll);
		}
	}

	void changeScroll(int invID, int scrollChange);

	void onClickItem(int invID, int num);
}