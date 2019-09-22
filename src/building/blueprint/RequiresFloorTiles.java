package building.blueprint;

import com.fasterxml.jackson.jr.ob.comp.*;
import com.fasterxml.jackson.jr.stree.*;
import file.*;
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

	public RequiresFloorTiles(BlueprintNode node)
	{
		if(!node.data.equals(getClass().getSimpleName()))
			throw new RuntimeException(node.data + ", required: " + getClass().getSimpleName());
		floorTileType = FloorTileType.valueOf(node.get(0).data);
		amount = node.get(1).dataInt();
		minRange = node.get(2).dataInt();
		maxRange = node.get(3).dataInt();
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