package levelMap.importX;

import geom.f1.*;
import java.util.*;

public class TestImportSector implements ImportSector
{
	private final TileType y1;
	public int range;
	public List<ImportTile> tiles;

	public TestImportSector(TileType y1, int range)
	{
		this.y1 = y1;
		this.range = range;
	}

	@Override
	public List<ImportTile> tiles()
	{
		return tiles;
	}

	public TestImportSector generate()
	{
		tiles = new ArrayList<>();
		for(int i = -range; i <= range; i++)
		{
			for(int j = Math.min(0, -i) + range; j >= Math.max(0, -i) - range; j--)
			{
				tiles.add(new ImportTile(y1.create2(j, i)));
			}
		}
		return this;
	}
}