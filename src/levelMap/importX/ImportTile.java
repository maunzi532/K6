package levelMap.importX;

import geom.f1.*;
import java.util.*;
import levelMap.*;

public class ImportTile
{
	private static final Random R = new Random();

	protected Tile t1;

	public ImportTile(Tile t1)
	{
		this.t1 = t1;
	}

	protected FloorTile toTile(int sector)
	{
		return new FloorTile(sector, FloorTileType.values()[R.nextInt(3)]);
	}

	public void importIntoMap(LevelMap levelMap, int sector)
	{
		levelMap.addFloor(t1, toTile(sector));
	}
}