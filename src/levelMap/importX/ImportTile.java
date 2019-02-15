package levelMap.importX;

import geom.hex.*;
import java.util.*;
import levelMap.*;

public class ImportTile
{
	private static final Random R = new Random();

	public ImportTile(Hex hex)
	{
		this.hex = hex;
	}

	protected Hex hex;

	protected FloorTile toTile(Sector sector)
	{
		return new FloorTile(sector, FloorTileType.values()[R.nextInt(3)]);
	}

	public void importIntoMap(LevelMap levelMap, Sector sector)
	{
		levelMap.addFloor(hex, toTile(sector));
	}
}