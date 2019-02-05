package logic.gui;

public class XGUI
{
	protected GuiTile[][] tiles;

	public XGUI()
	{
		initTiles();
	}

	protected void initTiles()
	{
		tiles = new GuiTile[xw()][yw()];
		for(int i = 0; i < xw(); i++)
		{
			for(int j = 0; j < yw(); j++)
			{
				tiles[i][j] = GuiTile.EMPTY;
			}
		}
	}

	public GuiTile[][] getTiles()
	{
		return tiles;
	}

	public int xw()
	{
		return 0;
	}

	public int yw()
	{
		return 0;
	}

	public void click(int x, int y)
	{
		/*System.out.println("x = " + x);
		System.out.println("y = " + y);*/
	}
}