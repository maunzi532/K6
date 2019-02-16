package gui;

public class InvGUIPart
{
	private int invID;
	private int xl, yl;
	private int xc, yc;
	private int xs, ys;
	private int scroll = 0;
	private CTile scrollUp;
	private CTile scrollDown;
	private boolean updateGUI;

	public InvGUIPart(int invID, int xl, int yl, int xc, int yc, int xs, int ys)
	{
		this.invID = invID;
		this.xl = xl;
		this.yl = yl;
		this.xc = xc;
		this.yc = yc;
		this.xs = xs;
		this.ys = ys;
		scrollUp = new CTile(xl, yl, new GuiTile("Scroll"), xs, ys);
		scrollDown = new CTile(xl, yl + (yc - 1) * ys, new GuiTile("Scroll"), xs, ys);
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

	public void addToGUI(int size, InvGUI invGUI)
	{
		boolean canScrollUp = scroll > 0;
		boolean canScrollDown = (scroll + yc) * xc < size;
		if(canScrollUp)
		{
			invGUI.setTile(scrollUp);
		}
		if(canScrollDown)
		{
			invGUI.setTile(scrollDown);
		}
		for(int iy = scroll + (canScrollUp ? 1 : 0); iy < scroll + yc - (canScrollDown ? 1 : 0) && iy * xc < size; iy++)
		{
			for(int ix = 0; ix < xc && iy * xc + ix < size; ix++)
			{
				invGUI.itemView(invID, xl + ix * xs, yl + (iy - scroll) * ys, iy * xc + ix);
			}
		}
	}

	public boolean target(int x, int y, int size, InvGUI invGUI)
	{
		int xr = x - xl;
		int yr = y - yl;
		if(xr < 0 || xr >= xc * xs)
			return false;
		if(yr < 0 || yr >= yc * ys)
			return false;
		int xr2 = Math.floorDiv(xr, xs);
		int xi = Math.floorMod(xr, xs);
		int yr2 = Math.floorDiv(yr, ys);
		int yi = Math.floorMod(yr, ys);
		boolean canScrollUp = scroll > 0;
		boolean canScrollDown = scroll + yc < size;
		if(canScrollUp && yr2 == 0)
		{
			invGUI.setTargeted(scrollUp);
			return true;
		}
		if(canScrollDown && yr2 == yc - 1)
		{
			invGUI.setTargeted(scrollDown);
			return true;
		}
		int num = (yr2 + scroll) * xc + xr2;
		if(num >= 0 && num < size)
			invGUI.onTarget(invID, num, xi, yi, new CTile(xl + xr2 * xs, yl + yr2 * ys, xs, ys));
		return true;
	}

	public void checkClick(int x, int y, int size, InvGUI invGUI)
	{
		int xr = x - xl;
		int yr = y - yl;
		if(xr < 0 || xr >= xc * xs)
			return;
		if(yr < 0 || yr >= yc * ys)
			return;
		int xr2 = Math.floorDiv(xr, xs);
		int xi = Math.floorMod(xr, xs);
		int yr2 = Math.floorDiv(yr, ys);
		int yi = Math.floorMod(yr, ys);
		boolean canScrollUp = scroll > 0;
		boolean canScrollDown = scroll + yc < size;
		if(canScrollUp && yr2 == 0)
		{
			scroll -= 1;
			updateGUI = true;
			return;
		}
		if(canScrollDown && yr2 == yc - 1)
		{
			scroll += 1;
			updateGUI = true;
			return;
		}
		int num = (yr2 + scroll) * xc + xr2;
		if(num >= 0 && num < size)
			invGUI.onClickItem(invID, num, xi, yi);
	}
}