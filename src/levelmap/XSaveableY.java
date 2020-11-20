package levelmap;

import com.fasterxml.jackson.jr.ob.comp.*;
import com.fasterxml.jackson.jr.stree.*;
import geom.tile.*;
import java.io.*;
import java.util.*;
import load.*;

@FunctionalInterface
public interface XSaveableY
{
	void save(ObjectComposer<? extends ComposerBase> a1, TileType y1) throws IOException;

	static Tile loadLocation(JrsObject data, TileType y1)
	{
		return y1.create2(LoadHelper.asInt(data.get("sx")), LoadHelper.asInt(data.get("sy")));
	}

	static void saveLocation(Tile tile, ObjectComposer<? extends ComposerBase> a1, TileType y1) throws IOException
	{
		a1.put("sx", y1.sx(tile));
		a1.put("sy", y1.sy(tile));
	}

	static void saveObject(String key, XSaveableY save,
			ObjectComposer<? extends ComposerBase> a1, TileType y1) throws IOException
	{
		var a2 = a1.startObjectField(key);
		save.save(a2, y1);
		a2.end();
	}

	static void saveList(String key, List<? extends XSaveableY> save,
			ObjectComposer<? extends ComposerBase> a1, TileType y1) throws IOException
	{
		var a2 = a1.startArrayField(key);
		for(XSaveableY saveable : save)
		{
			var a3 = a2.startObject();
			saveable.save(a3, y1);
			a3.end();
		}
		a2.end();
	}
}