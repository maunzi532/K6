package logic.gui;

public class InvGUIPart
{
	private int invID;
	private int x, y;
	private int xw, yh;
	private int scroll = 0;
	private String name;
	private boolean updateGUI;
	private boolean updateInvView;

	public InvGUIPart(int invID, int x, int y, int xw, int yh, String name)
	{
		this.invID = invID;
		this.x = x;
		this.y = y;
		this.xw = xw;
		this.yh = yh;
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

	public void addToGUI(GuiTile[][] tiles, int size, InvGUI invGUI)
	{
		tiles[x][y] = new GuiTile(name);
		boolean canScrollUp = scroll > 0;
		boolean canScrollDown = scroll + yh - 1 < size;
		if(canScrollUp)
		{
			tiles[x + 1][y + 1] = new GuiTile("Scroll");
		}
		if(canScrollDown)
		{
			tiles[x + 1][y + yh - 1] = new GuiTile("Scroll");
		}
		for(int i = scroll + (canScrollUp ? 1 : 0); i < scroll + yh - (canScrollDown ? 2 : 1) && i < size; i++)
		{
			invGUI.itemView(invID, x, y + 1 - scroll + i, i);
		}
	}

	public void checkClick(int xc, int yc, int size, InvGUI invGUI)
	{
		if(xc < x || xc >= x + xw)
			return;
		boolean canScrollUp = scroll > 0;
		boolean canScrollDown = scroll + yh - 1 < size;
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
			if(num >= 0 && num < size)
				invGUI.onClickItem(invID, yc - y - 1 + scroll);
		}
	}
}