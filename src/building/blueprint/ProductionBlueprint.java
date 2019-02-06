package building.blueprint;

import file.*;
import inv.*;
import java.util.*;
import java.util.stream.*;

public class ProductionBlueprint
{
	public final ItemList inputLimits;
	public final ItemList outputLimits;
	public final List<Recipe> recipes;

	public ProductionBlueprint(ItemList inputLimits, ItemList outputLimits, Recipe... recipes)
	{
		this.inputLimits = inputLimits;
		this.outputLimits = outputLimits;
		this.recipes = Arrays.asList(recipes);
	}

	public ProductionBlueprint(BlueprintNode node)
	{
		if(!node.data.equals(getClass().getSimpleName()))
			throw new RuntimeException(node.data + ", required: " + getClass().getSimpleName());
		inputLimits = new ItemList(node.get(0));
		outputLimits = new ItemList(node.get(1));
		recipes = node.get(2).inside.stream().map(Recipe::new).collect(Collectors.toList());
	}
}