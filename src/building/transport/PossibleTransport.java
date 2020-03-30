package building.transport;

import com.fasterxml.jackson.jr.ob.comp.*;
import com.fasterxml.jackson.jr.stree.*;
import doubleinv.*;
import geom.tile.*;
import item.*;
import java.io.*;
import java.util.*;

public record PossibleTransport(Item item, DoubleInv from, DoubleInv to, int priorityFrom, int priorityTo)
{
	@Override
	public boolean equals(Object obj)
	{
		if(this == obj) return true;
		if(!(obj instanceof PossibleTransport other)) return false;
		return item.equals(other.item) &&
				from.equals(other.from) &&
				to.equals(other.to);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(item, from, to);
	}

	public static PossibleTransport create(JrsObject data, ItemLoader itemLoader, TileType y1)
	{
		Item item = itemLoader.loadItem((JrsObject) data.get("Item"));
		DoubleInv from = PreConnectMapObject.create((JrsObject) data.get("From"), y1);
		DoubleInv to = PreConnectMapObject.create((JrsObject) data.get("To"), y1);
		return new PossibleTransport(item, from, to, 0, 0);
	}

	public <T extends ComposerBase> void save(ObjectComposer<T> a1, ItemLoader itemLoader, TileType y1) throws IOException
	{
		itemLoader.saveItem(a1.startObjectField("Item"), item, true);
		new PreConnectMapObject(from.location(), from.type()).save(a1.startObjectField("From"), y1);
		new PreConnectMapObject(to.location(), to.type()).save(a1.startObjectField("To"), y1);
		a1.end();
	}
}