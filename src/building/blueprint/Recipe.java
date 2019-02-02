package building.blueprint;

import inv.*;

public class Recipe
{
	public final ItemList required;
	public final ItemList results;

	public Recipe(ItemList required, ItemList results)
	{
		this.required = required;
		this.results = results;
	}
}