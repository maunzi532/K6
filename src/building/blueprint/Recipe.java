package building.blueprint;

import com.fasterxml.jackson.jr.ob.comp.*;
import com.fasterxml.jackson.jr.stree.*;
import item.*;
import java.io.*;

public record Recipe(ItemList required, ItemList results)
{
	public static Recipe create(JrsObject data, ItemLoader itemLoader)
	{
		ItemList required = new ItemList((JrsArray) data.get("Required"), itemLoader);
		ItemList results = new ItemList((JrsArray) data.get("Results"), itemLoader);
		return new Recipe(required, results);
	}

	public <T extends ComposerBase> void save(ObjectComposer<T> a1, ItemLoader itemLoader) throws IOException
	{
		required.save(a1.startArrayField("Required"), itemLoader);
		results.save(a1.startArrayField("Results"), itemLoader);
		a1.end();
	}
}