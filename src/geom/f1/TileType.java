package geom.f1;

import java.util.*;
import java.util.stream.*;

public interface TileType
{
	Tile create2(int n1, int n2);

	Tile add(Tile t1, Tile t2);

	Tile subtract(Tile t1, Tile minus);

	Tile multiply(Tile t1, int scalar);

	int length(Tile t1);

	default int distance(Tile t1, Tile t2)
	{
		return length(subtract(t1, t2));
	}

	int directionCount();

	Tile directionTile(int direction);

	default Tile neighbor(Tile t1, int direction)
	{
		return add(t1, directionTile(direction));
	}

	default Tile[] neighbors(Tile t1)
	{
		return IntStream.range(0, directionCount()).mapToObj(i -> neighbor(t1, i)).toArray(Tile[]::new);
	}

	List<Tile> range(Tile t1, int minRange, int maxRange);

	default List<Tile> betweenArea(Tile t1, Tile t2)
	{
		int d0 = distance(t1, t2);
		for(int i = 0; i < directionCount(); i++)
		{
			int i2 = (i + 1) % directionCount();
			if(distance(neighbor(t1, i), t2) < d0 && distance(neighbor(t1, i2), t2) < d0)
			{
				List<Tile> between1 = new ArrayList<>();
				for(int j1 = 0; j1 <= d0; j1++)
				{
					for(int j2 = 0; j2 <= d0; j2++)
					{
						between1.add(add(add(t1, multiply(directionTile(i), j1)), multiply(directionTile(i2), j2)));
					}
				}
				int i1a = (i + directionCount() / 2) % directionCount();
				int i2a = (i2 + directionCount() / 2) % directionCount();
				List<Tile> between2 = new ArrayList<>();
				for(int j1 = 0; j1 <= d0; j1++)
				{
					for(int j2 = 0; j2 <= d0; j2++)
					{
						between2.add(add(add(t2, multiply(directionTile(i1a), j1)), multiply(directionTile(i2a), j2)));
					}
				}
				return between1.stream().filter(between2::contains).collect(Collectors.toList());
			}
		}
		for(int i = 0; i < directionCount(); i++)
		{
			if(distance(neighbor(t1, i), t2) < d0)
			{
				List<Tile> between = new ArrayList<>();
				for(int j = 0; j <= d0; j++)
				{
					between.add(add(t1, multiply(directionTile(i), j)));
				}
				return between;
			}
		}
		return List.of(t1);
	}

	Tile fromOffset(int x, int y);

	Tile toOffset(Tile t1);

	int sx(Tile t1);

	int sy(Tile t1);
}