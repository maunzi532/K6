package building.blueprint;

import com.fasterxml.jackson.jr.ob.comp.*;
import com.fasterxml.jackson.jr.stree.*;
import java.io.*;
import levelMap.*;

public class RequiresFloorTiles
{
	public final FloorTileType floorTileType;
	public final int amount;
	public final int minRange;
	public final int maxRange;

	public RequiresFloorTiles(FloorTileType floorTileType, int amount, int minRange, int maxRange)
	{
		this.floorTileType = floorTileType;
		this.amount = amount;
		this.minRange = minRange;
		this.maxRange = maxRange;
	}

	public RequiresFloorTiles(JrsObject data)
	{
		floorTileType = FloorTileType.valueOf(data.get("Type").asText().toUpperCase());
		amount = ((JrsNumber) data.get("Amount")).getValue().intValue();
		minRange = ((JrsNumber) data.get("MinRange")).getValue().intValue();
		maxRange = ((JrsNumber) data.get("MaxRange")).getValue().intValue();
	}

	public <T extends ComposerBase> ObjectComposer<T> save(ObjectComposer<T> a1) throws IOException
	{
		return a1.put("Type", floorTileType.name())
				.put("Amount", amount)
				.put("MinRange", minRange)
				.put("MaxRange", maxRange);
	}
}