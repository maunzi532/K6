package building.blueprint;

import com.fasterxml.jackson.jr.ob.comp.*;
import com.fasterxml.jackson.jr.stree.*;
import java.io.*;
import levelMap.*;

public record RequiresFloorTiles(FloorTileType floorTileType, int amount, int minRange, int maxRange)
{
	public static RequiresFloorTiles create(JrsObject data)
	{
		FloorTileType floorTileType = FloorTileType.valueOf(data.get("Type").asText().toUpperCase());
		int amount = ((JrsNumber) data.get("Amount")).getValue().intValue();
		int minRange = ((JrsNumber) data.get("MinRange")).getValue().intValue();
		int maxRange = ((JrsNumber) data.get("MaxRange")).getValue().intValue();
		return new RequiresFloorTiles(floorTileType, amount, minRange, maxRange);
	}

	public <T extends ComposerBase> ObjectComposer<T> save(ObjectComposer<T> a1) throws IOException
	{
		return a1.put("Type", floorTileType.name())
				.put("Amount", amount)
				.put("MinRange", minRange)
				.put("MaxRange", maxRange);
	}
}