package building.blueprint;

import com.fasterxml.jackson.jr.ob.comp.*;
import com.fasterxml.jackson.jr.stree.*;
import item.*;
import java.io.*;

public class Recipe
{
	public final ItemList required;
	public final ItemList results;

	public Recipe(ItemList required, ItemList results)
	{
		this.required = required;
		this.results = results;
	}

	public Recipe(JrsObject data, ItemLoader itemLoader)
	{
		required = new ItemList((JrsArray) data.get("Required"), itemLoader);
		results = new ItemList((JrsArray) data.get("Results"), itemLoader);
	}

	public <T extends ComposerBase> ObjectComposer<T> save(ObjectComposer<T> a1, ItemLoader itemLoader) throws IOException
	{
		var a2 = required.save(a1.startArrayField("Required"), itemLoader).end();
		return results.save(a2.startArrayField("Results"), itemLoader).end();
	}
}