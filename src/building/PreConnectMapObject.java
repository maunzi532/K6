package building;

import com.fasterxml.jackson.jr.ob.comp.*;
import com.fasterxml.jackson.jr.stree.*;
import geom.f1.*;
import item.inv.*;
import building.transport.*;
import java.io.*;
import levelMap.*;

public class PreConnectMapObject implements DoubleInv
{
	public Tile location;
	public DoubleInvType type;

	public PreConnectMapObject(Tile location, DoubleInvType type)
	{
		this.location = location;
		this.type = type;
	}

	@Override
	public DoubleInvType type()
	{
		return type;
	}

	@Override
	public String name()
	{
		return null;
	}

	@Override
	public Tile location()
	{
		return null;
	}

	@Override
	public Inv inputInv()
	{
		return null;
	}

	@Override
	public Inv outputInv()
	{
		return null;
	}

	@Override
	public boolean active()
	{
		return false;
	}

	public DoubleInv restore(LevelMap levelMap)
	{
		return switch(type)
		{
			case BUILDING -> levelMap.getBuilding(location);
			case ENTITY -> levelMap.getEntity(location);
		};
	}

	public PreConnectMapObject(JrsObject data, TileType y1)
	{
		location = y1.create2(((JrsNumber) data.get("sx")).getValue().intValue(), ((JrsNumber) data.get("sy")).getValue().intValue());
		type = DoubleInvType.valueOf(data.get("Type").asText().toUpperCase());
	}

	public <T extends ComposerBase> ObjectComposer<T> save(ObjectComposer<T> a1, TileType y1) throws IOException
	{
		return a1.put("sx", y1.sx(location))
				.put("sy", y1.sy(location))
				.put("Type", type.name());
	}
}