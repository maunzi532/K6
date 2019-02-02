package levelMap.importX;

import java.util.*;
import levelMap.*;

public interface ImportSector
{
	List<ImportTile> tiles();

	default void importIntoMap(LevelMap levelMap)
	{
		Sector sector = sector();
		for(ImportTile importTile : tiles())
		{
			importTile.importIntoMap(levelMap, sector);
		}
	}

	default Sector sector()
	{
		return new Sector(true);
	}
}