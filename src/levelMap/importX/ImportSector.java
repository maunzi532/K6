package levelMap.importX;

import java.util.*;
import levelMap.*;

public interface ImportSector
{
	List<ImportTile> tiles();

	default void importIntoMap(LevelMap levelMap)
	{
		int sector = levelMap.createSector(true);
		for(ImportTile importTile : tiles())
		{
			importTile.importIntoMap(levelMap, sector);
		}
	}
}