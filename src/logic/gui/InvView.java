package logic.gui;

import inv.*;
import java.util.List;
import java.util.stream.Collectors;

public class InvView
{
	private int invID;
	private int x, y;
	private int xw, yh;
	private int scroll = 0;
	private List<ItemView> invView;
	private String name;
	private boolean updateGUI;
	private boolean updateInvView;

	public InvView(int invID, int x, int y, int xw, int yh, List<ItemView> invView, String name)
	{
		this.invID = invID;
		this.x = x;
		this.y = y;
		this.xw = xw;
		this.yh = yh;
		this.invView = invView;
		this.name = name;
	}

	public boolean updateGUIFlag()
	{
		if(updateGUI)
		{
			updateGUI = false;
			return true;
		}
		return false;
	}

	public boolean updateInvViewFlag()
	{
		if(updateInvView)
		{
			updateInvView = false;
			return true;
		}
		return false;
	}

	public void setInvView(List<ItemView> invView)
	{
		this.invView = invView;
	}

	public void addToGUI(GuiTile[][] tiles, InvGUI invGUI)
	{
		tiles[x][y] = new GuiTile(name);
		boolean canScrollUp = scroll > 0;
		boolean canScrollDown = scroll + yh - 1 < invView.size();
		if(canScrollUp)
		{
			tiles[x + 1][y + 1] = new GuiTile("Scroll");
		}
		if(canScrollDown)
		{
			tiles[x + 1][y + yh - 1] = new GuiTile("Scroll");
		}
		int yShift = canScrollUp ? 1 : 0;
		List<ItemView> views = invView.stream().skip(scroll + yShift).limit(yh - yShift - (canScrollDown ? 2 : 1)).collect(Collectors.toList());
		for(int i = 0; i < views.size(); i++)
		{
			invGUI.itemView(x, y + yShift + 1 + i, views.get(i));
		}
	}

	public void checkClick(int xc, int yc, InvGUI invGUI)
	{
		if(xc < x || xc >= x + xw)
			return;
		boolean canScrollUp = scroll > 0;
		boolean canScrollDown = scroll + yh - 1 < invView.size();
		if(canScrollUp)
		{
			if(yc == y + 1)
			{
				scroll -= 1;
				updateGUI = true;
			}
		}
		if(canScrollDown)
		{
			if(yc == y + yh - 1)
			{
				scroll += 1;
				updateGUI = true;
			}
		}
		if(yc >= y + (canScrollUp ? 2 : 1) && yc < y + yh - (canScrollDown ? 1 : 0))
		{
			int num = yc - y - 1 + scroll;
			if(num >= 0 && num < invView.size())
				invGUI.onClickItem(invID, yc - y - 1 + scroll);
		}
	}
}