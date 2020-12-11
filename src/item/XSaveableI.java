package item;

import com.fasterxml.jackson.jr.ob.comp.*;
import java.io.*;
import java.util.*;

@FunctionalInterface
public interface XSaveableI
{
	void save(ObjectComposer<? extends ComposerBase> a1, AllItemsList allItemsList) throws IOException;

	static void saveObject(String key, XSaveableI save,
			ObjectComposer<? extends ComposerBase> a1, AllItemsList allItemsList) throws IOException
	{
		var a2 = a1.startObjectField(key);
		save.save(a2, allItemsList);
		a2.end();
	}

	static void saveList(String key, List<? extends XSaveableI> save,
			ObjectComposer<? extends ComposerBase> a1, AllItemsList allItemsList) throws IOException
	{
		var a2 = a1.startArrayField(key);
		for(XSaveableI saveable : save)
		{
			var a3 = a2.startObject();
			saveable.save(a3, allItemsList);
			a3.end();
		}
		a2.end();
	}
}