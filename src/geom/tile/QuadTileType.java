package geom.tile;

import java.util.*;

public class QuadTileType implements TileType
{
	@Override
	public Tile create2(int n1, int n2)
	{
		return new Tile(new int[]{n1, n2});
	}

	@Override
	public Tile add(Tile t1, Tile t2)
	{
		return create2(t1.v()[0] + t2.v()[0], t1.v()[1] + t2.v()[1]);
	}

	@Override
	public Tile subtract(Tile t1, Tile minus)
	{
		return create2(t1.v()[0] - minus.v()[0], t1.v()[1] - minus.v()[1]);
	}

	@Override
	public Tile multiply(Tile t1, int scalar)
	{
		return create2(t1.v()[0] * scalar, t1.v()[1] * scalar);
	}

	@Override
	public int length(Tile t1)
	{
		return Math.abs(t1.v()[0]) + Math.abs(t1.v()[1]);
	}

	@Override
	public int directionCount()
	{
		return 4;
	}

	private static final Tile[] directions =
			{
					new Tile(new int[]{1, 0}), new Tile(new int[]{0, -1}),
					new Tile(new int[]{-1, 0}), new Tile(new int[]{0, 1})
			};

	@Override
	public Tile directionTile(int direction)
	{
		return directions[Math.floorMod(direction, directionCount())];
	}

	@Override
	public List<Tile> range(Tile t1, int minRange, int maxRange)
	{
		List<Tile> re = new ArrayList<>();
		if(minRange <= 0)
			re.add(t1);
		for(int i = Math.max(minRange, 1); i <= maxRange; i++)
		{
			for(int j = 0; j < directionCount(); j++)
			{
				for(int k = 0; k < i; k++)
				{
					re.add(add(t1, add(multiply(directionTile(j), i - k), multiply(directionTile(j + 1), k))));
				}
			}
		}
		return re;
	}

	@Override
	public Tile fromOffset(int x, int y)
	{
		return create2(x, y);
	}

	@Override
	public Tile toOffset(Tile t1)
	{
		return t1;
	}

	@Override
	public int sx(Tile t1)
	{
		return t1.v()[0];
	}

	@Override
	public int sy(Tile t1)
	{
		return t1.v()[1];
	}
}