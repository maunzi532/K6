package geom.f1;

import java.util.*;

public class HexTileType implements TileType
{
	@Override
	public Tile create2(int n1, int n2)
	{
		return new Tile(new int[]{n1, -n1 - n2, n2});
	}

	protected Tile create3(int n1, int n2, int n3)
	{
		return new Tile(new int[]{n1, n2, n3});
	}

	@Override
	public Tile add(Tile t1, Tile t2)
	{
		return create3(t1.v()[0] + t2.v()[0], t1.v()[1] + t2.v()[1], t1.v()[2] + t2.v()[2]);
	}

	@Override
	public Tile subtract(Tile t1, Tile minus)
	{
		return create3(t1.v()[0] - minus.v()[0], t1.v()[1] - minus.v()[1], t1.v()[2] - minus.v()[2]);
	}

	@Override
	public Tile multiply(Tile t1, int scalar)
	{
		return create3(t1.v()[0] * scalar, t1.v()[1] * scalar, t1.v()[2] * scalar);
	}

	@Override
	public int length(Tile t1)
	{
		return (Math.abs(t1.v()[0]) + Math.abs(t1.v()[1]) + Math.abs(t1.v()[2])) / 2;
	}

	@Override
	public int directionCount()
	{
		return 6;
	}

	private static final Tile[] directions = new Tile[]
			{
					new Tile(new int[]{1, -1, 0}), new Tile(new int[]{1, 0, -1}), new Tile(new int[]{0, 1, -1}),
					new Tile(new int[]{-1, 1, 0}), new Tile(new int[]{-1, 0, 1}), new Tile(new int[]{0, -1, 1})
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
					re.add(add(t1, add(multiply(directionTile(j), i), multiply(directionTile(j + 2), k))));
				}
			}
		}
		return re;
	}

	@Override
	public Tile fromOffset(int x, int y)
	{
		return create2(x - Math.floorDiv(y, 2), y);
	}

	@Override
	public Tile toOffset(Tile t1)
	{
		return new Tile(new int[]{Math.floorDiv(t1.v()[0] - t1.v()[1], 2), t1.v()[2]});
	}

	@Override
	public int sx(Tile t1)
	{
		return t1.v()[0];
	}

	@Override
	public int sy(Tile t1)
	{
		return t1.v()[2];
	}
}