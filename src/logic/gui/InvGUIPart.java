package logic.gui;

public class InvGUIPart
{
	private int invID;
	private int xl, yl;
	private int xc, yc;
	private int xs, ys;
	private int scroll = 0;
	private String name;
	private boolean updateGUI;
	private boolean updateInvView;

	public InvGUIPart(int invID, int xl, int yl, int xc, int yc, int xs, int ys, String name)
	{
		this.invID = invID;
		this.xl = xl;
		this.yl = yl;
		this.xc = xc;
		this.yc = yc;
		this.xs = xs;
		this.ys = ys;
		this.name = name;
	}

	public void setName(String name)
	{
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
		tiles[xl][yl] = new GuiTile(name);
		int yl1 = yl + 1;
		boolean canScrollUp = scroll > 0;
		boolean canScrollDown = (scroll + yc) * xc < size;
		if(canScrollUp)
		{
			tiles[xl + 1][yl1] = new GuiTile("Scroll");
		}
		if(canScrollDown)
		{
			tiles[xl + 1][yl1 + (yc - 1) * ys] = new GuiTile("Scroll");
		}
		for(int iy = scroll + (canScrollUp ? 1 : 0); iy < scroll + yc - (canScrollDown ? 1 : 0) && iy * xc < size; iy++)
		{
			for(int ix = 0; ix < xc && iy * xc + ix < size; ix++)
			{
				invGUI.itemView(invID, xl + ix * xs, yl1 + (iy - scroll) * ys, iy * xc + ix);
			}
		}
	}

	public void checkClick(int x, int y, int size, InvGUI invGUI)
	{
		int xr = x - xl;
		int yr1 = y - yl - 1;
		if(xr < 0 || xr >= xc * xs)
			return;
		if(yr1 < 0 || yr1 >= yc * ys)
			return;
		int xr2 = xr / xs;
		int yr2 = yr1 / ys;
		boolean canScrollUp = scroll > 0;
		boolean canScrollDown = scroll + yc < size;
		if(canScrollUp)
		{
			if(yr2 == 0)
			{
				scroll -= 1;
				updateGUI = true;
				return;
			}
		}
		if(canScrollDown)
		{
			if(yr2 == yc - 1)
			{
				scroll += 1;
				updateGUI = true;
				return;
			}
		}
		int num = (yr2 + scroll) * xc + xr2;
		if(num >= 0 && num < size)
			invGUI.onClickItem(invID, num);
	}
}