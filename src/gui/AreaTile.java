package gui;

public final class AreaTile
{
	public static final AreaTile NONE = new AreaTile(0, 0, 0, 0);

	public final int x, y;
	public final GuiTile guiTile;
	public final int right, down;
	public final GuiTile other;

	public AreaTile(int x, int y)
	{
		this.x = x;
		this.y = y;
		guiTile = null;
		right = 1;
		down = 1;
		other = null;
	}

	public AreaTile(int x, int y, GuiTile guiTile)
	{
		this.x = x;
		this.y = y;
		this.guiTile = guiTile;
		right = 1;
		down = 1;
		other = null;
	}

	public AreaTile(int x, int y, GuiTile copy, int right, int down)
	{
		this.x = x;
		this.y = y;
		guiTile = new GuiTile(copy, right, down);
		this.right = right;
		this.down = down;
		other = getOther(guiTile);
	}

	public AreaTile(int x, int y, int right, int down)
	{
		this.x = x;
		this.y = y;
		guiTile = null;
		this.right = right;
		this.down = down;
		other = null;
	}

	public boolean contains(int xt, int yt)
	{
		return xt >= x && xt < x + right && yt >= y && yt < y + down;
	}

	public static GuiTile getOther(GuiTile guiTile)
	{
		return new GuiTile(null, null, false, guiTile.color);
	}

}