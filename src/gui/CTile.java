package gui;

public class CTile
{
	public final int x, y;
	public final GuiTile guiTile;

	public CTile(int x, int y)
	{
		this.x = x;
		this.y = y;
		guiTile = null;
	}

	public CTile(int x, int y, GuiTile guiTile)
	{
		this.x = x;
		this.y = y;
		this.guiTile = guiTile;
	}

	public boolean targeted(int xt, int yt)
	{
		return xt == x && yt == y;
	}
}