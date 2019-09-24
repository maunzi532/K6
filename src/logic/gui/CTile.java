package logic.gui;

public class CTile
{
	public static final CTile NONE = new CTile(0, 0, 0, 0);

	public final int x, y;
	public final GuiTile guiTile;
	public final int r, d;
	public final GuiTile other;

	public CTile(int x, int y)
	{
		this.x = x;
		this.y = y;
		guiTile = null;
		r = 1;
		d = 1;
		other = null;
	}

	public CTile(int x, int y, GuiTile guiTile)
	{
		this.x = x;
		this.y = y;
		this.guiTile = guiTile;
		r = 1;
		d = 1;
		other = null;
	}

	public CTile(int x, int y, GuiTile copy, int r, int d)
	{
		this.x = x;
		this.y = y;
		guiTile = new GuiTile(copy, r, d);
		this.r = r;
		this.d = d;
		other = getOther(guiTile);
	}

	public CTile(int x, int y, int r, int d)
	{
		this.x = x;
		this.y = y;
		guiTile = null;
		this.r = r;
		this.d = d;
		other = null;
	}

	public boolean contains(int xt, int yt)
	{
		return xt >= x && xt < x + r && yt >= y && yt < y + d;
	}

	public static GuiTile getOther(GuiTile guiTile)
	{
		return new GuiTile(null, null, false, guiTile.color);
	}
}