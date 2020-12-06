package levelmap;

import com.fasterxml.jackson.jr.ob.comp.*;
import geom.tile.*;
import java.io.*;
import java.util.*;
import system.*;

@FunctionalInterface
public interface XSaveableYS
{
	void save(ObjectComposer<? extends ComposerBase> a1, TileType y1, SystemScheme systemScheme) throws IOException;

	static void saveObject(String key, XSaveableYS save,
			ObjectComposer<? extends ComposerBase> a1, TileType y1, SystemScheme systemScheme) throws IOException
	{
		var a2 = a1.startObjectField(key);
		save.save(a2, y1, systemScheme);
		a2.end();
	}

	static void saveList(String key, List<? extends XSaveableYS> save,
			ObjectComposer<? extends ComposerBase> a1, TileType y1, SystemScheme systemScheme) throws IOException
	{
		var a2 = a1.startArrayField(key);
		for(XSaveableYS saveable : save)
		{
			var a3 = a2.startObject();
			saveable.save(a3, y1, systemScheme);
			a3.end();
		}
		a2.end();
	}
}