package building.blueprint;

import com.fasterxml.jackson.jr.ob.comp.*;
import com.fasterxml.jackson.jr.stree.*;
import doubleinv.*;
import java.io.*;

public record RequiresFloorTiles(FloorTileType floorTileType, int amount, int minRange, int maxRange)
{
	public static RequiresFloorTiles create(JrsObject data)
	{
		FloorTileType floorTileType = FloorTileType.valueOf(data.get("Type").asText());
		int amount = ((JrsNumber) data.get("Amount")).getValue().intValue();
		int minRange = ((JrsNumber) data.get("MinRange")).getValue().intValue();
		int maxRange = ((JrsNumber) data.get("MaxRange")).getValue().intValue();
		return new RequiresFloorTiles(floorTileType, amount, minRange, maxRange);
	}

	public <T extends ComposerBase> void save(ObjectComposer<T> a1) throws IOException
	{
		a1.put("Type", floorTileType.name());
		a1.put("Amount", amount);
		a1.put("MinRange", minRange);
		a1.put("MaxRange", maxRange);
		a1.end();
	}
}