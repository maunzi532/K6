package building.blueprint;

import inv.*;
import java.util.*;

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
}