package building.transport;

import com.fasterxml.jackson.jr.ob.comp.*;
import com.fasterxml.jackson.jr.stree.*;
import doubleinv.*;
import geom.f1.*;
import item.*;
import java.io.*;
import java.util.*;

public record PossibleTransport(Item item, DoubleInv from, DoubleInv to, int priorityFrom, int priorityTo)
{
	@Override
	public boolean equals(Object o)
	{
		if(this == o) return true;
		if(!(o instanceof PossibleTransport that)) return false;
		return item.equals(that.item) &&
				from.equals(that.from) &&
				to.equals(that.to);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(item, from, to);
	}

	public static PossibleTransport create(JrsObject data, ItemLoader itemLoader, TileType y1)
	{
		Item item = itemLoader.loadItem(data);
		DoubleInv from = PreConnectMapObject.create((JrsObject) data.get("From"), y1);
		DoubleInv to = PreConnectMapObject.create((JrsObject) data.get("To"), y1);
		return new PossibleTransport(item, from, to, 0, 0);
	}

	public <T extends ComposerBase> ObjectComposer<T> save(ObjectComposer<T> a1, ItemLoader itemLoader, TileType y1) throws IOException
	{
		itemLoader.saveItem(a1, item);
		new PreConnectMapObject(from.location(), from.type()).save(a1.startObjectField("From"), y1).end();
		new PreConnectMapObject(to.location(), to.type()).save(a1.startObjectField("To"), y1).end();
		return a1;
	}
}