package logic.gui;

import inv.*;
import java.util.List;

public interface InventoryView
{
	default void addInventoryView(GuiTile[][] tiles, int x, int y, int yh, int scroll, Inv inv, boolean withEmpty, String name)
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
			itemView(tiles, x, y + yShift + i, views.get(i));
		}
	}

	default void itemView(GuiTile[][] tiles, int x, int y, ItemView view)
	{
		tiles[x][y] = new GuiTile(view.currentWithLimit());
		tiles[x + 1][y] = new GuiTile(null, view.item.image(), null);
	}
}