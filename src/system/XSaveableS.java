package system;

import com.fasterxml.jackson.jr.ob.comp.*;
import java.io.*;
import java.util.*;

@FunctionalInterface
public interface XSaveableS
{
	void save(ObjectComposer<? extends ComposerBase> a1, WorldSettings worldSettings) throws IOException;

	static void saveObject(String key, XSaveableS save,
			ObjectComposer<? extends ComposerBase> a1, WorldSettings worldSettings) throws IOException
	{
		var a2 = a1.startObjectField(key);
		save.save(a2, worldSettings);
		a2.end();
	}

	static void saveList(String key, List<? extends XSaveableS> save,
			ObjectComposer<? extends ComposerBase> a1, WorldSettings worldSettings) throws IOException
	{
		var a2 = a1.startArrayField(key);
		for(XSaveableS saveable : save)
		{
			var a3 = a2.startObject();
			saveable.save(a3, worldSettings);
			a3.end();
		}
		a2.end();
	}
}