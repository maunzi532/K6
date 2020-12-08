package levelmap;

import com.fasterxml.jackson.jr.ob.comp.*;
import com.fasterxml.jackson.jr.stree.*;
import geom.tile.*;
import java.io.*;
import load.*;

public record StartingLocation(Tile location, boolean active, String startName, boolean locationLocked, boolean emptyInv) implements XSaveableY
{
	public static StartingLocation load(JrsObject data, TileType y1)
	{
		Tile location = XSaveableY.loadLocation(data, y1);
		boolean active = LoadHelper.asBoolean(data.get("Active"));
		String startName = LoadHelper.asOptionalString(data.get("StartName"));
		boolean locationLocked = LoadHelper.asBoolean(data.get("LocationLocked"));
		boolean emptyInv = LoadHelper.asBoolean(data.get("EmptyInv"));
		return new StartingLocation(location, active, startName, locationLocked, emptyInv);
	}

	@Override
	public void save(ObjectComposer<? extends ComposerBase> a1, TileType y1) throws IOException
	{
		XSaveableY.saveLocation(location, a1, y1);
		a1.put("Active", active);
		if(startName != null)
		{
			a1.put("StartName", startName);
		}
		a1.put("LocationLocked", locationLocked);
		a1.put("EmptyInv", emptyInv);
	}
}