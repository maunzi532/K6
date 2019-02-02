package levelMap.importX;

import hex.*;
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
		return new FloorTile(sector, R.nextBoolean() ? FloorTileType.BLUE : FloorTileType.TECH);
	}

	public void importIntoMap(LevelMap levelMap, Sector sector)
	{
		levelMap.addFloor(hex, toTile(sector));
	}
}