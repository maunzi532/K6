package doubleinv;

import com.fasterxml.jackson.jr.ob.comp.*;
import com.fasterxml.jackson.jr.stree.*;
import geom.tile.*;
import item.inv.*;
import java.io.*;

public record PreConnectMapObject(Tile location, DoubleInvType type) implements DoubleInv
{
	@Override
	public DoubleInvType type()
	{
		return type;
	}

	@Override
	public CharSequence name()
	{
		return null;
	}

	@Override
	public Tile location()
	{
		return null;
	}

	@Override
	public Inv inv(TradeDirection tradeDirection)
	{
		return null;
	}

	@Override
	public boolean active()
	{
		return false;
	}

	@Override
	public void afterTrading(){}

	public static PreConnectMapObject create(JrsObject data, TileType y1)
	{
		Tile location = y1.create2(((JrsNumber) data.get("sx")).getValue().intValue(), ((JrsNumber) data.get("sy")).getValue().intValue());
		DoubleInvType type = DoubleInvType.valueOf(data.get("Type").asText());
		return new PreConnectMapObject(location, type);
	}

	public <T extends ComposerBase> void save(ObjectComposer<T> a1, TileType y1) throws IOException
	{
		a1.put("sx", y1.sx(location));
		a1.put("sy", y1.sy(location));
		a1.put("Type", type.name());
		a1.end();
	}
}