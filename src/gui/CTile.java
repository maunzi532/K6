package gui;

public class CTile
{
	public final int x, y;

	public CTile(int x, int y)
	{
		this.x = x;
		this.y = y;
	}

	public boolean targeted(int xt, int yt)
	{
		return xt == x && yt == y;
	}
}