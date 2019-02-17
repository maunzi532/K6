package building.blueprint;

import file.*;
import item.*;

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
}