package levelMap.importX;

import hex.*;
import java.util.*;

public class TestImportSector implements ImportSector
{
	public int range;
	public List<ImportTile> tiles;

	public TestImportSector(int range)
	{
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
				tiles.add(new ImportTile(new Hex(j, i)));
			}
		}
		return this;
	}
}