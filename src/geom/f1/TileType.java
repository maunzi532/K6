package geom.f1;

import java.util.*;
import java.util.stream.*;

public interface TileType
{
	Tile create(int[] v);

	Tile create2(int n1, int n2);

	Tile create3(int n1, int n2, int n3);

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

	Tile rotate(Tile t1, boolean inverse);

	List<Tile> range(Tile t1, int minRange, int maxRange);
}