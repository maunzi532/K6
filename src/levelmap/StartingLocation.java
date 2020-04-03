package levelmap;

import com.fasterxml.jackson.jr.ob.comp.*;
import com.fasterxml.jackson.jr.stree.*;
import geom.tile.*;
import item.*;
import item.inv.*;
import java.io.*;
import text.*;

public record StartingLocation(int number, CharSequence characterName, Tile location, boolean canSwap, Inv invOverride, int startingDelay)
{
	public boolean canTrade()
	{
		return invOverride == null;
	}

	public static StartingLocation load(JrsObject data, ItemLoader itemLoader, TileType y1, int number)
	{
		CharSequence characterName = new NameText(data.get("Name").asText());
		Tile location = y1.create2(((JrsNumber) data.get("sx")).getValue().intValue(), ((JrsNumber) data.get("sy")).getValue().intValue());
		boolean canSwap = ((JrsBoolean) data.get("CanSwap")).booleanValue();
		Inv invOverride;
		if(data.get("InvOverride") != null)
			invOverride = new WeightInv((JrsObject) data.get("InvOverride"), itemLoader);
		else
			invOverride = null;
		int startingDelay = ((JrsNumber) data.get("StartingDelay")).getValue().intValue();
		return new StartingLocation(number, characterName, location, canSwap, invOverride, startingDelay);
	}

	public <T extends ComposerBase> void save(ObjectComposer<T> a1, ItemLoader itemLoader, TileType y1) throws IOException
	{
		a1.put("Name", characterName.toString());
		a1.put("sx", y1.sx(location));
		a1.put("sy", y1.sy(location));
		a1.put("CanSwap", canSwap);
		if(invOverride != null)
			invOverride.save(a1.startObjectField("InvOverride"), itemLoader);
		a1.put("StartingDelay", startingDelay);
	}
}