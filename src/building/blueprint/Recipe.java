package building.blueprint;

import com.fasterxml.jackson.jr.ob.comp.*;
import com.fasterxml.jackson.jr.stree.*;
import file.*;
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

	public Recipe(BlueprintNode node)
	{
		if(!node.data.equals(getClass().getSimpleName()))
			throw new RuntimeException(node.data + ", required: " + getClass().getSimpleName());
		required = new ItemList(node.get(0));
		results = new ItemList(node.get(1));
	}

	public Recipe(JrsObject data)
	{
		required = new ItemList((JrsArray) data.get("Required"));
		results = new ItemList((JrsArray) data.get("Results"));
	}

	public <T extends ComposerBase> ObjectComposer<T> save(ObjectComposer<T> a1) throws IOException
	{
		var a2 = required.save(a1.startArrayField("Required")).end();
		return results.save(a2.startArrayField("Results")).end();
	}
}